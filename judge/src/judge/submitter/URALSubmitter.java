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

public class URALSubmitter extends Submitter {

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
		"93140DK",
		"93141YK",
		"93142SM",
		"93143PW",
		"93144DO"
	};
	
	private void getMaxRunId() throws Exception {
		GetMethod getMethod = new GetMethod("http://acm.timus.ru/status.aspx");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		Pattern p = Pattern.compile("<TD class=\"id\">(\\d+)");

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
		
        PostMethod postMethod = new PostMethod("http://acm.timus.ru/submit.aspx");
		postMethod.addParameter("Action", "submit");
		postMethod.addParameter("Language", submission.getLanguage());
		postMethod.addParameter("ProblemNum", problem.getOriginProb());
		postMethod.addParameter("Source", submission.getSource());
		postMethod.addParameter("JudgeID", password);
		postMethod.addParameter("SpaceID", "1");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        httpClient.getParams().setContentCharset("UTF-8"); 

        System.out.println("submit...");
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);
		if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY){
			throw new Exception();
		}
	}
	
	public void getResult(String username) throws Exception{
		String reg = "aspx/(\\d+)\\.txt[\\s\\S]*?class=\"verdict_\\w{2,5}\">([\\s\\S]*?)</TD>[\\s\\S]*?runtime\">([\\d\\.]*)[\\s\\S]*?memory\">([\\d\\s]*)", result;
        Pattern p = Pattern.compile(reg);
        GetMethod getMethod = new GetMethod("http://acm.timus.ru/status.aspx?author=" + username.substring(0, 5));
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
	    				submission.setMemory(Integer.parseInt(m.group(4).replaceAll(" ", "")));
	    				submission.setTime((int)(0.5 + 1000 * Double.parseDouble(m.group(3))));
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
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	//ural oj限制每两次提交之间至少隔X秒
		synchronized (using) {
			using[idx] = false;
		}
	}

}
