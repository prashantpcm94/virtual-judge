/**
 * 处理比赛相关功能
 */

package judge.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import judge.bean.Contest;
import judge.bean.Cproblem;
import judge.bean.DataTablesPage;
import judge.bean.Problem;
import judge.bean.Submission;
import judge.bean.User;
import judge.service.IBaseService;
import judge.submitter.Submitter;
import judge.tool.ApplicationContainer;
import judge.tool.MD5;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("unchecked")
public class ContestAction extends BaseAction {

	private static final long serialVersionUID = -3594499743692326065L;
	private List dataList, tList;
	private Contest contest;
	private Problem problem;
	private Submission submission;
	private Cproblem cproblem;

	private int hour, minute, d_day, d_hour, d_minute;
	private int id, pid, cid, uid;
	private int res;	//result
	private int isOpen;
	private String password;
	private String language;
	private String source;
	private String un, num;
	private Date curDate;
	private Map<String, String> numList;
	private Map<Object, String> languageList;
	private String _64Format;
	private int contestOver;
	private List<Object[]> sameContests;
	
	private List pids;
	private List OJs;
	private List probNums;
	private List<String> titles;

	private List<Problem> pList;
	
	private boolean s, r, e;	//比赛进行状态
	private DataTablesPage dataTablesPage;
	
	
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
	
	public String toListContest(){
		curDate = new Date();
		Map session = ActionContext.getContext().getSession();
		if (session.containsKey("error")){
			this.addActionError((String) session.get("error"));
		}
		session.remove("error");
		return SUCCESS;
	}

	public String listContest() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		int userId = user != null ? user.getId() : -1;
		int sup = user != null ? user.getSup() : 0;
		
		StringBuffer hql = new StringBuffer("select contest, user from Contest contest, User user where contest.manager.id = user.id ");
		long cnt = baseService.count(hql.toString());
		dataTablesPage = new DataTablesPage();
		dataTablesPage.setITotalRecords(cnt);
		Map paraMap = new HashMap();
		if (sSearch != null && !sSearch.trim().isEmpty()){
			sSearch = sSearch.toLowerCase().trim();
			paraMap.put("keyword", "%" + sSearch + "%");
			hql.append(" and (contest.title like :keyword or user.username like :keyword) ");
		}

		curDate = new Date();
		String curDateString = "'" + sdf.format(curDate) + "'";
		if (s && !r && !e){
			hql.append(" and contest.beginTime > " + curDateString);
		} else if (!s && r && !e) {
			hql.append(" and contest.beginTime < " + curDateString + " and  contest.endTime > " + curDateString);
		} else if (!s && !r && e) {
			hql.append(" and contest.endTime < " + curDateString);
		} else if (s && r && !e) {
			hql.append(" and contest.endTime > " + curDateString);
		} else if (!s && r && e) {
			hql.append(" and contest.beginTime < " + curDateString);
		} else if (s && !r && e) {
			hql.append(" and (contest.beginTime > " + curDateString + " or contest.endTime < " + curDateString + ") ");
		} else if (!s && !r && !e) {
			hql.append(" and 1 = 0 ");
		}
		dataTablesPage.setITotalDisplayRecords(baseService.count(hql.toString(), paraMap));
		
//		System.out.println("iSortCol_0 = " + iSortCol_0);
		if (iSortCol_0 != null){
			if (iSortCol_0 == 0){			//按id
				hql.append(" order by contest.id " + sSortDir_0);
			} else if (iSortCol_0 == 1){	//按标题
				hql.append(" order by contest.title " + sSortDir_0);
			} else if (iSortCol_0 == 2){	//按开始时间
				hql.append(" order by contest.beginTime " + sSortDir_0 + ", contest.id " + sSortDir_0);
			} else if (iSortCol_0 == 6){	//按管理员用户名
				hql.append(" order by user.username " + sSortDir_0);
			}
		}

