package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class ZTreningSpider extends Spider {
	
	public void crawl() throws Exception{
		
		if (!problem.getOriginProb().matches("\\d{1,6}")) {
			throw new Exception();
		}
		problem.setUrl("http://www.z-trening.com/tasks.php?show_task=" + (5000000000L + Integer.parseInt(problem.getOriginProb())) + "&lang=uk");

		String tLine = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(problem.getUrl());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
		}
		catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		problem.setTitle(regFind(tLine, "<TITLE>Task :: ([\\s\\S]*?)</TITLE>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}
		Double timeLimit = 1000 * Double.parseDouble(regFind(tLine, "Time:</TD><TD CLASS=\"right\">(\\S*?) sec"));
		problem.setTimeLimit(timeLimit.intValue());
		problem.setMemoryLimit(1024 * Integer.parseInt(regFind(tLine, "Memory:</TD><TD CLASS=\"right\">(\\d+) MB")));
		description.setDescription(regFind(tLine, "<DIV CLASS=\"taskText\">([\\s\\S]*?)</DIV></DIV><DIV CLASS=\"boxHeader\">Submit Solution"));
	}
}
