/**
 * 处理比赛相关功能
 */

package judge.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import judge.bean.Contest;
import judge.bean.Cproblem;
import judge.bean.Problem;
import judge.bean.Submission;
import judge.bean.User;
import judge.service.IBaseService;
import judge.submitter.Submitter;
import judge.tool.MD5;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class ContestAction extends ActionSupport {

	private static final long serialVersionUID = -3594499743692326065L;
	private List dataList, tList;
	private Contest contest;
	private Problem problem;
	private Submission submission;
	private Cproblem cproblem;
	private IBaseService baseService;

	private int year, month, date, hour, minute, d_day, d_hour, d_minute;
	private int id, pid, cid, uid;
	private int isOpen;
	private String password;
	private String problemList;
	private String language;
	private String source;
	private String un, num;
	private Date curDate;
	private Map<Object, String> languageList;
	
	class ContestInfo{
		public String handle;
		public int userId;
		public int solCnt;
		public long penalty;
		public String sPenalty;
		public long[] ACtime;
		public String[] sACtime;
		public int[] attempts;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getUn() {
		return un;
	}
	public void setUn(String un) {
		this.un = un;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public List getTList() {
		return tList;
	}
	public void setTList(List list) {
		tList = list;
	}
	public Cproblem getCproblem() {
		return cproblem;
	}
	public void setCproblem(Cproblem cproblem) {
		this.cproblem = cproblem;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Map<Object, String> getLanguageList() {
		return languageList;
	}
	public void setLanguageList(Map<Object, String> languageList) {
		this.languageList = languageList;
	}
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getProblemList() {
		return problemList;
	}
	public void setProblemList(String problemList) {
		this.problemList = problemList;
	}
	public Date getCurDate() {
		return curDate;
	}
	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getD_day() {
		return d_day;
	}
	public void setD_day(int d_day) {
		this.d_day = d_day;
	}
	public int getD_hour() {
		return d_hour;
	}
	public void setD_hour(int d_hour) {
		this.d_hour = d_hour;
	}
	public int getD_minute() {
		return d_minute;
	}
	public void setD_minute(int d_minute) {
		this.d_minute = d_minute;
	}
	public Contest getContest() {
		return contest;
	}
	public void setContest(Contest contest) {
		this.contest = contest;
	}
	public IBaseService getBaseService() {
		return baseService;
	}
	public void setBaseService(IBaseService baseService) {
		this.baseService = baseService;
	}
	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public String listContest() {
		curDate = new Date();
		dataList = baseService.list("select contest from Contest contest order by contest.beginTime desc, contest.id desc", 0, 100);
		Map session = ActionContext.getContext().getSession();
		this.addActionError((String) session.get("error"));
		session.remove("error");
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public String toAddContest() {
		curDate = new Date();
		year = curDate.getYear() + 1900;
		month = curDate.getMonth() + 1;
		date = curDate.getDate();
		hour = curDate.getHours();
		minute = curDate.getMinutes();
		d_hour = 5;
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public String addContest() {
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null) {
			return ERROR;
		}

		boolean beiju = false;

		/**
		 * 标题不能为空
		 */
		if (contest.getTitle() == null || contest.getTitle().isEmpty()) {
			this.addActionError("Contest Title shouldn't be empty!");
			beiju = true;
		} else if (contest.getTitle().length() > 90) {
			this.addActionError("Contest Title should be shorter than 90 characters!");
			beiju = true;
		}
		if (contest.getDescription().length() > 65000) {
			this.addActionError("Contest description should be shorter than 65000 characters!");
			beiju = true;
		}
		
		List<String> pl = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(problemList);
		while (m.find()) {
			pl.add(m.group());
		}

		/**
		 * 至少要加一道题
		 */
		if (pl.isEmpty()) {
//			this.addActionError("Please add one problem at least!");
//			beiju = true;
		} else {
			for (int i = 0; i < pl.size(); i++) {
				problem = (Problem) baseService.query(Problem.class, Integer.parseInt(pl.get(i)));
				if (problem == null) {
					this.addActionError("Problem " + pl.get(i) + " doesn't exist!");
					beiju = true;
				} else if (problem.getHidden() == 1 && user.getId() != problem.getCreatorId() && user.getSup() != 1){
					this.addActionError("Problem " + pl.get(i) + " is hidden!");
					beiju = true;
				}
			}
			for (int i = 0; !beiju && i < pl.size(); i++) {
				for (int j = i + 1; !beiju && j < pl.size(); j++) {
					if (pl.get(i).equals(pl.get(j))){
						this.addActionError("Duplcate problems are not allowed!");
						beiju = true;
					}
				}
			}
		}
		
		if (pl.size() > 26){
			this.addActionError("At most 26 problems!");
			beiju = true;
		}

		contest.setManagerId(user.getId());
		if (contest.getPassword().isEmpty()) {
			contest.setPassword(null);
		} else {
			contest.setPassword(MD5.getMD5(contest.getPassword()));
		}
		contest.setBeginTime(new Date(year - 1900, month - 1, date, hour, minute));
		contest.setEndTime(new Date(contest.getBeginTime().getTime() + d_day * 86400000L + d_hour * 3600000L + d_minute * 60000L));
		long dur = contest.getEndTime().getTime() - contest.getBeginTime().getTime();
		long start = contest.getBeginTime().getTime() - new Date().getTime();
		
		/**
		 * 开始时间不能比当前时间早, 比赛必须在30天内开始
		 */
		if (start < 1) {
			this.addActionError("Begin time should be later than the current time!");
			beiju = true;
		} else if (start > 2592000000L) {
			this.addActionError("The contest should begin in 30 days from now on!");
			beiju = true;
		}
		
		/**
		 * 结束时间必须比开始时间晚,持续时间必须短于30天
		 */
		if (dur < 1) {
			this.addActionError("End time should be later than begin time!");
			beiju = true;
		} else if (dur > 2592000000L) {
			this.addActionError("Contest duration should be shorter than 30 days!");
			beiju = true;
		}

		if (beiju) {
			return INPUT;
		}

		int contestId = (Integer) baseService.add(contest);
		for (int i = 0; i < pl.size(); i++) {
			cproblem = new Cproblem();
			cproblem.setContestId(contestId);
			cproblem.setProblemId(Integer.parseInt(pl.get(i)));
			cproblem.setNum((char)('A' + i) + "");
			baseService.add(cproblem);

			problem = (Problem) baseService.query(Problem.class, Integer.parseInt(pl.get(i)));
			problem.setHidden(1);
		}

		return SUCCESS;
	}
	
	public String viewContest() {
		Map session = ActionContext.getContext().getSession();
		User user = (User)session.get("visitor");
		int uid = user != null ? user.getId() : -1;
		contest = (Contest) baseService.query(Contest.class, cid);

		curDate = new Date();
		Date endDate = contest.getEndTime();
		//如果比赛已结束且比赛结束未超过半小时，则将该比赛所有题目取消hidden，方便在题库练习
		//(后一条件基于如下设想：如果比赛刚结束后没有一个人想再看题目，说明关注度不够，这些题目也没有必要再公开了)
		if (curDate.getTime() - endDate.getTime() > 0 && curDate.getTime() - endDate.getTime() < 1800000){
			openProblems(cid);
		}
		
		if (contest.getPassword() == null || user != null && user.getSup() == 1){
			session.put("C" + cid, 1);
		}
		if (session.get("C" + cid) == null){
			return INPUT;
		}
		if (user != null && (user.getSup() == 1 || user.getId() == contest.getManagerId()) || curDate.compareTo(contest.getBeginTime()) >= 0){
			dataList = baseService.list("select problem.id, cproblem.num, problem.title, problem.id, cproblem.id from Cproblem cproblem, Problem problem " +
					"where cproblem.contestId = '" + cid + "' and problem.id = cproblem.problemId order by cproblem.id asc", 0, 100);
			for (int i = 0; i < dataList.size(); i++){
				int pid = (Integer)((Object[])dataList.get(i))[0];
				if (uid < 0){
					((Object[])dataList.get(i))[0] = null;
				}
				long uac = baseService.count("from Submission submission where submission.status = 'Accepted' and submission.userId = '" + uid + "' and " +
						"submission.contestId = '" + cid + "' and submission.problemId = '" + pid + "'");
				if (uac == 0){
					((Object[])dataList.get(i))[0] = null;
				}
				long ac = baseService.count("from Submission submission where submission.status = 'Accepted' and " +
						"submission.contestId = '" + cid + "' and submission.problemId = '" + pid + "'");
				long total = baseService.count("from Submission submission where submission.contestId = '" + cid + "' and submission.problemId = '" + pid + "'");
				((Object[])dataList.get(i))[3] = (total == 0 ? 0.0 : ((long)(0.5 + 10000 * ac / total))/100.0) + "%(" + ac + "/" + total + ")";
			}
		}
		return SUCCESS;
	}
	
	public String loginContest(){
		contest = (Contest) baseService.query(Contest.class, cid);
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if ((user != null && user.getSup() != 0) || MD5.getMD5(password).equals(contest.getPassword())){
			session.put("C" + cid, 1);
			return SUCCESS;
		}
		this.addActionError("Password is not correct!");
		curDate = new Date();
		return INPUT;
	}
	
	
	public String viewProblem(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		cproblem = (Cproblem) baseService.query(Cproblem.class, pid);
		if (cproblem == null){
			return ERROR;
		}
		cid = cproblem.getContestId();
		contest = (Contest) baseService.query(Contest.class, cid);
		if (session.get("C" + cid) == null){
			contest = (Contest) baseService.query(Contest.class, cid);
			if (contest.getPassword() == null || user != null && user.getSup() == 1){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		if (contest.getBeginTime().compareTo(new Date()) > 0 && (user == null || user.getSup() == 0 && user.getId() != contest.getManagerId())){
			return "notbegin";
		}
		num = cproblem.getNum();
		problem = (Problem) baseService.query(Problem.class, cproblem.getProblemId());
		if (problem.getDescription() != null && problem.getDescription().trim().isEmpty()){
			problem.setDescription(null);
		}
		if (problem.getInput() != null && problem.getInput().trim().isEmpty()){
			problem.setInput(null);
		}
		if (problem.getOutput() != null && problem.getOutput().trim().isEmpty()){
			problem.setOutput(null);
		}
		if (problem.getSampleInput() != null && problem.getSampleInput().trim().isEmpty()){
			problem.setSampleInput(null);
		}
		if (problem.getSampleOutput() != null && problem.getSampleOutput().trim().isEmpty()){
			problem.setSampleOutput(null);
		}
		if (problem.getHint() != null && problem.getHint().trim().isEmpty()){
			problem.setHint(null);
		}
		problem.setOriginOJ(ProblemAction.lf.get(problem.getOriginOJ()));
		return SUCCESS;
	}

	
	public String toSubmit(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null){
//			session.put("redir", "../contest/toSubmit.action?pid=" + pid);
			return ERROR;
		}
		cproblem = (Cproblem) baseService.query(Cproblem.class, pid);
		cid = cproblem.getContestId();
		if (session.get("C" + cid) == null){
			contest = (Contest) baseService.query(Contest.class, cid);
			if (contest.getPassword() == null || user != null && user.getSup() == 1){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		isOpen = user.getShare();
		problem = (Problem) baseService.query(Problem.class, cproblem.getProblemId());
		language = (String) session.get("L" + problem.getOriginOJ());
		ServletContext sc = ServletActionContext.getServletContext();
		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		return SUCCESS;
	}
	
	public String submit(){
		Map session = ActionContext.getContext().getSession();
		cproblem = (Cproblem) baseService.query(Cproblem.class, pid);
		problem = (Problem) baseService.query(Problem.class, cproblem.getProblemId());
		ServletContext sc = ServletActionContext.getServletContext();
		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		cid = cproblem.getContestId();
		contest = (Contest) baseService.query(Contest.class, cid);
		if (session.get("C" + cid) == null){
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return "login";
			}
		}
		if (contest.getEndTime().compareTo(new Date()) < 0){
			this.addActionError("Contest has finished!");
			return INPUT;
		}
		if (contest.getBeginTime().compareTo(new Date()) > 0){
			this.addActionError("Contest has not began!");
			return INPUT;
		}

		User user = (User) session.get("visitor");
		if (user == null){
			return ERROR;
		}
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
		Submission submission = new Submission();
		submission.setSubTime(new Date());
		submission.setProblemId(problem.getId());
		submission.setContestId(cid);
		submission.setUserId(user.getId());
		submission.setStatus("Pending……");
		submission.setLanguage(language);
		submission.setSource(source);
		submission.setIsOpen(isOpen);
		baseService.add(submission);
		try {
			Submitter submitter = (Submitter) ProblemAction.submitterMap.get(problem.getOriginOJ()).clone();
			submitter.setSubmission(submission);
			submitter.start();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String status(){
		Map session = ActionContext.getContext().getSession();
		session.put("pageIndex", 0);
		
		if (session.get("C" + cid) == null){
			contest = (Contest) baseService.query(Contest.class, cid);
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		if (un != null){
			un = un.trim();
		}

		ServletContext sc = ServletActionContext.getServletContext();
		dataList = baseService.list("select submission.id, user.username, cproblem.num, submission.status, submission.memory, submission.time, submission.language, length(submission.source), submission.subTime, problem.originOJ, cproblem.id, submission.id, submission.isOpen, submission.userId from Submission submission, User user, Problem problem, Cproblem cproblem " +
				" where submission.contestId = " + cid + (un != null && !un.isEmpty() ? " and user.username = '" + un + "' " : " ") + (num != null && !num.isEmpty() ? " and cproblem.num = '" + num + "'" : "") + " and submission.userId = user.id and submission.problemId = problem.id and submission.problemId = cproblem.problemId and submission.contestId = cproblem.contestId order by submission.subTime desc", 0, 20);
		for (int i = 0; i < dataList.size(); i++){
			((Object [])dataList.get(i))[6] = ((Map<String, String>)sc.getAttribute((String) ((Object [])dataList.get(i))[9])).get(((Object [])dataList.get(i))[6]);
			String st = ((String)((Object [])dataList.get(i))[3]);
			((Object [])dataList.get(i))[11] = st.equals("Accepted") ? "yes" : st.contains("ing") ? "pending" : "no";
		}
		this.addActionError((String) session.get("error"));
		session.remove("error");
		return SUCCESS;
	}
	
	public String statusPrev(){
		Map session = ActionContext.getContext().getSession();
		int pageIndex = (Integer)session.get("pageIndex") - 1;
		if (pageIndex < 0){
			pageIndex = 0;
		}
		session.put("pageIndex", pageIndex);
		if (session.get("C" + cid) == null){
			contest = (Contest) baseService.query(Contest.class, cid);
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		ServletContext sc = ServletActionContext.getServletContext();
		dataList = baseService.list("select submission.id, user.username, cproblem.num, submission.status, submission.memory, submission.time, submission.language, length(submission.source), submission.subTime, problem.originOJ, cproblem.id, submission.id, submission.isOpen, submission.userId from Submission submission, User user, Problem problem, Cproblem cproblem " +
				" where submission.contestId = " + cid + (un != null && !un.isEmpty() ? " and user.username = '" + un + "' " : " ") + (num != null && !num.isEmpty() ? " and cproblem.num = '" + num + "'" : "") + " and submission.userId = user.id and submission.problemId = problem.id and submission.problemId = cproblem.problemId and submission.contestId = cproblem.contestId order by submission.subTime desc", pageIndex * 20, 20);
		for (int i = 0; i < dataList.size(); i++){
			((Object [])dataList.get(i))[6] = ((Map<String, String>)sc.getAttribute((String) ((Object [])dataList.get(i))[9])).get(((Object [])dataList.get(i))[6]);
			String st = ((String)((Object [])dataList.get(i))[3]);
			((Object [])dataList.get(i))[11] = st.equals("Accepted") ? "yes" : st.contains("ing") ? "pending" : "no";
		}
		return SUCCESS;
	}
	
	public String statusNext(){
		Map session = ActionContext.getContext().getSession();
		int pageIndex = (Integer)session.get("pageIndex") + 1;
		if (session.get("C" + cid) == null){
			contest = (Contest) baseService.query(Contest.class, cid);
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		ServletContext sc = ServletActionContext.getServletContext();
		dataList = baseService.list("select submission.id, user.username, cproblem.num, submission.status, submission.memory, submission.time, submission.language, length(submission.source), submission.subTime, problem.originOJ, cproblem.id, submission.id, submission.isOpen, submission.userId from Submission submission, User user, Problem problem, Cproblem cproblem " +
				" where submission.contestId = " + cid + (un != null && !un.isEmpty() ? " and user.username = '" + un + "' " : " ") + (num != null && !num.isEmpty() ? " and cproblem.num = '" + num + "'" : "") + " and submission.userId = user.id and submission.problemId = problem.id and submission.problemId = cproblem.problemId and submission.contestId = cproblem.contestId order by submission.subTime desc", pageIndex * 20, 20);
		if (dataList.size() == 0){
			pageIndex--;
			dataList = baseService.list("select submission.id, user.username, cproblem.num, submission.status, submission.memory, submission.time, submission.language, length(submission.source), submission.subTime, problem.originOJ, cproblem.id, submission.id, submission.isOpen, submission.userId from Submission submission, User user, Problem problem, Cproblem cproblem " +
					" where submission.contestId = " + cid + (un != null && !un.isEmpty() ? " and user.username = '" + un + "' " : " ") + (num != null && !num.isEmpty() ? " and cproblem.num = '" + num + "'" : "") + " and submission.userId = user.id and submission.problemId = problem.id and submission.problemId = cproblem.problemId and submission.contestId = cproblem.contestId order by submission.subTime desc", pageIndex * 20, 20);
		}
		session.put("pageIndex", pageIndex);
		for (int i = 0; i < dataList.size(); i++){
			((Object [])dataList.get(i))[6] = ((Map<String, String>)sc.getAttribute((String) ((Object [])dataList.get(i))[9])).get(((Object [])dataList.get(i))[6]);
			String st = ((String)((Object [])dataList.get(i))[3]);
			((Object [])dataList.get(i))[11] = st.equals("Accepted") ? "yes" : st.contains("ing") ? "pending" : "no";
		}
		return SUCCESS;
	}
	
	
	public String standing(){
		Map session = ActionContext.getContext().getSession();
		contest = (Contest) baseService.query(Contest.class, cid);
		
		curDate = new Date();
		Date endDate = contest.getEndTime();
		if (curDate.getTime() - endDate.getTime() > 0 && curDate.getTime() - endDate.getTime() < 1800000){
			openProblems(cid);
		}

		if (session.get("C" + cid) == null){
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		
		tList = baseService.query("select cproblem from Cproblem cproblem where cproblem.contestId = " + cid);
		long beginTime = ((Contest)baseService.query(Contest.class, cid)).getBeginTime().getTime();
		int problemNum = tList.size();
		Map<Integer, Integer> pMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < problemNum; i++){
			pMap.put(((Cproblem)tList.get(i)).getProblemId(), i);
		}
		List sList = baseService.query("select submission from Submission submission where submission.contestId =" + cid);
		Map<Integer, ContestInfo> rankList = new HashMap<Integer, ContestInfo>();
		ContestInfo ci;
		Submission submission;
		for (int i = 0; i < sList.size(); i++){
			submission = (Submission)sList.get(i); 
			int uid = submission.getUserId();
			if (!rankList.containsKey(uid)){
				ci = new ContestInfo();
				ci.userId = uid;
				ci.ACtime = new long[problemNum];
				ci.sACtime = new String[problemNum];
				for (int j = 0; j < problemNum; j++){
					ci.ACtime[j] = -1L;
				}
				ci.attempts = new int[problemNum];
				rankList.put(uid, ci);
			} else {
				ci = rankList.get(uid);
			}
			int index = pMap.get(submission.getProblemId());
			if (ci.ACtime[index] < 0){
				if (submission.getStatus().equals("Accepted")){
					ci.ACtime[index] = submission.getSubTime().getTime() - beginTime;
					ci.solCnt++;
				} else {
					ci.attempts[index]++;
				}
			}
		}
		dataList = new ArrayList(rankList.values());
		for (int i = 0; i < dataList.size(); i++){
			ci = (ContestInfo)dataList.get(i);
			for (int j = 0; j < problemNum; j++){
				if ((Long)ci.ACtime[j] >= 0){
					ci.penalty += ci.ACtime[j] + 1200000L * ci.attempts[j];
				}
			}
			ci.handle = ((User)baseService.query(User.class, ci.userId)).getUsername();
		}
		Collections.sort(dataList, new Comparator() {  
			public int compare(Object o1, Object o2) {
				ContestInfo a = (ContestInfo)o1, b = (ContestInfo)o2;
				if (a.solCnt != b.solCnt){
					return a.solCnt > b.solCnt ? -1 : 1;
				} else {
					return a.penalty < b.penalty ? -1 : 1;
				}
			}
		});
		for (int i = 0; i < dataList.size(); i++){
			ci = (ContestInfo)dataList.get(i);
			ci.sPenalty = trans(ci.penalty);
			for (int j = 0; j < problemNum; j++){
				ci.sACtime[j] = trans(ci.ACtime[j]);
			}
		}
		return SUCCESS;
	}
	
	public String trans(Object object){
		long time = (Long)object;
		long d = time / 86400000;
		long h = time % 86400000 / 3600000;
		long m = time % 3600000 / 60000;
		long s = time % 60000 / 1000;
		return (d > 0 ? (d + "天") : "") + h + ":" + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;    
	}
	
	public String deleteContest(){
		Map session = ActionContext.getContext().getSession();
		contest = (Contest) baseService.query(Contest.class, cid);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && user.getId() != contest.getManagerId()){
			session.put("error", "You don't have access to operation on this contest!");
			return ERROR;
		}
		if (user.getSup() == 0 && contest.getBeginTime().compareTo(new Date()) < 0){
			session.put("error", "You can only delete a contest before it starts!");
			return ERROR;
		}
		dataList = baseService.query("select cproblem from Cproblem cproblem where cproblem.contestId = " + cid);
		for (int i = 0; i < dataList.size(); i++){
			baseService.delete(dataList.get(i));
		}
		baseService.delete(contest);
		return SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	public String toEditContest(){
		Map session = ActionContext.getContext().getSession();
		contest = (Contest) baseService.query(Contest.class, cid);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && user.getId() != contest.getManagerId()){
			session.put("error", "You don't have access to operation on this contest!");
			return ERROR;
		}
		curDate = new Date();
		year = contest.getBeginTime().getYear() + 1900;
		month = contest.getBeginTime().getMonth() + 1;
		date = contest.getBeginTime().getDate();
		hour = contest.getBeginTime().getHours();
		minute = contest.getBeginTime().getMinutes();
		long dur = (contest.getEndTime().getTime() - contest.getBeginTime().getTime());
		d_day = (int) (dur / 86400000);
		d_hour = (int) (dur % 86400000 / 3600000);
		d_minute = (int) (dur % 3600000 / 60000);
		dataList = baseService.query("select cproblem from Cproblem cproblem where cproblem.contestId = " + cid);
		problemList = "";
		for (int i = 0; i < dataList.size(); i++){
			problemList += ((Cproblem)dataList.get(i)).getProblemId() + "\n";
		}
		return contest.getBeginTime().compareTo(curDate) < 0 ? "running" : "scheduled";
	}
	
	@SuppressWarnings("deprecation")
	public String editContest(){
		boolean beiju = false;
		Map session = ActionContext.getContext().getSession();
		Contest mContest = (Contest) baseService.query(Contest.class, cid);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && user.getId() != mContest.getManagerId()){
			session.put("error", "You don't have access to operation on this contest!");
			return SUCCESS;
		}
		curDate = new Date();
		Date originBegin = mContest.getBeginTime();
//		Date originEnd = mContest.getEndTime();

		if (curDate.compareTo(originBegin) > 0){
			long dur = d_day * 86400000L + d_hour * 3600000L + d_minute * 60000L;
			mContest.setEndTime(new Date(mContest.getBeginTime().getTime() + dur));
			if (dur > 2592000000L){
				this.addActionError("Contest duration should be shorter than 30 days!");
				beiju = true;
			}
			if (contest.getDescription().length() > 65000) {
				this.addActionError("Contest description should be shorter than 65000 characters!");
				beiju = true;
			}
			mContest.setDescription(contest.getDescription());
			if (beiju){
				contest = (Contest) baseService.query(Contest.class, cid);
				return "running";
			}
			baseService.modify(mContest);
			return SUCCESS;
		}
		
		
		/**
		 * 标题不能为空
		 */
		if (contest.getTitle() == null || contest.getTitle().isEmpty()) {
			this.addActionError("Contest Title shouldn't be empty!");
			beiju = true;
		} else if (contest.getTitle().length() > 90) {
			this.addActionError("Contest Title should be shorter than 90 characters!");
			beiju = true;
		}
		if (contest.getDescription().length() > 65000) {
			this.addActionError("Contest description should be shorter than 65000 characters!");
			beiju = true;
		}
		
		List<String> pl = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(problemList);
		while (m.find()) {
			pl.add(m.group());
		}

		/**
		 * 至少要加一道题
		 */
		if (pl.isEmpty()) {
//			this.addActionError("Please add one problem at least!");
//			beiju = true;
		} else {
			for (int i = 0; i < pl.size(); i++) {
				problem = (Problem) baseService.query(Problem.class, Integer.parseInt(pl.get(i)));
				if (problem == null) {
					this.addActionError("Problem " + pl.get(i) + " doesn't exist!");
					beiju = true;
				} else if (problem.getHidden() == 1 && user.getId() != problem.getCreatorId() && user.getSup() != 1){
					this.addActionError("Problem " + pl.get(i) + " is hidden!");
					beiju = true;
				}
			}
			for (int i = 0; !beiju && i < pl.size(); i++) {
				for (int j = i + 1; !beiju && j < pl.size(); j++) {
					if (pl.get(i).equals(pl.get(j))){
						this.addActionError("Duplcate problems are not allowed!");
						beiju = true;
					}
				}
			}
		}
		
		if (pl.size() > 26){
			this.addActionError("At most 26 problems!");
			beiju = true;
		}

		if (contest.getPassword().isEmpty()) {
			contest.setPassword(null);
		} else {
			contest.setPassword(MD5.getMD5(contest.getPassword()));
		}
		contest.setBeginTime(new Date(year - 1900, month - 1, date, hour, minute));
		contest.setEndTime(new Date(contest.getBeginTime().getTime() + d_day * 86400000 + d_hour * 3600000 + d_minute * 60000));
		long dur = contest.getEndTime().getTime() - contest.getBeginTime().getTime();
		long start = contest.getBeginTime().getTime() - new Date().getTime();
		
		/**
		 * 开始时间不能比当前时间早, 比赛必须在30天内开始
		 */
		if (start < 1) {
			this.addActionError("Begin time should be later than the current time!");
			beiju = true;
		} else if (start > 2592000000L) {
			this.addActionError("The contest should begin in 30 days from now on!");
			beiju = true;
		}
		
		/**
		 * 结束时间必须比开始时间晚,持续时间必须短于30天
		 */
		if (dur < 1) {
			this.addActionError("End time should be later than begin time!");
			beiju = true;
		} else if (dur > 2592000000L) {
			this.addActionError("Contest duration should be shorter than 30 days!");
			beiju = true;
		}

		if (beiju) {
			contest = (Contest) baseService.query(Contest.class, cid);
			return "scheduled";
		}
		
		mContest.setTitle(contest.getTitle());
		mContest.setDescription(contest.getDescription());
		mContest.setBeginTime(contest.getBeginTime());
		mContest.setEndTime(contest.getEndTime());
		mContest.setPassword(contest.getPassword());
		dataList = baseService.query("select cproblem from Cproblem cproblem where cproblem.contestId = " + cid);
		for (int i = 0; i < dataList.size(); i++){
			baseService.delete(dataList.get(i));
		}
		for (int i = 0; i < pl.size(); i++) {
			cproblem = new Cproblem();
			cproblem.setContestId(cid);
			cproblem.setProblemId(Integer.parseInt(pl.get(i)));
			cproblem.setNum((char)('A' + i) + "");
			baseService.add(cproblem);
		}
		System.out.println("mContest = " + mContest);
		baseService.modify(mContest);
		return SUCCESS;
	}

	
	public String viewSource(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		submission = (Submission) baseService.query(Submission.class, id);
		problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
		dataList = baseService.query("select cproblem from Cproblem cproblem where cproblem.problemId = " + submission.getProblemId() + " and cproblem.contestId = " + submission.getContestId());
		cproblem = (Cproblem) dataList.get(0);
		contest = (Contest) baseService.query(Contest.class, submission.getContestId());
		cid = cproblem.getContestId();
		if (user == null || user.getSup() == 0 && user.getId() != submission.getUserId()){
			if (submission.getIsOpen() == 0){
				session.put("error", "No access to this code!");
				return ERROR;
			}
			if ((new Date()).compareTo(contest.getEndTime())< 0){
				session.put("error", "Come back when the contest ends, please :)");
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
	
	public String rejudge(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0){
			return ERROR;
		}
		if (id > 0){
			submission = (Submission) baseService.query(Submission.class, id);
			System.out.println(submission);
			submission.setStatus("Pending Rejudge");
			baseService.modify(submission);
			problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
			try {
				Submitter submitter = (Submitter) ProblemAction.submitterMap.get(problem.getOriginOJ()).clone();
				submitter.setSubmission(submission);
				submitter.start();
			} catch (Exception e) {
				e.printStackTrace();
				return ERROR;
			}
		} else if (pid > 0){
			cproblem = (Cproblem) baseService.query(Cproblem.class, pid);
			dataList = baseService.query("select s from Submission s where s.problemId = " + cproblem.getProblemId()  + " and s.contestId = " + cproblem.getContestId());
			for (int i = 0; i < dataList.size(); i++){
				submission = new Submission();
				submission = (Submission) dataList.get(i);
				System.out.println(submission);
				submission.setStatus("Pending Rejudge");
				baseService.modify(submission);
				problem = (Problem) baseService.query(Problem.class, submission.getProblemId());
				try {
					Submitter submitter = (Submitter) ProblemAction.submitterMap.get(problem.getOriginOJ()).clone();
					submitter.setSubmission(submission);
					submitter.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 公开比赛中的所有题目
	 * @param cid ContestID
	 */
	private void openProblems(int cid) {
		dataList = baseService.list("select problem from Cproblem cproblem, Problem problem where cproblem.contestId = '" + cid + "' and problem.id = cproblem.problemId", 0, 100);
		for (Object o : dataList) {
			Problem p = (Problem) o;
			if (p.getHidden() != 0){
				p.setHidden(0);
				baseService.modify(p);
			}
		}
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
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else if (srcLang.contains("ruby")){
			return "sh_ruby";
		} else {
			return "sh_c";
		}
	}
	
}


