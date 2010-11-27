package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class SPOJSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://www.spoj.pl/problems/" + problem.getOriginProb());
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
        
        if (tLine.contains("Wrong problem code!") || !tLine.contains("<h2>SPOJ Problem Set (classical)</h2>")){
			throw new Exception();
        }

		tLine = tLine.replaceAll("src=\"/", "src=\"http://www.spoj.pl/");
		
		problem.setTitle(regFind(tLine, "<h1>\\d+\\.([\\s\\S]*?)</h1>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}
		if (problem.getTitle() != null){
			problem.setTitle(problem.getTitle().trim());
		}
		problem.setTimeLimit(1000 * Integer.parseInt(regFind(tLine, "Time limit:</td><td>([\\s\\S]*?)s", 1)));
		description.setDescription(regFind(tLine, "<p align=\"justify\">([\\s\\S]*?)(<h3[^<>]*>Input|<hr>)", 1));
		description.setInput(regFind(tLine, "<h3[^<>]*>Input</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		description.setOutput(regFind(tLine, "<h3[^<>]*>Output</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		description.setSampleInput(regFind(tLine, "<h3[^<>]*>Example</h3>([\\s\\S]*?)(<h3[^<>]*>|<hr>)", 1));
		description.setHint(regFind(tLine, "<h3[^<>]*>Explanation</h3>([\\s\\S]*?)<hr>", 1) + regFind(tLine, "<h3[^<>]*>Hints*</h3>([\\s\\S]*?)<hr>", 1));
		
		problem.setSource(regFind(tLine, "Resource:</td><td>([\\s\\S]*?)</td></tr>", 1));
		problem.setUrl("http://www.spoj.pl/problems/" + problem.getOriginProb());
	}

}
