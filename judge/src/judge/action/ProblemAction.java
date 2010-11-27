/**
 * 处理题目相关功能
 */

package judge.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;

import judge.bean.DataTablesPage;
import judge.bean.Description;
import judge.bean.Problem;
import judge.bean.Submission;
import judge.bean.User;
import judge.service.IBaseService;
import judge.service.JudgeService;
import judge.spider.Spider;
import judge.submitter.Submitter;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings({ "unchecked", "serial" })
public class ProblemAction extends BaseAction{
	
	private IBaseService baseService;
	private JudgeService judgeService;
	
	private int id;	//problemId
	private int uid;
	private int isOpen;
	private int res;	//result
	private String OJId;
	private String probNum;
	private Problem problem;
	private Description description;
	private Submission submission;
	private List dataList;
	private String language;
	private String source;
	private String redir;
	private String un;
	private String _64Format;
	private DataTablesPage dataTablesPage;
	private Map<Object, String> languageList;
	
	public String toListProblem() {
		return SUCCESS;
	}
	
	public String listProblem() {
		Map session = ActionContext.getContext().getSession();
		StringBuffer hql = new StringBuffer("select problem.originOJ, problem.originProb, problem.title, problem.triggerTime, problem.source, problem.id, problem.url from Problem problem where 1=1 ");
		long cnt = baseService.count(hql.toString());
		dataTablesPage = new DataTablesPage();
		dataTablesPage.setITotalRecords(cnt);
		if (OJList.contains(OJId)){
			hql.append(" and problem.originOJ = '" + OJId + "' ");
		}
		Map paraMap = new HashMap();
		if (sSearch != null && !sSearch.trim().isEmpty()){
			sSearch = sSearch.toLowerCase().trim();
			paraMap.put("keyword", "%" + sSearch + "%");
			hql.append(" and (problem.title like :keyword or problem.originProb like :keyword or problem.source like :keyword) ");
		}
		dataTablesPage.setITotalDisplayRecords(baseService.count(hql.toString(), paraMap));
//		System.out.println("iSortCol_0 = " + iSortCol_0);
		if (iSortCol_0 != null){
			if (iSortCol_0 == 1){
				hql.append(" order by problem.originProb " + sSortDir_0);
			} else if (iSortCol_0 == 2){
				hql.append(" order by problem.title " + sSortDir_0);
			} else if (iSortCol_0 == 3){
				hql.append(" order by problem.triggerTime " + sSortDir_0);
			} else if (iSortCol_0 == 4){
				hql.append(" order by problem.source " + sSortDir_0);
			}
		}

		List<Object[]> aaData = baseService.list(hql.toString(), paraMap, iDisplayStart, iDisplayLength);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] o : aaData) {
			o[3] = sdf.format((Date)o[3]);
		}
		dataTablesPage.setAaData(aaData);
		
		this.addActionError((String) session.get("error"));
		session.remove("error");
		OJId = (String) session.get("OJId");

		return SUCCESS;
	}
	
	public String addProblem(){
		if (!OJList.contains(OJId)){
			this.addActionError("Please choose a legal OJ!");
			return ERROR;
		}
		if (probNum.isEmpty()){
			this.addActionError("Please enter the problem number!");
			return ERROR;
		}
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null){
			this.addActionError("Please login first!");
			return ERROR;
		}
		session.put("OJId", OJId);
		
		problem = judgeService.findProblem(OJId.trim(), probNum.trim());
		if (problem == null){
			problem = new Problem();
			problem.setOriginOJ(OJId.trim());
			problem.setOriginProb(probNum.trim());
		} else {
			for (Description desc : problem.getDescriptions()){
				if ("0".equals(desc.getAuthor())){
					description = desc;
					break;
				}
			}
		}
		if (description == null){
			description = new Description();
			description.setUpdateTime(new Date());
			description.setAuthor("0");
			description.setRemarks("Initializatioin.");
			description.setVote(0);
		}

		problem.setTitle("Crawling……");
		problem.setTimeLimit(1);
		problem.setTriggerTime(new Date());
		baseService.addOrModify(problem);
		Spider spider = (Spider) spiderMap.get(OJId).clone();
		spider.setProblem(problem);
		spider.setDescription(description);
		try {
			spider.start();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String viewProblem(){
		List list = baseService.query("select p from Problem p left join fetch p.descriptions where p.id = " + id);
		problem = (Problem) list.get(0);
		_64Format = lf.get(problem.getOriginOJ());
		Map session = ActionContext.getContext().getSession();
		session.put("problem", problem);
		return SUCCESS;
	}
	
	public String vote4Description(){
		Map session = ActionContext.getContext().getSession();
		Set votePids = (Set) session.get("votePids");
		if (votePids == null){
			votePids = new HashSet<Integer>();
			session.put("votePids", votePids);
		}
		Description desc = (Description) baseService.query(Description.class, id);
		desc.setVote(desc.getVote() + 1);
		baseService.modify(desc);
		votePids.add(desc.getProblem().getId());
		return SUCCESS;
	}
	
	public String toSubmit(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null){
//			session.put("redir", "../problem/toSubmit.action?id=" + id);
			return ERROR;
		}
		ServletContext sc = ServletActionContext.getServletContext();
		problem = (Problem) session.get("problem");
		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		language = (String) session.get("L" + problem.getOriginOJ());
		isOpen = user.getShare();
		return SUCCESS;
	}
	
	
	public String submit(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null){
			return ERROR;
		}
		problem = (Problem) session.get("problem");
		ServletContext sc = ServletActionContext.getServletContext();
		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());

		if (problem == null){
			this.addActionError("Please submit via usual approach!");
			return INPUT;
		}
		if (problem.getTimeLimit() == 1){
			this.addActionError("Crawling has not finished!");
			return INPUT;
		}

		if (problem.getHidden() > 0 && user.getSup() == 0 && problem.getCreatorId() != user.getId()){
			this.addActionError("This problem is temporarily hidden by the creator!");
			return INPUT;
		}
		

