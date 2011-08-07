package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class URALSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String tLine = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://acm.timus.ru/print.aspx?space=1&num=" + problem.getOriginProb());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
		}
		catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		
		if (tLine.contains(">Problem not found</DIV>")){
			throw new Exception();
		}

		tLine = tLine.replaceAll("SRC=\"", "SRC=\"http://acm.timus.ru");
		
		problem.setTitle(regFind(tLine, "problem_title\">\\d{4}. ([\\s\\S]*?)</H2>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		problem.setTimeLimit((int)(1000 * Double.parseDouble(regFind(tLine, "Time Limit: ([\\d\\.]*?) second"))));
		problem.setMemoryLimit(1024 * Integer.parseInt(regFind(tLine, "Memory Limit: ([\\d\\.]*?) MB")));
		description.setDescription(regFind(tLine, "problem_text\">([\\s\\S]*?)<H3 CLASS=\"problem_subtitle\">Input"));
		description.setInput(regFind(tLine, "problem_subtitle\">Input</H3>([\\s\\S]*?)<H3 CLASS=\"problem_subtitle\">Output"));
		description.setOutput(regFind(tLine, "problem_subtitle\">Output</H3>([\\s\\S]*?)<H3 CLASS=\"problem_subtitle\">Sample"));
		description.setSampleInput("<style type=\"text/css\">TABLE.sample{border-collapse:collapse;border: solid 1px #1A5CC8;}TABLE.sample TR TD, TABLE.sample TR TH{border: solid 1px #1A5CC8;vertical-align: top;padding: 3px;}TABLE.sample TR TH{color: #1A5CC8;}</style>"
				+ regFind(tLine, "problem_subtitle\">Samples*</H3>([\\s\\S]*?)(<DIV CLASS=\"problem_source\">|<H3 CLASS=\"problem_subtitle\">Hint)"));
		description.setHint(regFind(tLine, "problem_subtitle\">Hint</H3>([\\s\\S]*?)<DIV CLASS=\"problem_source"));

		problem.setSource(regFind(tLine, "<DIV CLASS=\"problem_source\">([\\s\\S]*?)</DIV></DIV>"));
		problem.setUrl("http://acm.timus.ru/problem.aspx?space=1&num=" + problem.getOriginProb());
	}
}
