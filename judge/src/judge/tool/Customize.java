package judge.tool;

import java.util.List;

import javax.servlet.ServletContext;

import judge.bean.Contest;
import judge.service.IBaseService;

@SuppressWarnings("unchecked")
public class Customize {
	
	static private IBaseService baseService;
	static public ServletContext sc;
	
	static public void exe(){
		
		List<Contest> cList = baseService.query("select c from Contest c");
		for (Contest contest : cList) {
			List<String[]> cpList = baseService.query("select cp.num, p.title from Cproblem cp, Problem p where cp.problemId = p.id and cp.contestId = " + contest.getId() +  " order by cp.num");
			StringBuffer hashCode = new StringBuffer();
			for (String s[] : cpList) {
				hashCode.append(s[1].toLowerCase().replaceAll("\\W", ""));
			}
			hashCode.append(cpList.size());
			contest.setHashCode(MD5.getMD5(hashCode.toString()));
		}
		baseService.addOrModify(cList);
	}

	

	public void setBaseService(IBaseService baseService) {
		Customize.baseService = baseService;
	}
	
}
