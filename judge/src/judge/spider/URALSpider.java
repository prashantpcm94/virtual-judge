package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class URALSpider extends Spider {
	
	public void run() {
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.timus.ru/print.aspx?space=1&num=" + problem.getOriginProb());
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if(statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "+getMethod.getStatusLine());
        		baseService.delete(problem);
        		return;
            }
            byte[] responseBody = getMethod.getResponseBody();
            tLine = new String(responseBody, "UTF-8");
        }
        catch(Exception e) {
			getMethod.releaseConnection();
			e.printStackTrace();
			baseService.delete(problem);
			return;
        }
        
        if (tLine.contains(">Problem not found</DIV>")){
        	baseService.delete(problem);
        	return;
        }

		tLine = tLine.replaceAll("SRC=\"", "SRC=\"http://acm.timus.ru");
		
		problem.setTitle(regFind(tLine, "problem_title\">\\d{4}. ([\\s\\S]*?)</H2>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			baseService.delete(problem);
			return;
		}
		problem.setTimeLimit((int)(1000 * Double.parseDouble(regFind(tLine, "Time Limit: ([\\d\\.]*?) second"))));
		problem.setMemoryLimit(1024 * Integer.parseInt(regFind(tLine, "Memory Limit: ([\\d\\.]*?) MB")));
		problem.setDescription(regFind(tLine, "problem_text\">([\\s\\S]*?)<H3 CLASS=\"problem_subtitle\">Input"));
		problem.setInput(regFind(tLine, "problem_subtitle\">Input</H3>([\\s\\S]*?)<H3 CLASS=\"problem_subtitle\">Output"));
		problem.setOutput(regFind(tLine, "problem_subtitle\">Output</H3>([\\s\\S]*?)<H3 CLASS=\"problem_subtitle\">Sample"));
		problem.setSampleInput("<style type=\"text/css\">TABLE.sample{border-collapse:collapse;border: solid 1px #1A5CC8;}TABLE.sample TR TD, TABLE.sample TR TH{border: solid 1px #1A5CC8;vertical-align: top;padding: 3px;}TABLE.sample TR TH{color: #1A5CC8;}</style>"
				 + regFind(tLine, "problem_subtitle\">Samples*</H3>([\\s\\S]*?)(<DIV CLASS=\"problem_source\">|<H3 CLASS=\"problem_subtitle\">Hint)"));
		problem.setSource(regFind(tLine, "<DIV CLASS=\"problem_source\">([\\s\\S]*?)</DIV></DIV>"));
		problem.setHint(regFind(tLine, "problem_subtitle\">Hint</H3>([\\s\\S]*?)<DIV CLASS=\"problem_source"));
		problem.setUrl("http://acm.timus.ru/problem.aspx?space=1&num=" + problem.getOriginProb());

		baseService.modify(problem);
	}

}
