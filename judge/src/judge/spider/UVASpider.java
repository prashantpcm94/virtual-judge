package judge.spider;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;

import judge.bean.Submission;
import judge.bean.User;
import judge.tool.ApplicationContainer;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

@SuppressWarnings("unchecked")
public class UVASpider extends Spider {

	public void crawl() throws Exception{

		String tLine = "";
		HttpClient httpClient = new HttpClient();
		if (!problem.getOriginProb().matches("\\d+")) {
			throw new Exception();
		}
		int category = Integer.parseInt(problem.getOriginProb()) / 100;
		GetMethod getMethod = new GetMethod("http://uva.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".html");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
				throw new Exception();
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();
		}

		tLine = tLine.replaceAll("((SRC=\")|(src=\"))(?!http)", "src=\"http://uva.onlinejudge.org/external/" + category + "/");
		tLine = tLine.replaceAll("((SRC=)|(src=))(?!\"*http)", "src=http://uva.onlinejudge.org/external/" + category + "/");

		if ("Crawling……".equals(problem.getTitle())) {
			problem.setTitle("UVa P" + problem.getOriginProb());
		}
		problem.setMemoryLimit(0);
		description.setDescription(regFind(tLine, "<body[\\s\\S]*?>([\\s\\S]*)</body>", 1));
		if (description.getDescription() == null || description.getDescription().trim().isEmpty()) {
			description.setDescription(tLine.replaceAll("(?i)</?html>", ""));
		}
		problem.setUrl("http://uva.onlinejudge.org/external/" + category + "/" + problem.getOriginProb() + ".html");
	}

	@Override
	public void extraOptr() throws Exception {
		if (!problem.getTitle().matches("UVa P\\d+")) {
			return;
		}
		
		ServletContext sc = ApplicationContainer.sc;
		Map<String, String> langMap = (Map<String, String>)sc.getAttribute(problem.getOriginOJ());
		String language = langMap.keySet().iterator().next();
		
		Submission submission = new Submission();
		submission.setSubTime(new Date());
		submission.setProblem(problem);
		submission.setUser(new User(1006));
		submission.setStatus("Pending……");
		submission.setLanguage(language);
		submission.setSource("int main(){return 0;}/* http://acm.hust.edu.cn:8080/judge */");
		submission.setIsOpen(0);
		submission.setIsPrivate(1);
		submission.setDispLanguage(langMap.get(language));
		submission.setUsername("Isun");
		submission.setOriginOJ(problem.getOriginOJ());
		submission.setOriginProb(problem.getOriginProb());
		baseService.addOrModify(submission);
		
		judgeService.rejudge(submission);
	}
}