		List<Object[]> tmp = baseService.list(hql.toString(), paraMap, iDisplayStart, iDisplayLength);
		List aaData =  new ArrayList();
		for (Object[] o : tmp) {
			contest = (Contest) o[0];
			user = (User) o[1];
			Object[] res = {
					contest.getId(),
					contest.getTitle(),
					sdf.format(contest.getBeginTime()),
					trans(contest.getEndTime().getTime() - contest.getBeginTime().getTime(), true),
					curDate.compareTo(contest.getBeginTime()) < 0 ? "Scheduled" : curDate.compareTo(contest.getEndTime()) < 0 ? "Running" : "Ended",
					contest.getPassword() == null ? "Public" : "Private",
					user.getUsername(),
					user.getId(),
					sup > 0 || user.getId() == userId ? 1 : 0
			};
			aaData.add(res);
		}
		dataTablesPage.setAaData(aaData);
		this.addActionError((String) session.get("error"));
		session.remove("error");

		return SUCCESS;
	}

	public String toAddContest() {
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null) {
			return ERROR;
		}
		if (cid > 0){
			contest = (Contest) baseService.query(Contest.class, cid);
			
			//比赛结束后才能clone
			if (new Date().compareTo(contest.getEndTime()) < 0){
				return ERROR;
			}

			contest.setTitle(null);
			contest.setDescription(null);
			contest.setPassword(null);
			
			List<Object []> cproblemList = baseService.query("select cproblem.id, p.originOJ, p.originProb, cproblem.title from Cproblem cproblem, Problem p where cproblem.problem.id = p.id and cproblem.contest.id = " + cid + " order by cproblem.num asc");
			pids = new ArrayList();
			OJs = new ArrayList();
			probNums = new ArrayList();
			titles = new ArrayList();
			for (Object[] o : cproblemList) {
				pids.add(o[0]);
				OJs.add(o[1]);
				probNums.add(o[2]);
				titles.add((String) o[3]);
			}
		} else {
			contest = new Contest();
		}
		contest.setBeginTime(new Date());
		d_hour = 5;
		return SUCCESS;
	}
	
	public void validateAddContest(){
		/**
		 * 标题不能为空，不能超过90字符
		 */
		if (contest.getTitle() == null || contest.getTitle().isEmpty()) {
			this.addActionError("Contest Title shouldn't be empty!");
		} else if (contest.getTitle().length() > 90) {
			this.addActionError("Contest Title should be shorter than 90 characters!");
		}
		
		/**
		 * 比赛描述不得多于65000字符
		 */
		if (contest.getDescription().length() > 65000) {
			this.addActionError("Contest description should be shorter than 65000 characters!");
		}

		/**
		 * 至少要加一道题
		 */
		if (pids.isEmpty()) {
			this.addActionError("Please add one problem at least!");
		}

		/**
		 * 至多26道题
		 */
		if (pids.size() > 26){
			this.addActionError("At most 26 problems!");
		}
		
		/**
		 * 不允许题目重复
		 */
		for (int i = 0; this.getActionErrors().isEmpty() && i < pids.size(); i++) {
			for (int j = i + 1; this.getActionErrors().isEmpty() && j < pids.size(); j++) {
				if (pids.get(i).equals(pids.get(j))){
					this.addActionError("Duplcate problems are not allowed!");
				}
			}
		}

		/**
		 * 题目必须存在于题库中
		 */
		pList = new ArrayList<Problem>();
		for (int i = 0; this.getActionErrors().isEmpty() && i < pids.size(); i++) {
			problem = (Problem) baseService.query(Problem.class, Integer.parseInt((String) pids.get(i)));
			pList.add(problem);
			if (problem == null || problem.getTimeLimit() == 1) {
				this.addActionError("Problem " + pids.get(i) + " doesn't exist!");
			}
		}
		
		contest.setEndTime(new Date(contest.getBeginTime().getTime() + d_day * 86400000L + d_hour * 3600000L + d_minute * 60000L));
		long dur = contest.getEndTime().getTime() - contest.getBeginTime().getTime();
		long start = contest.getBeginTime().getTime() - new Date().getTime();


		/**
		 * 开始时间不能比当前时间早, 比赛必须在30天内开始
		 */
		if (start < 1) {
			this.addActionError("Begin time should be later than the current time!");
		} else if (start > 2592000000L) {
			this.addActionError("The contest should begin in 30 days from now!");
		}
		
		/**
		 * 结束时间必须比开始时间晚,持续时间必须短于30天
		 */
		if (dur < 1) {
			this.addActionError("End time should be later than begin time!");
		} else if (dur > 2592000000L) {
			this.addActionError("Contest duration should be shorter than 30 days!");
		}
	}

	public String addContest() {
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null) {
			return ERROR;
		}

		contest.setManager(user);
		if (contest.getPassword().isEmpty()) {
			contest.setPassword(null);
		} else {
			contest.setPassword(MD5.getMD5(contest.getPassword()));
		}

		dataList = new ArrayList();
		dataList.add(contest);

		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < pids.size(); i++) {
			cproblem = new Cproblem();
			cproblem.setContest(contest);
			problem = pList.get(i);
			cproblem.setProblem(problem);
			if (titles.get(i) == null || titles.get(i).trim().isEmpty()){
				cproblem.setTitle(problem.getTitle());
			} else {
				cproblem.setTitle(titles.get(i).trim());
			}
			cproblem.setNum((char)('A' + i) + "");
			dataList.add(cproblem);
			hashCode.append(problem.getTitle().toLowerCase().replaceAll("\\W", ""));
		}
		hashCode.append(pids.size());
		contest.setHashCode(MD5.getMD5(hashCode.toString().replaceAll("&#\\d+;", "")));
		baseService.addOrModify(dataList);
		
		return SUCCESS;
	}
	
	public String viewContest() {
		Map session = ActionContext.getContext().getSession();
		User user = (User)session.get("visitor");
		int uid = user != null ? user.getId() : -1;
		contest = (Contest) baseService.query("select c from Contest c left join fetch c.manager where c.id = " + cid).get(0);

		curDate = new Date();
		
		if (contest.getPassword() == null || user != null && user.getSup() == 1){
			session.put("C" + cid, 1);
		}
		if (session.get("C" + cid) == null){
			return INPUT;
		}
		if (user != null && (user.getSup() == 1 || user.getId() == contest.getManager().getId()) || curDate.compareTo(contest.getBeginTime()) >= 0){
			dataList = baseService.list("select cproblem.problem.id, cproblem.num, cproblem.title, cproblem.problem.id, cproblem.id from Cproblem cproblem where cproblem.contest.id = '" + cid + "' order by cproblem.id asc", 0, 100);
			for (int i = 0; i < dataList.size(); i++){
				int pid = (Integer)((Object[])dataList.get(i))[0];
				if (uid < 0){
					((Object[])dataList.get(i))[0] = null;
				}
				long uac = baseService.count("from Submission submission where submission.status = 'Accepted' and submission.user.id = '" + uid + "' and " +
						"submission.contest.id = '" + cid + "' and submission.problem.id = '" + pid + "'");
				if (uac == 0){
					((Object[])dataList.get(i))[0] = null;
				}
				long ac = baseService.count("from Submission submission where submission.status = 'Accepted' and " +
						"submission.contest.id = '" + cid + "' and submission.problem.id = '" + pid + "'");
				long total = baseService.count("from Submission submission where submission.contest.id = '" + cid + "' and submission.problem.id = '" + pid + "'");
				((Object[])dataList.get(i))[3] = (total == 0 ? 0.0 : ((long)(0.5 + 10000 * ac / total))/100.0) + "%(" + ac + "/" + total + ")";
			}
		}
		return SUCCESS;
	}
	
	public String loginContest(){
		contest = (Contest) baseService.query("select c from Contest c left join fetch c.manager where c.id = " + cid).get(0);
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
		cid = cproblem.getContest().getId();
		contest = (Contest) baseService.query(Contest.class, cid);
		if (session.get("C" + cid) == null){
			if (contest.getPassword() == null || user != null && user.getSup() == 1){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		if (contest.getBeginTime().compareTo(new Date()) > 0 && (user == null || user.getSup() == 0 && user.getId() != contest.getManager().getId())){
			return "notbegin";
		}
		num = cproblem.getNum();

		List list = baseService.query("select p from Problem p left join fetch p.descriptions where p.id = " + cproblem.getProblem().getId());
		problem = (Problem) list.get(0);
		_64Format = lf.get(problem.getOriginOJ());
		
		if (new Date().compareTo(contest.getEndTime()) > 0){
			contestOver = 1;
		}

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
		cid = cproblem.getContest().getId();
		if (session.get("C" + cid) == null){
			contest = (Contest) baseService.query(Contest.class, cid);
			if (contest.getPassword() == null || user != null && user.getSup() == 1){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		isOpen = user.getShare();
		problem = (Problem) baseService.query(Problem.class, cproblem.getProblem().getId());
		language = (String) session.get("L" + problem.getOriginOJ());
		ServletContext sc = ServletActionContext.getServletContext();
		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		return SUCCESS;
	}
	
	public String submit(){
		Map session = ActionContext.getContext().getSession();
		List<Object[]> list = baseService.query("select cp, p, c from Cproblem cp left join cp.problem p left join cp.contest c where cp.id = " + pid);
		cproblem = (Cproblem) list.get(0)[0];
		problem = (Problem) list.get(0)[1];
		contest = (Contest) list.get(0)[2];
		cid = contest.getId();

		ServletContext sc = ApplicationContainer.sc;
		languageList = (Map<Object, String>) sc.getAttribute(problem.getOriginOJ());
		if (session.get("C" + cid) == null){
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return "login";
			}
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
			this.addActionError("No such language!");
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
		submission.setProblem(problem);
		if (contest.getEndTime().compareTo(new Date()) > 0){
			submission.setContest(contest);
			submission.setIsPrivate(contest.getPassword() == null ? 0 : 1);
		}
		submission.setUser(user);
		submission.setStatus("Pending……");
		submission.setLanguage(language);
		submission.setSource(source);
		submission.setIsOpen(isOpen);
		submission.setDispLanguage(((Map<String, String>)sc.getAttribute(problem.getOriginOJ())).get(language));
		submission.setUsername(user.getUsername());
		submission.setOriginOJ(problem.getOriginOJ());
		submission.setOriginProb(problem.getOriginProb());
		baseService.addOrModify(submission);
		try {
			Submitter submitter = (Submitter) ProblemAction.submitterMap.get(problem.getOriginOJ()).clone();
			submitter.setSubmission(submission);
			submitter.start();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return contest.getEndTime().compareTo(new Date()) > 0 ? SUCCESS : "practice";
	}
	
	public String status(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		
		if (session.get("C" + cid) == null){
			contest = (Contest) baseService.query(Contest.class, cid);
			if (contest.getPassword() == null || user != null && user.getSup() == 1){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		
		numList = new TreeMap();
		numList.put("-", "All");
		List<Object[]> tmpList = baseService.query("select cp.num, cp.title from Cproblem cp where cp.contest.id = " + cid + " order by cp.num asc");
		for (Object[] o : tmpList) {
			numList.put((String) o[0], o[0] + " - " + o[1]);
		}

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
		
		StringBuffer hql = new StringBuffer("select s.id, s.username, cp.num, s.status, s.memory, s.time, s.dispLanguage, length(s.source), s.subTime, s.user.id, s.isOpen, cp.id from Submission s, Cproblem cp where s.contest.id = " + cid + " and s.problem.id = cp.problem.id and s.contest.id = cp.contest.id ");

		dataTablesPage = new DataTablesPage();

		dataTablesPage.setITotalRecords(9999999L);

		if (un != null && !un.trim().isEmpty()){
			un = un.toLowerCase().trim();
			hql.append(" and s.username = '" + un + "' ");
		}
		
		if (!num.equals("-")){
			hql.append(" and cp.num = '" + num + "' ");
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
			hql.append(" and s.status like '%ing%' and s.status not like '%ting%' ");
		}
		
		hql.append(" order by s.id desc ");
		
		dataTablesPage.setITotalDisplayRecords(9999999L);
		
		List<Object[]> aaData = baseService.list(hql.toString(), iDisplayStart, iDisplayLength);

		for (Object[] o : aaData) {
			o[8] = sdf.format((Date)o[8]);
			o[10] = (Integer)o[10] > 0 ? 2 : sup > 0 || (Integer)o[9] == userId ? 1 : 0;
		}

		dataTablesPage.setAaData(aaData);
		this.addActionError((String) session.get("error"));
		session.remove("error");

		return SUCCESS;
	}

	public String standing2(){
		curDate = new Date();
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		contest = (Contest) baseService.query(Contest.class, cid);

		if (session.get("C" + cid) == null){
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		tList = baseService.query("select cproblem from Cproblem cproblem where cproblem.contest.id = " + cid);
		
		if (curDate.compareTo(contest.getBeginTime()) >= 0 || user != null && (user.getSup() != 0 || user.getId() == contest.getManager().getId())){
			Map paraMap = new HashMap();
			paraMap.put("hashCode", contest.getHashCode());
			paraMap.put("beginTime", contest.getBeginTime());
			paraMap.put("curTime", new Date());
			sameContests = baseService.query("select c.id, c.title, c.beginTime, c.endTime, c.manager.username, c.manager.id, c.id from Contest c where c.hashCode = :hashCode and (c.beginTime <= :beginTime or c.endTime <= :curTime) order by c.id desc ", paraMap);
			for (int i = 0; i < sameContests.size(); i++){
				sameContests.get(i)[6] = sameContests.get(i)[0].equals(cid) ? "Scheduled" : curDate.compareTo((Date) sameContests.get(i)[3]) > 0 ? "Ended" : "Runing";
				sameContests.get(i)[3] = trans(((Date)sameContests.get(i)[3]).getTime() - ((Date)sameContests.get(i)[2]).getTime(), true);
			}
		} else {
			sameContests = new ArrayList<Object[]>();
		}
		return SUCCESS;
	}
	
	
	
	public String standing(){
		Map session = ActionContext.getContext().getSession();
		contest = (Contest) baseService.query(Contest.class, cid);
		
		curDate = new Date();

		if (session.get("C" + cid) == null){
			if (contest.getPassword() == null){
				session.put("C" + cid, 1);
			} else {
				return INPUT;
			}
		}
		
		tList = baseService.query("select cproblem from Cproblem cproblem where cproblem.contest.id = " + cid);
		long beginTime = ((Contest)baseService.query(Contest.class, cid)).getBeginTime().getTime();
		int problemNum = tList.size();
		Map<Integer, Integer> pMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < problemNum; i++){
			pMap.put(((Cproblem)tList.get(i)).getProblem().getId(), i);
		}
		List sList = baseService.query("select submission from Submission submission where submission.contest.id =" + cid);
		Map<Integer, ContestInfo> rankList = new HashMap<Integer, ContestInfo>();
		ContestInfo ci;
		Submission submission;
		for (int i = 0; i < sList.size(); i++){
			submission = (Submission)sList.get(i); 
			int uid = submission.getUser().getId();
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
			int index = pMap.get(submission.getProblem().getId());
			if (ci.ACtime[index] < 0){
				String status = submission.getStatus();
				if (status.equals("Accepted")){
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
			ci.sPenalty = trans(ci.penalty, false);
			for (int j = 0; j < problemNum; j++){
				ci.sACtime[j] = trans(ci.ACtime[j], false);
			}
		}
		return SUCCESS;
	}
	
	public String trans(Object object, boolean hasDay){
		long time = (Long)object;
		long d = time / 86400000;
		long h = (hasDay ? time % 86400000 : time) / 3600000;
		long m = time % 3600000 / 60000;
		long s = time % 60000 / 1000;
		return (hasDay && d > 0 ? (d + "天") : "") + h + ":" + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;    
	}
	
	public String deleteContest(){
		Map session = ActionContext.getContext().getSession();
		contest = (Contest) baseService.query(Contest.class, cid);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && user.getId() != contest.getManager().getId()){
			session.put("error", "You don't have access to operation on this contest!");
			return ERROR;
		}
		Number submissionNumber = (Number) baseService.query("select count(s.id) from Contest c left join c.submissions s where c.id = " + cid).get(0);
		if (submissionNumber.longValue() > 0){
			session.put("error", "There are already submissions for this contest!");
			return ERROR;
		}
		baseService.execute("delete from Cproblem cproblem where cproblem.contest.id = " + cid);
		baseService.delete(contest);
		return SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	public String toEditContest(){
		Map session = ActionContext.getContext().getSession();
		contest = (Contest) baseService.query(Contest.class, cid);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && user.getId() != contest.getManager().getId()){
			session.put("error", "You don't have access to operation on this contest!");
			return ERROR;
		}
		curDate = new Date();
		hour = contest.getBeginTime().getHours();
		minute = contest.getBeginTime().getMinutes();
		long dur = contest.getEndTime().getTime() - contest.getBeginTime().getTime();
		d_day = (int) (dur / 86400000);
		d_hour = (int) (dur % 86400000 / 3600000);
		d_minute = (int) (dur % 3600000 / 60000);
		
		List<Object []> cproblemList = baseService.query("select cproblem.id, p.originOJ, p.originProb, cproblem.title from Cproblem cproblem, Problem p where cproblem.problem.id = p.id and cproblem.contest.id = " + cid + " order by cproblem.num asc");
		
		if (contest.getBeginTime().compareTo(curDate) < 0){
			return "running";
		}

		pids = new ArrayList();
		OJs = new ArrayList();
		probNums = new ArrayList();
		titles = new ArrayList();
		for (Object[] o : cproblemList) {
			pids.add(o[0]);
			OJs.add(o[1]);
			probNums.add(o[2]);
			titles.add((String) o[3]);
		}
		return "scheduled";
	}
	
	public String editContest(){
		boolean beiju = false;
		Map session = ActionContext.getContext().getSession();
		Contest oContest = (Contest) baseService.query(Contest.class, cid);
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0 && user.getId() != oContest.getManager().getId()){
			session.put("error", "You don't have access to operation on this contest!");
			return SUCCESS;
		}
		curDate = new Date();

		if (curDate.compareTo(oContest.getBeginTime()) > 0){
			long dur = d_day * 86400000L + d_hour * 3600000L + d_minute * 60000L;
			oContest.setEndTime(new Date(oContest.getBeginTime().getTime() + dur));
			if (dur > 2592000000L){
				this.addActionError("Contest duration should be shorter than 30 days!");
				beiju = true;
			}
			if (contest.getDescription().length() > 65000) {
				this.addActionError("Contest description should be shorter than 65000 characters!");
				beiju = true;
			}
			oContest.setDescription(contest.getDescription());
			if (beiju){
				contest = (Contest) baseService.query(Contest.class, cid);
				return "running";
			}
			baseService.addOrModify(oContest);
			return SUCCESS;
		}
		
		validateAddContest();
		
		if (!this.getActionErrors().isEmpty()) {
			contest = (Contest) baseService.query(Contest.class, cid);
			return "scheduled";
		}
		
		dataList = new ArrayList();
		dataList.add(oContest);
		
		StringBuffer hashCode = new StringBuffer();
		oContest.setTitle(contest.getTitle());
		oContest.setDescription(contest.getDescription());
		oContest.setBeginTime(contest.getBeginTime());
		oContest.setEndTime(contest.getEndTime());
		if (contest.getPassword().isEmpty()) {
			oContest.setPassword(null);
		} else {
			oContest.setPassword(MD5.getMD5(contest.getPassword()));
		}
		baseService.execute("delete from Cproblem cproblem where cproblem.contest.id = " + cid);
		for (int i = 0; i < pids.size(); i++) {
			cproblem = new Cproblem();
			cproblem.setContest(oContest);
			problem = pList.get(i);
			cproblem.setProblem(problem);
			if (titles.get(i) == null || titles.get(i).trim().isEmpty()){
				cproblem.setTitle(problem.getTitle());
			} else {
				cproblem.setTitle((String) titles.get(i));
			}
			cproblem.setNum((char)('A' + i) + "");
			dataList.add(cproblem);
			hashCode.append(problem.getTitle().toLowerCase().replaceAll("\\W", ""));
		}
		hashCode.append(pids.size());
		oContest.setHashCode(MD5.getMD5(hashCode.toString().replaceAll("&#\\d+;", "")));
		baseService.addOrModify(dataList);
		return SUCCESS;
	}
	
	public String viewSource(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		submission = (Submission) baseService.query(Submission.class, id);
		problem = (Problem) baseService.query(Problem.class, submission.getProblem().getId());
		dataList = baseService.query("select cproblem from Cproblem cproblem where cproblem.problem.id = " + submission.getProblem().getId() + " and cproblem.contest.id = " + submission.getContest().getId());
		cproblem = (Cproblem) dataList.get(0);
		contest = (Contest) baseService.query(Contest.class, submission.getContest().getId());
		cid = cproblem.getContest().getId();
		if (user == null || user.getSup() == 0 && user.getId() != submission.getUser().getId()){
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
		user = (User) baseService.query(User.class, submission.getUser().getId());
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
		if (user == null || user.getSup() == 0 && user.getId() != submission.getUser().getId()){
			session.put("error", "No access to this code!");
			return ERROR;
		}
		submission.setIsOpen(1 - submission.getIsOpen());
		baseService.addOrModify(submission);
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
			judgeService.rejudge(submission);
		} else if (pid > 0){
			List<Submission> submissionList = baseService.query("select s from Submission s, Cproblem cp where cp.id = " + pid + " and s.problem.id = cp.problem.id and s.contest.id = cp.contest.id");
			for (Submission submission : submissionList) {
				judgeService.rejudge(submission);
			}
		}
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
	
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public boolean isS() {
		return s;
	}
	public void setS(boolean s) {
		this.s = s;
	}
	public boolean isR() {
		return r;
	}
	public void setR(boolean r) {
		this.r = r;
	}
	public boolean isE() {
		return e;
	}
	public void setE(boolean e) {
		this.e = e;
	}
	public DataTablesPage getDataTablesPage() {
		return dataTablesPage;
	}
	public void setDataTablesPage(DataTablesPage dataTablesPage) {
		this.dataTablesPage = dataTablesPage;
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
	public Map<String, String> getNumList() {
		return numList;
	}
	public void setNumList(Map<String, String> numList) {
		this.numList = numList;
	}
	public String get_64Format() {
		return _64Format;
	}
	public void set_64Format(String _64Format) {
		this._64Format = _64Format;
	}
	public int getContestOver() {
		return contestOver;
	}
	public void setContestOver(int contestOver) {
		this.contestOver = contestOver;
	}
	public List getPids() {
		return pids;
	}
	public void setPids(List pids) {
		this.pids = pids;
	}
	public List getOJs() {
		return OJs;
	}
	public void setOJs(List oJs) {
		OJs = oJs;
	}
	public List getProbNums() {
		return probNums;
	}
	public void setProbNums(List probNums) {
		this.probNums = probNums;
	}
	public List getTitles() {
		return titles;
	}
	public void setTitles(List titles) {
		this.titles = titles;
	}
	public List getSameContests() {
		return sameContests;
	}
	public void setSameContests(List sameContests) {
		this.sameContests = sameContests;
	}

	
}


