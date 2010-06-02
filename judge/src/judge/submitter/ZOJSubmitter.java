package judge.submitter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Problem;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class ZOJSubmitter extends Submitter {

	static private HttpClient clientList[] = new HttpClient[5];
	static {
		for (int i = 0; i < clientList.length; i++){
			clientList[i] = new HttpClient();
		}
	}
	
	static private boolean using[] = new boolean[5];
	
	static private String usernameList[] = {
		"vjudge1",
		"vjudge2",
		"vjudge33",
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
	
	private String submit(HttpClient httpClient) throws HttpException, IOException{
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
		
		HttpClient hc = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=" + problem.getOriginProb());
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        int statusCode = hc.executeMethod(getMethod);
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
        try {
			System.out.println("submit...");
			statusCode = httpClient.executeMethod(postMethod);
			System.out.println("statusCode = " + statusCode);
            responseBody = postMethod.getResponseBody();
            tLine = new String(responseBody, "UTF-8");
//          System.out.println("resp: " + tLine);
			return tLine.contains("Submit Successfully") ? "success" : null;
		}
		catch(Exception e) {
			e.printStackTrace();
		    postMethod.releaseConnection();
		   	return null;
		}
	}
	
	private String login(HttpClient httpClient, String username, String password){
        PostMethod postMethod = new PostMethod("http://acm.zju.edu.cn/onlinejudge/login.do");
 
        postMethod.addParameter("handle", username);
        postMethod.addParameter("password", password);
        postMethod.addParameter("rememberMe", "on");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
			System.out.println("login...");
			int statusCode = httpClient.executeMethod(postMethod);
			System.out.println("statusCode = " + statusCode);
			return statusCode == HttpStatus.SC_MOVED_TEMPORARILY ? "success" : null;
        }
        catch(Exception e) {
        	e.printStackTrace();
            postMethod.releaseConnection();
           	return null;
        }
	}
	
	public void getResult(String username){
		String reg = "judgeReply\\w{2,5}\">([\\s\\S]*?)</span>[\\s\\S]*?runTime\">([\\s\\S]*?)</td>[\\s\\S]*?runMemory\">([\\s\\S]*?)</td>", result;
		HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.zju.edu.cn/onlinejudge/showRuns.do?contestId=1&handle=" + username);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        while (true){
	        try {
				System.out.println("getResult...");
	            int statusCode = httpClient.executeMethod(getMethod);
	            if(statusCode != HttpStatus.SC_OK) {
	                System.err.println("Method failed: "+getMethod.getStatusLine());
	            }
	            byte[] responseBody = getMethod.getResponseBody();
	            String tLine = new String(responseBody, "UTF-8");

	    		Pattern p = Pattern.compile(reg);
	    		Matcher m = p.matcher(tLine);
	    		if (m.find()){
	    			result = m.group(1).replaceAll("<[\\s\\S]*?>", "").trim();
	    			submission.setStatus(result);
//					System.out.println("###" + result.trim() + "###");
	    			if (!result.contains("ing") || result.contains("oating")){
	    				if (result.equals("Accepted")){
		    				submission.setMemory(Integer.parseInt(m.group(3)));
		    				submission.setTime(Integer.parseInt(m.group(2)));
	    				}
	    				baseService.modify(submission);
	    				return;
	    			}
    				baseService.modify(submission);
	    		}
	        }
	        catch(Exception e) {
				e.printStackTrace();
	            getMethod.releaseConnection();
	        }
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}

	public void run() {
		int idx = -1;
		while(true) {
			synchronized (using) {
				for (int i = 0; i < 5; i++) {
					if (!using[i]) {
						idx = i;
						using[i] = true;
						break;
					}
				}
			}
			if (idx >= 0){
				break;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if (submit(clientList[idx]) == null){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				login(clientList[idx], usernameList[idx], passwordList[idx]);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				submit(clientList[idx]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			baseService.delete(submission);
			return;
		}
		submission.setStatus("Running & Judging");
		baseService.modify(submission);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		getResult(usernameList[idx]);
		
		synchronized (using) {
			using[idx] = false;
		}
		
	}

}
