package judge.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import judge.bean.Problem;
import judge.bean.Submission;
import judge.bean.User;
import judge.service.IBaseService;

public class Customize {
	
	static private IBaseService baseService;
	static public ServletContext sc;

	
	@SuppressWarnings("unchecked")
	static public void exee(){

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