/*		
		dataList = baseService.query("select contest.beginTime, contest.endTime from Contest contest, Cproblem cproblem where cproblem.contestId = contest.id and cproblem.problemId = " + problem.getId());
		for (int i = 0; i < dataList.size(); i++){
			if (((Date)((Object[])dataList.get(i))[0]).compareTo(new Date()) < 0 && ((Date)((Object[])dataList.get(i))[1]).compareTo(new Date()) > 0){
				this.addActionError("This problem is now in use for contests!");
				return INPUT;
			}
		}
*/
		
		if (!languageList.containsKey(language)){
			this.addActionError("No such a language!");
			return INPUT;
		}
		session.put("L" + problem.getOriginOJ(), language);
		if (source.length() < 50){
			this.addActionError("Source code should be longer than 50 characters!");
			return INPUT;
		}
		if (source.length() > 30000){
			this.addActionError("Source code should be shorter than 30000 characters!");
			return INPUT;
		}
		submission = new Submission();
		submission.setSubTime(new Date());
		submission.setProblemId(problem.getId());
		submission.setUserId(user.getId());
		submission.setStatus("Pending……");
		submission.setLanguage(language);
		submission.setSource(source);
		submission.setIsOpen(isOpen);
		submission.setDispLanguage(((Map<String, String>)sc.getAttribute(problem.getOriginOJ())).get(language));
		submission.setUsername(user.getUsername());
		baseService.add(submission);
		try {
			Submitter submitter = (Submitter) submitterMap.get(problem.getOriginOJ()).clone();
			submitter.setSubmission(submission);
			submitter.start();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String status() {
		if (id != 0){
			problem = (Problem) baseService.query(Problem.class, id);
			OJId = problem.getOriginOJ();
			probNum = problem.getOriginProb();
		}
		
		Map session = ActionContext.getContext().getSession();
		if (session.containsKey("error")){
			this.addActionError((String) session.get("error"));
		}
		session.remove("error");

		return SUCCESS;
	}

	public String fetchStatus() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		int userId = user != null ? user.getId() : -1;
		int sup = user != null ? user.getSup() : 0;
		
		StringBuffer hql = new StringBuffer("select s.id, s.username, s.problemId, s.status, s.memory, s.time, s.dispLanguage, length(s.source), s.subTime, s.userId, s.isOpen, p.originOJ, p.originProb from Submission s, Problem p where s.problemId = p.id and s.contestId = 0 ");

		dataTablesPage = new DataTablesPage();

		dataTablesPage.setITotalRecords(9999999L);

		if (un != null && !un.trim().isEmpty()){
			un = un.toLowerCase().trim();
			hql.append(" and s.username = '" + un + "' ");
		}
		
		if (id != 0){
			hql.append(" and p.id = " + id);
		} else {
			if (OJList.contains(OJId)){
				hql.append(" and p.originOJ = '" + OJId + "' ");
			}
			if (!probNum.isEmpty()){
				hql.append(" and p.originProb = '" + probNum + "' ");
			}
		}
		
		if (res == 1){
			hql.append(" and s.status = 'Accepted' ");
		} else if (res == 2) {
			hql.append(" and s.status like 'wrong%' ");
		} else if (res == 3) {
			hql.append(" and s.status like 'time%' ");
		} else if (res == 4) {
			hql.append(" and (s.status like 'runtime%' or s.status like 'segment%' or s.status like 'crash%') ");
		} else if (res == 5) {
			hql.append(" and (s.status like 'presentation%' or s.status like 'format%') ");
		} else if (res == 6) {
			hql.append(" and s.status like 'compil%' ");
		} else if (res == 7) {
			hql.append(" and s.status = 'Judging Error' ");
		}
		
		hql.append(" order by s.id desc ");
		
		dataTablesPage.setITotalDisplayRecords(9999999L);
		
		List<Object[]> aaData = baseService.list(hql.toString(), iDisplayStart, iDisplayLength);
		
		for (Object[] o : aaData) {
			o[8] = sdf.format((Date)o[8]);
			o[10] = (Integer)o[10] > 0 ? 2 : sup > 0 || (Integer)o[9] == userId ? 1 : 0; 
		}

		dataTablesPage.setAaData(aaData);

		return SUCCESS;
	}
	
	public String deleteProblem(){
		Map session = ActionContext.getContext().getSession();
		problem = (Problem) baseService.query(Problem.class, id);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && problem.getCreatorId() != user.getId()){
			session.put("error", "You don't have access to operation on this problem!");
			return ERROR;
		}
		long c = baseService.count("from Submission submission where submission.problemId = " + id);
		long d = baseService.count("from Cproblem cproblem where cproblem.problemId = " + id);
		if (c > 0){
			session.put("error", "There are already submissions for this problem!");
			return ERROR;
		} else if (d > 0){
			session.put("error", "There are already contests using this problem!");
			return ERROR;
		} else {
			baseService.delete(problem);
		}
		return SUCCESS;
	}

	public String toggleAccess(){
		Map session = ActionContext.getContext().getSession();
		problem = (Problem) baseService.query(Problem.class, id);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && problem.getCreatorId() != user.getId()){
			session.put("error", "You don't have access to operation on this problem!");
			return ERROR;
		}
		problem.setHidden(1 - problem.getHidden());
		baseService.modify(problem);
		return SUCCESS;
	}
	
	public String toEditDescription(){
		Map session = ActionContext.getContext().getSession();
		List list = baseService.query("select d from Description d left join fetch d.problem where d.id = " + id);
		description = (Description) list.get(0);
		problem = description.getProblem();
		if (session.get("visitor") == null){
			return "login";
		}
		return SUCCESS;
	}
	
	public String editDescription(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null){
			session.put("error", "Please login first!");
			return ERROR;
		}
		if (id == 0){
			return ERROR;
		}
		description.setUpdateTime(new Date());
		description.setAuthor(user.getUsername());
		description.setVote(0);
		description.setProblem(new Problem(id));
		baseService.execute("delete from Description d where d.author = '" + user.getUsername() + "' and d.problem.id = " + id);
		baseService.add(description);
		return SUCCESS;
	}
	
	public String deleteDescription(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user != null){
			description = (Description) baseService.query(Description.class, id);
			if (!description.getAuthor().equals("0") && (user.getSup() == 1 || user.getUsername().equals(description.getAuthor()))){
				baseService.delete(description);
			}
		}
		return SUCCESS;
	}
	
	public String viewSource(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		submission = (Submission) baseService.query(Submission.class, id);
		if (user == null || user.getSup() == 0 && user.getId() != submission.getUserId()){
			if (submission.getIsOpen() == 0){
				session.put("error", "No access to this code!");
				return ERROR;
			}
			problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
			if (problem.getHidden() == 1){
				session.put("error", "This source is currently not visible since the problem is hidden!");
				return ERROR;
			}
		}
		StringBuffer sb = new StringBuffer();
		String os = submission.getSource();
		for (int i = 0; i < os.length(); i++){
			char c = os.charAt(i);
			if (c == '&'){
				sb.append("&#38;");
			} else if (c == '"'){
				sb.append("&#34;");
			} else if (c == '<'){
				sb.append("&lt;");
			} else if (c == '>'){
				sb.append("&gt;");
			} else {
				sb.append(c);
			}
		}
		submission.setSource(sb.toString());
		problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
		ServletContext sc = ServletActionContext.getServletContext();
		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		submission.setLanguage(languageList.get(submission.getLanguage()));
		user = (User) baseService.query(User.class, submission.getUserId());
		uid = user.getId();
		un = user.getUsername();

		//这里language用作为shjs提供语言识别所需要的class名
		language = findClass4SHJS(submission.getLanguage());

		return SUCCESS;
	}

	public String toggleOpen(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		submission = (Submission) baseService.query(Submission.class, id);
		if (user == null || user.getSup() == 0 && user.getId() != submission.getUserId()){
			session.put("error", "No access to this code!");
			return ERROR;
		}
		submission.setIsOpen(1 - submission.getIsOpen());
		baseService.modify(submission);
		return SUCCESS;
	}
	
	private String findClass4SHJS(String srcLang) {
		srcLang = " " + srcLang.toLowerCase() + " ";
		if (srcLang.contains("c++") || srcLang.contains("cpp") || srcLang.contains("g++")){
			return "sh_cpp";
		} else if (srcLang.contains(" c ") || srcLang.contains("gcc")){
			return "sh_c";
		} else if (srcLang.contains("c#")){
			return "sh_csharp";
		} else if (srcLang.contains("java ")){
			return "sh_java";
		} else if (srcLang.contains("pascal") || srcLang.contains("fpc")){
			return "sh_pascal";
		} else if (srcLang.contains("tcl")){
			return "sh_tcl";
		} else if (srcLang.contains("scala")){
			return "sh_scala";
		} else if (srcLang.contains("perl")){
			return "sh_perl";
		} else if (srcLang.contains("python")){
			return "sh_python";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("php")){
			return "sh_php";
		} else if (srcLang.contains("prolog")){
			return "sh_prolog";
		} else if (srcLang.contains("javascript")){
			return "sh_javascript";
		} else {
			return "sh_c";
		}
	}
	
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Submission getSubmission() {
		return submission;
	}
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	public Map<Object, String> getLanguageList() {
		return languageList;
	}
	public void setLanguageList(Map<Object, String> languageList) {
		this.languageList = languageList;
	}
	public String getRedir() {
		return redir;
	}
	public void setRedir(String redir) {
		this.redir = redir;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	public String getOJId() {
		return OJId;
	}
	public void setOJId(String id) {
		OJId = id;
	}
	public String getProbNum() {
		return probNum;
	}
	public void setProbNum(String probNum) {
		this.probNum = probNum;
	}
	public IBaseService getBaseService() {
		return baseService;
	}
	public void setBaseService(IBaseService baseService) {
		this.baseService = baseService;
	}
	public DataTablesPage getDataTablesPage() {
		return dataTablesPage;
	}
	public void setDataTablesPage(DataTablesPage dataTablesPage) {
		this.dataTablesPage = dataTablesPage;
	}
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public String getUn() {
		return un;
	}
	public void setUn(String un) {
		this.un = un;
	}
	public Description getDescription() {
		return description;
	}
	public void setDescription(Description description) {
		this.description = description;
	}
	public String get_64Format() {
		return _64Format;
	}
	public void set_64Format(String _64Format) {
		this._64Format = _64Format;
	}
	public JudgeService getJudgeService() {
		return judgeService;
	}
	public void setJudgeService(JudgeService judgeService) {
		this.judgeService = judgeService;
	}

}
