package judge.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVALiveSpider extends Spider {

	public void crawl() throws Exception{

		String tLine = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://acmicpc-live-archive.uva.es/nuevoportal/data/problem.php?p=" + problem.getOriginProb());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			tLine = new String(responseBody, "UTF-8");
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new Exception();

		}

		if (tLine.contains("<title>Problem not found</title>")) {
			throw new Exception();
		}

		tLine = tLine.replaceAll("((SRC=\")|(src=\"))(?!http)", "src=\"http://acmicpc-live-archive.uva.es/nuevoportal/data/");
		tLine = tLine.replaceAll("((SRC=)|(src=))(?!\"*http)", "src=http://acmicpc-live-archive.uva.es/nuevoportal/data/");

		problem.setTitle(regFind(tLine,	"<title>\\d{3,} - ([\\s\\S]*?)</title>"));
		if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()){
			throw new Exception();
		}
		problem.setTimeLimit(0);
		problem.setMemoryLimit(0);
		description.setDescription(regFind(tLine, "<b>Ranking</b></a></td></tr></table>([\\s\\S]*?)<hr><ADDRESS>"));
		problem.setSource(regFind(tLine, "<hr><ADDRESS>([\\s\\S]*?)</ADDRESS>"));
		problem.setUrl("http://acmicpc-live-archive.uva.es/nuevoportal/data/problem.php?p="	+ problem.getOriginProb());
	}
}
