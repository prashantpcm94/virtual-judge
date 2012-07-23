package judge.submitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.tool.ApplicationContainer;
import judge.tool.Tools;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class CodeForcesSubmitter extends Submitter {

	static final String OJ_NAME = "CodeForces";
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
			clientList[i].getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.17) Gecko/20110420 Firefox/3.6.17");
			clientList[i].getHttpConnectionManager().getParams().setConnectionTimeout(60000);
			clientList[i].getHttpConnectionManager().getParams().setSoTimeout(60000);  
		}
		
		Map<String, String> languageList = new TreeMap<String, String>();
		languageList.put("1", "GNU C++ 4.6");
		languageList.put("2", "Microsoft Visual C++ 2005+");
		languageList.put("3", "Delphi 7");
		languageList.put("4", "Free Pascal 2");
		languageList.put("5", "Java 6");
		languageList.put("6", "PHP 5.2+");
		languageList.put("7", "Python 2.6+");
		languageList.put("8", "Ruby 1.7+");
		languageList.put("9", "C# Mono 2.6+");
		languageList.put("10", "GNU C 4");
		languageList.put("12", "Haskell GHC 6.12");
		languageList.put("14", "ActiveTcl 8.5");
		languageList.put("15", "Io-2008-01-07 (Win32)");
		languageList.put("16", "GNU C++0x 4");
		languageList.put("17", "Pike 7.8");
		sc.setAttribute("CodeForces", languageList);
	}
	
	private void getMaxRunId() throws Exception {
		GetMethod getMethod = new GetMethod("http://codeforces.com/problemset/status");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		Pattern p = Pattern.compile("submissionId=\"(\\d+)\"");

		int count = 0;
		while (true) {
			try {
				httpClient.executeMethod(getMethod);
				break;
			} catch (SocketException e) {
				if (!e.getMessage().contains("reset") || ++count > 5) {
					getMethod.releaseConnection();
					throw e;
				}
				Thread.sleep(4000);
			}
		}
		
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
		GetMethod getMethod = new GetMethod("http://codeforces.com/profile");
		int statusCode = httpClient.executeMethod(getMethod);
		if (statusCode == HttpStatus.SC_OK) {
			byte[] responseBody = getMethod.getResponseBody();
			String tLine = new String(responseBody, "UTF-8");
			if (tLine.contains("Login - Codeforces")) {
				throw new Exception();
			}
		} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			if (getMethod.getResponseHeader("Location").getValue().contains("enter")) {
				throw new Exception();
			}
		}

		String source = submission.getSource() + "\n";
		int random = (int) (Math.random() * 87654321);
		while (random > 0) {
			source += random % 2 == 0 ? ' ' : '\t';
			random /= 2;
		}
		String contestId = submission.getOriginProb().substring(0, submission.getOriginProb().length() - 1);
		String problemNum = submission.getOriginProb().substring(submission.getOriginProb().length() - 1);

		PostMethod postMethod = new PostMethod("http://codeforces.com/problemset/submit");
		postMethod.addParameter("_tta", getTTA());
		postMethod.addParameter("action", "submitSolutionFormSubmitted");
		postMethod.addParameter("submittedProblemCode", contestId + problemNum);
		postMethod.addParameter("programTypeId", submission.getLanguage());
		postMethod.addParameter("source", source);
		postMethod.addParameter("sourceFile", "");
		postMethod.addParameter("sourceCodeConfirmed", "true");
		postMethod.addParameter("doNotShowWarningAgain", "on");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		httpClient.getParams().setContentCharset("UTF-8"); 

		System.out.println("submit...");
		statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);
		
		String tLine = new String(postMethod.getResponseBody(), "UTF-8");
		if (tLine.contains("error for__programTypeId")) {
			throw new Exception("judge_exception:Language Error");
		}
		if (tLine.contains("error for__source")) {
			throw new Exception("judge_exception:Source Code Error");
		}
		
		if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY){
			throw new Exception();
		}
		if (!postMethod.getResponseHeader("Location").getValue().contains("status")) {
			throw new Exception();
		}
	}
	
	private void login(String username, String password) throws Exception{
		PostMethod postMethod = new PostMethod("http://codeforces.com/enter");
		postMethod.addParameter("_tta", getTTA());
		postMethod.addParameter("handle", username);
		postMethod.addParameter("password", password);
		postMethod.addParameter("remember", "on");
		postMethod.addParameter("submitted", "true");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		System.out.println("login...");
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);

		//注意:此处判断登陆成功条件并不充分,相当于默认成功
		if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY){
			throw new Exception();
		}
	}
	
	private String getTTA() throws HttpException, IOException {
		String _39ce7 = null;
		for (Cookie cookie : httpClient.getState().getCookies()) {
			if (cookie.getName().equals("39ce7")) {
				_39ce7 = cookie.getValue();
			}
		}
		if (_39ce7 == null) {
			GetMethod getMethod = new GetMethod("http://codeforces.com");
			httpClient.executeMethod(getMethod);
			for (Cookie cookie : httpClient.getState().getCookies()) {
				if (cookie.getName().equals("39ce7")) {
					_39ce7 = cookie.getValue();
				}
			}
		}
		
	    Integer tta  = 0;
	    for(int c = 0; c < _39ce7.length(); c++){
	    	tta = (tta + (c + 1) * (c + 2) * _39ce7.charAt(c)) % 1009;
	        if(c % 3 == 0) tta++;
	        if(c % 2 == 0) tta *= 2;
	        if(c > 0) tta -= ((int)(_39ce7.charAt(c / 2) / 2)) * (tta % 5);
	        while(tta < 0) tta += 1009;
	        while(tta >= 1009) tta -= 1009;
	    }
	    return tta.toString();
	}
	
	public void getResult(String username) throws Exception{
		String reg = username + "</a>    </td>[\\s\\S]*?submissionId=\"(\\d+)\" >([\\s\\S]*?)</td>[\\s\\S]*?(\\d+)[\\s\\S]*?(\\d+)", result;
		Pattern p = Pattern.compile(reg);

		GetMethod getMethod = new GetMethod("http://codeforces.com/problemset/status");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		long cur = new Date().getTime(), interval = 2000;
		while (new Date().getTime() - cur < 600000){
			System.out.println("getResult...");
			httpClient.executeMethod(getMethod);
			byte[] responseBody = getMethod.getResponseBody();
			String tLine = new String(responseBody, "UTF-8");

			Matcher m = p.matcher(tLine);
			if (m.find() && Integer.parseInt(m.group(1)) > maxRunId) {
				result = m.group(2).replaceAll("<[\\s\\S]*?>", "").trim().replaceAll("judge\\b", "judging").replaceAll("queue", "queueing");
				if (result.isEmpty()) {
					result = "processing";
				}
				submission.setStatus(result);
				submission.setRealRunId(m.group(1));
				if (!result.contains("ing")){
					if (result.equals("Accepted")){
						submission.setTime(Integer.parseInt(m.group(3)));
						submission.setMemory(Integer.parseInt(m.group(4)));
					} else if (result.contains("Compilation")) {
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
		PostMethod postMethod = new PostMethod("http://codeforces.com/data/judgeProtocol");
		postMethod.addParameter("submissionId", runId);
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		httpClient.executeMethod(postMethod);
		String additionalInfo = Tools.getHtml(postMethod, null);
		
		submission.setAdditionalInfo("<pre>" + additionalInfo.replaceAll("(\\\\r)?\\\\n", "\n").replaceAll("\\\\\\\\", "\\\\") + "</pre>");
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
			if (e.getMessage() != null && e.getMessage().startsWith("judge_exception:")){
				submission.setStatus(e.getMessage().substring(16));
			} else {
				submission.setStatus("Judging Error " + errorCode);
			}
			baseService.addOrModify(submission);
		}
		
	}

	@Override
	public void waitForUnfreeze() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	//CodeForces貌似不限制每两次提交之间的提交间隔
		synchronized (using) {
			using[idx] = false;
		}
	}

}
