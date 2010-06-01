// ========================================================================
// 文件名：BaseService.java
//
// 文件说明：
//     本文件主要实现各模块service层的公共功能，包括增、删、改、计算
//
// 历史记录：
// [日期]------[姓名]--[描述]
// 2009-12-02  雷达       创建文件。
// 2010-03-12  刘雅琴  修改文件，添加获得统计图方法。
// ========================================================================
package judge.service.imp;

import java.awt.Font;
import java.io.Serializable;
import java.util.List;

import judge.dao.IBaseDao;
import judge.service.IBaseService;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;


public class BaseService implements IBaseService {

	static private IBaseDao baseDao;
	private JFreeChart chart;

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		BaseService.baseDao = baseDao;
	}

	public Serializable add(Object entity) {
		return BaseService.baseDao.add(entity);
	}

	public void delete(Object entity) {
		BaseService.baseDao.delete(entity);
	}

	@SuppressWarnings("unchecked")
	public void delete(Class entityClass, Serializable id) {
		BaseService.baseDao.delete(entityClass, id);
	}

	public void modify(Object entity) {
		BaseService.baseDao.modify(entity);
	}

	@SuppressWarnings("unchecked")
	public Object query(Class entityClass, Serializable id) {
		return BaseService.baseDao.query(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List query(String queryString) {
		return BaseService.baseDao.query(queryString);
	}
		
	public long count(String hql) {
		hql = hql.substring(hql.indexOf("from"));
		hql = "select count(*) " + hql;
		long re;
		//根据hql中是否有group by来选择计算方法
		if (hql.contains("group by")){
			re = this.query(hql).size();
		}else{
			re = ((Number)this.query(hql).get(0)).longValue();
		}
		return re;
	}
	
	@SuppressWarnings("unchecked")
	public List list(String queryString, int FirstResult, int MaxResult) {
		return BaseService.baseDao.query(queryString, FirstResult, MaxResult);
	}
	
	// ==============================================================
	// 方法名: getStatisticChart
	// 方法描述: 本方法用来统计数据，将统计结果存放在数据集中,并根据数据
	// 集生成统计图
	// 返回值:
	// chart:统计图
	// ==============================================================
	@SuppressWarnings("unchecked")
	public JFreeChart getStatisticChart(String title, String hql) {
		try {
			DefaultPieDataset dataset = new DefaultPieDataset();
			List statisticList = this.query(hql);
			int n = statisticList.size();
			for (int i = 0; i < n; i++) {
				Object[] o = (Object[]) statisticList.get(i);
				boolean b = o[1] == null;
				if (!b) {
					dataset.setValue(o[0].toString(), (Number) o[1]);
				}
			}
			chart = ChartFactory.createPieChart(title, dataset, true, false,
					false);
			chart.setTitle(new TextTitle(title, new Font("黑体", Font.BOLD, 15)));
			LegendTitle legend = chart.getLegend(0);
			legend.setItemFont(new Font("黑体", Font.BOLD, 10));
			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setLabelFont(new Font("黑体", Font.BOLD, 10));
			plot.setBackgroundAlpha(0.1f);
			plot.setForegroundAlpha(0.8f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chart;
	}
}
