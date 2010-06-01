package judge.service.imp;

import java.util.List;

import judge.bean.User;
import judge.service.IUserService;

public class UserService extends BaseService implements IUserService {
	
	// ==============================================================
	// 函数名：checkUsername
	// 函数描述：检测用户名是否存在
	// 返回值：true表示存在，false表示不存在。
	// ==============================================================
	@SuppressWarnings("unchecked")
	public boolean checkUsername(String username) {
		List re = this.query("select user.username from User user where user.username = '" + username + "'");
		return re.size() > 0;
	}


	// ==============================================================
	// 函数名：checkPassword
	// 函数描述：检测用户名和密码是否匹配
	// 返回值：true表示匹配，false表示不匹配。
	// ==============================================================
	@SuppressWarnings("unchecked")
	public boolean checkPassword(String username,String password){
		List re = this.query("select user from User user where user.username = '" + username + "' and user.password = '" + password + "'");
		return re.size() > 0;
	}
	

	// ==============================================================
	// 函数名：getByUsername
	// 函数描述：根据用户名查找用户
	// 返回值：返回用户对象
	// ==============================================================
	@SuppressWarnings("unchecked")
	public User getByUsername(String username) {
		List list = this.query("select user from User user where user.username = '" + username + "'");
		return  list.size() > 0 ? (User) list.get(0) : null;
	}
}