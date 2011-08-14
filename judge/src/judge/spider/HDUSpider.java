package judge.spider;

import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HDUSpider extends Spider {
	
	public void crawl() throws Exception{
		String html = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://acm.hdu.edu.cn/showproblem.php?pid=" + problem.getOriginProb());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+getMethod.getStatusLine());
			}
			html = Tools.getHtml(getMethod.getResponseBodyAsStream());
		} catch(Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		if (html.contains("<DIV>No such problem")){
			throw new Exception();
		}
		
		html = html.replaceAll("src=[^'\"]*?/images", "src=http://acm.hdu.edu.cn/data/images");
		html = html.replaceAll("src='[^'\"]*?/images", "src='http://acm.hdu.edu.cn/data/images");
		html = html.replaceAll("src=\"[^'\"]*?/images", "src=\"http://acm.hdu.edu.cn/data/images");
		//System.out.println(tLine);
		
		problem.setTitle(regFind(html, "color:#1A5CC8'>([\\s\\S]*?)</h1>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(Integer.parseInt(regFind(html, "(\\d*) MS")));
		problem.setMemoryLimit(Integer.parseInt(regFind(html, "/(\\d*) K")));
		description.setDescription(regFind(html, "Problem Description</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setInput(regFind(html, "Input</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setOutput(regFind(html, "Output</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setSampleInput(regFind(html, "Sample Input</div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		description.setSampleOutput(regFind(html, "Sample Output</div>([\\s\\S]*?)(<br><[^<>]*?panel_title[^<>]*?>|<[^<>]*?><[^<>]*?><i>Hint)") + "</div></div>");
		description.setHint(regFind(html, "<i>Hint</i></div>([\\s\\S]*?)<br><[^<>]*?panel_title[^<>]*?>"));
		if (description.getHint().length() > 0){
			description.setHint("<pre>" + description.getHint() + "</pre>");
		}
		
		problem.setSource(regFind(html, "Source</div> <div class=panel_content>([\\s\\S]*?)<[^<>]*?panel_[^<>]*?>").replaceAll("<[\\s\\S]*?>", ""));
		problem.setUrl("http://acm.hdu.edu.cn/showproblem.php?pid=" + problem.getOriginProb());
	}
}
