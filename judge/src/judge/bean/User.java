package judge.bean;

import java.util.Set;

/**
 * 用户,即OJ账号
 * @author Isun
 *
 */
public class User {
	
	private int id;
	private String username;
	private String password;
	private String qq;
	private String school;
	private String email;
	private String blog;
	private String nickname;
	private int share;
	private int sup;
	

/*	private Set<Submission> submission;
	private Set<Submission> contest;
	
	public Set<Submission> getSubmission() {
		return submission;
	}
	private void setSubmission(Set<Submission> submission) {
		this.submission = submission;
	}
	public Set<Submission> getContest() {
		return contest;
	}
	private void setContest(Set<Submission> contest) {
		this.contest = contest;
	}
*/
	
	public int getId() {
		return id;
	}
	public int getSup() {
		return sup;
	}
	public void setSup(int sup) {
		this.sup = sup;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public User(){}
	
	
	
	
}