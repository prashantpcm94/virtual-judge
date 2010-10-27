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

public class HUSTSubmitter extends Submitter {

	static private HttpClient clientList[] = new HttpClient[5];
	static {
		for (int i = 0; i < clientList.length; i++){
			clientList[i] = new HttpClient();
			clientList[i].getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
		}
	}
	
	static private boolean using[] = new boolean[5];
	
	static private String usernameList[] = {
		"vjudge1",
		"vjudge2",
		"vjudge3",
		"vjudge4",
		"vjudge5"
	};

	static private String passwordList[] = {
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang"
	};
	
	private void getMaxRunId() throws Exception {
		GetMethod getMethod = new GetMethod("http://acm.hust.edu.cn/thx/status.php");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		Pattern p = Pattern.compile("class='evenrow'><td>(\\d+)");

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
		
        PostMethod postMethod = new PostMethod("http://acm.hust.edu.cn/thx/submit.php");
        postMethod.addParameter("language", submission.getLanguage());
        postMethod.addParameter("id", problem.getOriginProb());
        postMethod.addParameter("source", submission.getSource());
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        httpClient.getParams().setContentCharset("UTF-8"); 
		System.out.println("submit...");
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);
		if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY){
			throw new Exception();
		}
	}
	
	private void login(String username, String password) throws Exception{
        PostMethod postMethod = new PostMethod("http://acm.hust.edu.cn/thx/login.php");
  
        postMethod.addParameter("password", password);
        postMethod.addParameter("submit", "Submit");
        postMethod.addParameter("user_id", username);
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        System.out.println("login...");
		httpClient.executeMethod(postMethod);
		byte[] responseBody = postMethod.getResponseBody();
		String tLine = new String(responseBody, "UTF-8");
		if (!tLine.contains("history.go(-2)")){
			throw new Exception();
		}
	}
	
	public void getResult(String username) throws Exception{
		String reg = "class='evenrow'><td>(\\d+)[\\s\\S]*?<font[\\s\\S]*?>([\\s\\S]*?)</font>[\\s\\S]*?<td>([\\s\\S]*?)<td>([\\s\\S]*?)<td>", result;
		Pattern p = Pattern.compile(reg);
		GetMethod getMethod = new GetMethod("http://acm.hust.edu.cn/thx/status.php?user_id=" + username);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		long cur = new Date().getTime(), interval = 2000;
		while (new Date().getTime() - cur < 600000){
			System.out.println("getResult...");
			httpClient.executeMethod(getMethod);
			byte[] responseBody = getMethod.getResponseBody();
			String tLine = new String(responseBody, "UTF-8");

			Matcher m = p.matcher(tLine);
			if (m.find() && Integer.parseInt(m.group(1)) > maxRunId){
				result = m.group(2).replaceAll("<[\\s\\S]*?>", "").trim();
				submission.setStatus(result);
				if (!result.contains("ing")){
					if (result.equals("Accepted")){
						submission.setMemory(Integer.parseInt(m.group(3).replaceAll(" <font[\\s\\S]*?font>", "")));
						submission.setTime(Integer.parseInt(m.group(4).replaceAll(" <font[\\s\\S]*?font>", "")));
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
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	//host oj限制每两次提交之间至少隔10秒
		synchronized (using) {
			using[idx] = false;
		}
	}

}
