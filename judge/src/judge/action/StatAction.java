/**
 * ofc
 */

package judge.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import sun.awt.color.CMM.CSAccessor;

import jofc2.model.Chart;
import jofc2.model.elements.PieChart;
import judge.bean.User;
import judge.bean.Vlog;
import judge.service.IBaseService;
import judge.service.IUserService;
import judge.service.StatService;
import judge.tool.MD5;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class StatAction extends ActionSupport {

	private static final long serialVersionUID = -4110838947220309361L;

    private Chart ofcChart;
    private StatService statService;
    private List dataList;
    
    public String findBrowser(String ref){
    	ref = ref.toUpperCase();
    	if (ref.contains("MSIE")){
    		return "IE";
    	} else if (ref.contains("FIREFOX")){
    		return "Firefox";
    	} else if (ref.contains("CHROME")){
    		return "Chrome";
    	} else if (ref.contains("NETSCAPE")){
    		return "NetScape";
    	} else {
			return "Unknown";
		}
    }

    public String findOS(String ref){
    	ref = ref.toUpperCase();
    	if (ref.contains("WINDOWS")){
    		return "Windows";
    	} else if (ref.contains("LINUX")){
    		return "Linux";
    	} else if (ref.contains("MAC")){
    		return "Mac";
    	} else if (ref.contains("UNIX")){
    		return "Unix";
    	} else {
			return "Unknown";
		}
    }

    public String listOnlineUsers() {
    	
    	try {
			
		List userInfo;
		Vlog vlog;
		User user;
		dataList = new ArrayList<Object>();
		for (HttpSession hs : StatService.sessions){
			userInfo = new ArrayList<Object>();
			vlog = statService.getBySessionId(hs.getId());
			user = (User)hs.getAttribute("visitor");
			userInfo.add(vlog.getIp());
			userInfo.add("");
			userInfo.add(findBrowser(vlog.getUserAgent()));
			userInfo.add(findOS(vlog.getUserAgent()));
			userInfo.add(user != null ? user.getUsername() : "");
			userInfo.add(user != null ? user.getId() : "");
			userInfo.add(vlog.getCreateTime());
			dataList.add(userInfo);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
    
    public String listHistoryUsers() {
		Map session = ActionContext.getContext().getSession();
		session.put("pageIndex", 0);
		dataList = statService.list("select vlog from Vlog vlog order by vlog.createTime desc", 0, 25);
		return SUCCESS;
	}
    
    public String prevHistoryUsers() {
		Map session = ActionContext.getContext().getSession();
		int pageIndex = (Integer)session.get("pageIndex") - 1;
		if (pageIndex < 0){
			pageIndex = 0;
		}
		dataList = statService.list("select vlog from Vlog vlog order by vlog.createTime desc", pageIndex*25, 25);
		session.put("pageIndex", pageIndex);
		return SUCCESS;
	}
    
    public String nextHistoryUsers() {
		Map session = ActionContext.getContext().getSession();
		int pageIndex = (Integer)session.get("pageIndex") + 1;
		dataList = statService.list("select vlog from Vlog vlog order by vlog.createTime desc", pageIndex*25, 25);
		if (dataList.size() == 0){
			pageIndex--;
			dataList = statService.list("select vlog from Vlog vlog order by vlog.createTime desc", pageIndex*25, 25);
		}
		session.put("pageIndex", pageIndex);
		return SUCCESS;
	}
	
	public String draw() {
		PieChart c2 = new PieChart(); // 饼图
		
		for (int i = 0; i < 10; i++) {
			c2.addSlice(i * i, i + "");
		}

		c2.setStartAngle(90); // 开始的角度
		c2.setColours(new String[] { "0x336699", "0x88AACC", "0x999933",
				"0x666699", "0xCC9933", "0x006666", "0x3399FF", "0x993300",
				"0xAAAA77", "0x666666", "0xFFCC66", "0x6699CC", "0x663366",
				"0x9999CC", "0xAAAAAA", "0x669999", "0xBBBB55", "0xCC6600",
				"0x9999FF", "0x0066CC", "0x99CCCC", "0x999999", "0xFFCC00",
				"0x009999", "0x99CC33", "0xFF9900", "0x999966", "0x66CCCC",
				"0x339966", "0xCCCC33" });// 饼图每块的颜色
		c2.setTooltip("#val#  /  #total#<br>占百分之 #percent# #label#\n 角度 = #radius#"); // 鼠标移动上去后提示内容
		ofcChart = new Chart("企业性质排序" + 2010 + "年"); // 整个图的标题
		ofcChart.addElements(c2); // 把饼图加入到图表
		return SUCCESS;
	}
	
    public Chart getOfcChart() {
        return ofcChart;
    }


	public StatService getStatService() {
		return statService;
	}


	public void setStatService(StatService statService) {
		this.statService = statService;
	}


	public List getDataList() {
		return dataList;
	}


	public void setDataList(List dataList) {
		this.dataList = dataList;
	}


	public void setOfcChart(Chart ofcChart) {
		this.ofcChart = ofcChart;
	}


}
