package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class SPOJSpider extends Spider {
	
	public void run() {
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://www.spoj.pl/problems/" + problem.getOriginProb());
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
        }
        
        if (tLine.contains("Wrong problem code!") || !tLine.contains("<h2>SPOJ Problem Set (classical)</h2>")){
        	baseService.delete(problem);
        	return;
        }

		tLine = tLine.replaceAll("src=\"/", "src=\"http://www.spoj.pl/");
		
		problem.setTitle(regFind(tLine, "<h1>\\d+\\.([\\s\\S]*?)</h1>"));
		if (problem.getTitle() != null){
			problem.setTitle(problem.getTitle().trim());
		}
		problem.setTimeLimit(1000 * Integer.parseInt(regFind(tLine, "Time limit:</td><td>([\\s\\S]*?)s", 1)));
		problem.setDescription(regFind(tLine, "<p align=\"justify\">([\\s\\S]*?)(<h3[^<>]*>Input|<hr>)", 1));
		problem.setInput(regFind(tLine, "<h3[^<>]*>Input</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		problem.setOutput(regFind(tLine, "<h3[^<>]*>Output</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		problem.setSampleInput(regFind(tLine, "<h3[^<>]*>Example</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		problem.setHint(regFind(tLine, "<h3[^<>]*>Explanation</h3>([\\s\\S]*?)<hr>", 1));
		problem.setSource(regFind(tLine, "Resource:</td><td>([\\s\\S]*?)</td></tr>", 1));
		problem.setUrl("http://www.spoj.pl/problems/" + problem.getOriginProb());
		baseService.modify(problem);
	}

}
