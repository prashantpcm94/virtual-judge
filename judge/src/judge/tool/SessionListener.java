package judge.tool;

import java.util.Date;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import judge.bean.Vlog;
import judge.service.LogService;


@SuppressWarnings("unchecked")
public class SessionListener implements HttpSessionListener {
	
	static private LogService logService;


	/* 监听session创建 */
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ServletContext application = session.getServletContext();

		// 在application范围由一个HashSet集保存所有的session
		HashSet sessions = (HashSet) application.getAttribute("sessions");
		if (sessions == null) {
			sessions = new HashSet();
			application.setAttribute("sessions", sessions);
		}

		// 新创建的session均添加到HashSet集中
		sessions.add(session);
		// 可以在别处从application范围中取出sessions集合
		// 然后使用sessions.size()获取当前活动的session数，即为“在线人数”
		System.out.println(new Date());
		System.out.println("创建seesion, 总连接数：" + sessions.size());
	}

	/* 监听session销毁 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ServletContext application = session.getServletContext();
		HashSet sessions = (HashSet) application.getAttribute("sessions");
		
		Vlog vlog = logService.getBySessionId(session.getId());
		vlog.setDestroyTime(new Date());
		logService.modify(vlog);

		// 销毁的session均从HashSet集中移除
		if (sessions != null) {
			sessions.remove(session);
			System.out.println(new Date());
			System.out.println("销毁seesion, 总连接数：" + sessions.size());
		}
	}

	public static LogService getLogService() {
		return logService;
	}

	public static void setLogService(LogService logService) {
		SessionListener.logService = logService;
	}
	
	
}