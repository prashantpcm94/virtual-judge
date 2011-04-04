package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class POJSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://poj.org/problem?id=" + problem.getOriginProb());
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

		if (tLine.contains("<li>Can not find problem")){
			throw new Exception();
		}
        
        tLine = tLine.replaceAll("src=images", "src=http://poj.org/images");
		tLine = tLine.replaceAll("src='images", "src='http://poj.org/images");
		tLine = tLine.replaceAll("src=\"images", "src=\"http://poj.org/images");
		
		problem.setTitle(regFind(tLine, "<title>\\d{3,} -- ([\\s\\S]*?)</title>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(Integer.parseInt(regFind(tLine, "<b>Time Limit:</b> (\\d{3,})MS</td>")));
		problem.setMemoryLimit(Integer.parseInt(regFind(tLine, "<b>Memory Limit:</b> (\\d{2,})K</td>")));
		description.setDescription(regFind(tLine, "<p class=\"pst\">Description</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setInput(regFind(tLine, "<p class=\"pst\">Input</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setOutput(regFind(tLine, "<p class=\"pst\">Output</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setSampleInput(regFind(tLine, "<p class=\"pst\">Sample Input</p>([\\s\\S]*?)<p class=\"pst\">"));
		description.setSampleOutput(regFind(tLine, "<p class=\"pst\">Sample Output</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setSource(regFind(tLine, "<p class=\"pst\">Source</p>([\\s\\S]*?)</td></tr></table>"));
		if (problem.getSource() != null){
			problem.setSource(problem.getSource().replaceAll("<a href=\"searchproblem", "<a href=\"http://poj.org/searchproblem"));
		}
		description.setHint(regFind(tLine, "<p class=\"pst\">Hint</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setUrl("http://poj.org/problem?id=" + problem.getOriginProb());
	}

	@Override
	public void extraOptr() throws Exception {
	}

}
