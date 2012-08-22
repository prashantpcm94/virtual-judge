package judge.spider;

import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


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
			
			String[][] pairs = new String[][]{
				{"img", "src"},
				{"script", "src"},
				{"link", "href"},
				{"a", "href"}
			};

			Document doc = Jsoup.parse(html, getMethod.getURI().toString());
			for (String[] pair : pairs) {
				Elements links = doc.select(pair[0]);
				for (Element element : links) {
					element.attr(pair[1], element.absUrl(pair[1]));
				}
			}
			
			html = doc.toString();
		} catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		if (html.contains("Time Limit :  sec, Memory Limit :  KB")) {
			throw new Exception();
		}
		
		problem.setTitle(Tools.regFind(html, "<h1 class=\"title\">([\\s\\S]*?)</h1>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(1000 * Integer.parseInt(Tools.regFind(html, "Time Limit : (\\d+) sec")));
		problem.setMemoryLimit(Integer.parseInt(Tools.regFind(html, "Memory Limit : (\\d+) KB")));
		description.setDescription(Tools.regFind(html, "<div class=\"description\">([\\s\\S]*?)<hr />").replaceAll("^[\\s\\S]*</h1>", ""));
		problem.setSource(Tools.regFind(html, "style=\"font-size:10pt\">\\s*Source:([\\s\\S]*?)</div>"));
		problem.setUrl("http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=" + problem.getOriginProb());
	}
}
