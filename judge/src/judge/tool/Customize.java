package judge.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import judge.bean.Cproblem;
import judge.bean.Description;
import judge.bean.Problem;
import judge.bean.Submission;
import judge.bean.User;
import judge.service.IBaseService;

public class Customize {
	
	static private IBaseService baseService;
	static public ServletContext sc;
	
	static int pto[] = new int[100000];
	
	@SuppressWarnings("unchecked")
	static public void exe(){

		List<Object[]> plist = baseService.query("select p.id, p.originOJ, p.originProb from Problem p order by p.originOJ asc, p.originProb asc");
		
		String last = null, now;
		int std = 0;
		for (int i = 0; i < plist.size(); i++) {
			now = (String)plist.get(i)[1] + (String)plist.get(i)[2];
			if (i == 0 || !now.equals(last)){
				std = (Integer)plist.get(i)[0];
			}
			pto[(Integer)plist.get(i)[0]] = std;
			last = now;
		}
		
		List<Submission> ls = new ArrayList<Submission>();
		for (int i = 1; i < 100000; i += 2000){
			
			System.out.print(i + " - ");

			ls = baseService.query("select s from Submission s where s.id between " + i + " and " + (i + 1999));
			
			System.out.println(ls.size());
			if (ls.size() == 0){
				continue;
			}
			
			for (Submission s : ls) {
				s.setProblemId(pto[s.getProblemId()]);
			}
	
			baseService.addOrModify(ls);
		}
		
		List<Cproblem> lcp = new ArrayList<Cproblem>();
		for (int i = 1; i < 100000; i += 2000){
			
			System.out.print(i + " - ");

			lcp = baseService.query("select cp from Cproblem cp where cp.id between " + i + " and " + (i + 1999));
			
			System.out.println(lcp.size());
			if (lcp.size() == 0){
				continue;
			}
			
			for (Cproblem s : lcp) {
				s.setProblemId(pto[s.getProblemId()]);
			}
	
			baseService.addOrModify(lcp);
		}
		
		for (int i = 0; i < pto.length; i++){
			if (pto[i] != 0 && pto[i] != i){
				System.out.println(i + " -> " + pto[i]);
				baseService.execute("delete from Problem p where p.id = " + i);
			}
		}
		
		List<Description> ld = new ArrayList<Description>();
		Description description;
		for (int i = 1; i < 100000; i += 2000){
			
			System.out.print(i + " - ");

			List<Problem> lp = baseService.query("select p from Problem p left join fetch p.descriptions where p.id between " + i + " and " + (i + 1999));
			
			System.out.println(lp.size());
			if (lp.size() == 0){
				continue;
			}
			
			ld.clear();
			for (Problem problem : lp) {
				if (problem.getTriggerTime() == null){
					problem.setTriggerTime(problem.getAddTime());
				}
				if (problem.getDescriptions() != null && !problem.getDescriptions().isEmpty()){
					continue;
				}
				description = new Description();
				description.setDescription(problem.getDescription());
				description.setInput(problem.getInput());
				description.setOutput(problem.getOutput());
				description.setSampleInput(problem.getSampleInput());
				description.setSampleOutput(problem.getSampleOutput());
				description.setHint(problem.getHint());
				description.setUpdateTime(problem.getAddTime());
				description.setAuthor("0");
				description.setRemarks("Initializatioin.");
				description.setVote(0);
				description.setProblem(problem);
				ld.add(description);
			}
			baseService.addOrModify(ld);
			baseService.addOrModify(lp);
		}
		
	}


	
	@SuppressWarnings("unchecked")
	static public void exe1(){

		List<Submission> ls = new ArrayList<Submission>();
		List<Object[]> list;
		
		System.out.println("begin");
		
		for (int i = 29000; i < 50000; i += 2000){
			
			System.out.print(i + " - ");

			list = baseService.query("select s, u, p from Submission s, User u, Problem p where s.problemId = p.id and s.userId = u.id and s.id between " + i + " and " + (i + 1999));
			
			System.out.println(list.size());
			if (list.size() == 0){
				continue;
			}
			
			ls.clear();
			for (Object[] o : list) {
				Submission s = (Submission) o[0];
				User u = (User) o[1];
				Problem p = (Problem) o[2];

//				System.out.println(s.getStatus());
//				System.out.println(p.getTitle());
				
				ls.add(s);
				s.setDispLanguage(((Map<Object, String>) sc.getAttribute(p.getOriginOJ())).get(s.getLanguage()));
				s.setUsername(u.getUsername());
			}
			
			list.clear();
	
			baseService.addOrModify(ls);
		}
		
	}


	public IBaseService getBaseService() {
		return baseService;
	}


	public void setBaseService(IBaseService baseService) {
		Customize.baseService = baseService;
	}

	
	
}
