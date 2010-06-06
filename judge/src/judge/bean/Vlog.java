package judge.bean;

import java.util.Date;
import java.util.Set;

/**
 * 用户,即OJ账号
 * @author Isun
 *
 */
public class Vlog {
	
	private int id;
	private String sessionId;
	private String ip;
	private Date createTime;
	private Date destroyTime;
	private String referer;
	private String userAgent;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getDestroyTime() {
		return destroyTime;
	}
	public void setDestroyTime(Date destroyTime) {
		this.destroyTime = destroyTime;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	

	
}