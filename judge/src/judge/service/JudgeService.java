package judge.service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import judge.action.BaseAction;
import judge.bean.Contest;
import judge.bean.Problem;
import judge.bean.Submission;
import judge.service.imp.BaseService;
import judge.submitter.Submitter;
import judge.tool.ApplicationContainer;

@SuppressWarnings("unchecked")
public class JudgeService extends BaseService {
	
	/**
	 * 根据提交ID查询结果
	 * @param id
	 * @return 0:ID 1:结果 2:内存 3:时间
	 */
	public Object[] getResult(int id){
		Object[] ret = new Object[4];
		Submission s = (Submission) query(Submission.class, id);
		ret[0] = id;
		ret[1] = s.getStatus();
		ret[2] = s.getMemory();
		ret[3] = s.getTime();
		return ret;
	}
	
	public Set fetchDescriptions(int id){
		List list = query("select p from Problem p left join fetch p.descriptions where p.id = " + id);
		return list.isEmpty() ? null : ((Problem)list.get(0)).getDescriptions();
	}
	
	public Problem findProblem(String OJ, String problemId){
		Map paraMap = new HashMap<String, String>();
		paraMap.put("OJ", OJ.trim());
		paraMap.put("pid", problemId.trim());
		List list = query("select p from Problem p left join fetch p.descriptions where p.originOJ = :OJ and p.originProb = :pid", paraMap);
		return list.isEmpty() ? null : (Problem)list.get(0);
	}

	public List findProblemSimple(String OJ, String problemId){
		Map paraMap = new HashMap<String, String>();
		paraMap.put("OJ", OJ.trim());
		paraMap.put("pid", problemId.trim());
		List list = query("select p from Problem p left join fetch p.descriptions where p.originOJ = :OJ and p.originProb = :pid", paraMap);
		if (list.isEmpty()){
			return null;
		}
		List res = new ArrayList();
		res.add(((Problem)list.get(0)).getId());
		res.add(((Problem)list.get(0)).getTitle());
		return res;
	}
	
	public void rejudge(Submission submission){
		if (submission.getId() == 0){
			return;
		}
		submission.setStatus("Pending Rejudge");
		this.addOrModify(submission);
		try {
			Submitter submitter = (Submitter) BaseAction.submitterMap.get(submission.getOriginOJ()).clone();
			submitter.setSubmission(submission);
			submitter.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initJudge(){
		List<Submission> sList = this.query("select s from Submission s left join fetch s.problem where s.status like '%ing%' and s.status not like '%rror%' ");
		for (Submission submission : sList) {
			this.rejudge(submission);
		}
	}
	
	/**
	 * 更新比赛排行数据
	 * @param cid 比赛id
	 * @param force 是否强制更新，为false则只有当文件不存在时才更新
	 * @return 0:比赛id		1:数据文件url
	 * @throws Exception
	 */
	public Object[] updateStandingData(Integer cid, boolean force) throws Exception{
		long beginTime = ((Contest) this.query(Contest.class, cid)).getBeginTime().getTime();
		List<Object[]> submissionList = this.query("select s.username, cp.num, s.status, s.subTime from Submission s, Cproblem cp where s.contest.id = " + cid + " and s.problem.id = cp.problem.id and s.contest.id = cp.contest.id order by s.id asc");
		
		String relativePath = (String) ApplicationContainer.sc.getAttribute("StandingDataPath");
		String path = ApplicationContainer.sc.getRealPath(relativePath);
		File data = new File(path, cid.toString());
		Object[] res = new Object[]{cid, relativePath.substring(1) + "/" + cid.toString()};
		if (data.exists()){
			if (!force) {
				return res;
			}
		} else {
			data.createNewFile();
		}
		FileWriter filewriter = new FileWriter(data, false);
		StringBuffer sb = new StringBuffer("[");

		for (int i = 0; i < submissionList.size(); i++){
			Object[] info = submissionList.get(i);
			if (i > 0){
				sb.append(",");
			}
			sb.append("[\"").append(info[0]).append("\",").append(((String)info[1]).charAt(0) - 'A').append(",").append("Accepted".equals(info[2]) ? 1 : 0).append(",").append((((Date)info[3]).getTime() - beginTime) / 1000L).append("]");
		}
		sb.append("]");

		filewriter.write(sb.toString());
		filewriter.close();

		return res;
	}
	
	public Object[] getStandingData(Integer cid) throws Exception{
		return updateStandingData(cid, false);
	}
	
	/**
	 * 获取比赛时间信息
	 * @param cid 比赛ID
	 * @return 0:比赛总时间(s)		1:比赛已进行时间(s)
	 */
	public Long[] getContestTimeInfo(Integer cid){
		List<Object[]> list = this.query("select c.beginTime, c.endTime from Contest c where c.id = " + cid);
		Long beginTime = ((Date) list.get(0)[0]).getTime();
		Long endTime = ((Date) list.get(0)[1]).getTime();
		Long totalTime = endTime - beginTime;
		Long elapsedTime = Math.max(new Date().getTime() - beginTime, 0L);
		return new Long[]{totalTime / 1000L, Math.min(totalTime, elapsedTime) / 1000L};
	}
	
	/**
	 * 切换源码公开属性
	 * @param sid source ID
	 */
	public void toggleOpen(Integer sid) {
		Submission submission = (Submission) this.query(Submission.class, sid);
		submission.setIsOpen(1 - submission.getIsOpen());
		this.addOrModify(submission);
	}

}
