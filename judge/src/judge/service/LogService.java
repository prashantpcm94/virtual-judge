package judge.service;

import java.util.List;

import judge.bean.Vlog;
import judge.service.imp.BaseService;

public class LogService extends BaseService {
	
	// ==============================================================
	// 函数名：getBySessionId
	// 函数描述：根据sessionId查找登录信息
	// 返回值：返回登录信息
	// ==============================================================
	@SuppressWarnings("unchecked")
	public Vlog getBySessionId(String sessionId) {
		List list = this.query("select vlog from Vlog vlog where vlog.sessionId = '" + sessionId + "'");
		return  list.size() > 0 ? (Vlog) list.get(0) : null;
	}

	/**
	 * 检查sessionId是否已存数据库
	 * @param sessionId
	 * @return 是否已存
	 */
	@SuppressWarnings("unchecked")
	public boolean checkSessionId(String sessionId) {
		List list = this.query("select vlog.id from Vlog vlog where vlog.sessionId = '" + sessionId + "'");
		return  list.size() > 0;
	}
	
	
}
