package judge.spider;

import java.util.Date;

import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVASpider extends Spider {
	
	public static String problemNumberMap[];
	public static Long lastTime = 0L;
	
	public void crawl() throws Exception{
		if (new Date().getTime() - lastTime > 7 * 86400 * 1000L) {
			problemNumberMap = null;
			lastTime = new Date().getTime();
		}
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
		if (realProblemNumber == null) {
			throw new Exception();
		}

		String html = "";
		HttpClient httpClient = new HttpClient();
		int category = Integer.parseInt(problem.getOriginProb()) / 100;
		GetMethod getMethod = new GetMethod("http://uva.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".html");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
				throw new Exception();
			}
			html = Tools.getHtml(getMethod.getResponseBodyAsStream(), getMethod.getResponseHeader("Content-Type"));
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		html = html.replaceAll("((SRC=\")|(src=\"))(?!http)", "src=\"http://uva.onlinejudge.org/external/" + category + "/");
		html = html.replaceAll("((SRC=)|(src=))(?!\"*http)", "src=http://uva.onlinejudge.org/external/" + category + "/");

		problem.setMemoryLimit(0);
		description.setDescription(regFind(html, "<body[\\s\\S]*?>([\\s\\S]*)</body>", 1).replaceAll("(?i)</?html>", ""));
		problem.setUrl("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=" + realProblemNumber);

		getMethod = new GetMethod(problem.getUrl());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			html = Tools.getHtml(getMethod.getResponseBodyAsStream(), getMethod.getResponseHeader("Content-Type"));
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		problem.setTitle(regFind(html, "<h3>" + problem.getOriginProb() + " - ([\\s\\S]+?)</h3>").trim());
		problem.setTimeLimit(Integer.parseInt(regFind(html, "Time limit: ([\\d\\.]+)").replaceAll("\\.", "")));
	}

}
