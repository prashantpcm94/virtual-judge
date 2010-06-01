package judge.submitter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Submission;
import judge.service.IBaseService;

public class Submitter extends Thread implements Cloneable {
	public Submission submission;
	static public IBaseService baseService;
	
	public Submission getSubmission() {
		return submission;
	}
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
	public void setBaseService(IBaseService baseService) {
		System.out.println("submitter baseservice init...");
		Submitter.baseService = baseService;
	}
	
	public String regFind(String text, String reg){
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(text);
		if (m.find()){
			String res = m.group(1);
			System.out.println("RESULT : " + res);
			return res;
		}
		return null;
	}
	
	public Object clone() {
		Submitter o = null;
		try {
			o = (Submitter) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
	

}
