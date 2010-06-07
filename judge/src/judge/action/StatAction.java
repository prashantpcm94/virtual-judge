/**
 * ofc
 */

package judge.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import sun.awt.color.CMM.CSAccessor;

import jofc2.model.Chart;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.PieChart;
import jofc2.model.elements.BarChart.Bar;
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
    private int unique, id;
    private Vlog vlog;
    
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
    	} else if (ref.contains("OPERA")){
    		return "Opera";
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
    
    public String viewHU(){
    	vlog = (Vlog) statService.query(Vlog.class, id);
    	return SUCCESS;
    }
    
    public String toListBrowser(){
    	return SUCCESS;
    }
    
    public String listBrowser(){
		PieChart c2 = new PieChart(); // 饼图
		dataList = statService.query("select vlog.ip, vlog.userAgent from Vlog vlog order by vlog.createTime desc");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<String> set = new HashSet<String>();
		
		for (int i = 0; i < dataList.size(); i++){
			if (unique > 0 && set.contains((String) ((Object [])dataList.get(i))[0]))
				continue;
			String st = (String) ((Object [])dataList.get(i))[1];
			String br = findBrowser(st);
			if (map.containsKey(br)){
				map.put(br, map.get(br) + 1);
			} else {
				map.put(br, 1);
			}
			set.add((String) ((Object [])dataList.get(i))[0]);
		}
		for (String it : map.keySet()){
			c2.addSlice(map.get(it), it);
		}
		
		c2.setStartAngle(90); // 开始的角度
		c2.setColours(new String[] { "0x336699", "0x88AACC", "0x999933",
				"0x666699", "0xCC9933", "0x006666", "0x3399FF", "0x993300",
				"0xAAAA77", "0x666666", "0xFFCC66", "0x6699CC", "0x663366",
				"0x9999CC", "0xAAAAAA", "0x669999", "0xBBBB55", "0xCC6600",
				"0x9999FF", "0x0066CC", "0x99CCCC", "0x999999", "0xFFCC00",
				"0x009999", "0x99CC33", "0xFF9900", "0x999966", "0x66CCCC",
				"0x339966", "0xCCCC33" });// 饼图每块的颜色
		c2.setTooltip("#val#  /  #total# -- #percent#<br /> #label#\n"); // 鼠标移动上去后提示内容
		ofcChart = new Chart("Browser Distribution" + (unique>0?"(By IP)":"(By Session)")); // 整个图的标题
		ofcChart.addElements(c2); // 把饼图加入到图表
		return SUCCESS;
    }

    
    public String toListOS(){
    	return SUCCESS;
    }

    public String listOS(){
		PieChart c2 = new PieChart(); // 饼图
		dataList = statService.query("select vlog.ip, vlog.userAgent from Vlog vlog order by vlog.createTime desc");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<String> set = new HashSet<String>();
		
		for (int i = 0; i < dataList.size(); i++){
			if (unique > 0 && set.contains((String) ((Object [])dataList.get(i))[0]))
				continue;
			String st = (String) ((Object [])dataList.get(i))[1];
			String br = findOS(st);
			if (map.containsKey(br)){
				map.put(br, map.get(br) + 1);
			} else {
				map.put(br, 1);
			}
			set.add((String) ((Object [])dataList.get(i))[0]);
		}
		for (String it : map.keySet()){
			c2.addSlice(map.get(it), it);
		}
		
		c2.setStartAngle(90); // 开始的角度
		c2.setColours(new String[] { "0x336699", "0x88AACC", "0x999933",
				"0x666699", "0xCC9933", "0x006666", "0x3399FF", "0x993300",
				"0xAAAA77", "0x666666", "0xFFCC66", "0x6699CC", "0x663366",
				"0x9999CC", "0xAAAAAA", "0x669999", "0xBBBB55", "0xCC6600",
				"0x9999FF", "0x0066CC", "0x99CCCC", "0x999999", "0xFFCC00",
				"0x009999", "0x99CC33", "0xFF9900", "0x999966", "0x66CCCC",
				"0x339966", "0xCCCC33" });// 饼图每块的颜色
		c2.setTooltip("#val#  /  #total# -- #percent#<br /> #label#\n"); // 鼠标移动上去后提示内容
		ofcChart = new Chart("OS Distribution" + (unique>0?"(By IP)":"(By Session)")); // 整个图的标题
		ofcChart.addElements(c2); // 把饼图加入到图表
		return SUCCESS;
    }
    
	public String toShowStayTime() {
		return SUCCESS;
	}
    
	public String showStayTime() {
		try {
			
		
		BarChart c2 = new BarChart(BarChart.Style.GLASS); // 柱状图
		if (unique == 0){
			dataList = statService.query("select vlog.duration from Vlog vlog");
		} else {
			dataList = statService.query("select avg(vlog.duration) from Vlog vlog group by vlog.ip");
		}
		long maxx = 0, maxy = 0;
		for (int i = 0; i < dataList.size(); i++){
			long tmp = unique > 0 ? ((Double)dataList.get(i)).longValue() : (Long)dataList.get(i);
			if (maxx < tmp){
				maxx = tmp;
			}
		}

		int cnt[] = new int[60];
		for (int i = 0; i < dataList.size(); i++){
			long l = unique > 0 ? ((Double)dataList.get(i)).longValue() : (Long)dataList.get(i);
			int tmp = (int) (50 * l / maxx);
			cnt[tmp]++;
			if (maxy < cnt[tmp]){
				maxy = cnt[tmp];
			}
		}

		XAxis x = new XAxis();//X 轴  
		for(int i = 0; i < 51; i++){  
			Bar bar = new Bar(cnt[i], 0);			//条标题，显示在x轴上  
			bar.setColour("0x336699");				// 颜色
			bar.setTooltip(cnt[i] + " sessions");	//鼠标移动上去后的提示  
			System.out.println("cnt = " + i + " " + cnt[i]);
			c2.addBars(bar);   
			x.addLabels(maxx * i / 50 / 60000 + "");//x轴的文字  
		}
		ofcChart = new Chart("Stay time distribution"); // 整个图的标题
		YAxis y = new YAxis();  //y 轴  
		y.setMax(maxy+1.0); //y轴最大值  
		y.setSteps(maxy/10.0); //步进 
		ofcChart.setYAxis(y);
		ofcChart.setXAxis(x);
		ofcChart.addElements(c2); // 把饼图加入到图表
		System.out.println("\n" + ofcChart.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public int getUnique() {
		return unique;
	}

	public void setUnique(int unique) {
		this.unique = unique;
	}

	public Vlog getVlog() {
		return vlog;
	}

	public void setVlog(Vlog vlog) {
		this.vlog = vlog;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
