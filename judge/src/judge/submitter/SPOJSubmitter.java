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

public class SPOJSubmitter extends Submitter {

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
	
	private String submit(HttpClient httpClient, int idx){
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
        PostMethod postMethod = new PostMethod("http://www.spoj.pl/submit/complete/");

        postMethod.addParameter("lang", submission.getLanguage());
        postMethod.addParameter("login_user", usernameList[idx]);
        postMethod.addParameter("password", passwordList[idx]);
        postMethod.addParameter("problemcode", problem.getOriginProb());
        postMethod.addParameter("file", submission.getSource());
        postMethod.addParameter("submit", "Send");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        httpClient.getParams().setContentCharset("UTF-8"); 
        try {
			System.out.println("submit...");
			int statusCode = httpClient.executeMethod(postMethod);
			System.out.println("statusCode = " + statusCode);
            byte[] responseBody = postMethod.getResponseBody();
            String tLine = new String(responseBody, "UTF-8");
//          System.out.println("\n" + tLine + "\n");
            if (tLine.contains("submit in this language for this problem")){
            	baseService.delete(submission);
            	submission = null;
            }
			return statusCode == HttpStatus.SC_MOVED_TEMPORARILY ? "success" : null;
		}
		catch(Exception e) {
			e.printStackTrace();
		    postMethod.releaseConnection();
		   	return null;
		}
	}
	
	public void getResult(String username){
		String reg = "<td class=\"statusres\"[\\s\\S]*?>([\\s\\S]*?)</td>\n<td[\\s\\S]*?>([\\s\\S]*?)</td>\n<td[\\s\\S]*?>([\\s\\S]*?)</td>", result;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://www.spoj.pl/status/" + username);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		long cur = new Date().getTime();
		long interval = 2000;
		while (new Date().getTime() - cur < 600000){
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
	    				if (result.equals("accepted")){
		    				submission.setStatus("Accepted");
		    				result = m.group(3).trim();
		    				int mul = result.contains("M") ? 1024 : 1;
		    				submission.setMemory((int)(mul * Double.parseDouble(result.replaceAll("[Mk]", ""))));
		    				submission.setTime((int)(1000 * Double.parseDouble(m.group(2).replaceAll("<[\\s\\S]*?>", "").trim())));
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
				Thread.sleep(interval);
				interval += 500;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
		submission.setStatus("Judging Error");
		baseService.modify(submission);
	}

	public void run() {
		int idx = -1;
		while(true) {
			int length = usernameList.length;
			int begIdx = (int) (Math.random() * length);
			synchronized (using) {
				for (int i = begIdx; i < begIdx + length; i++) {
					int j = i % length;
					if (!using[j]) {
						idx = j;
						using[j] = true;
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
		
		if (submission != null) {
			submission.setStatus("Running & Judging");
			baseService.modify(submission);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			getResult(usernameList[idx]);
		}
		
		synchronized (using) {
			using[idx] = false;
		}
		
	}

}
