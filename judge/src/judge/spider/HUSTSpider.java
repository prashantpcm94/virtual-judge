package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class HUSTSpider extends Spider {
	
	public void run() {
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.hust.edu.cn/thx/problem.php?id=" + problem.getOriginProb());
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
        
        if (tLine.contains("<title>No Such Problem!</title>")){
        	baseService.delete(problem);
        	return;
        }

		tLine = tLine.replaceAll("src='\\.", "src='http://acm.hust.edu.cn/thx");
		
		problem.setTitle(regFind(tLine, "<title>[\\s\\S]*?-- ([\\s\\S]*?)</title>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			baseService.delete(problem);
			return;
		}
		
		problem.setTimeLimit(1000 * Integer.parseInt(regFind(tLine, "Time Limit: </b>([\\d\\.]*?) Sec")));
		problem.setMemoryLimit(1024 * Integer.parseInt(regFind(tLine, "Memory Limit: </b>([\\d\\.]*?) MB")));
		problem.setDescription(regFind(tLine, "<h2>Description</h2>([\\s\\S]*?)<h2>"));
		problem.setInput(regFind(tLine, "<h2>Input</h2>([\\s\\S]*?)<h2>"));
		problem.setOutput(regFind(tLine, "<h2>Output</h2>([\\s\\S]*?)<h2>"));
		problem.setSampleInput(regFind(tLine, "<h2>Sample Input</h2>([\\s\\S]*?)<h2>"));
		problem.setSampleOutput(regFind(tLine, "<h2>Sample Output</h2>([\\s\\S]*?)<h2>"));
		problem.setHint(regFind(tLine, "<h2>HINT</h2>([\\s\\S]*?)<h2>"));
		problem.setSource(regFind(tLine, "<h2>Source</h2>([\\s\\S]*?)<center>"));
		problem.setUrl("http://acm.hust.edu.cn/thx/problem.php?id=" + problem.getOriginProb());
		baseService.modify(problem);
	}

}
