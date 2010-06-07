package judge.tool;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import antlr.collections.List;

import judge.bean.Vlog;
import judge.service.StatService;


public class MyFilter implements Filter{
	
	static private StatService statService;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpSession httpSession = request.getSession();

			String sessionId = httpSession.getId();
			if (!statService.checkSessionId(sessionId)){
				Vlog vlog = new Vlog();
				vlog.setSessionId(sessionId);
				vlog.setIp(request.getRemoteAddr());
				vlog.setReferer((String) request.getHeader("referer"));
				vlog.setUserAgent((String) request.getHeader("user-agent"));
				vlog.setCreateTime(new Date(httpSession.getCreationTime()));
				System.out.println("URL: " + request.getRequestURL());
				System.out.println("sessionId: " + sessionId);
				statService.add(vlog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(req, res);
    }

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public static StatService getStatService() {
		return statService;
	}

	public static void setStatService(StatService statService) {
		MyFilter.statService = statService;
	}


	

}