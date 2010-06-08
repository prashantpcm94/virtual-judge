/**
 * 处理用户相关功能
 */

package judge.action;

import java.util.Map;


import org.apache.struts2.ServletActionContext;

import judge.bean.User;
import judge.bean.Vlog;
import judge.service.IUserService;
import judge.service.StatService;
import judge.tool.MD5;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;



@SuppressWarnings("unchecked")
public class UserAction extends ActionSupport {
	
	private static final long serialVersionUID = -4110838947220309361L;
	private User user;
	private int uid;
	private String username;
	private String nickname;
	private String qq;
	private String school;
	private String email;
	private String blog;
	private int share;
	private String password;
	private String repassword;
	private String newpassword;
	private String redir;
	private IUserService userService;
	private StatService statService;
	
	
	public StatService getStatService() {
		return statService;
	}
	public void setStatService(StatService statService) {
		this.statService = statService;
	}
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
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
	public String getRedir() {
		return redir;
	}
	public void setRedir(String redir) {
		this.redir = redir;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getRepassword() {
		return repassword;
	}
	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	public IUserService getUserService() {
		return userService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
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
	
	
	public String toLogin(){
		redir = ServletActionContext.getRequest().getHeader("Referer");
		return SUCCESS;
	}
	
	public String login(){
		Map session = ActionContext.getContext().getSession();
		User user = userService.getByUsername(username);
		if (user == null || !user.getPassword().equals(MD5.getMD5(password))){
			this.addActionError("Username and password don't match!");
			return INPUT;
		}
		session.put("visitor", user);
		Vlog vlog = statService.getBySessionId(ServletActionContext.getRequest().getSession().getId());
		vlog.setLoginer(user.getId());
		statService.modify(vlog);
		return SUCCESS;
	}
	
	public String logout(){
		Map session = ActionContext.getContext().getSession();
		session.remove("visitor");
		redir = ServletActionContext.getRequest().getHeader("Referer");
		return SUCCESS;
	}

	public String toRegister(){
		redir = ServletActionContext.getRequest().getHeader("Referer");
		return SUCCESS;
	}
	
	public String register(){
		if (!username.matches("[0-9a-zA-Z_]+")){
			this.addActionError("Username should only contain digits, letters, or '_'s !");
		}
		if (username.length() < 1 || username.length() > 16){
			this.addActionError("Username should have at least 1 character and at most 16 characters!");
		}
		if (password.length() < 4 || password.length() > 30){
			this.addActionError("Password should have at least 4 characters and at most 30 characters!");
		}
		if (!password.equals(repassword)){
			this.addActionError("Passwords are not match!");
		}
		if (userService.checkUsername(username)){
			this.addActionError("Username has been registered!");
		}
		if (qq.length() > 15){
			this.addActionError("QQ is too long!");
		}
		if (school.length() > 95){
			this.addActionError("School is too long!");
		}
		if (email.length() > 95){
			this.addActionError("Email is too long!");
		}
		if (blog.length() > 995){
			this.addActionError("Blog is too long!");
		}
		if (!this.getActionErrors().isEmpty()){
			return INPUT;
		}
		User user = new User(username, MD5.getMD5(password));
		user.setNickname(nickname);
		user.setQq(qq);
		user.setSchool(school);
		user.setEmail(email);
		user.setBlog(blog);
		user.setShare(share);
		userService.add(user);
		Map session = ActionContext.getContext().getSession();
		session.put("visitor", user);
		return SUCCESS;
	}
	
	public String toUpdate(){
		user = (User) userService.query(User.class, uid);
		username = user.getUsername();
		nickname = user.getNickname();
		school = user.getSchool();
		qq = user.getQq();
		email = user.getEmail();
		blog = user.getBlog();
		share = user.getShare();
		uid = user.getId();
		redir = ServletActionContext.getRequest().getHeader("Referer");
		return SUCCESS;
	}

	
	public String update(){
		user = (User) userService.query(User.class, uid);
		Map session = ActionContext.getContext().getSession();
		User cUser = (User) session.get("visitor");
		if (user == null || cUser == null || cUser.getId() != user.getId()){
			return ERROR;
		}
		if (!user.getPassword().equals(MD5.getMD5(password))){
			this.addActionError("Enter the correct old password!");
		}
		if (!newpassword.isEmpty() || !repassword.isEmpty()){
			if (newpassword.length() < 4 || newpassword.length() > 30){
				this.addActionError("Password should have at least 4 characters and at most 30 characters!");
			}
			if (!newpassword.equals(repassword)){
				this.addActionError("Passwords are not match!");
			}
			user.setPassword(MD5.getMD5(newpassword));
		}
		if (qq.length() > 15){
			this.addActionError("QQ is too long!");
		}
		if (school.length() > 95){
			this.addActionError("School is too long!");
		}
		if (email.length() > 95){
			this.addActionError("Email is too long!");
		}
		if (blog.length() > 995){
			this.addActionError("Blog is too long!");
		}
		if (!this.getActionErrors().isEmpty()){
			return INPUT;
		}
		user.setNickname(nickname);
		user.setQq(qq);
		user.setSchool(school);
		user.setEmail(email);
		user.setBlog(blog);
		user.setShare(share);
		userService.modify(user);
		session.put("visitor", user);
		return SUCCESS;
	}

	
	public String profile() {
		user = (User) userService.query(User.class, uid);
		return SUCCESS;		
	}

}
