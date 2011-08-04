package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HDUSpider extends Spider {
	
	public void crawl() throws Exception{
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
			throw new Exception();
		}

		if (tLine.contains("<DIV>No such problem")){
			throw new Exception();
		}
		
		tLine = tLine.replaceAll("src=[\\S]*?/images", "src=http://acm.hdu.edu.cn/data/images");
		tLine = tLine.replaceAll("src='[\\S]*?/images", "src='http://acm.hdu.edu.cn/data/images");
		tLine = tLine.replaceAll("src=\"[\\S]*?/images", "src=\"http://acm.hdu.edu.cn/data/images");
		//System.out.println(tLine);
		
		problem.setTitle(regFind(tLine, "color:#1A5CC8'>([\\s\\S]*?)</h1>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(Integer.parseInt(regFind(tLine, "(\\d*) MS")));
		problem.setMemoryLimit(Integer.parseInt(regFind(tLine, "/(\\d*) K")));
		description.setDescription(regFind(tLine, "Problem Description</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setInput(regFind(tLine, "Input</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setOutput(regFind(tLine, "Output</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setSampleInput(regFind(tLine, "Sample Input</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setSampleOutput(regFind(tLine, "Sample Output</div>([\\s\\S]*?)(<br><[^<>]*?panel_title[^<>]*?>|<[^<>]*?><[^<>]*?><i>Hint)") + "</div></div>");
		description.setHint(regFind(tLine, "<i>Hint</i></div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		if (description.getHint() != null){
			description.setHint("<pre>" + description.getHint() + "</pre>");
		}
		
		problem.setSource(regFind(tLine, "Source</div> <div class=panel_content>([\\s\\S]*?)<[^<>]*?panel_[^<>]*?>"));
		if (problem.getSource() != null){
			problem.setSource(problem.getSource().replaceAll("<[\\s\\S]*?>", ""));
		}
		problem.setUrl("http://acm.hdu.edu.cn/showproblem.php?pid=" + problem.getOriginProb());
	}
}
