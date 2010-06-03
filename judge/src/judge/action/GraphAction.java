/**
 * ofc
 */

package judge.action;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import sun.awt.color.CMM.CSAccessor;

import jofc2.model.Chart;
import jofc2.model.elements.PieChart;
import judge.bean.User;
import judge.service.IBaseService;
import judge.service.IUserService;
import judge.tool.MD5;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class GraphAction extends ActionSupport {

	private static final long serialVersionUID = -4110838947220309361L;

    private Chart ofcChart;

    public Chart getOfcChart() {
        return ofcChart;
    }
	
	public String draw() {
		PieChart c2 = new PieChart(); // 饼图

		for (int i = 0; i < 10; i++) {
			c2.addSlice(i * i, i + "");
		}

		c2.setStartAngle(-90); // 开始的角度
		c2.setColours(new String[] { "0x336699", "0x88AACC", "0x999933",
				"0x666699", "0xCC9933", "0x006666", "0x3399FF", "0x993300",
				"0xAAAA77", "0x666666", "0xFFCC66", "0x6699CC", "0x663366",
				"0x9999CC", "0xAAAAAA", "0x669999", "0xBBBB55", "0xCC6600",
				"0x9999FF", "0x0066CC", "0x99CCCC", "0x999999", "0xFFCC00",
				"0x009999", "0x99CC33", "0xFF9900", "0x999966", "0x66CCCC",
				"0x339966", "0xCCCC33" });// 饼图每块的颜色
		c2.setTooltip("#val#  /  #total#<br>占百分之 #percent#\n 角度 = #radius#"); // 鼠标移动上去后提示内容
		ofcChart = new Chart("企业性质排序" + 2010 + "年"); // 整个图的标题
		ofcChart.addElements(c2); // 把饼图加入到图表
		return SUCCESS;
	}

}
