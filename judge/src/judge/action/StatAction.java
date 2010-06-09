/**
 * 用于统计网站数据
 */

package judge.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;

import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.LineChart;
import jofc2.model.elements.PieChart;
import jofc2.model.elements.BarChart.Bar;
import judge.bean.User;
import judge.bean.Vlog;
import judge.service.StatService;

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
    private String username;
    
    public String findBrowser(String ref){
    	if (ref == null)
    		return "Unknown";
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
    	if (ref == null)
    		return "Unknown";
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
		return SUCCESS;
	}
    
    public String listHistoryUsers() {
		Map session = ActionContext.getContext().getSession();
		session.put("pageIndex", 0);
		dataList = statService.list("select vlog, user.username from Vlog vlog, User user where user.id = vlog.loginer order by vlog.createTime desc", 0, 25);
		return SUCCESS;
	}
    
    public String prevHistoryUsers() {
		Map session = ActionContext.getContext().getSession();
		int pageIndex = (Integer)session.get("pageIndex") - 1;
		if (pageIndex < 0){
			pageIndex = 0;
		}
		dataList = statService.list("select vlog, user.username from Vlog vlog, User user where user.id = vlog.loginer order by vlog.createTime desc", pageIndex*25, 25);
		session.put("pageIndex", pageIndex);
		return SUCCESS;
	}
    
    public String nextHistoryUsers() {
		Map session = ActionContext.getContext().getSession();
		int pageIndex = (Integer)session.get("pageIndex") + 1;
		dataList = statService.list("select vlog, user.username from Vlog vlog, User user where user.id = vlog.loginer order by vlog.createTime desc", pageIndex*25, 25);
		if (dataList.size() == 0){
			pageIndex--;
			dataList = statService.list("select vlog, user.username from Vlog vlog, User user where user.id = vlog.loginer order by vlog.createTime desc", pageIndex*25, 25);
		}
		session.put("pageIndex", pageIndex);
		return SUCCESS;
	}
    
    public String viewHU(){
    	vlog = (Vlog) statService.query(Vlog.class, id);
    	if (vlog.getLoginer() > 0){
	    	User user = (User) statService.query(User.class, vlog.getLoginer());
	    	username = user.getUsername();
    	}
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
		c2.setTooltip("#val#  /  #total# -- #percent#\n#label#\n"); // 鼠标移动上去后提示内容
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
		c2.setTooltip("#val#  /  #total# -- #percent#\n #label#\n"); // 鼠标移动上去后提示内容
		ofcChart = new Chart("OS Distribution" + (unique>0?"(By IP)":"(By Session)")); // 整个图的标题
		ofcChart.addElements(c2); // 把饼图加入到图表
		return SUCCESS;
    }
    
	public String toShowStayTime() {
		return SUCCESS;
	}
    
	public String showStayTime() {
		
		BarChart c2 = new BarChart(BarChart.Style.GLASS); // 柱状图
		if (unique == 0){
			dataList = statService.query("select vlog.duration from Vlog vlog");
		} else {
			dataList = statService.query("select avg(vlog.duration) from Vlog vlog group by vlog.ip");
		}
		long maxx = 1, maxy = 1;
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
			long s = maxx * i / 50000;
			bar.setTooltip("stay time: " + (s>60?(s/60+"min"):"") + s%60 + "sec\n" + cnt[i] + (unique>0?" IPs":" sessions"));	//鼠标移动上去后的提示  
//			System.out.println("cnt = " + i + " " + cnt[i]);
			c2.addBars(bar);
			x.addLabels(maxx * i / 50 / 60000 + "");//x轴的文字  
		}
		ofcChart = new Chart("Stay time distribution"); // 整个图的标题
		YAxis y = new YAxis();  //y 轴
		y.setMax(maxy/2.0+1.0); //y轴最大值  
		y.setSteps(maxy/10.0); //步进
		ofcChart.setYAxis(y);
		ofcChart.setXAxis(x);
		ofcChart.addElements(c2); // 把饼图加入到图表
