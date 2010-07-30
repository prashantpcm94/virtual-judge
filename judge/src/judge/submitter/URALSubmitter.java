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

public class URALSubmitter extends Submitter {

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
		"93140DK",
		"93141YK",
		"93142SM",
		"93143PW",
		"93144DO"
	};
	
	
	private String submit(HttpClient httpClient, int idx){
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
		
        PostMethod postMethod = new PostMethod("http://acm.timus.ru/submit.aspx");
		postMethod.addParameter("Action", "submit");
		postMethod.addParameter("Language", submission.getLanguage());
		postMethod.addParameter("ProblemNum", problem.getOriginProb());
		postMethod.addParameter("Source", submission.getSource());
		postMethod.addParameter("JudgeID", passwordList[idx]);
		postMethod.addParameter("SpaceID", "1");
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
		String reg = "<TD class=\"verdict_\\w{2,5}\">([\\s\\S]*?)</TD>[\\s\\S]*?runtime\">([\\d\\.]*)[\\s\\S]*?memory\">([\\d\\s]*)", result;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.timus.ru/status.aspx?author=" + username.substring(0, 5));
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		int tryNum = 0;
		while (tryNum++ < 100){
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
		    				submission.setMemory(Integer.parseInt(m.group(3).replaceAll(" ", "")));
		    				submission.setTime((int)(1000 * Double.parseDouble(m.group(2))));
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
				Thread.sleep(2000);
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
		getResult(passwordList[idx]);
		
		synchronized (using) {
			using[idx] = false;
		}
		
	}

}
