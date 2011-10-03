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
			html = Tools.getHtml(getMethod, null);
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		problem.setTitle(Tools.regFind(html, "<h3>" + problem.getOriginProb() + " - ([\\s\\S]+?)</h3>").trim());
		problem.setTimeLimit(Integer.parseInt(Tools.regFind(html, "Time limit: ([\\d\\.]+)").replaceAll("\\.", "")));
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
			html = Tools.getHtml(getMethod, null).replaceAll("(?i)(src|href)\\s*=\\s*(['\"]?)\\s*(?!\\s*['\"]?\\s*http)", "$1=$2http://livearchive.onlinejudge.org/external/" + category + "/");
			description.setDescription(pdfLink + Tools.regFind(html, "^([\\s\\S]*?)<H2><FONT size=4 COLOR=#ff0000><A NAME=\"SECTION000100\\d000000000000000\">"));
			description.setInput(Tools.regFind(html, "Int?put</A>&nbsp;</FONT>\\s*</H2>([\\s\\S]*?)<H2><FONT size=4 COLOR=#ff0000><A NAME=\"SECTION000100\\d000000000000000\">"));
			description.setOutput(Tools.regFind(html, "Output</A>&nbsp;</FONT>\\s*</H2>([\\s\\S]*?)<H2><FONT size=4 COLOR=#ff0000><A NAME=\"SECTION000100\\d000000000000000\">"));
			description.setSampleInput(Tools.regFind(html, "Sample Int?put</A>&nbsp;</FONT>\\s*</H2>([\\s\\S]*?)<H2><FONT size=4 COLOR=#ff0000><A NAME=\"SECTION000100\\d000000000000000\">"));
			description.setSampleOutput(Tools.regFind(html, "Sample Output</A>&nbsp;</FONT>\\s*</H2>([\\s\\S]*)"));
			
			if (description.getSampleInput().isEmpty() || description.getSampleOutput().isEmpty()) {
				description.setDescription(pdfLink + html);
				description.setInput(null);
				description.setOutput(null);
				description.setSampleInput(null);
				description.setSampleOutput(null);
			}
		} catch (Exception e) {
			getMethod.releaseConnection();
		}
	}

}