//		System.out.println("\n" + ofcChart.toString());

		return SUCCESS;
	}
	
	public String toShowDayVisits() {
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public String showDayVisits(){
		List<LineChart.Dot> data1 = new ArrayList<LineChart.Dot>(), data2 = new ArrayList<LineChart.Dot>();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = new Date(new Date().getTime() - 30 * 86400 * 1000L);
		
		int[] cnt1 = new int[35];
		int[] cnt2 = new int[35];
		int maxy = 0;
		dataList = statService.query("select count(*), day(vlog.createTime), month(vlog.createTime), year(vlog.createTime) from Vlog vlog group by day(vlog.createTime), month(vlog.createTime), year(vlog.createTime)" +
				" where vlog.createTime > " + f.format(begin));
		for (int i = 0; i < dataList.size(); i++){
			Object[] o = (Object[]) dataList.get(i);
			int dif = (int) (((new Date((Integer)o[3]-1900, (Integer)o[2]-1, (Integer)o[1])).getTime() - begin.getTime()) / 86400000);
			System.out.println("dif = " + dif);
			cnt1[dif] = ((Long)o[0]).intValue();
			if (maxy < cnt1[dif]){
				maxy = cnt1[dif];
			}
		}

		dataList = statService.query("select vlog.id, day(vlog.createTime), month(vlog.createTime), year(vlog.createTime) from Vlog vlog group by vlog.ip, day(vlog.createTime), month(vlog.createTime), year(vlog.createTime)" +
				" where vlog.createTime > " + f.format(begin));
		for (int i = 0; i < dataList.size(); i++){
			Object[] o = (Object[]) dataList.get(i);
			int dif = (int) (((new Date((Integer)o[3]-1900, (Integer)o[2]-1, (Integer)o[1])).getTime() - begin.getTime()) / 86400000);
			System.out.println("dif = " + dif);
			cnt2[dif] ++;
		}
		
		for(int i = 0; i < 30; i++){
			// line 1 dot
			LineChart.Dot dot1 = new LineChart.Dot(cnt1[i]);
			dot1.setDotSize(5);            // 点大小
			dot1.setColour("#f00000");    // 设置点颜色
			data1.add(dot1);

			// line 2 dot
			LineChart.Dot dot2 = new LineChart.Dot(cnt2[i]);
			dot2.setDotSize(3);
			dot2.setHaloSize(1);        // 点外空白大小
			dot2.setColour("#3D5C56");
			data2.add(dot2);
		}

		Locale locale = new Locale("zh","CN");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);

		// line 1
		LineChart line1 = new LineChart();
		line1.setTooltip("#x_label#\n#val# visits");
		line1.setText("visits statistics");
//		line1.setDotStyle(new LineChart.Style(LineChart.Style.Type.DOT));
		line1.setWidth(1);            // 线宽
		line1.addDots(data1);        // 增加数据

		// line 2
		LineChart line2 = new LineChart();
		line2.setTooltip("#x_label#\n#val# IPs");
		line2.setText("IPs statistics");
//		line2.setDotStyle(new LineChart.Style(LineChart.Style.Type.DOT));
		line2.setColour("#3D5C56");
		line2.setWidth(2);
		line2.addDots(data2);

		YAxis y = new YAxis();
		y.setRange(0, maxy+1, (maxy+1)/4.0);        // 设置Y柚范围，参数依次为最小值、最大值、间隔

		XAxis x = new XAxis(); // X 轴
		for(int i = 0; i < 30; i++){
			begin = new Date(begin.getTime() + 86400000);
			int month = begin.getMonth();
			int date = begin.getDate();
			x.addLabels((month+1) + "." + date);
		}
		
		ofcChart = new Chart();
		ofcChart.setTitle(new Text(dateFormat.format(new Date())));    // 设置标题
		ofcChart.addElements(line1);                            // 增加线到图表
		ofcChart.addElements(line2);
		ofcChart.setYAxis(y);                                    // 设置Y柚
		ofcChart.setXAxis(x);                                    // 设置Y柚

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


}
