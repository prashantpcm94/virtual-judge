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

public class HDUSubmitter extends Submitter {

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
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang"
	};
	
	private String submit(HttpClient httpClient){
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
		
        PostMethod postMethod = new PostMethod("http://acm.hdu.edu.cn/submit.php?action=submit");
        postMethod.addParameter("check", "0");
        postMethod.addParameter("language", submission.getLanguage());
        postMethod.addParameter("problemid", problem.getOriginProb());
        postMethod.addParameter("usercode", submission.getSource());
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        httpClient.getParams().setContentCharset("UTF-8"); 
        try {
			System.out.println("submit...");
			httpClient.executeMethod(postMethod);
			System.out.println("Location = " + postMethod.getResponseHeader("Location").getValue());
			return postMethod.getResponseHeader("Location").getValue().contains("user") ? null : "success";
		}
		catch(Exception e) {
			e.printStackTrace();
		    postMethod.releaseConnection();
		   	return null;
		}
	}
	
	private String login(HttpClient httpClient, String username, String password){
        PostMethod postMethod = new PostMethod("http://acm.hdu.edu.cn/userloginex.php?action=login&cid=0&notice=0");
  
        postMethod.addParameter("login", "Sign In");
        postMethod.addParameter("username", username);
        postMethod.addParameter("userpass", password);
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
		String reg = ">\\d{7,}</td><td>[\\s\\S]*?</td><td>([\\s\\S]*?)</td><td>[\\s\\S]*?</td><td>(\\d*?)MS</td><td>(\\d*?)K</td>", result;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.hdu.edu.cn/status.php?user=" + username);
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
//	    		System.out.println("\n\n\n" + tLine + "\n\n\n");
	    		if (m.find()){
	    			result = m.group(1).replaceAll("<[\\s\\S]*?>", "").trim();
    				submission.setStatus(result);
	    			if (!result.contains("ing")){
	    				if (result.equals("Accepted")){
		    				submission.setTime(Integer.parseInt(m.group(2)));
		    				submission.setMemory(Integer.parseInt(m.group(3)));
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
