package judge.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UVaSpiderInitializer extends Thread {
	
	public static int threadCnt;
	private String rootUrl;
	
	public UVaSpiderInitializer(String url) {
		if (UVASpider.problemNumberMap == null) {
			UVASpider.problemNumberMap = new String[50000];
		}
		rootUrl = url;
	}
	
	public void run() {
		++threadCnt;
		try {
			String html = null;
			GetMethod getMethod = new GetMethod(rootUrl);
			HttpClient httpClient = new HttpClient();
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			try {
				int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK) {
					System.err.println("Method failed: " + getMethod.getStatusLine());
					throw new Exception();
				}
				byte[] responseBody = getMethod.getResponseBody();
				html = new String(responseBody, "UTF-8");
				html = html.substring(html.indexOf("Total Users / Solving"));
			} catch (Exception e) {
				e.printStackTrace();
				getMethod.releaseConnection();
			}
			
			Matcher matcher = Pattern.compile("category=(\\d+)\">").matcher(html);
			while (matcher.find()) {
				new UVaSpiderInitializer("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=" + matcher.group(1)).start();
			}
			
			matcher = Pattern.compile("page=show_problem&amp;problem=(\\d+)\">(\\d+)").matcher(html);
			while (matcher.find()) {
				System.out.println(matcher.group(2) + "->" +  matcher.group(1));
				UVASpider.problemNumberMap[Integer.parseInt(matcher.group(2))] = matcher.group(1);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		--threadCnt;
	}
}
