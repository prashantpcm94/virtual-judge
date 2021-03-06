package judge.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import judge.bean.Cproblem;
import judge.bean.DataTablesPage;
import judge.bean.Description;
import judge.bean.Problem;
import judge.bean.Submission;
import judge.bean.User;
import judge.remote.language.common.LanguageManager;
import judge.remote.status.RemoteStatusType;
import judge.remote.status.RunningSubmissions;
import judge.remote.submitter.common.SubmitCodeManager;
import judge.service.ProblemService;
import judge.tool.OnlineTool;
import judge.tool.Tools;

import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;

/**
 * 处理 题库/练习 前端相关功能
 * @author Isun
 *
 */
@SuppressWarnings("unchecked")
public class ProblemAction extends BaseAction{
	private final static Logger log = LoggerFactory.getLogger(ProblemAction.class);

	private static final long serialVersionUID = 5557740709776919006L;
	private int id;	//problemId
	private int uid;
	private int isOpen;
	private int res;	//result
	private String OJId;
	private String probNum, probNum1, probNum2;
	private Problem problem;
	private Description description;
	private Submission submission;
	private List dataList;
	private String language;
	private String source;
	private String redir;
	private String un;
	private String _64Format;
	private Integer isSup;
	private DataTablesPage dataTablesPage;
	private Map<String, String> languageList;
	private String submissionInfo;
	
	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private SubmitCodeManager submitManager;

	@Autowired
	private RunningSubmissions runningSubmissions;

	@Autowired
	private LanguageManager languageManager;

	public String toListProblem() {
		Map session = ActionContext.getContext().getSession();
		if (session.containsKey("error")){
			this.addActionError((String) session.get("error"));
		}
		session.remove("error");
		return SUCCESS;
	}

	public String listProblem() {
		Map session = ActionContext.getContext().getSession();
		StringBuffer hql = new StringBuffer("select problem.originOJ, problem.originProb, problem.title, problem.triggerTime, problem.source, problem.id, problem.url, problem.timeLimit from Problem problem where 1=1 ");
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
//		log.info("iSortCol_0 = " + iSortCol_0);
		if (iSortCol_0 != null){
			if (!"desc".equals(sSortDir_0)) {
				sSortDir_0 = "";
			}
			if (iSortCol_0 == 1){
				hql.append(" order by problem.originProb " + sSortDir_0);
			} else if (iSortCol_0 == 2){
				hql.append(" order by problem.title " + sSortDir_0);
			} else if (iSortCol_0 == 3){
				hql.append(" order by problem.triggerTime " + sSortDir_0 + ", problem.originProb " + sSortDir_0);
			} else if (iSortCol_0 == 4){
				hql.append(" order by problem.source " + sSortDir_0);
			}
		}

		List<Object[]> aaData = baseService.list(hql.toString(), paraMap, iDisplayStart, iDisplayLength);
		for (Object[] o : aaData) {
			o[3] = ((Date)o[3]).getTime();
		}
		dataTablesPage.setAaData(aaData);

		if (session.containsKey("error")){
			this.addActionError((String) session.get("error"));
		}
		session.remove("error");

		return SUCCESS;
	}

