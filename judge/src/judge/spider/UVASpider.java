package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVASpider extends Spider {

	public void crawl() throws Exception{

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
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		tLine = tLine.replaceAll("((SRC=\")|(src=\"))(?!http)", "src=\"http://uva.onlinejudge.org/external/" + category + "/");
		tLine = tLine.replaceAll("((SRC=)|(src=))(?!\"*http)", "src=http://uva.onlinejudge.org/external/" + category + "/");

		problem.setTitle("UVa P" + problem.getOriginProb());
		problem.setTimeLimit(0);
		problem.setMemoryLimit(0);
		description.setDescription(regFind(tLine, "<body[\\s\\S]*?>([\\s\\S]*)</body>", 1));
		problem.setUrl("http://uva.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".html");
	}
}
