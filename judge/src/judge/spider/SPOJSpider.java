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
			html = Tools.getHtml(getMethod, null);
		} catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		
		if (html.contains("Wrong problem code!") || !html.contains("<h2>SPOJ Problem Set (classical)</h2>") && !html.contains("<h2>SPOJ Problem Set (tutorial)</h2>")){
			throw new Exception();
		}

		html = html.replaceAll("src=\"/", "src=\"http://www.spoj.pl/");
		html = html.replaceAll("(?i)src=([\"']?)[\\./]{2,}content", "src=$1http://www.spoj.pl/content");
		
		problem.setTitle(Tools.regFind(html, "<h1>\\d+\\.([\\s\\S]*?)</h1>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		Double timeLimit = 1000 * Double.parseDouble(Tools.regFind(html, "Time limit:</td><td>([\\s\\S]*?)s"));
		problem.setTimeLimit(timeLimit.intValue());
		description.setDescription(Tools.regFind(html, "<p align=\"justify\">([\\s\\S]*?)(<h3|<hr|<script)"));
		description.setInput(Tools.regFind(html, "<h3[^<>]*>Input</h3>([\\s\\S]*?)(<h3|<hr|<script)"));
		description.setOutput(Tools.regFind(html, "<h3[^<>]*>Output</h3>([\\s\\S]*?)(<h3|<hr|<script)"));
		description.setSampleInput(Tools.regFind(html, "<h3[^<>]*>Example</h3>([\\s\\S]*?)(<h3|<hr|<script)"));
		description.setHint(Tools.regFind(html, "<h3[^<>]*>Explanation</h3>([\\s\\S]*?)(<h3|<hr|<script)") + Tools.regFind(html, "<h3[^<>]*>Hints*</h3>([\\s\\S]*?)(<h3|<hr|<script)"));
		
		if (description.getDescription().isEmpty() || 
			description.getInput().isEmpty() || 
			description.getOutput().isEmpty() || 
			description.getSampleInput().isEmpty()) {
			description.setDescription(Tools.regFind(html, "<p align=\"justify\">([\\s\\S]*?)<script type=\"text/javascript\"><!--\\s*google_ad_client"));
			description.setInput(null);
			description.setOutput(null);
			description.setSampleInput(null);
			description.setSampleOutput(null);
			description.setHint(null);
		}
		
		problem.setSource(Tools.regFind(html, "Resource:</td><td>([\\s\\S]*?)</td></tr>"));
		problem.setUrl("http://www.spoj.pl/problems/" + problem.getOriginProb());
	}
}
