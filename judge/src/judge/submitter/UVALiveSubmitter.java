package judge.submitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Problem;
import judge.tool.ApplicationContainer;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVALiveSubmitter extends Submitter {

	static final String OJ_NAME = "UVALive";
	static private HttpClient clientList[];
	static private boolean using[];
	static private String[] usernameList;
	static private String[] passwordList;

	static {
		List<String> uList = new ArrayList<String>(), pList = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(ApplicationContainer.sc.getRealPath("WEB-INF" + File.separator + "accounts.conf"));
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String info[] = br.readLine().split("\\s+");
				if (info.length >= 3 && info[0].equalsIgnoreCase(OJ_NAME)){
					uList.add(info[1]);
					pList.add(info[2]);
				}
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		usernameList = uList.toArray(new String[0]);
		passwordList = pList.toArray(new String[0]);
		using = new boolean[usernameList.length];
		clientList = new HttpClient[usernameList.length];
		for (int i = 0; i < clientList.length; i++){
			clientList[i] = new HttpClient();
			clientList[i].getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
		}
	}
	
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
