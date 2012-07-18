package judge.spider;

import judge.tool.Tools;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class CodeForcesSpider extends Spider {
	
	public void crawl() throws Exception{

		String html = "";
		HttpClient httpClient = new HttpClient();
		int splitIndex = 0;
		while (problem.getOriginProb().charAt(splitIndex) <= '9') {
			++splitIndex;
		}
		String contestNum = problem.getOriginProb().substring(0, splitIndex);
		String problemNum = problem.getOriginProb().substring(splitIndex);
		GetMethod getMethod = new GetMethod("http://codeforces.com/problemset/problem/" + contestNum + "/" + problemNum);
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

		problem.setTitle(Tools.regFind(html, "<div class=\"title\">" + problemNum + "\\. ([\\s\\S]*?)</div>").trim());
		if (problem.getTitle().isEmpty()){
			throw new Exception();
		}
		Double timeLimit = 1000 * Double.parseDouble(Tools.regFind(html, "</div>([\\d\\.]+) seconds?</div>"));
		problem.setTimeLimit(timeLimit.intValue());
		problem.setMemoryLimit(1024 * Integer.parseInt(Tools.regFind(html, "</div>(\\d+) megabytes</div>")));
		description.setDescription(Tools.regFind(html, "standard output</div></div><div>([\\s\\S]*?)</div><div class=\"input-specification"));
		description.setInput(Tools.regFind(html, "<div class=\"section-title\">Input</div>([\\s\\S]*?)</div><div class=\"output-specification\">"));
		description.setOutput(Tools.regFind(html, "<div class=\"section-title\">Output</div>([\\s\\S]*?)</div><div class=\"sample-tests\">"));
		description.setSampleInput("<style type=\"text/css\">.input, .output {border: 1px solid #888888;} .output {margin-bottom:1em;position:relative;top:-1px;} .output pre,.input pre {background-color:#EFEFEF;line-height:1.25em;margin:0;padding:0.25em;} .title {background-color:#FFFFFF;border-bottom: 1px solid #888888;font-family:arial;font-weight:bold;padding:0.25em;}</style>" + Tools.regFind(html, "<div class=\"sample-test\">([\\s\\S]*?)</div>\\s*</div>\\s*</div>"));
		description.setHint(Tools.regFind(html, "<div class=\"section-title\">Note</div>([\\s\\S]*?)</div></div></div>\\s+</div>"));
		problem.setUrl("http://codeforces.com/problemset/problem/" + contestNum + "/" + problemNum);
	}
}
