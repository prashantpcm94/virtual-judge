package judge.submitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.tool.ApplicationContainer;
import judge.tool.Tools;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVASubmitter extends Submitter {

	static final String OJ_NAME = "UVA";
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
			clientList[i].getHttpConnectionManager().getParams().setConnectionTimeout(60000);
			clientList[i].getHttpConnectionManager().getParams().setSoTimeout(60000);  
		}
		
		Map<String, String> languageList = new TreeMap<String, String>();
		languageList.put("1", "ANSI C 4.1.2");
		languageList.put("2", "JAVA 1.6.0");
		languageList.put("3", "C++ 4.1.2");
		languageList.put("4", "PASCAL 2.0.4");
		sc.setAttribute("UVA", languageList);
	}
	
	private void getMaxRunId() throws Exception {
		GetMethod getMethod = new GetMethod("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=19");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		Pattern p = Pattern.compile("componentheading[\\s\\S]*?<td>(\\d{7,})");

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
	
	private void submit() throws Exception{
		PostMethod postMethod = new PostMethod("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=25&page=save_submission");
		postMethod.addParameter("problemid", "");
		postMethod.addParameter("category", "");
		postMethod.addParameter("localid", submission.getOriginProb());
		postMethod.addParameter("language", submission.getLanguage());
		postMethod.addParameter("code", submission.getSource());
		postMethod.addParameter("codeupl", "");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		httpClient.getParams().setContentCharset("UTF-8"); 

		System.out.println("submit...");
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);
		
		if (statusCode != HttpStatus.SC_MOVED_PERMANENTLY){
			throw new Exception();
		}
		String headerLocation = postMethod.getResponseHeader("Location").getValue();
		if (!headerLocation.contains("Submission+received+with+ID")){
			throw new Exception();
		}
		String submissionId = Tools.regFind(headerLocation, "with\\+ID\\+(\\d+)");
		submission.setRealRunId(submissionId);
		baseService.addOrModify(submission);
	}
	
	private void login(String username, String password) throws Exception{
		PostMethod postMethod = new PostMethod("http://uva.onlinejudge.org/index.php?option=com_comprofiler&task=login");
		GetMethod getMethod = new GetMethod("http://uva.onlinejudge.org/index.php");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		String tLine = "";
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		
		String reg = "<input type=\"hidden\" name=\"([\\s\\S]*?)\" value=\"([\\s\\S]*?)\" />";
		Matcher matcher = Pattern.compile(reg).matcher(tLine);
		int number = 0;
		while (matcher.find()){
			String name = matcher.group(1);
			String value = matcher.group(2);
			if (number > 0 && number < 9) {
				postMethod.addParameter(name, value);
			}
			++number;
		}
		postMethod.addParameter("remember", "yes");
		postMethod.addParameter("username", username);
		postMethod.addParameter("passwd", password);
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		System.out.println("login...");
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);

		//注意:此处判断登陆成功条件并不充分,相当于默认成功
		if (statusCode != HttpStatus.SC_MOVED_PERMANENTLY){
			throw new Exception();
		}
	}
	
	public void getResult(String username) throws Exception{
		String reg = "<td>(\\d{7,})</td>[\\s\\S]*?show_problem&amp;problem=(\\d+)[\\s\\S]*?</td>[\\s\\S]*?</td>[\\s\\S]*?<td>([\\s\\S]*?)</td>[\\s\\S]*?</td>[\\s\\S]*?<td>([\\s\\S]*?)</td>", result;
		Pattern p = Pattern.compile(reg);

		GetMethod getMethod = new GetMethod("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=9");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		long cur = new Date().getTime(), interval = 2000;
		while (new Date().getTime() - cur < 900000){
			System.out.println("getResult...");
			httpClient.executeMethod(getMethod);
			byte[] responseBody = getMethod.getResponseBody();
			String tLine = new String(responseBody, "UTF-8");

			Matcher m = p.matcher(tLine);
			if (m.find() && Integer.parseInt(m.group(1)) > maxRunId) {
				result = m.group(3).replaceAll("<[\\s\\S]*?>", "").trim().replaceAll("judge", "judging").replaceAll("queue", "queueing").replaceAll("Received", "processing");
				if (result.isEmpty()) {
					result = "processing";
				}
				submission.setStatus(result);
				//submission.setRealRunId(m.group(1));
				if (!result.contains("ing")){
					if (result.equals("Accepted")){
						submission.setTime(Integer.parseInt(m.group(4).replaceAll("\\.", "")));
					} else if (result.contains("Compilation error")) {
						getAdditionalInfo(submission.getRealRunId());
					}
					baseService.addOrModify(submission);
					return;
				}
				baseService.addOrModify(submission);
			}
			Thread.sleep(interval);
			interval += 500;
		}
		throw new Exception();
	}
	
	private void getAdditionalInfo(String runId) throws HttpException, IOException {
		GetMethod getMethod = new GetMethod("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=9&page=show_compilationerror&submission=" + runId);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		httpClient.executeMethod(getMethod);
		String additionalInfo = Tools.getHtml(getMethod, null);
		
		submission.setAdditionalInfo(Tools.regFind(additionalInfo, "Compilation error for submission " + runId + "</div>\\s*(<pre>[\\s\\S]*?</pre>)"));
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
	
	public void work() {
		idx = getIdleClient();
		int errorCode = 1;

		try {
			getMaxRunId();
			try {
				//第一次尝试提交
				submit();
			} catch (Exception e1) {
				//失败,认为是未登录所致
				e1.printStackTrace();
				Thread.sleep(2000);
				login(usernameList[idx], passwordList[idx]);
				Thread.sleep(2000);
				submit();
			}
			errorCode = 2;
			submission.setStatus("Running & Judging");
			baseService.addOrModify(submission);
			Thread.sleep(2000);
			getResult(usernameList[idx]);
		} catch (Exception e) {
			e.printStackTrace();
			submission.setStatus("Judging Error " + errorCode);
			baseService.addOrModify(submission);
		}
		
	}

	@Override
	public void waitForUnfreeze() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	//POJ限制每两次提交之间至少隔3秒
		synchronized (using) {
			using[idx] = false;
		}
	}
}
