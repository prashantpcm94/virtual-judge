package judge.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.bean.Contest;
import judge.bean.Cproblem;
import judge.bean.DataTablesPage;
import judge.bean.Description;
import judge.bean.Problem;
import judge.bean.ReplayStatus;
import judge.bean.Submission;
import judge.bean.User;
import judge.service.IBaseService;
import judge.submitter.Submitter;
import judge.tool.ApplicationContainer;
import judge.tool.MD5;
import judge.tool.OnlineTool;
import judge.tool.Tools;
import judge.tool.ZipUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.opensymphony.xwork2.ActionContext;

/**
 * 处理比赛前端相关功能
 * @author Isun
 */
public class ContestAction extends BaseAction {

	private static final long serialVersionUID = -3594499743692326065L;
	private List dataList, tList;
	private Contest contest;
	private Problem problem;
	private Submission submission;
	private Cproblem cproblem;

	private int d_day, d_hour, d_minute;
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
	private long beginTime;
	private long endTime;
	private Integer isSup;

	private List pids;
	private List OJs;
	private List probNums;
	private List<String> titles;

	private List<Problem> pList;
	private List<Description> descList;

	private boolean s, r, e;	//比赛进行状态
	private DataTablesPage dataTablesPage;

	private int contestType;	//0:普通比赛    1:比赛回放
	private File ranklistFile;	//ranklist数据(csv或excel格式)
	private List<String> selectedCellMeaning;	//选择的cell意义

	private Map cellMeaningOptions;

	private String submissionInfo;

	private String cids;
	private int afterContest;
	private List<Integer> contestIds;
	private List statisticRank;

	private File sourceFile;

	public InputStream getSourceInputStream() throws FileNotFoundException {
		 return new FileInputStream(sourceFile);
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

		if (contestType == 0) {
			hql.append(" and contest.replayStatus is null");
		} else {
			hql.append(" and contest.replayStatus is not null");
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
					contest.getBeginTime().getTime(),
					Tools.transPeriod(contest.getEndTime().getTime() - contest.getBeginTime().getTime(), true),
					contest.getPassword() == null ? "Public" : "Private",
					user.getUsername(),
					user.getId(),
					sup > 0 || user.getId() == userId ? 1 : 0,
					curDate.compareTo(contest.getBeginTime()) < 0 ? "Scheduled" : curDate.compareTo(contest.getEndTime()) < 0 ? "Running" : "Ended"
			};
			aaData.add(res);
		}
		dataTablesPage.setAaData(aaData);
		this.addActionError((String) session.get("error"));
		session.remove("error");

