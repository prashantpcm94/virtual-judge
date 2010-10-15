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

public class SGUSubmitter extends Submitter {

	static private HttpClient clientList[] = new HttpClient[5];
	static {
		for (int i = 0; i < clientList.length; i++){
			clientList[i] = new HttpClient();
			clientList[i].getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
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
	
	private void getMaxRunId() throws Exception {
		// 获取当前最大RunID
		GetMethod getMethod = new GetMethod("http://acm.sgu.ru/status.php");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		Pattern p = Pattern.compile("<TD>(\\d{7,})</TD>");

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
	
	
	private void submit(String username, String password) throws Exception{
		Problem problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
        PostMethod postMethod = new PostMethod("http://acm.sgu.ru/sendfile.php?contest=0");
        postMethod.addParameter("elang", submission.getLanguage());
        postMethod.addParameter("id", username);
        postMethod.addParameter("pass", password);
        postMethod.addParameter("problem", problem.getOriginProb());
        postMethod.addParameter("source", submission.getSource());
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        httpClient.getParams().setContentCharset("UTF-8");

        System.out.println("submit...");
		httpClient.executeMethod(postMethod);
		byte[] responseBody = postMethod.getResponseBody();
		String tLine = new String(responseBody, "UTF-8");
		if (!tLine.contains("successfully submitted")){
			throw new Exception();
		}
	}
	
	public void getResult(String username) throws Exception{
		String reg = "<TD>(\\d{7,})</TD>[\\s\\S]*?<TD class=btab>([\\s\\S]*?)</TD>[\\s\\S]*?([\\d]*?) ms</TD><TD>([\\d]*?) kb</TD>", result;
		Pattern p = Pattern.compile(reg);

		GetMethod getMethod = new GetMethod("http://acm.sgu.ru/status.php?idmode=1&id=" + username);
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

		try {
			getMaxRunId();
				
			submit(usernameList[idx], passwordList[idx]);	//非登陆式,只需交一次
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
			Thread.sleep(35000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	//sgu限制每两次提交之间至少隔30秒
		synchronized (using) {
			using[idx] = false;
		}
	}
	

}
