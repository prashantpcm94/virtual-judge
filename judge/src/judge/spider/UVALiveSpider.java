package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVALiveSpider extends Spider {

	public void crawl() throws Exception{

		String tLine = "";
		HttpClient httpClient = new HttpClient();
		if (!problem.getOriginProb().matches("\\d+")) {
			throw new Exception();
		}
		
		//抓描述
		GetMethod getMethod = new GetMethod("http://livearchive.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=" + (Integer.parseInt(problem.getOriginProb()) - 1999));
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
		problem.setTitle(regFind(tLine, "<h3>" + problem.getOriginProb() + " - ([\\s\\S]+?)</h3>"));
		problem.setTimeLimit(Integer.parseInt(regFind(tLine, "Time limit: ([\\d\\.]+)").replaceAll("\\.", "")));
		problem.setMemoryLimit(0);
		problem.setUrl("http://livearchive.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=" + (Integer.parseInt(problem.getOriginProb()) - 1999));

		//抓标题、时限
		int category = Integer.parseInt(problem.getOriginProb()) / 100;
		getMethod = new GetMethod("http://livearchive.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".html");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
				throw new Exception();
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
			tLine = tLine.replaceAll("(?i)(src=\"?)(?!\"*http)", "$1http://livearchive.onlinejudge.org/external/" + category + "/");
			description.setDescription(tLine);
		} catch (Exception e) {
			getMethod.releaseConnection();
		}
	}

	@Override
	public void extraOptr() throws Exception {
	}
}
