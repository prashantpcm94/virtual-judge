package judge.spider;

import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVALiveSpider extends Spider {

	public void crawl() throws Exception{

		String html = "";
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
			html = Tools.getHtml(getMethod.getResponseBodyAsStream(), getMethod.getResponseHeader("Content-Type"));
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		problem.setTitle(regFind(html, "<h3>" + problem.getOriginProb() + " - ([\\s\\S]+?)</h3>").trim());
		problem.setTimeLimit(Integer.parseInt(regFind(html, "Time limit: ([\\d\\.]+)").replaceAll("\\.", "")));
		problem.setMemoryLimit(0);
		problem.setUrl("http://livearchive.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=" + (Integer.parseInt(problem.getOriginProb()) - 1999));

		//抓标题、时限
		int category = Integer.parseInt(problem.getOriginProb()) / 100;
		String pdfLink = "<span style='float:right'><a target='_blank' href='http://livearchive.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".pdf'><img width='100' height='26' border='0' title='Download as PDF' alt='Download as PDF' src='http://livearchive.onlinejudge.org/components/com_onlinejudge/images/button_pdf.png'></a></span><div style='clear:both'></div>";
		description.setDescription(pdfLink);
		getMethod = new GetMethod("http://livearchive.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".html");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
				throw new Exception();
			}
			html = Tools.getHtml(getMethod.getResponseBodyAsStream(), getMethod.getResponseHeader("Content-Type"));
			html = pdfLink + html.replaceAll("(?i)(src=\"?)(?!\"*http)", "$1http://livearchive.onlinejudge.org/external/" + category + "/");
			description.setDescription(html);
		} catch (Exception e) {
			getMethod.releaseConnection();
		}
	}

}
