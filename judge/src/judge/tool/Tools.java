package judge.tool;

public class Tools {
	
	/**
	 * 转义
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
	 * 反转义
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

}
