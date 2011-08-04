package judge.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;



public class HYSBZSpider extends Spider {
	
	public void crawl() throws Exception{
		
		String tLine = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://www.zybbs.org/JudgeOnline/problem.php?id=" + problem.getOriginProb());
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

		if (tLine.contains("<title>Problem is not Availables")){
			throw new Exception();
		}
		
		tLine = tLine.replaceAll("src=images", "src=http://www.zybbs.org/JudgeOnline/images");
		tLine = tLine.replaceAll("src='images", "src='http://www.zybbs.org/JudgeOnline/images");
		tLine = tLine.replaceAll("src=\"images", "src=\"http://www.zybbs.org/JudgeOnline/images");
		
		problem.setTitle(regFind(tLine, "<center><h2>([\\s\\S]*?)</h2>").replaceAll(problem.getOriginProb() + ": ", ""));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}

		problem.setSource(regFind(tLine, "<h2>Source</h2>[\\s\\S]*?<div class=content><p>([\\s\\S]*?)</p></div><center>"));
		Matcher matcher = Pattern.compile("\\[(.*)\\](.*)").matcher(problem.getTitle());
		if (matcher.find()) {
			problem.setTitle(matcher.group(2));
			problem.setSource(matcher.group(1));
		}

		problem.setTimeLimit(1000 * Integer.parseInt(regFind(tLine, "Time Limit: </span>(\\d+) Sec")));
		problem.setMemoryLimit(1024 * Integer.parseInt(regFind(tLine, "Memory Limit: </span>(\\d+) MB")));
		description.setDescription(regFind(tLine, "<h2>Description</h2>([\\s\\S]*?)<h2>Input</h2>"));
		description.setInput(regFind(tLine, "<h2>Input</h2>([\\s\\S]*?)<h2>Output</h2>"));
		description.setOutput(regFind(tLine, "<h2>Output</h2>([\\s\\S]*?)<h2>Sample Input</h2>"));
		description.setSampleInput(regFind(tLine, "<h2>Sample Input</h2>([\\s\\S]*?)<h2>Sample Output</h2>").replaceAll("<span", "<pre").replaceAll("</span>", "</pre>"));
		description.setSampleOutput(regFind(tLine, "<h2>Sample Output</h2>([\\s\\S]*?)<h2>HINT</h2>").replaceAll("<span", "<pre").replaceAll("</span>", "</pre>"));
		description.setHint(regFind(tLine, "<h2>HINT</h2>([\\s\\S]*?)<h2>Source</h2>"));
		problem.setUrl("http://www.zybbs.org/JudgeOnline/problem.php?id=" + problem.getOriginProb());
	}
}
