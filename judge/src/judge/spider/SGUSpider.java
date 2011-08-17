package judge.spider;


import judge.tool.Tools;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class SGUSpider extends Spider {
	

	public void crawl() throws Exception{
		
		String html = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://acm.sgu.ru/problem.php?contest=0&problem=" + problem.getOriginProb());
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

		if (html.contains("<h4>no such problem</h4>")){
			throw new Exception();
		}
		
		String tl = Tools.regFind(html, "ime limit per test: ([\\d\\.]*)");
		if (tl.length() > 0){
			problem.setTimeLimit((int)(1000 * Double.parseDouble(tl)));
		}
		
		String ml = Tools.regFind(html, "emory limit per test: ([\\d]*)");
		if (ml.length() > 0){
			problem.setMemoryLimit(Integer.parseInt(ml));
		} else {
			ml = Tools.regFind(html, "emory limit: ([\\d]*)");
			if (ml.length() > 0){
				problem.setMemoryLimit(Integer.parseInt(ml));
			}
		}

		problem.setTitle(Tools.regFind(html, problem.getOriginProb() + "\\.([\\s\\S]*?)</[th]", 1).trim());
		
		if (Integer.parseInt(problem.getOriginProb()) >= 277){
			description.setDescription(Tools.regFind(html, "output: standard</div><br/>([\\s\\S]*?)<b>Input</b>") + "</div>");
			description.setInput(Tools.regFind(html, "<b>Input</b></div>([\\s\\S]*?)<b>Output</b>") + "</div>");
			description.setOutput(Tools.regFind(html, "<b>Output</b></div>([\\s\\S]*?)<b>Example") + "</div>");
			description.setSampleInput(Tools.regFind(html, "<b>Example\\(s\\)</b></div>([\\s\\S]*?)(<b>Note|<hr>)") + "</div>");
			description.setHint(Tools.regFind(html, "<b>Note</b></div>([\\s\\S]*?)<hr>") + "</div>");
		} else {
			description.setDescription(Tools.regFind(html, "(<BODY[\\s\\S]*?</BODY>)", 1));
			problem.setSource(Tools.regFind(html, "Resource:</td><td>([\\s\\S]*?)\n</td>"));
		}
		problem.setUrl("http://acm.sgu.ru/problem.php?contest=0&problem=" + problem.getOriginProb());
	}
}
