package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HDUSpider extends Spider {
	
	public void run() {
		
		String tLine = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://acm.hdu.edu.cn/showproblem.php?pid=" + problem.getOriginProb());
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
        }

        if (tLine.contains("<DIV>No such problem")){
        	baseService.delete(problem);
        	return;
        }
        
        tLine = tLine.replaceAll("src=[\\S]*?/images", "src=http://acm.hdu.edu.cn/data/images");
		System.out.println(tLine);
		
		problem.setTitle(regFind(tLine, "color:#1A5CC8'>([\\s\\S]*?)</h1>"));
		problem.setTimeLimit(Integer.parseInt(regFind(tLine, "(\\d*) MS")));
		problem.setMemoryLimit(Integer.parseInt(regFind(tLine, "/(\\d*) K")));
		problem.setDescription(regFind(tLine, "Problem Description</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		problem.setInput(regFind(tLine, "Input</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		problem.setOutput(regFind(tLine, "Output</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		problem.setSampleInput(regFind(tLine, "Sample Input</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		problem.setSampleOutput(regFind(tLine, "Sample Output</div>([\\s\\S]*?)(<br><[^<>]*?panel_title[^<>]*?>|<[^<>]*?><[^<>]*?><i>Hint)") + "</div></div>");
		problem.setHint(regFind(tLine, "<i>Hint</i></div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		problem.setSource(regFind(tLine, "Source</div> <div class=panel_content>([\\s\\S]*?)<[^<>]*?panel_[^<>]*?>"));
		if (problem.getSource() != null){
			problem.setSource(problem.getSource().replaceAll("<[\\s\\S]*?>", ""));
		}
		problem.setUrl("http://acm.hdu.edu.cn/showproblem.php?pid=" + problem.getOriginProb());
		baseService.modify(problem);
	}

}
