package judge.tool;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import judge.service.JudgeService;


public class StartUpListener implements ServletContextListener {

	/* 监听服务器启动 */
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("系统启动");
		
		ServletContext sc = event.getServletContext();
		ApplicationContainer.sc = sc;
		
        Properties prop = new Properties();
        FileInputStream in;
		try {
			in = new FileInputStream(sc.getRealPath("WEB-INF" + File.separator + "web.properties"));
            prop.load(in);
            String basePath = prop.getProperty("basePath").trim();
            sc.setAttribute("basePath", basePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sc.setAttribute("StandingDataPath", "/data/standing");
		
		JudgeService judgeService = (JudgeService) SpringBean.getBean("judgeService", sc);
		judgeService.initJudge();
		
	}

	/* 监听服务器关闭 */
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("系统关闭");
	}
}