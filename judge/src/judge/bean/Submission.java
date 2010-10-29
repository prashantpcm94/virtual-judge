package judge.bean;

import java.util.Date;

/**
 * 提交记录
 * @author Isun
 *
 */
public class Submission {
	private int id;			//Hibernate统编ID
	private String status;		//状态
	//Pending,
	//Judging,
	//Accepted,
	//Wrong Answer,
	//Time Limit Exceeded,
	//Memory Limit Exceeded
	//Output Limit Exceeded
	//Runtime Error
	//Compile Error
	
	private int time;			//运行时间(未AC提交为空	单位:ms)
	private int memory;			//运行内存(未AC提交为空	单位:KB)
	private Date subTime;		//提交时间
	
	private int problemId;		//外键	题目
	private int userId;			//外键	提交人
	private int contestId;		//外键	比赛
	
	private String language;	//语言
	private String source;		//源代码
	private int isOpen;			//代码是否公开
	
	private String dispLanguage;//用于显示的语言
	private String username;	//提交者用户名
	

	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	public Date getSubTime() {
		return subTime;
	}
	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}
	public int getProblemId() {
		return problemId;
	}
	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getContestId() {
		return contestId;
	}
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
	public String getDispLanguage() {
		return dispLanguage;
	}
	public void setDispLanguage(String dispLanguage) {
		this.dispLanguage = dispLanguage;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	

}
