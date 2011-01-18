package judge.tool;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import judge.bean.Contest;
import judge.bean.Description;
import judge.bean.Submission;
import judge.service.IBaseService;

@SuppressWarnings("unchecked")
public class Customize {
	
	static private IBaseService baseService;
	static public ServletContext sc;
	
	static public void fixSubmissionPrivacy() throws Exception{
		int interval = 3000;
		List<Object[]> list;
		List dataList;
		
		for (int i = 9500; i < 100000; i += interval){
			System.out.println(i);
			dataList = new ArrayList();
			list = baseService.query("select s, c from Submission s left join s.contest c where s.id between " + i + " and " + (i + interval - 1));
			for (Object[] o : list) {
				Submission s = (Submission) o[0];
				if (o[1] == null && s.getIsPrivate() != 0){
					s.setIsPrivate(0);
					dataList.add(s);
				}
			}
			baseService.addOrModify(dataList);
		}
	}
	
	static public void fixDescRemark() throws Exception{
		List<Description> list = baseService.query("select d from Description d");
		for (Description description : list) {
			if (description != null && "Initializatioin.".equals(description.getRemarks())){
				description.setRemarks("Initialization.");
			}
		}
		baseService.addOrModify(list);
	}
	
	static public void fillSubmissions() throws Exception{
		int interval = 3000;
		List<Object[]> list;
		List dataList;
		
		for (int i = 9500; i < 100000; i += interval){
			System.out.println(i);
			dataList = new ArrayList();
			list = baseService.query("select s, p.originOJ, p.originProb, c.password from Submission s left join s.problem p left join s.contest c where s.id between " + i + " and " + (i + interval - 1));
			for (Object[] o : list) {
				Submission s = (Submission) o[0];
				s.setOriginOJ((String) o[1]);
				s.setOriginProb((String) o[2]);
				s.setIsPrivate(o[3] == null ? 0 : 1);
				dataList.add(s);
			}
			baseService.addOrModify(dataList);
		}
	}
	
	static public void calcHashCode4Contests(){
		
		List<Contest> cList = baseService.query("select c from Contest c");
		for (Contest contest : cList) {
			List<Object[]> cpList = baseService.query("select cp.num, p.title from Cproblem cp, Problem p where cp.problem.id = p.id and cp.contest.id = " + contest.getId() +  " order by cp.num asc");
			StringBuffer hashCode = new StringBuffer();
			for (Object s[] : cpList) {
				hashCode.append(((String)s[1]).toLowerCase().replaceAll("\\W", ""));
			}
			hashCode.append(cpList.size());
			contest.setHashCode(MD5.getMD5(hashCode.toString().replaceAll("&#\\d+;", "")));
		}
		baseService.addOrModify(cList);
	}

	

	public void setBaseService(IBaseService baseService) {
		Customize.baseService = baseService;
	}
	
}
