package judge.spider;

import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class SPOJSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String html = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://www.spoj.pl/problems/" + problem.getOriginProb());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+getMethod.getStatusLine());
			}
			html = Tools.getHtml(getMethod.getResponseBodyAsStream(), getMethod.getResponseHeader("Content-Type"));
		} catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		
		if (html.contains("Wrong problem code!") || !html.contains("<h2>SPOJ Problem Set (classical)</h2>") && !html.contains("<h2>SPOJ Problem Set (tutorial)</h2>")){
			throw new Exception();
		}

		html = html.replaceAll("src=\"/", "src=\"http://www.spoj.pl/");
		
		problem.setTitle(regFind(html, "<h1>\\d+\\.([\\s\\S]*?)</h1>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		Double timeLimit = 1000 * Double.parseDouble(regFind(html, "Time limit:</td><td>([\\s\\S]*?)s", 1));
		problem.setTimeLimit(timeLimit.intValue());
		description.setDescription(regFind(html, "<p align=\"justify\">([\\s\\S]*?)(<h3[^<>]*>Input|<hr>)", 1));
		description.setInput(regFind(html, "<h3[^<>]*>Input</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		description.setOutput(regFind(html, "<h3[^<>]*>Output</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		description.setSampleInput(regFind(html, "<h3[^<>]*>Example</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		description.setHint(regFind(html, "<h3[^<>]*>Explanation</h3>([\\s\\S]*?)<hr>", 1) + regFind(html, "<h3[^<>]*>Hints*</h3>([\\s\\S]*?)<hr>", 1));
		
		problem.setSource(regFind(html, "Resource:</td><td>([\\s\\S]*?)</td></tr>", 1));
		problem.setUrl("http://www.spoj.pl/problems/" + problem.getOriginProb());
	}
}
