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
	
	private void getMaxRunId() throws Exception {
		// 获取当前最大RunID
		GetMethod getMethod = new GetMethod("http://www.spoj.pl/status");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(10, true));
		Pattern p = Pattern.compile("id=\"max_id\" value=\"(\\d+)");

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
		PostMethod postMethod = new PostMethod("http://www.spoj.pl/submit/complete/");

		postMethod.addParameter("lang", submission.getLanguage());
		postMethod.addParameter("login_user", username);
		postMethod.addParameter("password", password);
		postMethod.addParameter("problemcode", problem.getOriginProb());
		postMethod.addParameter("file", submission.getSource());
		postMethod.addParameter("submit", "Send");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		httpClient.getParams().setContentCharset("UTF-8"); 

		System.out.println("submit...");
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode = " + statusCode);
		byte[] responseBody = postMethod.getResponseBody();
		String tLine = new String(responseBody, "UTF-8");
		if (tLine.contains("submit in this language for this problem")){
			submission.setStatus("Language Error");
			baseService.modify(submission);
			throw new Exception();
		}
		//注意:此处判断登陆成功条件并不充分,相当于默认成功
	}
	
	public void getResult(String username) throws Exception{
		String reg = "id=\"max_id\" value=\"(\\d+)[\\s\\S]*?<td class=\"statusres\"[\\s\\S]*?>([\\s\\S]*?)</td>\n<td[\\s\\S]*?>([\\s\\S]*?)</td>\n<td[\\s\\S]*?>([\\s\\S]*?)</td>", result;
		Pattern p = Pattern.compile(reg);
		GetMethod getMethod = new GetMethod("http://www.spoj.pl/status/" + username);
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
					if (result.equals("accepted")){
						submission.setStatus("Accepted");
						result = m.group(4).trim();
						int mul = result.contains("M") ? 1024 : 1;
						submission.setMemory((int)(0.5 + mul * Double.parseDouble(result.replaceAll("[Mk]", ""))));
						submission.setTime((int)(0.5 + 1000 * Double.parseDouble(m.group(3).replaceAll("<[\\s\\S]*?>", "").trim())));
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
			if (!"Language Error".equals(submission.getStatus())){
				submission.setStatus("Judging Error");
				baseService.modify(submission);
			}
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
