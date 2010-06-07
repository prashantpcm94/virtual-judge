package judge.tool;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import judge.bean.Vlog;
import judge.service.StatService;

public class SessionListener implements HttpSessionListener {
	
	static private StatService statService;


	/* 监听session创建 */
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();

		synchronized (StatService.sessions) {
			StatService.sessions.add(session);
		}
		
		System.out.println(new Date());
		System.out.println("创建seesion");
	}

	/* 监听session销毁 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
//		ServletContext application = session.getServletContext();
		
		Vlog vlog = statService.getBySessionId(session.getId());
		vlog.setDuration(new Date().getTime() - session.getCreationTime());
		statService.modify(vlog);

		synchronized (StatService.sessions) {
			StatService.sessions.remove(session);
		}
		System.out.println(new Date());
		System.out.println("销毁seesion");
	}

	public static StatService getStatService() {
		return statService;
	}

	public static void setStatService(StatService statService) {
		SessionListener.statService = statService;
	}


	
	
}