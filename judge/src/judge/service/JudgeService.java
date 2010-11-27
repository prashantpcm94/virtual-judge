package judge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import judge.bean.Problem;
import judge.bean.Submission;
import judge.service.imp.BaseService;

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


}