		return SUCCESS;
	}

	public String toAddContest() {
		User user = OnlineTool.getCurrentUser();
		if (user == null) {
			return ERROR;
		}
		if (cid > 0 && judgeService.checkAuthorizeStatus(cid) != 0) {
			contest = (Contest) baseService.query(Contest.class, cid);

			//比赛结束后才能clone
			if (new Date().compareTo(contest.getEndTime()) < 0) {
				//但是超级管理员和比赛创建者例外
				if (user.getSup() == 0 && user.getId() != contest.getManager().getId()) {
					return ERROR;
				}
			}

			contest.setTitle(null);
			contest.setDescription(null);
			contest.setAnnouncement(null);
			contest.setPassword(null);

			List<Object []> cproblemList = baseService.query("select p.id, p.originOJ, p.originProb, cproblem.title from Cproblem cproblem, Problem p where cproblem.problem.id = p.id and cproblem.contest.id = " + cid + " order by cproblem.num asc");
			pids = new ArrayList();
			OJs = new ArrayList();
			probNums = new ArrayList();
			titles = new ArrayList();
			for (Object[] o : cproblemList) {
				pids.add(o[0]);
				OJs.add(o[1]);
				probNums.add(o[2]);
				titles.add(Tools.toPlainChar((String) o[3]));
			}
			long dur = contest.getEndTime().getTime() - contest.getBeginTime().getTime();
			d_day = (int) (dur / 86400000);
			d_hour = (int) (dur % 86400000 / 3600000);
			d_minute = (int) (dur % 3600000 / 60000);
		} else {
			contest = new Contest();
			d_hour = 5;
		}
		contest.setBeginTime(new Date());
		contestType = 0;
		return SUCCESS;
	}

	public void validateAddContest(){
		/**
		 * 标题不能为空，不能超过90字符
		 */
		contest.setTitle(Tools.toHTMLChar(contest.getTitle()));
		if (contest.getTitle() == null || contest.getTitle().trim().isEmpty()) {
			this.addActionError("Contest Title shouldn't be empty!");
			return;
		} else if (contest.getTitle().length() > 90) {
			this.addActionError("Contest Title should be shorter than 90 characters!");
			return;
		}

		/**
		 * 比赛描述不得多于65000字符
		 */
		contest.setDescription(Tools.dropScript(contest.getDescription()));
		if (contest.getDescription() != null && contest.getDescription().length() > 65000) {
			this.addActionError("Contest description should be shorter than 65000 characters!");
			return;
		}

		/**
		 * 比赛公告不得多于65000字符
		 */
		contest.setAnnouncement(Tools.dropScript(contest.getAnnouncement()));
		if (contest.getAnnouncement() != null && contest.getAnnouncement().length() > 65000) {
			this.addActionError("Contest announcement should be shorter than 65000 characters!");
			return;
		}

		/**
		 * 允许TimeMachine的值校验
		 */
		if (contest.getEnableTimeMachine() != 0 && contest.getEnableTimeMachine() != 1) {
			contest.setEnableTimeMachine(1);
		}

		/**
		 * Replay的比赛必须为public,且启用Time Machine
		 */
		if (contestType != 0) {
			contest.setPassword("");
			contest.setEnableTimeMachine(1);
		}

		/**
		 * 至少要加一道题
		 */
		if (pids == null || pids.isEmpty()) {
			this.addActionError("Please add one problem at least!");
			return;
		}

		/**
		 * 至多26道题
		 */
		if (pids.size() > 26){
			this.addActionError("You can't add more than 26 problem!");
			return;
		}

		/**
		 * 不允许题目重复
		 */
		for (int i = 0; i < pids.size(); i++) {
			for (int j = i + 1; this.getActionErrors().isEmpty() && j < pids.size(); j++) {
				if (pids.get(i).equals(pids.get(j))){
					this.addActionError("Duplcate problems are not allowed!");
					return;
				}
			}
		}

		/**
		 * 题目必须存在于题库中
		 */
		pList = new ArrayList<Problem>();
		descList = new ArrayList<Description>();
		Session session = baseService.getSession();
		try {
			for (int i = 0; this.getActionErrors().isEmpty() && i < pids.size(); i++) {
				try {
					Description description = (Description) session.createQuery("select d from Description d left join fetch d.problem where d.problem.id = " + Integer.parseInt((String) pids.get(i)) + " order by d.vote desc, d.updateTime desc").list().get(0);
					descList.add(description);
					problem = description.getProblem();
				} catch (Exception e) {
					e.printStackTrace();
					this.addActionError("Problem " + pids.get(i) + " doesn't exist!");
					return;
				}
				pList.add(problem);
				if (problem.getTimeLimit() == 1 || problem.getTimeLimit() == 2) {
					this.addActionError("Problem " + problem.getOriginOJ() + " " + problem.getOriginProb() + " doesn't finish crawling!");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.addActionError("Unknown error");
			return;
		} finally {
			baseService.releaseSession(session);
		}

		contest.setBeginTime(new Date(beginTime));
		contest.setEndTime(new Date(contest.getBeginTime().getTime() + d_day * 86400000L + d_hour * 3600000L + d_minute * 60000L));
		long dur = contest.getEndTime().getTime() - contest.getBeginTime().getTime();
		long start = contest.getBeginTime().getTime() - new Date().getTime();


		if (contestType == 0) {
			/**
			 * 【普通比赛】:开始时间不能比当前时间早, 比赛必须在30天内开始
			 */
			if (start < 1) {
				this.addActionError("Begin time should be later than the current time!");
				return;
			} else if (start > 2592000000L) {
				this.addActionError("The contest should begin in 30 days from now!");
				return;
			}
		} else {
			/**
			 * 【比赛回放】:比赛必须已经结束
			 */
			if (start + dur > 0) {
				this.addActionError("The contest should have ended!");
				return;
			}
		}

		/**
		 * 结束时间必须比开始时间晚,持续时间必须短于60天
		 */
		if (dur < 1) {
			this.addActionError("End time should be later than begin time!");
			return;
		} else if (dur > 2 * 2592000000L) {
			this.addActionError("Contest duration should be shorter than 60 days!");
			return;
		}

	}

	public String addContest() {
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null) {
			this.addActionError("Please login first!");
			return INPUT;
		}

		contest.setManager(user);
		if (contest.getPassword() == null || contest.getPassword().isEmpty()) {
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
			cproblem.setDescription(descList.get(i));
			if (titles.get(i) == null || titles.get(i).trim().isEmpty()){
				cproblem.setTitle(problem.getTitle());
			} else {
				cproblem.setTitle(Tools.toHTMLChar(titles.get(i).trim()));
			}
			cproblem.setNum((char)('A' + i) + "");
			dataList.add(cproblem);
			hashCode.append(problem.getTitle().toLowerCase().replaceAll("&#\\d+", "").replaceAll("\\W", ""));
		}
		hashCode.append(pids.size());
		contest.setHashCode(MD5.getMD5(hashCode.toString()));

		if (contestType == 0) {
			baseService.addOrModify(dataList);
			cid = contest.getId();
			return SUCCESS;
		} else {
			String[][] ranklistCells = null;
			try {
				ranklistCells = judgeService.splitCells(ranklistFile, pids.size());
				cellMeaningOptions = judgeService.getCellMeaningOptions(ranklistCells, contest.getEndTime().getTime() - contest.getBeginTime().getTime());
			} catch (Exception e) {
				e.printStackTrace();
				this.addActionError(e.getMessage());
				return INPUT;
			}
			session.put("contest", contest);
			session.put("contestData", dataList);
			session.put("ranklistCells", ranklistCells);
			session.put("cellMeaningOptions", cellMeaningOptions);
			return "choose_meaning";
		}
	}

	public String addReplay() {
		Map session = ActionContext.getContext().getSession();
		try {
			String[][] ranklistCells = (String[][]) session.get("ranklistCells");
			cellMeaningOptions = (Map) session.get("cellMeaningOptions");
			contest = (Contest) session.get("contest");
			ReplayStatus replayStatus = judgeService.getReplayStatus(ranklistCells, cellMeaningOptions, selectedCellMeaning, contest.getEndTime().getTime() - contest.getBeginTime().getTime());
			ReplayStatus oldReplayStatus = contest.getReplayStatus();
			contest.setReplayStatus(replayStatus);

			dataList = (List) session.get("contestData");
			dataList.add(replayStatus);

			if (oldReplayStatus != null) {
				baseService.delete(oldReplayStatus);
			}
			baseService.execute("delete from Cproblem cproblem where cproblem.contest.id = " + contest.getId());
			baseService.addOrModify(dataList);

			//删除原有的replayStatus文件
			String relativePath = (String) ApplicationContainer.sc.getAttribute("StandingDataPath");
			String path = ApplicationContainer.sc.getRealPath(relativePath);
			File data = new File(path, contest.getId() + ".json");
			if (data.exists()) {
				data.delete();
			}

			session.remove("cellMeaningOptions");
			session.remove("ranklistCells");
			session.remove("contestData");
			session.remove("contest");
			cid = contest.getId();
		} catch (Exception e) {
			e.printStackTrace();
			this.addActionError(e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}

	public String view() {
		if (judgeService.checkAuthorizeStatus(cid) == 0) {
			return ERROR;
		}

		/////////////////////////// overview ///////////////////////////////

		curDate = new Date();
		contest = (Contest) baseService.query("select c from Contest c left join fetch c.manager where c.id = " + cid).get(0);

		if (curDate.compareTo(contest.getEndTime()) > 0){
			contestOver = 1;
		}

		User user = OnlineTool.getCurrentUser();
		if (curDate.compareTo(contest.getBeginTime()) >= 0 || user != null && (user.getSup() == 1 || user.getId() == contest.getManager().getId())){
			dataList = baseService.query("select cproblem.num, cproblem.problem.originOJ, cproblem.problem.originProb, cproblem.problem.url, cproblem.title from Cproblem cproblem where cproblem.contest.id = '" + cid + "' order by cproblem.num asc");
		}
		beginTime = contest.getBeginTime().getTime();
		endTime = contest.getEndTime().getTime();

		/////////////////////////// problem ///////////////////////////////

		numList = new TreeMap();
		List<Object[]> tmpList = baseService.query("select cp.num, cp.title from Cproblem cp where cp.contest.id = " + cid + " order by cp.num asc");
		for (Object[] o : tmpList) {
			numList.put((String) o[0], o[0] + " - " + o[1]);
		}

		/////////////////////////// status ///////////////////////////////

		/////////////////////////// rank ///////////////////////////////


		return SUCCESS;
	}

	public String showProblem() {
		Map json = new HashMap();
		int authorizeStatus = judgeService.checkAuthorizeStatus(cid);
		if (authorizeStatus == 0) {
			json.put("title", "<script type='text/javascript'>window.location.hash='#overview';window.location.reload()</script>");
		} else {
			Session session = baseService.getSession();
			try {
				String hql = "select cp from Cproblem cp left join fetch cp.problem left join fetch cp.description left join fetch cp.contest where cp.contest.id = :cid and cp.num = :num";
				cproblem = (Cproblem) session.createQuery(hql).setParameter("cid", cid).setParameter("num", num).uniqueResult();
				if (cproblem == null || authorizeStatus != 2 && new Date().compareTo(cproblem.getContest().getBeginTime()) < 0) {
					json.put("title", "<script type='text/javascript'>window.location.hash='#overview';window.location.reload()</script>");
				} else {
					problem = cproblem.getProblem();
					List<Description> descriptions = new ArrayList<Description>();
					if (authorizeStatus == 2) {
						descriptions = new ArrayList<Description>(problem.getDescriptions());
					} else {
						descriptions.add(cproblem.getDescription());
					}
					for (int i = 0; i < descriptions.size(); i++) {
						if (cproblem.getDescription().getId() == descriptions.get(i).getId()) {
							json.put("desc_index", i);
						}
					}
					json.put("descriptions", descriptions);
					if (authorizeStatus == 2 || new Date().compareTo(cproblem.getContest().getEndTime()) > 0) {
						json.put("pid", problem.getId());
						json.put("originURL", problem.getUrl());
						json.put("originProblemNumber", problem.getOriginOJ() + " " + problem.getOriginProb());
					}
					json.put("title", cproblem.getTitle());
					json.put("timeLimit", problem.getTimeLimit());
					json.put("memoryLimit", problem.getMemoryLimit());
					json.put("_64IOFormat", lf.get(problem.getOriginOJ()));
					json.put("languageList", ApplicationContainer.sc.getAttribute(problem.getOriginOJ()));
					json.put("oj", problem.getOriginOJ());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				baseService.releaseSession(session);
			}
		}
		this.json = json;
		return SUCCESS;
	}

	/**
	 * 指定比赛用题的描述
	 * @return
	 */
	public String appointDescription() {
		Session session = baseService.getSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "select cp from Cproblem cp left join fetch cp.contest left join fetch cp.problem where cp.contest.id = :cid and cp.num = :num";
			cproblem = (Cproblem) session.createQuery(hql).setParameter("cid", cid).setParameter("num", num).uniqueResult();

			User user = OnlineTool.getCurrentUser();
			if (user != null && user.getId() == cproblem.getContest().getManager().getId()) {
				Description description = (Description) session.get(Description.class, id);
				if (description.getProblem().getId() == cproblem.getProblem().getId()) {
					cproblem.setDescription(description);
				}
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

	public String checkAuthorizeStatus() {
		json = judgeService.checkAuthorizeStatus(cid) == 0 ? ERROR : SUCCESS;
		return SUCCESS;
	}

	public String loginContest(){
		Map httpSession = ActionContext.getContext().getSession();
		Session session = baseService.getSession();
		String encryptedPassword = (String) session.createQuery("select c.password from Contest c where c.id = " + cid).uniqueResult();
		baseService.releaseSession(session);
		if (MD5.getMD5(password).equals(encryptedPassword)) {
			httpSession.put("P" + cid, 1);
			httpSession.put("lpc", 1);	//Logged into Private Contest
			json = SUCCESS;
		} else {
			json = "Password is not correct!";
		}
		return SUCCESS;
	}


	public String submit() throws UnsupportedEncodingException {
		User user = OnlineTool.getCurrentUser();
		if (user == null) {
			json = "Please login first";
			return SUCCESS;
		}

		Session session = baseService.getSession();
		try {
			cproblem = (Cproblem) session.createQuery("select cproblem from Cproblem cproblem left join fetch cproblem.problem left join fetch cproblem.contest where cproblem.num = :num and cproblem.contest.id = :cid").setParameter("num", num).setParameter("cid", cid).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			json = "No such problem";
			return SUCCESS;
		} finally {
			baseService.releaseSession(session);
		}
		problem = cproblem.getProblem();
		contest = cproblem.getContest();

		if (judgeService.checkAuthorizeStatus(cid) == 0) {
			json = "No access to this contest";
			return SUCCESS;
		}

		if (contest.getBeginTime().compareTo(new Date()) > 0){
			json = "Contest has not began";
			return SUCCESS;
		}

		if (problem.getTimeLimit() == 1 || problem.getTimeLimit() == 2){
			json = "Crawling of this problem has not finished";
			return SUCCESS;
		}

		languageList = (Map<Object, String>) ApplicationContainer.sc.getAttribute(problem.getOriginOJ());
		if (!languageList.containsKey(language)){
			json = "No such language";
			return SUCCESS;
		}

		source = new String(Base64.decodeBase64(source), "utf-8");
		if (source.length() < 50){
			json = "Source code should be longer than 50 characters";
			return SUCCESS;
		}
		if (source.getBytes("utf-8").length > 30000){
			json = "Source code should be shorter than 30000 bytes in UTF-8";
			return SUCCESS;
		}

		Submission submission = new Submission();
		submission.setSubTime(new Date());
		submission.setProblem(problem);
		submission.setContest(contest);
		submission.setIsPrivate(contest.getPassword() == null ? 0 : 1);
		submission.setUser(user);
		submission.setStatus("Pending……");
		submission.setLanguage(language);
		submission.setSource(source);
		submission.setIsOpen(isOpen);
		submission.setDispLanguage(languageList.get(language));
		submission.setUsername(user.getUsername());
		submission.setOriginOJ(problem.getOriginOJ());
		submission.setOriginProb(problem.getOriginProb());
		baseService.addOrModify(submission);
		if (user.getShare() != submission.getIsOpen()) {
			user.setShare(submission.getIsOpen());
			baseService.addOrModify(user);
		}
		try {
			Submitter submitter = submitterMap.get(problem.getOriginOJ()).getClass().newInstance();
			submitter.setSubmission(submission);
			submitter.start();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		json = SUCCESS;
		return SUCCESS;
	}

	public String fetchStatus() {
		int authorizeStatus = judgeService.checkAuthorizeStatus(cid);
		if (authorizeStatus == 0) {
			return ERROR;
		}

		Map session = ActionContext.getContext().getSession();
		User user = OnlineTool.getCurrentUser();
		int userId = user != null ? user.getId() : -1;

		StringBuffer hql = new StringBuffer("select s.id, s.username, cp.num, s.status, s.memory, s.time, s.dispLanguage, length(s.source), s.subTime, s.user.id, s.isOpen, cp.id, s.additionalInfo from Submission s, Cproblem cp where s.contest.id = " + cid + " and s.problem.id = cp.problem.id and s.contest.id = cp.contest.id ");

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
			o[8] = ((Date)o[8]).getTime();
			o[10] = (Integer)o[10] > 0 ? 2 : o[9].equals(userId) || authorizeStatus == 2 ? 1 : 0;
			o[12] = o[12] == null ? 0 : 1;
		}

		dataTablesPage.setAaData(aaData);
		this.addActionError((String) session.get("error"));
		session.remove("error");

		return SUCCESS;
	}

	public String showRankSetting() {
		int authorizeStatus = judgeService.checkAuthorizeStatus(cid);
		if (authorizeStatus == 0) {
			return ERROR;
		}

		curDate = new Date();
		contest = (Contest) baseService.query("select c from Contest c left join fetch c.manager where c.id = " + cid).get(0);

		if (authorizeStatus == 2 || curDate.compareTo(contest.getEndTime()) >= 0 || contest.getEnableTimeMachine() == 1 && curDate.compareTo(contest.getBeginTime()) >= 0){
			Map paraMap = new HashMap();
			paraMap.put("cid", cid);
			paraMap.put("hashCode", contest.getHashCode());
			paraMap.put("beginTime", contest.getBeginTime());
			paraMap.put("curTime", new Date());
			sameContests = baseService.query("select c.id, c.replayStatus.id, c.title, c.beginTime, c.endTime, c.manager.username, c.manager.id, c.id from Contest c where c.id <> :cid and c.password is null and c.hashCode = :hashCode and (c.beginTime <= :beginTime or c.endTime <= :curTime) order by c.beginTime desc ", paraMap);
			for (int i = 0; i < sameContests.size(); i++){
				sameContests.get(i)[7] = (curDate.compareTo((Date) sameContests.get(i)[3]) < 0 ? "Scheduled" : curDate.compareTo((Date) sameContests.get(i)[4]) > 0 ? "Ended" : "Running");
				sameContests.get(i)[4] = Tools.transPeriod(((Date)sameContests.get(i)[4]).getTime() - ((Date)sameContests.get(i)[3]).getTime(), true);
			}
		} else {
			sameContests = new ArrayList<Object[]>();
		}

		return SUCCESS;
	}

	public String deleteContest(){
		Map httpSession = ActionContext.getContext().getSession();
		Session session = baseService.getSession();
		Transaction tx = session.beginTransaction();
		try {
			contest = (Contest) session.get(Contest.class, cid);
			if (judgeService.checkAuthorizeStatus(cid) != 2){
				httpSession.put("error", "You don't have access to operation on this contest!");
				return ERROR;
			}
			Number submissionNumber = (Number) session.createQuery("select count(s.id) from Contest c left join c.submissions s where c.id = " + cid).uniqueResult();
			if (submissionNumber.longValue() > 0){
				httpSession.put("error", "There are already submissions for this contest!");
				return ERROR;
			}

			session.createQuery("delete from Cproblem cproblem where cproblem.contest.id = " + cid).executeUpdate();
			ReplayStatus replayStatus = contest.getReplayStatus();
			session.delete(contest);
			if (replayStatus != null) {
				session.delete(replayStatus);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			baseService.releaseSession(session);
		}
		return SUCCESS;
	}

	public String toEditContest(){
		Map session = ActionContext.getContext().getSession();
		if (judgeService.checkAuthorizeStatus(cid) != 2) {
			session.put("error", "You don't have access to operation on this contest!");
			return ERROR;
		}
		contest = (Contest) baseService.query(Contest.class, cid);
		curDate = new Date();
		beginTime = contest.getBeginTime().getTime();
		long dur = contest.getEndTime().getTime() - contest.getBeginTime().getTime();
		d_day = (int) (dur / 86400000);
		d_hour = (int) (dur % 86400000 / 3600000);
		d_minute = (int) (dur % 3600000 / 60000);
		contest.setTitle(Tools.toPlainChar(contest.getTitle()));

		List<Object []> cproblemList = baseService.query("select cproblem.id, p.originOJ, p.originProb, cproblem.title from Cproblem cproblem, Problem p where cproblem.problem.id = p.id and cproblem.contest.id = " + cid + " order by cproblem.num asc");

		if (contest.getReplayStatus() == null && curDate.compareTo(contest.getBeginTime()) > 0){
			return "brief_edit";
		}

		pids = new ArrayList();
		OJs = new ArrayList();
		probNums = new ArrayList();
		titles = new ArrayList();
		for (Object[] o : cproblemList) {
			pids.add(o[0]);
			OJs.add(o[1]);
			probNums.add(o[2]);
			titles.add(Tools.toPlainChar((String) o[3]));
		}
		contestType = contest.getReplayStatus() == null ? 0 : 1;
		return "detail_edit";
	}

	public String editContest(){
		boolean beiju = false;
		Map session = ActionContext.getContext().getSession();
		if (judgeService.checkAuthorizeStatus(cid) != 2) {
			session.put("error", "You don't have access to operation on this contest!");
			return SUCCESS;
		}

		Contest oContest = (Contest) baseService.query(Contest.class, cid);
		curDate = new Date();
		if (oContest.getReplayStatus() == null && curDate.compareTo(oContest.getBeginTime()) > 0){
			long dur = d_day * 86400000L + d_hour * 3600000L + d_minute * 60000L;
			oContest.setEndTime(new Date(oContest.getBeginTime().getTime() + dur));
			if (dur > 2 * 2592000000L){
				this.addActionError("Contest duration should be shorter than 60 days!");
				beiju = true;
			}

			/**
			 * 标题不能为空，不能超过90字符
			 */
			contest.setTitle(Tools.toHTMLChar(contest.getTitle()));
			if (contest.getTitle() == null || contest.getTitle().trim().isEmpty()) {
				this.addActionError("Contest Title shouldn't be empty!");
				beiju = true;
			} else if (contest.getTitle().length() > 90) {
				this.addActionError("Contest Title should be shorter than 90 characters!");
				beiju = true;
			}

			/**
			 * 比赛描述不得多于65000字符
			 */
			contest.setDescription(Tools.dropScript(contest.getDescription()));
			if (contest.getDescription() != null && contest.getDescription().length() > 65000) {
				this.addActionError("Contest description should be shorter than 65000 characters!");
			}

			/**
			 * 比赛公告不得多于65000字符
			 */
			contest.setAnnouncement(Tools.dropScript(contest.getAnnouncement()));
			if (contest.getAnnouncement() != null && contest.getAnnouncement().length() > 65000) {
				this.addActionError("Contest announcement should be shorter than 65000 characters!");
			}
			oContest.setTitle(contest.getTitle());
			oContest.setDescription(contest.getDescription());
			oContest.setAnnouncement(contest.getAnnouncement());
			if (beiju){
				contest = (Contest) baseService.query(Contest.class, cid);
				return "brief_edit";
			}
			baseService.addOrModify(oContest);
			return SUCCESS;
		}

		validateAddContest();

		if (!this.getActionErrors().isEmpty()) {
			contest = (Contest) baseService.query(Contest.class, cid);
			return "detail_edit";
		}

		dataList = new ArrayList();
		dataList.add(oContest);

		StringBuffer hashCode = new StringBuffer();
		oContest.setTitle(contest.getTitle());
		oContest.setDescription(contest.getDescription());
		oContest.setAnnouncement(contest.getAnnouncement());
		oContest.setBeginTime(contest.getBeginTime());
		oContest.setEndTime(contest.getEndTime());
		oContest.setEnableTimeMachine(contest.getEnableTimeMachine());
		if (contest.getPassword().isEmpty()) {
			oContest.setPassword(null);
		} else {
			oContest.setPassword(MD5.getMD5(contest.getPassword()));
		}
		for (int i = 0; i < pids.size(); i++) {
			cproblem = new Cproblem();
			cproblem.setContest(oContest);
			problem = pList.get(i);
			cproblem.setProblem(problem);
			cproblem.setDescription(descList.get(i));
			if (titles.get(i) == null || titles.get(i).trim().isEmpty()){
				cproblem.setTitle(problem.getTitle());
			} else {
				cproblem.setTitle(Tools.toHTMLChar(titles.get(i).trim()));
			}
			cproblem.setNum((char)('A' + i) + "");
			dataList.add(cproblem);
			hashCode.append(problem.getTitle().toLowerCase().replaceAll("&#\\d+;", "").replaceAll("\\W", ""));
		}
		hashCode.append(pids.size());
		oContest.setHashCode(MD5.getMD5(hashCode.toString()));

		if (contestType == 0 || ranklistFile == null) {
			if (contestType == 0 && oContest.getReplayStatus() != null) {
				baseService.delete(oContest.getReplayStatus());
				oContest.setReplayStatus(null);
			}
			baseService.execute("delete from Cproblem cproblem where cproblem.contest.id = " + cid);
			baseService.addOrModify(dataList);
			return SUCCESS;
		} else {
			String[][] ranklistCells = null;
			try {
				ranklistCells = judgeService.splitCells(ranklistFile, pids.size());
				cellMeaningOptions = judgeService.getCellMeaningOptions(ranklistCells, contest.getEndTime().getTime() - contest.getBeginTime().getTime());
			} catch (Exception e) {
				e.printStackTrace();
				this.addActionError(e.getMessage());
				return "detail_edit";
			}
			session.put("contest", oContest);
			session.put("contestData", dataList);
			session.put("ranklistCells", ranklistCells);
			session.put("cellMeaningOptions", cellMeaningOptions);
			return "choose_meaning";
		}
	}

	public String viewSource(){
		User user = OnlineTool.getCurrentUser();
		int userId = user == null ? -1 : user.getId();

		Session session = baseService.getSession();
		try {
			submission = (Submission) session.get(Submission.class, id);
			contest = submission.getContest();
			contest.getTitle();
			cproblem = (Cproblem) session.createQuery("select cp from Cproblem cp where cp.contest.id = " + contest.getId() + " and cp.problem.id = " + submission.getProblem().getId()).uniqueResult();
			int authorizeStatus = judgeService.checkAuthorizeStatus(contest.getId());
			if (userId != submission.getUser().getId() && authorizeStatus != 2 && (submission.getIsOpen() == 0 || new Date().compareTo(contest.getEndTime()) < 0)) {
				if (submission.getIsOpen() == 0){
					submission.setSource("No access to this code!");
				} else {
					submission.setSource("Come back when the contest ends, please :)");
				}
				language = "sh_sql";
			} else {
				submission.setSource(Tools.toHTMLChar(submission.getSource()));
				//这里language用作为shjs提供语言识别所需要的class名
				language = Tools.findClass4SHJS(submission.getDispLanguage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		} finally {
			baseService.releaseSession(session);
		}
		return SUCCESS;
	}

	public String rejudge(){
		Map session = ActionContext.getContext().getSession();
		User user = (User) session.get("visitor");
		if (user == null || user.getSup() == 0){
			return ERROR;
		}
		List<Submission> submissionList = new ArrayList<Submission>();
		if (id > 0){
			submissionList = baseService.query("select s from Submission s left join fetch s.problem where s.id = " + id);
		} else if (pid > 0){
			submissionList = baseService.query("select s from Submission s left join fetch s.problem, Cproblem cp where cp.id = " + pid + " and s.problem.id = cp.problem.id and s.contest.id = cp.contest.id");
		}
		for (Submission submission : submissionList) {
			judgeService.rejudge(submission);
		}
		return SUCCESS;
	}

	public String fetchSubmissionInfo() {
		List<Submission> list = baseService.query("select s from Submission s left join fetch s.contest where s.id = " + id);
		if (list.isEmpty()) {
			submissionInfo = "Invalid request!";
			return SUCCESS;
		}
		submission = list.get(0);
		contest = submission.getContest();
		if (contest == null) {
			submissionInfo = "Invalid request!";
			return SUCCESS;
		}
		User user = OnlineTool.getCurrentUser();
		if (user != null && user.getId() == submission.getUser().getId() || judgeService.checkAuthorizeStatus(contest.getId()) == 2 || new Date().compareTo(contest.getEndTime()) > 0) {
			submissionInfo = submission.getAdditionalInfo();
		} else if (submission.getIsOpen() == 0) {
			submissionInfo = "No access to this info!";
		} else if (new Date().compareTo(contest.getEndTime()) < 0) {
			submissionInfo = "Come back when the contest ends, please :)";
		}
		return SUCCESS;
	}

	public String statistic() {
		contestIds = new ArrayList<Integer>();
		Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

		Matcher matcher = Pattern.compile("\\d+").matcher(cids + "");
		while (matcher.find() && contestIds.size() < 20) {
			int cid = Integer.parseInt(matcher.group());
			contestIds.add(cid);
		}
		if (contestIds.isEmpty()) {
			return SUCCESS;
		}
		Collections.sort(contestIds);
		for (int i = 0; i < contestIds.size(); i++) {
			indexMap.put(contestIds.get(i), i);
		}

		Map paraMap = new HashMap();
		paraMap.put("cids", contestIds);
		List<Object[]> submissions = baseService.query("select submission.username, submission.contest.id, cproblem.num, submission.problem.id from Submission submission, Cproblem cproblem where submission.status = 'Accepted' and submission.contest.id in :cids and cproblem.problem.id = submission.problem.id and cproblem.contest.id = submission.contest.id " + (afterContest == 0 ? " and submission.subTime < submission.contest.endTime" : ""), paraMap);

		HashMap<String, Object[]> rank = new HashMap<String, Object[]>();
		for (Object[] submission : submissions) {
			Object[] userDetail = rank.get(submission[0]);
			if (userDetail == null) {
				userDetail = new Object[contestIds.size() + 2];
				userDetail[0] = submission[0];
				for (int i = 1; i < userDetail.length; i++) {
					userDetail[i] = new TreeSet();
				}
				rank.put((String) submission[0], userDetail);
			}
			((Set)userDetail[1 + indexMap.get(submission[1])]).add(submission[2]);
			((Set)userDetail[1 + contestIds.size()]).add(submission[3]);
		}
		statisticRank = new ArrayList(rank.values());

		Collections.sort(statisticRank, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				Integer v1 = ((Set)o1[o1.length - 1]).size();
				Integer v2 = ((Set)o2[o2.length - 1]).size();
				return v2.compareTo(v1);
			}
		});

		return SUCCESS;
	}

	public String exportSource() throws Exception {
		if (judgeService.checkAuthorizeStatus(cid) != 2) {
			return ERROR;
		}
		contest = (Contest) baseService.query(Contest.class, cid);
		if (new Date().compareTo(contest.getEndTime()) < 0) {
			return ERROR;
		}

		String relativePath = (String) ApplicationContainer.sc.getAttribute("ContestSourceCodeZipFilePath");
		String basePath = ApplicationContainer.sc.getRealPath(relativePath);

		sourceFile = new File(basePath + "/" + cid + ".zip");
		if (sourceFile.exists()) {
			return SUCCESS;
		}

		File dir = new File(basePath + "/" + cid);
		FileUtils.deleteDirectory(dir);
		dir.mkdirs();

		List<Object[]> submissions = baseService.query("select submission.id, submission.username, cproblem.num, submission.status, submission.dispLanguage, submission.source from Submission submission, Cproblem cproblem where submission.contest.id = " + cid + " and submission.status = 'Accepted' and submission.subTime <= submission.contest.endTime and submission.problem.id = cproblem.problem.id and cproblem.contest.id = " + cid);
		for (Object[] submission : submissions) {
			Integer id = (Integer) submission[0];
			String username = (String) submission[1];
			String pnum = (String) submission[2];
//			String status = (String) submission[3];
			String language = (String) submission[4];
			String source = (String) submission[5];

			String extensionName = Tools.findClass4SHJS(language);

			if (extensionName.equals("sh-cpp")) {
				extensionName = ".cpp";
			} else if (extensionName.equals("sh-c")) {
				extensionName = ".c";
			} else if (extensionName.equals("sh-csharp")) {
				extensionName = ".cs";
			} else if (extensionName.equals("sh-java")) {
				extensionName = ".java";
			} else if (extensionName.equals("sh-pascal")) {
				extensionName = ".pas";
			} else if (extensionName.equals("sh-python")) {
				extensionName = ".py";
			} else if (extensionName.equals("sh-ruby")) {
				extensionName = ".rb";
			}

			File problemDir = new File(basePath + "/" + cid + "/" + pnum);
			if (!problemDir.exists()) {
				problemDir.mkdirs();
			}
			File file = new File(problemDir, id + "_" + username + "_" + pnum + extensionName);
			try{
				FileWriter filewriter = new FileWriter(file, false);
				filewriter.write(source);
				filewriter.close();
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		sourceFile = new File(basePath + "/" + cid + ".zip");
		ZipUtil.zip(sourceFile, dir);
		FileUtils.deleteDirectory(dir);

		return SUCCESS;
	}

	/////////////////////  deprecated  //////////////////////////

	public String viewContest() {
		return SUCCESS;
	}

	public String standing2() {
		return SUCCESS;
	}

	public String viewProblem(){
		cproblem = (Cproblem) baseService.query(Cproblem.class, pid);
		contest = (Contest) baseService.query(Contest.class, cproblem.getContest().getId());
		if (contest.getBeginTime().compareTo(new Date()) > 0) {
			return ERROR;
		} else {
			cid = contest.getId();
			num = cproblem.getNum();
			return SUCCESS;
		}
	}

	/////////////////////  getter & setter  //////////////////////////


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
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public Integer getIsSup() {
		return isSup;
	}
	public void setIsSup(Integer isSup) {
		this.isSup = isSup;
	}
	public int getContestType() {
		return contestType;
	}
	public void setContestType(int contestType) {
		this.contestType = contestType;
	}
	public File getRanklistFile() {
		return ranklistFile;
	}
	public void setRanklistFile(File ranklistFile) {
		this.ranklistFile = ranklistFile;
	}
	public Map getCellMeaningOptions() {
		return cellMeaningOptions;
	}
	public void setCellMeaningOptions(Map cellMeaningOptions) {
		this.cellMeaningOptions = cellMeaningOptions;
	}
	public List<String> getSelectedCellMeaning() {
		return selectedCellMeaning;
	}
	public void setSelectedCellMeaning(List<String> selectedCellMeaning) {
		this.selectedCellMeaning = selectedCellMeaning;
	}
	public String getSubmissionInfo() {
		return submissionInfo;
	}
	public void setSubmissionInfo(String submissionInfo) {
		this.submissionInfo = submissionInfo;
	}
	public String getCids() {
		return cids;
	}
	public void setCids(String cids) {
		this.cids = cids;
	}
	public List getStatisticRank() {
		return statisticRank;
	}
	public void setStatisticRank(List statisticRank) {
		this.statisticRank = statisticRank;
	}
	public List<Integer> getContestIds() {
		return contestIds;
	}
	public void setContestIds(List<Integer> contestIds) {
		this.contestIds = contestIds;
	}
	public int getAfterContest() {
		return afterContest;
	}
	public void setAfterContest(int afterContest) {
		this.afterContest = afterContest;
	}
	public File getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

}