	public String addProblem() throws InstantiationException, IllegalAccessException{
		Map session = ActionContext.getContext().getSession();

		if (id > 0) {
			problem = (Problem) baseService.query(Problem.class, id);
			if (problem == null) {
				session.put("error", "Please choose a legal OJ!");
				return ERROR;
			}
			OJId = problem.getOriginOJ();
			probNum1 = problem.getOriginProb();
			probNum2 = null;
		}

		if (!OJList.contains(OJId)){
			session.put("error", "Please choose a legal OJ!");
			return ERROR;
		}

		if (probNum1 != null){
			probNum1 = probNum1.trim();
			if (probNum1.isEmpty()){
				probNum1 = null;
			}
		}
		if (probNum2 != null){
			probNum2 = probNum2.trim();
			if (probNum2.isEmpty()){
				probNum2 = null;
			}
		}
		if (probNum1 == null && probNum2 == null){
			session.put("error", "Please enter at least ONE problem number!");
			return ERROR;
		}
		List<String> probNumList = new ArrayList<String>();
		if (probNum2 == null){
			probNumList.add(probNum1);
		} else if (probNum1 == null){
			probNumList.add(probNum2);
		} else if (probNum1.equals(probNum2)){
			probNumList.add(probNum1);
		} else if (probNum1.matches("\\d+") && probNum2.matches("\\d+")){
			int l = Integer.parseInt(probNum1), r = Integer.parseInt(probNum2), tmp;
			if (l > r){
				tmp = l;
				l = r;
				r = tmp;
			}
			if (r - l > 19){
				session.put("error", "You can add 20 problems at most for each time!");
				return ERROR;
			}
			for (Integer a = l; a <= r; ++a) {
				probNumList.add(a.toString());
			}
		} else {
			session.put("error", "Invalid problem number ...");
			return ERROR;
		}

		for (String probNum : probNumList) {
			probNum = probNum.replaceAll("\\W", "");
			////////////////////////////////////////////////////////////
			problem = judgeService.findProblem(OJId.trim(), probNum);
			if (problem == null){
				problem = new Problem();
				problem.setOriginOJ(OJId.trim());
				problem.setOriginProb(probNum.toUpperCase());
				problem.setTitle("N/A");
			}
			problemService.updateProblem(problem, true);
		}
		return id > 0 ? "recrawl" : SUCCESS;
	}
	

	public String viewProblem(){
		List list = baseService.query("select p from Problem p left join fetch p.descriptions where p.id = " + id);
		problem = (Problem) list.get(0);
		_64Format = lf.get(problem.getOriginOJ());
		
		problemService.updateProblem(problem, false);
		
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
		baseService.addOrModify(desc);
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
		problem = (Problem) baseService.query(Problem.class, id);
		if (problem == null) {
			return ERROR;
		}
		
		languageList = languageManager.getLanguages(problem.getOriginOJ());
//		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		isOpen = user.getShare();
		return SUCCESS;
	}


	public String submit() throws Exception{
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null){
			return ERROR;
		}
		problem = (Problem) baseService.query(Problem.class, id);
//		ServletContext sc = ServletActionContext.getServletContext();
//		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		languageList = languageManager.getLanguages(problem.getOriginOJ());

		if (problem == null){
			this.addActionError("Please submit via normal approach!");
			return INPUT;
		}
		if (problem.getTimeLimit() == 1 || problem.getTimeLimit() == 2){
			this.addActionError("Crawling has not finished!");
			return INPUT;
		}

		if (!languageList.containsKey(language)){
			this.addActionError("No such a language!");
			return INPUT;
		}
		source = new String(Base64.decodeBase64(source), "utf-8");
		if (source.length() < 50){
			this.addActionError("Source code should be longer than 50 characters!");
			return INPUT;
		}
		if (source.getBytes("utf-8").length > 30000){
			this.addActionError("Source code should be shorter than 30000 bytes in UTF-8!");
			return INPUT;
		}
		submission = new Submission();
		submission.setSubTime(new Date());
		submission.setProblem(problem);
		submission.setUser(user);
		submission.setStatus("Pending");
		submission.setStatusCanonical(RemoteStatusType.PENDING.name());
		submission.setLanguage(language);
		submission.setSource(source);
		submission.setIsOpen(isOpen);
		submission.setDispLanguage(languageManager.getLanguages(problem.getOriginOJ()).get(language));
		submission.setUsername(user.getUsername());
		submission.setOriginOJ(problem.getOriginOJ());
		submission.setOriginProb(problem.getOriginProb());
		baseService.addOrModify(submission);
		if (user.getShare() != submission.getIsOpen()) {
			user.setShare(submission.getIsOpen());
			baseService.addOrModify(user);
		}
		
		submitManager.submitCode(submission);

