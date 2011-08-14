package judge.spider;

import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class POJSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String html = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://poj.org/problem?id=" + problem.getOriginProb());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+getMethod.getStatusLine());
			}
			html = Tools.getHtml(getMethod.getResponseBodyAsStream());
		} catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		if (html.contains("<li>Can not find problem")){
			throw new Exception();
		}
		
		html = html.replaceAll("src=images", "src=http://poj.org/images");
		html = html.replaceAll("src='images", "src='http://poj.org/images");
		html = html.replaceAll("src=\"images", "src=\"http://poj.org/images");
		
		problem.setTitle(regFind(html, "<title>\\d{3,} -- ([\\s\\S]*?)</title>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(Integer.parseInt(regFind(html, "<b>Time Limit:</b> (\\d{3,})MS</td>")));
		problem.setMemoryLimit(Integer.parseInt(regFind(html, "<b>Memory Limit:</b> (\\d{2,})K</td>")));
		description.setDescription(regFind(html, "<p class=\"pst\">Description</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setInput(regFind(html, "<p class=\"pst\">Input</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setOutput(regFind(html, "<p class=\"pst\">Output</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setSampleInput(regFind(html, "<p class=\"pst\">Sample Input</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setSampleOutput(regFind(html, "<p class=\"pst\">Sample Output</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setSource(regFind(html, "<p class=\"pst\">Source</p>([\\s\\S]*?)</td></tr></table>"));
		problem.setSource(problem.getSource().replaceAll("<a href=\"searchproblem", "<a href=\"http://poj.org/searchproblem"));
		description.setHint(regFind(html, "<p class=\"pst\">Hint</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setUrl("http://poj.org/problem?id=" + problem.getOriginProb());
	}
}
