package isun;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.sun.org.apache.xpath.internal.XPathAPI;

public class GetSrm {

	public DefaultHttpClient httpclient;
	public HttpGet httpget;
	public HttpResponse response;
	public HttpEntity entity;
	int NUM = 10000;
	
	public void login() throws ClientProtocolException, IOException {
		httpclient = new DefaultHttpClient();
		httpget = new HttpGet("http://www.topcoder.com/tc");
		response = httpclient.execute(httpget);
		entity = response.getEntity();

		System.out.println("Login form get: " + response.getStatusLine());
		if (entity != null) {
			entity.consumeContent();
		}
		System.out.println("Initial set of cookies:");
		List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		if (cookies.isEmpty()) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				System.out.println("- " + cookies.get(i).toString());
			}
		}
		HttpPost httpost = new HttpPost("http://www.topcoder.com/tc");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("module", "Login"));
		nvps.add(new BasicNameValuePair("nextpage",	"http://www.topcoder.com/tc"));
		nvps.add(new BasicNameValuePair("password", "liangliang"));
		nvps.add(new BasicNameValuePair("username", "Isun"));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		response = httpclient.execute(httpost);
		entity = response.getEntity();

		System.out.println("Login form get: " + response.getStatusLine());
		if (entity != null) {
			entity.consumeContent();
		}

		System.out.println("Post logon cookies:");
		cookies = httpclient.getCookieStore().getCookies();
		if (cookies.isEmpty()) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				System.out.println("- " + cookies.get(i).toString());
			}
		}

	}
	
	public void getList() throws ClientProtocolException, IOException, TransformerException {
		httpget = new HttpGet("http://www.topcoder.com/tc?module=ProblemArchive&er=" + NUM);
		response = httpclient.execute(httpget);
		entity = response.getEntity();
		
		String reg = "/tc\\?module=ProblemDetail&rd=(\\d{1,})&pm=(\\d{1,})", sCurrentLine;
		Pattern p = Pattern.compile(reg);

		InputStream l_urlStream = entity.getContent();
		BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));

		FileWriter fw = new FileWriter("list.txt");
		BufferedWriter bw = new BufferedWriter(fw);

		while ((sCurrentLine = l_reader.readLine()) != null) {
			Matcher m = p.matcher(sCurrentLine);
			if (m.find()) {
				System.out.println(m.group(1) + " " + m.group(2));
				bw.write(m.group(1) + "\n" + m.group(2) + "\n");
				bw.newLine();
			}
		}
		bw.close();
		fw.close();
	}
	
	public static String inputStream2String(InputStream is) throws IOException { 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		int i = -1; 
		while((i = is.read()) != -1) { 
			baos.write(i); 
		}
		return baos.toString(); 
	}
	
	public void getDescription(int pm) throws IOException, SAXException {
		File f = new File("desc\\", "desc_" + pm + ".html");
		if (f.exists()) {
			return;
		}
		f.createNewFile();
		FileWriter fw = new FileWriter("desc\\desc_" + pm + ".html");// 创建FileWriter对象，用来写入字符流
		BufferedWriter bw = new BufferedWriter(fw);	//将缓冲对文件的输出
		
		httpget = new HttpGet("http://www.topcoder.com/stat?c=problem_statement&pm="+pm);
		response = httpclient.execute(httpget);
		String sc = inputStream2String(response.getEntity().getContent()).replaceAll("\\n", "");
//		System.out.println(sc);

		String reg = "Problem Statement for (.+?)</TD>.+?problemText\" VALIGN=\"middle\" ALIGN=\"left\">(.+)<hr><p>This";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(sc);
		if (m.find()){
			bw.write("<html>\n");
			bw.write("<title></title>\n");
			bw.write("<body>\n");
			bw.write("<center><h1>" + m.group(1) + "</h1></center>\n");
			bw.write(m.group(2));
			bw.newLine();
			bw.write("</body>\n");
			bw.write("</html>\n");
		}
		bw.flush();
		bw.close();
		fw.close();
	}

	public void getData(int rd, int pm) throws IOException {
		
		httpget = new HttpGet("http://www.topcoder.com/tc?module=ProblemDetail&rd="+rd+"&pm="+pm);
		response = httpclient.execute(httpget);
		entity = response.getEntity();

		String reg = "(/stat\\?c=problem_solution&amp;cr=\\d{1,}&amp;rd=\\d{1,}&amp;pm=\\d{1,})", sCurrentLine;
		Pattern p = Pattern.compile(reg);

		InputStream l_urlStream = entity.getContent();
		BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));

		boolean find = false;
		while ((sCurrentLine = l_reader.readLine()) != null) {
			Matcher m = p.matcher(sCurrentLine);
			if (m.find()) {
				httpget = new HttpGet("http://www.topcoder.com" + m.group(1).replaceAll("&amp;", "&"));
				find = true;
			}
		}
		if (!find){
			return;
		}
		
		File f = new File("in\\", pm + ".in");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileWriter infw = new FileWriter("in\\" + pm + ".in");// 创建FileWriter对象，用来写入字符流
		BufferedWriter inbw = new BufferedWriter(infw);	//将缓冲对文件的输出

		f = new File("out\\", pm + ".out");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileWriter outfw = new FileWriter("out\\" + pm + ".out");// 创建FileWriter对象，用来写入字符流
		BufferedWriter outbw = new BufferedWriter(outfw);	//将缓冲对文件的输出

		response = httpclient.execute(httpget);
		String sc = inputStream2String(response.getEntity().getContent()).replaceAll("\\n", "");
		
		String  inreg = "statText[^<>]*?left.*?>(.*?)</TD>";
		String outreg = "statText[^<>]*?right.*?>(.*?)</TD>.*?Passed</TD>";
//		String outreg = "right\">(.+?)</TD>";
		Pattern inp = Pattern.compile(inreg);
		Pattern outp = Pattern.compile(outreg);

		
		Matcher inm = inp.matcher(sc);
		while (inm.find()) {
			inbw.write(inm.group(1));
			inbw.newLine();
		}
		inbw.flush();
		inbw.close();
		infw.close();
		
		int cnt = 0;
		boolean first = true;
		Matcher outm = outp.matcher(sc);
		while (outm.find()) {
			cnt = (cnt + 1) % 2;
			if (cnt == 0 && !first){
				outbw.write(outm.group(1));
				outbw.newLine();
			}
			first = false;
		}
		outbw.flush();
		outbw.close();
		outfw.close();	

	}

	
	public void getDetail() throws NumberFormatException, IOException, SAXException {
		FileReader fr = new FileReader("list.txt");
		BufferedReader br = new BufferedReader(fr); // 缓冲指定文件的输入

		while (br.ready()){
			int rd = Integer.parseInt(br.readLine());
			int pm = Integer.parseInt(br.readLine());
			br.readLine();
			getDescription(pm);
			getData(rd, pm);
		}
	}
		



	public static void main(String[] args) throws Exception {

		GetSrm gs = new GetSrm();
		gs.login();
		gs.getList();
		
		gs.getDetail();

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		// httpclient.getConnectionManager().shutdown();
	}
}
