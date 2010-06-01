package judge.submitter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Problem;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVALiveSubmitter extends Submitter {

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
		"vjudge3",
		"vjudge4",
		"vjudge5"
	};

	static private String passwordList[] = {
		"23773EX",
		"23775XQ",
		"23776XA",
		"23777BB",
		"23778BS"
	};
	
	private String submit(HttpClient httpClient, int idx){
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
		
        PostMethod postMethod = new PostMethod("http://acmicpc-live-archive.uva.es/nuevoportal/mailer.php");
        postMethod.addParameter("paso", "paso");
        postMethod.addParameter("language", submission.getLanguage());
        postMethod.addParameter("problem", problem.getOriginProb());
        postMethod.addParameter("code", submission.getSource());
        postMethod.addParameter("userid", passwordList[idx]);
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        httpClient.getParams().setContentCharset("UTF-8"); 
        try {
			System.out.println("submit...");
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
		String reg = "class=\"V_\\w{2,5}\">([\\s\\S]*?)<td>([\\s\\S]*?)<td>([\\s\\S]*?)<td>", result;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acmicpc-live-archive.uva.es/nuevoportal/status.php?u=" + username);
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
	    			if (!result.contains("ing")){
	    				if (result.equals("Accepted")){
		    				submission.setMemory(m.group(3).equals("Minimum") ? 0 : Integer.parseInt(m.group(3)));
		    				submission.setTime((int)(1000 * Double.parseDouble(m.group(2))));
//		    				System.out.println(username + " " + submission.getMemory() + "KB " + submission.getTime() + "ms");
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
		
		submit(clientList[idx], idx);
		submission.setStatus("Running & Judging");
		baseService.modify(submission);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		getResult(passwordList[idx]);
		
		synchronized (using) {
			using[idx] = false;
		}
		
	}

}
