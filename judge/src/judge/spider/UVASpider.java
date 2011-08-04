package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVASpider extends Spider {
	
	public static String problemNumberMap[];
	
	public void crawl() throws Exception{
		
		if (problemNumberMap == null || problemNumberMap[12049] == null) {
			new UVaSpiderInitializer("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8").start();
		}
		do {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (UVaSpiderInitializer.threadCnt > 0);

		String realProblemNumber = problemNumberMap[Integer.parseInt(problem.getOriginProb())];

		String tLine = "";
		HttpClient httpClient = new HttpClient();
		if (!problem.getOriginProb().matches("\\d+")) {
			throw new Exception();
		}
		int category = Integer.parseInt(problem.getOriginProb()) / 100;
		GetMethod getMethod = new GetMethod("http://uva.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".html");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
				throw new Exception();
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		tLine = tLine.replaceAll("((SRC=\")|(src=\"))(?!http)", "src=\"http://uva.onlinejudge.org/external/" + category + "/");
		tLine = tLine.replaceAll("((SRC=)|(src=))(?!\"*http)", "src=http://uva.onlinejudge.org/external/" + category + "/");

		problem.setMemoryLimit(0);
		description.setDescription(regFind(tLine, "<body[\\s\\S]*?>([\\s\\S]*)</body>", 1));
		if (description.getDescription() == null || description.getDescription().trim().isEmpty()) {
			description.setDescription(tLine.replaceAll("(?i)</?html>", ""));
		}
		problem.setUrl("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=" + realProblemNumber);

		getMethod = new GetMethod(problem.getUrl());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
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
		problem.setTitle(regFind(tLine, "<h3>" + problem.getOriginProb() + " - ([\\s\\S]+?)</h3>"));
		problem.setTimeLimit(Integer.parseInt(regFind(tLine, "Time limit: ([\\d\\.]+)").replaceAll("\\.", "")));
	}

}
