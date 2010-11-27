package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class HYSBZSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://61.187.179.132:8080/JudgeOnline/showproblem?problem_id=" + problem.getOriginProb());
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
        	int statusCode = httpClient.executeMethod(getMethod);
        	if(statusCode != HttpStatus.SC_OK) {
        		System.err.println("Method failed: "+getMethod.getStatusLine());
        	}
        	byte[] responseBody = getMethod.getResponseBody();
        	tLine = new String(responseBody, "GB2312");
        }
        catch(Exception e) {
        	getMethod.releaseConnection();
        	throw new Exception();
        }

        if (tLine.contains("<li>Can not find problem")){
			throw new Exception();
        }
        
        tLine = tLine.replaceAll("src=images", "src=http://61.187.179.132:8080/JudgeOnline/images");
		tLine = tLine.replaceAll("src='images", "src='http://61.187.179.132:8080/JudgeOnline/images");
		tLine = tLine.replaceAll("src=\"images", "src=\"http://61.187.179.132:8080/JudgeOnline/images");
		
		problem.setTitle(regFind(tLine, "<title>\\d{3,} -- ([\\s\\S]*?)</title>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(Integer.parseInt(regFind(tLine, "Time Limit:(\\d{2,})MS")));
		problem.setMemoryLimit(Integer.parseInt(regFind(tLine, "Memory Limit:(\\d{2,})K")));
		description.setDescription(regFind(tLine, "size=\"5\">Description</font>\\s+</b>([\\s\\S]*?)<p align=\"left\"><b><font color=\"#333399"));
		description.setInput(regFind(tLine, "size=\"5\">Input</font>\\s+</b>([\\s\\S]*?)<p align=\"left\"><b><font color=\"#333399"));
		description.setOutput(regFind(tLine, "size=\"5\">Output</font>\\s+</b>([\\s\\S]*?)<p align=\"left\"><b><font color=\"#333399"));
		description.setSampleInput(regFind(tLine, "size=\"5\">Sample Input</font>\\s+</b>([\\s\\S]*?)<p align=\"left\"><b><font color=\"#333399"));
		description.setSampleOutput(regFind(tLine, "size=\"5\">Sample Output</font>\\s+</b>([\\s\\S]*?)<p align=\"left\"><b><font color=\"#333399"));
		description.setHint(regFind(tLine, "size=\"5\">Hint</font>\\s+</b>([\\s\\S]*?)<p align=\"left\"><b><font color=\"#333399"));
		problem.setSource(regFind(problem.getTitle(), "\\[([\\s\\S]*?)\\]"));
		problem.setUrl("http://61.187.179.132:8080/JudgeOnline/showproblem?problem_id=" + problem.getOriginProb());
	}

}
