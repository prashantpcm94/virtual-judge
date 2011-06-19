package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class CodeForcesSpider extends Spider {
	
	public void crawl() throws Exception{

		String tLine = "";
		HttpClient httpClient = new HttpClient();
		String contestNum = problem.getOriginProb().substring(0, problem.getOriginProb().length() - 1);
		String problemNum = problem.getOriginProb().substring(problem.getOriginProb().length() - 1);
		GetMethod getMethod = new GetMethod("http://codeforces.com/problemset/problem/" + contestNum + "/" + problemNum);
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
			throw new Exception();
		}

		problem.setTitle(regFind(tLine, "<div class=\"title\">" + problemNum + "\\. ([\\s\\S]*?)</div>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}
		
		problem.setTimeLimit(1000 * Integer.parseInt(regFind(tLine, "</div>(\\d+) seconds?</div>")));
		problem.setMemoryLimit(1024 * Integer.parseInt(regFind(tLine, "</div>(\\d+) megabytes</div>")));
		description.setDescription(regFind(tLine, "<div class=\"legend\">([\\s\\S]*?)</div><div class=\"input-specification\">"));
		description.setInput(regFind(tLine, "<div class=\"section-title\">Input</div>([\\s\\S]*?)</div><div class=\"output-specification\">"));
		description.setOutput(regFind(tLine, "<div class=\"section-title\">Output</div>([\\s\\S]*?)</div><div class=\"sample-tests\">"));
		description.setSampleInput("<style type=\"text/css\">.input, .output {border: 1px solid #888888;} .output {margin-bottom:1em;position:relative;top:-1px;} .output pre,.input pre {background-color:#EFEFEF;line-height:1.25em;margin:0;padding:0.25em;} .title {background-color:#FFFFFF;border-bottom: 1px solid #888888;font-family:arial;font-weight:bold;padding:0.25em;}</style>" + regFind(tLine, "<div class=\"sample-test\">([\\s\\S]*?</pre></div>)</div></div>"));
		description.setHint(regFind(tLine, "<div class=\"section-title\">Note</div>([\\s\\S]*?)</div></div><p> </p>"));
		problem.setUrl("http://codeforces.com/problemset/problem/" + contestNum + "/" + problemNum);
	}

	@Override
	public void extraOptr() throws Exception {
	}

}
