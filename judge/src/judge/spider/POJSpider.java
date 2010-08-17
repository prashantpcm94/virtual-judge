package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class POJSpider extends Spider {
	
	public void run() {
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.pku.edu.cn/JudgeOnline/problem?id=" + problem.getOriginProb());
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
        }

		if (tLine.contains("<li>Can not find problem")){
			baseService.delete(problem);
			return;
		}
        
        tLine = tLine.replaceAll("src=images", "src=http://acm.pku.edu.cn/JudgeOnline/images");
		tLine = tLine.replaceAll("src='images", "src='http://acm.pku.edu.cn/JudgeOnline/images");
		tLine = tLine.replaceAll("src=\"images", "src=\"http://acm.pku.edu.cn/JudgeOnline/images");
		
		problem.setTitle(regFind(tLine, "<title>\\d{3,} -- ([\\s\\S]*?)</title>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			baseService.delete(problem);
			return;
		}
		
		problem.setTimeLimit(Integer.parseInt(regFind(tLine, "<b>Time Limit:</b> (\\d{3,})MS</td>")));
		problem.setMemoryLimit(Integer.parseInt(regFind(tLine, "<b>Memory Limit:</b> (\\d{2,})K</td>")));
		problem.setDescription(regFind(tLine, "<p class=\"pst\">Description</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setInput(regFind(tLine, "<p class=\"pst\">Input</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setOutput(regFind(tLine, "<p class=\"pst\">Output</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setSampleInput(regFind(tLine, "<p class=\"pst\">Sample Input</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setSampleOutput(regFind(tLine, "<p class=\"pst\">Sample Output</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setSource(regFind(tLine, "<p class=\"pst\">Source</p>([\\s\\S]*?)</td></tr></table>"));
		if (problem.getSource() != null){
			problem.setSource(problem.getSource().replaceAll("<a href=\"searchproblem", "<a href=\"http://acm.pku.edu.cn/JudgeOnline/searchproblem"));
		}
		problem.setHint(regFind(tLine, "<p class=\"pst\">Hint</p>([\\s\\S]*?)<p class=\"pst\">"));
		problem.setUrl("http://acm.timus.ru/problem.aspx?space=1&num=" + problem.getOriginProb());
		problem.setUrl("http://acm.pku.edu.cn/JudgeOnline/problem?id=" + problem.getOriginProb());

		baseService.modify(problem);
	}

}
