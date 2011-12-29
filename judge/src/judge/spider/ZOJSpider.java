package judge.spider;


import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class ZOJSpider extends Spider {
	

	public void crawl() throws Exception{
		
		String html = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=" + problem.getOriginProb());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+getMethod.getStatusLine());
			}
			html = Tools.getHtml(getMethod, null);
		} catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}
		
		if (html.contains("No such problem.")){
			throw new Exception();
		}

		html = html.replaceAll("showImage\\.do", "http://acm.zju.edu.cn/onlinejudge/showImage.do");
		
		problem.setTitle(Tools.regFind(html, "<span class=\"bigProblemTitle\">([\\s\\S]*?)</span>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		problem.setTimeLimit(1000 * Integer.parseInt(Tools.regFind(html, "Time Limit: </font> ([\\s\\S]*?) Second")));
		problem.setMemoryLimit(Integer.parseInt(Tools.regFind(html, "Memory Limit: </font> ([\\s\\S]*?) KB")));
		if (html.contains(">Input<") && html.contains(">Output<") && html.contains(">Sample Input<") && html.contains(">Sample Output<")){
			description.setDescription(Tools.regFind(html, "KB[\\s\\S]*?</center>[\\s\\S]*?<hr>([\\s\\S]*?)>[\\s]*Input"));
			description.setInput(Tools.regFindCaseSensitive(html, ">[\\s]*Input([\\s\\S]*?)>[\\s]*Out?put"));
			description.setOutput(Tools.regFindCaseSensitive(html, ">[\\s]*Out?put([\\s\\S]*?)>[\\s]*Sample Input"));
			description.setSampleInput(Tools.regFind(html, ">[\\s]*Sample Input([\\s\\S]*?)>[\\s]*Sample Out?put"));
			description.setSampleOutput(Tools.regFind(html, ">[\\s]*Sample Out?put([\\s\\S]*?)<hr"));
		} else {
			description.setDescription(Tools.regFind(html, "KB[\\s\\S]*?</center>[\\s\\S]*?<hr>([\\s\\S]*?)<hr"));
		}
		problem.setSource(Tools.regFind(html, "Source: <strong>([\\s\\S]*?)</strong><br>"));
		problem.setUrl("http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=" + problem.getOriginProb());
	}
}
