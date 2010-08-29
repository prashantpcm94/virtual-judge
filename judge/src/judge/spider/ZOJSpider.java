package judge.spider;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class ZOJSpider extends Spider {
	

	public void run() {
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=" + problem.getOriginProb());
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
			e.printStackTrace();
			baseService.delete(problem);
			return;
        }
        
        if (tLine.contains("No such problem.")){
        	baseService.delete(problem);
        	return;
        }

		tLine = tLine.replaceAll("showImage\\.do", "http://acm.zju.edu.cn/onlinejudge/showImage.do");
		
		problem.setTitle(regFind(tLine, "<span class=\"bigProblemTitle\">([\\s\\S]*?)</span>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			baseService.delete(problem);
			return;
		}
		problem.setTimeLimit(1000 * Integer.parseInt(regFind(tLine, "Time Limit: </font> ([\\s\\S]*?) Second")));
		problem.setMemoryLimit(Integer.parseInt(regFind(tLine, "Memory Limit: </font> ([\\s\\S]*?) KB")));
		if (tLine.contains("Input<") && tLine.contains("Output<") && tLine.contains("Sample Input<") && tLine.contains("Sample Output<")){
			problem.setDescription(regFind(tLine, "KB[\\s\\S]*?</center>[\\s\\S]*?<hr>([\\s\\S]*?)>[\\s]*Input"));
			problem.setInput(regFind(tLine, ">[\\s]*Input([\\s\\S]*?)>[\\s]*Output"));
			problem.setOutput(regFind(tLine, ">[\\s]*Output([\\s\\S]*?)>[\\s]*Sample Input"));
			problem.setSampleInput(regFind(tLine, ">[\\s]*Sample Input([\\s\\S]*?)>[\\s]*Sample Output"));
			problem.setSampleOutput(regFind(tLine, ">[\\s]*Sample Output([\\s\\S]*?)<hr"));
		} else {
			problem.setDescription(regFind(tLine, "KB[\\s\\S]*?</center>[\\s\\S]*?<hr>([\\s\\S]*?)<hr"));
		}
		problem.setSource(regFind(tLine, "Source: <strong>([\\s\\S]*?)</strong><br>"));
		problem.setUrl("http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=" + problem.getOriginProb());
		baseService.modify(problem);
	}

}
