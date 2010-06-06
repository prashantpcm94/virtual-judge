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
import judge.service.LogService;


public class MyFilter implements Filter{
	
	static private LogService logService;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpSession httpSession = request.getSession();

			String sessionId = httpSession.getId();
			if (!logService.checkSessionId(sessionId)){
				Vlog vlog = new Vlog();
				vlog.setSessionId(sessionId);
				vlog.setIp(request.getRemoteAddr());
				vlog.setReferer((String) request.getHeader("referer"));
				vlog.setUserAgent((String) request.getHeader("user-agent"));
				vlog.setCreateTime(new Date(httpSession.getCreationTime()));
				logService.add(vlog);
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

	public LogService getLogService() {
		return logService;
	}

	static public void setLogService(LogService logService) {
		MyFilter.logService = logService;
	}
	
	

}