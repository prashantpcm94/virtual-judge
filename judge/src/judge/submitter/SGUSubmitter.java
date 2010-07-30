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

public class SGUSubmitter extends Submitter {

	static private HttpClient clientList[] = new HttpClient[5];
	static {
		for (int i = 0; i < clientList.length; i++){
			clientList[i] = new HttpClient();
		}
	}
	
	static private boolean using[] = new boolean[5];
	
	static private String usernameList[] = {
		"040405",
		"040406",
		"040407",
		"040408",
		"040409"
	};

	static private String passwordList[] = {
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang",
		"xiaotuliangliang"
	};
	
	int maxRunId = 0;
	
	
	private String submit(HttpClient httpClient, int idx){
        GetMethod getMethod = new GetMethod("http://acm.sgu.ru/status.php");
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		int tryNum = 0;
		while (tryNum++ < 100){
	        try {
	            int statusCode = httpClient.executeMethod(getMethod);
	            if(statusCode != HttpStatus.SC_OK) {
	                System.err.println("Method failed: "+getMethod.getStatusLine());
	            }
	            byte[] responseBody = getMethod.getResponseBody();
	            String tLine = new String(responseBody, "UTF-8");
	    		Pattern p = Pattern.compile("<TD>(\\d{7,})</TD>");
	    		Matcher m = p.matcher(tLine);
	    		if (m.find()){
	    			maxRunId = Integer.parseInt(m.group(1));
	    			break;
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
        System.out.println("maxRunId : " + maxRunId);
		
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
        PostMethod postMethod = new PostMethod("http://acm.sgu.ru/sendfile.php?contest=0");
        postMethod.addParameter("elang", submission.getLanguage());
        postMethod.addParameter("id", usernameList[idx]);
        postMethod.addParameter("pass", passwordList[idx]);
        postMethod.addParameter("problem", problem.getOriginProb());
        postMethod.addParameter("source", submission.getSource());
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
		String reg = "<TD>(\\d{7,})</TD>[\\s\\S]*?<TD class=btab>([\\s\\S]*?)</TD>[\\s\\S]*?([\\d]*?) ms</TD><TD>([\\d]*?) kb</TD>", result;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.sgu.ru/status.php?idmode=1&id=" + username);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		int tryNum = 0;
		while (tryNum++ < 1000){
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
	    		if (m.find() && Integer.parseInt(m.group(1)) > maxRunId){
	    			result = m.group(2).replaceAll("<[\\s\\S]*?>", "").trim();
    				submission.setStatus(result);
	    			if (!result.contains("ing")){
	    				if (result.equals("Accepted")){
		    				submission.setMemory(Integer.parseInt(m.group(4)));
		    				submission.setTime(Integer.parseInt(m.group(3)));
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
				Thread.sleep(2012);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		submit(clientList[idx], idx);
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
