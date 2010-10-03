package judge.spider;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class SGUSpider extends Spider {
	

	public void run() {
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.sgu.ru/problem.php?contest=0&problem=" + problem.getOriginProb());
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

        if (tLine.contains("<h4>no such problem</h4>")){
        	baseService.delete(problem);
        	return;
        }
        
		problem.setTitle(regFind(tLine, "\\d{3}\\. ([\\s\\S]*?)</title>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			baseService.delete(problem);
			return;
		}
		
		String tl = regFind(tLine, "ime limit per test: ([\\d\\.]*)");
		if (tl != null){
			problem.setTimeLimit((int)(1000 * Double.parseDouble(tl)));
		}
		
		String ml = regFind(tLine, "emory limit per test: ([\\d]*)");
		if (ml != null){
			problem.setMemoryLimit(Integer.parseInt(ml));
		} else {
			ml = regFind(tLine, "emory limit: ([\\d]*)");
			if (ml != null){
				problem.setMemoryLimit(Integer.parseInt(ml));
			}
		}
		
		if (Integer.parseInt(problem.getOriginProb()) >= 277){
			problem.setDescription(regFind(tLine, "output: standard</div><br/>([\\s\\S]*?)<b>Input</b>") + "</div>");
			problem.setInput(regFind(tLine, "<b>Input</b></div>([\\s\\S]*?)<b>Output</b>") + "</div>");
			problem.setOutput(regFind(tLine, "<b>Output</b></div>([\\s\\S]*?)<b>Example") + "</div>");
			problem.setSampleInput(regFind(tLine, "<b>Example\\(s\\)</b></div>([\\s\\S]*?)(<b>Note|<hr>)") + "</div>");
			problem.setHint(regFind(tLine, "<b>Note</b></div>([\\s\\S]*?)<hr>") + "</div>");
		} else {
			problem.setDescription(regFind(tLine, "(<BODY[\\s\\S]*?</BODY>)", 1));
			problem.setSource(regFind(tLine, "Resource:</td><td>([\\s\\S]*?)\n</td>"));
		}
		problem.setUrl("http://acm.sgu.ru/problem.php?contest=0&problem=" + problem.getOriginProb());
		baseService.modify(problem);
	}

}
