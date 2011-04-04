package judge.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Description;
import judge.bean.Problem;
import judge.service.IBaseService;
import judge.service.JudgeService;
import judge.tool.ApplicationContainer;
import judge.tool.SpringBean;

public abstract class Spider extends Thread implements Cloneable {
	static public IBaseService baseService = (IBaseService) SpringBean.getBean("baseService", ApplicationContainer.sc);
	static public JudgeService judgeService = (JudgeService) SpringBean.getBean("judgeService", ApplicationContainer.sc);

	public Problem problem;
	public Description description;

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
	
	/**
	 * 抓取题目,对problem和description进行赋值
	 * @throws Exception
	 */
	public abstract void crawl() throws Exception;
	
	/**
	 * 抓取后续操作
	 * @throws Exception
	 */
	public abstract void extraOptr() throws Exception;
	
	
	public void run() {
		try {
			crawl();
			description.setProblem(problem);
			baseService.addOrModify(problem);
			baseService.addOrModify(description);
			extraOptr();
		} catch (Exception e) {
			e.printStackTrace();
			if (problem.getUrl() == null){
				//本次是第一次抓取，且失败，认为输入OJ题号错误，删除
				baseService.delete(problem);
			} else {
				//本次虽失败，但因为题目本来是好的，估计是网络问题，故不删
				problem.setTitle("[Crawling failed]");
				baseService.addOrModify(problem);
			}
		}
	}
	
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	public Description getDescription() {
		return description;
	}
	public void setDescription(Description description) {
		this.description = description;
	}

	
}