		return SUCCESS;
	}

	public String status() {
		if (id != 0){
			problem = (Problem) baseService.query(Problem.class, id);
			OJId = problem.getOriginOJ();
			probNum = problem.getOriginProb();
		}
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		isSup = user == null ? 0 : user.getSup();

		if (session.containsKey("error")){
			this.addActionError((String) session.get("error"));
		}
		session.remove("error");

		return SUCCESS;
	}

	public String fetchStatus() {
		Map session = ActionContext.getContext().getSession();
		Map paraMap = new HashMap();
		User user = (User) session.get("visitor");
		int userId = user != null ? user.getId() : -1;
		int sup = user != null ? user.getSup() : 0;

		StringBuffer hql = new StringBuffer(
				"select "
				+ " s.id, " // 0
				+ " s.username, "
				+ " s.problem.id, "
				+ " s.status, "
				+ " s.memory, "
				+ " s.time, " // 5
				+ " s.dispLanguage, "
				+ " length(s.source), "
				+ " s.subTime, "
				+ " s.user.id, "
				+ " s.isOpen, " // 10
				+ " s.originOJ, "
				+ " s.originProb, "
				+ " s.contest.id, "
				+ " s.additionalInfo, " // 14
				+ " s.statusCanonical, "
				+ " s.id "
				+ "from Submission s ");

		dataTablesPage = new DataTablesPage();

		dataTablesPage.setITotalRecords(9999999L);

		if (sup == 0){
			hql.append(" left join s.contest c where s.isPrivate = 0 and (c is null or c.endTime < :currentTime) ");
			paraMap.put("currentTime", new Date());
		} else {
			hql.append(" where 1 = 1 ");
		}

		if (un != null && !un.trim().isEmpty()){
			hql.append(" and s.username = :un ");
			paraMap.put("un", un.toLowerCase().trim());
		}

		if (id != 0){
			hql.append(" and s.problem.id = " + id);
		} else {
			if (!probNum.isEmpty()){
				hql.append(" and s.originProb = :probNum ");
				paraMap.put("probNum", probNum);
			}
			if (OJList.contains(OJId)){
				hql.append(" and s.originOJ = :OJId ");
				paraMap.put("OJId", OJId);
			}
		}

		if (res == 1){
			hql.append(" and s.statusCanonical = 'AC' ");
		} else if (res == 2) {
			hql.append(" and s.statusCanonical = 'PE' ");
		} else if (res == 3) {
			hql.append(" and s.statusCanonical = 'WA' ");
		} else if (res == 4) {
			hql.append(" and s.statusCanonical = 'TLE' ");
		} else if (res == 5) {
			hql.append(" and s.statusCanonical = 'MLE' ");
		} else if (res == 6) {
			hql.append(" and s.statusCanonical = 'OLE' ");
		} else if (res == 7) {
			hql.append(" and s.statusCanonical = 'RE' ");
		} else if (res == 8) {
			hql.append(" and s.statusCanonical = 'CE' ");
		} else if (res == 9) {
			hql.append(" and s.statusCanonical = 'FAILED_OTHER' ");
		} else if (res == 10) {
			hql.append(" and s.statusCanonical in ('SUBMIT_FAILED_TEMP', 'SUBMIT_FAILED_PERM') ");
		} else if (res == 11) {
			hql.append(" and s.statusCanonical in ('PENDING', 'SUBMITTED', 'QUEUEING', 'COMPILING', 'JUDGING') ");
		}

		hql.append(" order by s.id desc ");

		dataTablesPage.setITotalDisplayRecords(9999999L);

		List<Object[]> aaData = baseService.list(hql.toString(), paraMap, iDisplayStart, iDisplayLength);

		for (Object[] o : aaData) {
			o[8] = ((Date)o[8]).getTime();
			o[10] = (Integer)o[10] > 0 ? 2 : sup > 0 || (Integer)o[9] == userId ? 1 : 0;
			o[14] = o[14] == null ? 0 : 1;
			
			RemoteStatusType statusType = RemoteStatusType.valueOf((String)o[15]);
			o[15] = statusType == RemoteStatusType.AC ? 0 : statusType.finalized ? 1 : 2; // 0-green 1-red 2-purple
			
			int submissionId = (Integer) o[0];
			try {
				if (!statusType.finalized && statusType != RemoteStatusType.SUBMIT_FAILED_TEMP) {
					if (runningSubmissions.getFreezeLength(submissionId) > 300000L) {
						submission = (Submission) baseService.query(Submission.class, submissionId);
						judgeService.rejudge(submission, false);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			
			o[16] = runningSubmissions.contains(submissionId) ? 1 : 0; // 1-working 0-notWorking
		}

		dataTablesPage.setAaData(aaData);

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
		redir = ServletActionContext.getRequest().getHeader("Referer") + "&edit=1";
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
		description.setDescription(Jsoup.clean(description.getDescription(), Whitelist.basic()));
		description.setInput(Jsoup.clean(description.getInput(), Whitelist.basic()));
		description.setOutput(Jsoup.clean(description.getOutput(), Whitelist.basic()));
		description.setSampleInput(Jsoup.clean(description.getSampleInput(), Whitelist.basic()));
		description.setSampleOutput(Jsoup.clean(description.getSampleOutput(), Whitelist.basic()));
		description.setHint(Jsoup.clean(description.getHint(), Whitelist.basic()));
		
		description.setUpdateTime(new Date());
		description.setAuthor(user.getUsername());
		description.setVote(0);
		description.setProblem(new Problem(id));
		baseService.execute("delete from Description d where d.author = '" + user.getUsername() + "' and d.problem.id = " + id);
		baseService.addOrModify(description);
		return SUCCESS;
	}

	public String deleteDescription(){
		User user = OnlineTool.getCurrentUser();
		if (user == null) {
			return ERROR;
		}
		Session session = baseService.getSession();
		Transaction tx = session.beginTransaction();
		try {
			description = (Description) session.get(Description.class, id);
			if (!description.getAuthor().equals("0") && (user.getSup() == 1 || user.getUsername().equals(description.getAuthor()))){
				Set<Cproblem> cproblems = description.getCproblems();
				if (cproblems.size() > 0) {
					//需要把引用该描述的cproblem置为引用system crawler对应的描述
					Set<Description> descriptions = description.getProblem().getDescriptions();
					Description sysDescription = null;
					for (Description description : descriptions) {
						if (description.getAuthor().equals("0")) {
							sysDescription = description;
							break;
						}
					}
					for (Cproblem cproblem : cproblems) {
						cproblem.setDescription(sysDescription);
					}
				}
				session.delete(description);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			baseService.releaseSession(session);
		}
		return SUCCESS;
	}

	public String viewSource(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		List list = baseService.query("select s from Submission s left join fetch s.contest left join fetch s.problem left join fetch s.user where s.id = " + id);
		if (list.isEmpty()){
			session.put("error", "No such submission!");
			return ERROR;
		}
		submission = (Submission) list.get(0);
		if (!(user != null && (user.getSup() != 0 || user.getId() == submission.getUser().getId()) || submission.getIsOpen() == 1 && (submission.getContest() == null || new Date().compareTo(submission.getContest().getEndTime()) > 0))){
			session.put("error", "No access to this code!");
			return ERROR;
		}
		problem = submission.getProblem();
		submission.setSource(Tools.toHTMLChar(submission.getSource()));
		languageList = languageManager.getLanguages(problem.getOriginOJ());
//		languageList = (Map<Object, String>) ApplicationContainer.serveletContext.getAttribute(problem.getOriginOJ());
		submission.setLanguage(languageList.get(submission.getLanguage()));
		uid = submission.getUser().getId();
		un = submission.getUser().getUsername();

		//这里language用作为shjs提供语言识别所需要的class名
		language = Tools.findClass4SHJS(submission.getLanguage());

		return SUCCESS;
	}



	public String rejudge(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		List<Submission> submissionList = baseService.query("select s from Submission s left join fetch s.problem where s.id = " + id);
		if (!submissionList.isEmpty()) {
			submission = submissionList.get(0);
		}
		if (submission == null){
			return ERROR;
		} else {
			judgeService.rejudge(submission, false);
			return SUCCESS;
		}
	}

	public String toggleOpen() {
		judgeService.toggleOpen(id);
		return SUCCESS;
	}

	public String fetchSubmissionInfo() {
		submission = (Submission) baseService.query(Submission.class, id);
		submissionInfo = submission.getAdditionalInfo();
		return SUCCESS;
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
	public Map<String, String> getLanguageList() {
		return languageList;
	}
	public void setLanguageList(Map<String, String> languageList) {
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
	public String getProbNum1() {
		return probNum1;
	}
	public void setProbNum1(String probNum1) {
		this.probNum1 = probNum1;
	}
	public String getProbNum2() {
		return probNum2;
	}
	public void setProbNum2(String probNum2) {
		this.probNum2 = probNum2;
	}
	public Integer getIsSup() {
		return isSup;
	}
	public void setIsSup(Integer isSup) {
		this.isSup = isSup;
	}
	public String getSubmissionInfo() {
		return submissionInfo;
	}
	public void setSubmissionInfo(String submissionInfo) {
		this.submissionInfo = submissionInfo;
	}
}
