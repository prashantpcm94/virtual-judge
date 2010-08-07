package judge.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Problem;
import judge.service.IBaseService;

public class Spider extends Thread implements Cloneable {
	public Problem problem;
	static public IBaseService baseService;

	public void setBaseService(IBaseService baseService) {
		System.out.println("spider baseservice init...");
		Spider.baseService = baseService;
	}
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	public String regFind(String text, String reg){
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(text);
		if (m.find()){
			String res = m.group(1);
//			System.out.println("RESULT : " + res);
			return res;
		}
		return null;
	}

	public String regFind(String text, String reg, int x){
		Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		if (m.find()){
			String res = m.group(1);
//			System.out.println("RESULT : " + res);
			return res;
		}
		return null;
	}

	
	public Object clone() {
		Spider o = null;
		try {
			o = (Spider) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}
