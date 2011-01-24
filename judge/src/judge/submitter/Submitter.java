package judge.submitter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.commons.httpclient.HttpClient;

import judge.bean.Submission;
import judge.service.IBaseService;
import judge.service.JudgeService;
import judge.tool.ApplicationContainer;
import judge.tool.SpringBean;

public abstract class Submitter extends Thread implements Cloneable {
	static public ServletContext sc = ApplicationContainer.sc;
	static public IBaseService baseService = (IBaseService) SpringBean.getBean("baseService", sc);
	static public JudgeService judgeService = (JudgeService) SpringBean.getBean("judgeService", sc);

	public Submission submission;

	protected HttpClient httpClient;
	protected int maxRunId = 0;
	protected int idx;

	public abstract void work();
	public abstract void waitForUnfreeze();

	public void run() {
		work();
		updateStanding();
		waitForUnfreeze();
	}
	
	private void updateStanding() {
		if (submission.getContest() != null){
			try {
				judgeService.updateStandingData(submission.getContest().getId(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	
	
	
	public Submission getSubmission() {
		return submission;
	}
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

}
