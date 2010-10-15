package judge.submitter;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Problem;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class ZOJSubmitter extends Submitter {

	static private HttpClient clientList[] = new HttpClient[5];
	static {
		for (int i = 0; i < clientList.length; i++){
			clientList[i] = new HttpClient();
			clientList[i].getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
		}
	}
	
	static private boolean using[] = new boolean[5];
	
	final static private String usernameList[] = {
		"vjudge1",
		"vjudge2",
		"vjudge33",
		"vjudge4",
		"vjudge5"
	};

	final static private String passwordList[] = {
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang"
	};
	
	private void getMaxRunId() throws Exception {
		GetMethod getMethod = new GetMethod("http://acm.zju.edu.cn/onlinejudge/showRuns.do?contestId=1");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		Pattern p = Pattern.compile("<td class=\"runId\">(\\d+)</td>");

		httpClient.executeMethod(getMethod);
		byte[] responseBody = getMethod.getResponseBody();
		String tLine = new String(responseBody, "UTF-8");
		Matcher m = p.matcher(tLine);
		if (m.find()) {
			maxRunId = Integer.parseInt(m.group(1));
			System.out.println("maxRunId : " + maxRunId);
		} else {
			throw new Exception();
		}
	}

	private void submit() throws Exception{
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());

		GetMethod getMethod = new GetMethod("http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=" + problem.getOriginProb());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		int statusCode = httpClient.executeMethod(getMethod);
		byte[] responseBody = getMethod.getResponseBody();
		String tLine = new String(responseBody, "UTF-8");
		String problemId = regFind(tLine, "problemId=([\\s\\S]*?)\"><font");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		PostMethod postMethod = new PostMethod("http://acm.zju.edu.cn/onlinejudge/submit.do");
		postMethod.addParameter("languageId", submission.getLanguage());
		postMethod.addParameter("problemId", problemId);
		postMethod.addParameter("source", submission.getSource());
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		httpClient.getParams().setContentCharset("UTF-8"); 

		System.out.println("submit...");
		statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);
        responseBody = postMethod.getResponseBody();
        tLine = new String(responseBody, "UTF-8");
        if (!tLine.contains("Submit Successfully")){
        	throw new Exception();
        }
	}
	
	private void login(String username, String password) throws Exception{
        PostMethod postMethod = new PostMethod("http://acm.zju.edu.cn/onlinejudge/login.do");
 
        postMethod.addParameter("handle", username);
        postMethod.addParameter("password", password);
        postMethod.addParameter("rememberMe", "on");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		System.out.println("login...");
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);
		if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY){
        	throw new Exception();
        }
	}
	
	public void getResult(String username) throws Exception{
		String reg = "<td class=\"runId\">(\\d+)[\\s\\S]*?judgeReply\\w{2,5}\">([\\s\\S]*?)</span>[\\s\\S]*?runTime\">([\\s\\S]*?)</td>[\\s\\S]*?runMemory\">([\\s\\S]*?)</td>", result;
		Pattern p = Pattern.compile(reg);

		GetMethod getMethod = new GetMethod("http://acm.zju.edu.cn/onlinejudge/showRuns.do?contestId=1&handle=" + username);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		long cur = new Date().getTime(), interval = 2000;
		while (new Date().getTime() - cur < 600000){
			System.out.println("getResult...");
			httpClient.executeMethod(getMethod);
			byte[] responseBody = getMethod.getResponseBody();
			String tLine = new String(responseBody, "UTF-8");

			Matcher m = p.matcher(tLine);
			if (m.find() && Integer.parseInt(m.group(1)) > maxRunId) {
				result = m.group(2).replaceAll("<[\\s\\S]*?>", "").trim();
				submission.setStatus(result);
				// System.out.println("###" + result.trim() + "###");
				if (!result.contains("ing") || result.contains("oating")) {
					if (result.equals("Accepted")) {
						submission.setMemory(Integer.parseInt(m.group(4)));
						submission.setTime(Integer.parseInt(m.group(3)));
					}
					baseService.modify(submission);
					return;
				}
				baseService.modify(submission);
			}
			Thread.sleep(interval);
			interval += 500;
		}
		throw new Exception();
	}
	
	private int getIdleClient() {
		int length = usernameList.length;
		int begIdx = (int) (Math.random() * length);

		while(true) {
			synchronized (using) {
				for (int i = begIdx, j; i < begIdx + length; i++) {
					j = i % length;
					if (!using[j]) {
						using[j] = true;
						httpClient = clientList[j];
						return j;
					}
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public void run() {
		int idx = getIdleClient();
		httpClient = clientList[idx];

		try {
			getMaxRunId();
			try {
				//第一次尝试提交
				submit();
			} catch (Exception e1) {
				//失败,认为是未登录所致
				e1.printStackTrace();
				Thread.sleep(2000);
				login(usernameList[idx], passwordList[idx]);
				Thread.sleep(2000);
				submit();
			}
			submission.setStatus("Running & Judging");
			baseService.modify(submission);
			Thread.sleep(2000);
			getResult(usernameList[idx]);
		} catch (Exception e) {
			e.printStackTrace();
			submission.setStatus("Judging Error");
			baseService.modify(submission);
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	//zju oj限制每两次提交之间至少隔5秒
		synchronized (using) {
			using[idx] = false;
		}
	}

}
