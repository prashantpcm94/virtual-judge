package judge.spider;

import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class AizuSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String html = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=" + problem.getOriginProb());
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

		if (html.contains("Time Limit :  sec, Memory Limit :  KB")){
			throw new Exception();
		}
		
		html = html.replaceAll("src=(['\"]?)IMAGE(\\d+)", "src=$1http://judge.u-aizu.ac.jp/onlinejudge/IMAGE$2");
		
		problem.setTitle(Tools.regFind(html, "<h1 class=\"title\">([\\s\\S]*?)</h1>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(1000 * Integer.parseInt(Tools.regFind(html, "Time Limit : (\\d+) sec")));
		problem.setMemoryLimit(Integer.parseInt(Tools.regFind(html, "Memory Limit : (\\d+) KB")));
		description.setDescription(Tools.regFind(html, "<div class=\"description\">[\\s\\S]*?</h1>([\\s\\S]*?)<hr>"));
		if (description.getDescription().isEmpty()) {
			description.setDescription(Tools.regFind(html, "<div class=\"description\">([\\s\\S]*?)<hr>"));
		}
		problem.setSource(Tools.regFind(html, "style=\"font-size:10pt\">\\s*Source:([\\s\\S]*?)</div>"));
		problem.setUrl("http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=" + problem.getOriginProb());
	}
}
