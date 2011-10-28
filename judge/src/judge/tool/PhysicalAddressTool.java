package judge.tool;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class PhysicalAddressTool {
	
	static public Map<String, String> addressMap = new ConcurrentHashMap<String, String>();
	
	static public String getPhysicalAddressTool(final String ip) {
		if (addressMap.containsKey(ip)) {
			return addressMap.get(ip);
		}
		addressMap.put(ip, "");
		new Thread(new Runnable() {
			public void run() {
				HttpClient httpClient = new HttpClient();
				GetMethod getMethod = new GetMethod("http://ip.cn/getip2.php?action=queryip&ip_url=" + ip);
				try {
					httpClient.executeMethod(getMethod);
					String physicalAddress = Tools.getHtml(getMethod, "GB2312").replaceAll(".+来自：", "").trim();
					if (addressMap.size() >= 500) {
						addressMap.clear();
					}
					addressMap.put(ip, physicalAddress);
				} catch (HttpException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return "";
	}
}
