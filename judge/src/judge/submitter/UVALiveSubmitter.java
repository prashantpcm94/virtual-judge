package judge.submitter;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Problem;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVALiveSubmitter extends Submitter {

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
		"vjudge3",
		"vjudge4",
		"vjudge5"
	};

	final static private String passwordList[] = {
		"23773EX",
		"23775XQ",
		"23776XA",
		"23777BB",
		"23778BS"
	};
	
	private void getMaxRunId() throws Exception {
		// 获取当前最大RunID
		GetMethod getMethod = new GetMethod("http://acmicpc-live-archive.uva.es/nuevoportal/status.php");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		Pattern p = Pattern.compile("<td>&nbsp;(\\d+)&nbsp;");

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

	private void submit(String password) throws Exception{
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
		
        PostMethod postMethod = new PostMethod("http://acmicpc-live-archive.uva.es/nuevoportal/mailer.php");
        postMethod.addParameter("paso", "paso");
        postMethod.addParameter("language", submission.getLanguage());
        postMethod.addParameter("problem", problem.getOriginProb());
        postMethod.addParameter("code", submission.getSource());
        postMethod.addParameter("userid", password);
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        httpClient.getParams().setContentCharset("UTF-8"); 

    	System.out.println("submit...");
		httpClient.executeMethod(postMethod);
		byte[] responseBody = postMethod.getResponseBody();
		String tLine = new String(responseBody, "UTF-8");
		if (!tLine.contains("Problem submitted successfull")){
			throw new Exception();
		}
	}
	
	public void getResult(String username) throws Exception{
		String reg = "<td>&nbsp;(\\d+)&nbsp;[\\s\\S]*?class=\"V_\\w{2,5}\">([\\s\\S]*?)<td>([\\s\\S]*?)<td>([\\s\\S]*?)<td>", result;
		Pattern p = Pattern.compile(reg);
		GetMethod getMethod = new GetMethod("http://acmicpc-live-archive.uva.es/nuevoportal/status.php?u=" + username);
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

				if (result.equals("Accepted")){
    				submission.setMemory(m.group(4).equals("Minimum") ? 0 : Integer.parseInt(m.group(4)));
    				submission.setTime((int)(1000 * Double.parseDouble(m.group(3))));
//	   				System.out.println(username + " " + submission.getMemory() + "KB " + submission.getTime() + "ms");
				}
				baseService.modify(submission);
				return;
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
				
			submit(passwordList[idx]);	//非登陆式,只需交一次
			submission.setStatus("Running & Judging");
			baseService.modify(submission);
			Thread.sleep(2000);
			getResult(passwordList[idx]);
		} catch (Exception e) {
			e.printStackTrace();
			submission.setStatus("Judging Error");
			baseService.modify(submission);
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	//client冷却时间
		synchronized (using) {
			using[idx] = false;
		}
	}
	

}
