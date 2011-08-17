package judge.tool;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.io.IOUtils;

import com.Ostermiller.util.CSVParser;

/**
 * 公用工具类
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class Tools {
	
	/**
	 * html转义
	 * @param str
	 * @return
	 */
	public static String toHTMLChar(String str) {  
		if (str == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if (c == '&'){
				sb.append("&#38;");
			} else if (c == '"'){
				sb.append("&#34;");
			} else if (c == '\''){
				sb.append("&#39;");
			} else if (c == '<'){
				sb.append("&lt;");
			} else if (c == '>'){
				sb.append("&gt;");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * html反转义
	 * @param str
	 * @return
	 */
	public static String toPlainChar(String str) {  
		if (str == null) {
			return "";
		}
		return str.replaceAll("&#38;", "&").replaceAll("&#34;", "\"").replaceAll("&#39;", "'").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	}
	
	/**
	 * 去掉html中的js部分
	 * @return
	 */
	public static String dropScript(String html) {
		return html == null ? null : html.replaceAll("(?i)(?<=(\\b|java))script\\b", "ｓcript");
	}

	/**
	 * 从html中判断字符编码并将内容转成String返回
	 * @param method http方法
	 * @param proposedCharset 推荐的charset
	 * @return
	 * @throws IOException
	 */
	public static String getHtml(HttpMethodBase method, String proposedCharset) throws IOException {
		byte[] contentInByte = IOUtils.toByteArray(method.getResponseBodyAsStream());
		Charset charset = null;
		try {
			charset = Charset.forName(proposedCharset);
		} catch (Exception e) {}
		if (charset == null) {
			Header header = method.getResponseHeader("Content-Type");
			if (header != null) {
				Matcher matcher = Pattern.compile("(?i)charset=([-_\\w]+)").matcher(header.getValue());
				if (matcher.find()) {
					try {
						charset = Charset.forName(matcher.group(1));
					} catch (Exception e) {}
				}
			}
		}
		if (charset == null) {
			String tmpHtml = new String(contentInByte, "UTF-8");
			Matcher matcher = Pattern.compile("(?i)charset=([-_\\w]+)").matcher(tmpHtml);
			if (matcher.find()) {
				try {
					charset = Charset.forName(matcher.group(1));
				} catch (Exception e) {}
			}
		}
		if (charset == null) {
			charset = Charset.forName("UTF-8");
		}
		System.out.println(charset.name());
		return new String(contentInByte, charset);
	}
	
	/**
	 * 获取Excel中第一个sheet内容
	 * @param xls
	 * @return
	 * @throws IndexOutOfBoundsException
	 * @throws BiffException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[][] splitCellsFromExcel(File xls) throws IndexOutOfBoundsException, BiffException, FileNotFoundException, IOException {
		Sheet rs = Workbook.getWorkbook(new FileInputStream(xls)).getSheet(0);
		ArrayList<String[]> tmpContent = new ArrayList<String[]>();
		for (int i = 0; i < rs.getRows(); i++) {
			List row = new ArrayList<String>(); 
			for (int j = 0; j < rs.getColumns(); j++) {
				row.add(rs.getCell(j, i).getContents().trim());
			}
			tmpContent.add((String[]) row.toArray(new String[0]));
		}
		return tmpContent.toArray(new String[0][]);
	}

	/**
	 * 获取csv内容
	 * @param csv
	 * @return
	 * @throws IOException
	 */
	public static String[][] splitCellsFromCsv(File csv) throws IOException {
		//先检测文件编码
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();  
		detector.add(JChardetFacade.getInstance());  
		Charset charset = Charset.forName("GB2312");
		InputStream inputStream = new BufferedInputStream(new FileInputStream(csv));
		int length = 100000;
		while (length > 5) {
			try {
				charset = detector.detectCodepage(inputStream, length);
				break;
			} catch (Exception ex) {
				ex.printStackTrace();
				length = length * 2 / 3;
			}
		}
		System.out.println(charset.name());
		CSVParser shredder = new CSVParser(new InputStreamReader(inputStream, charset));
		return shredder.getAllValues();
	}
	
	/**
	 * 按照reg解析text,获取第i组
	 * @param text
	 * @param reg
	 * @param i
	 * @return
	 */
	public static String regFind(String text, String reg, int i){
		Matcher m = Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(text);
		return m.find() ? m.group(i) : "";
	}

	/**
	 * 按照reg解析text,获取第1组
	 * @param text
	 * @param reg
	 * @param i
	 * @return
	 */
	public static String regFind(String text, String reg){
		return regFind(text, reg, 1);
	}

}
