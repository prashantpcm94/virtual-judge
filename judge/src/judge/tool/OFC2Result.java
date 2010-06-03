package judge.tool;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import jofc2.model.Chart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.ValueStack;

public class OFC2Result implements Result {
	private static final long serialVersionUID = 6881760833309063964L;
	private static final Log log = LogFactory.getLog(OFC2Result.class);
	
	public void execute(ActionInvocation invocation) throws Exception {
		ActionContext actionContext = invocation.getInvocationContext();
		HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
		
		try {
			ValueStack stack = invocation.getStack();
			Chart chart = (Chart)stack.findValue("ofcChart");
			
			response.setContentType("application/json-rpc;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "No-cache");
			
			PrintWriter out = response.getWriter();
			log.debug(chart.toString());
			out.print(chart.toString());
		} catch (IOException exception) {
			log.error(exception.getMessage(), exception);
			throw exception;
		}
	}
}
