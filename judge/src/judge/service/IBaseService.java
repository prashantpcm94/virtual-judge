package judge.service;

import java.io.Serializable;
import java.util.List;

import org.jfree.chart.JFreeChart;

public interface IBaseService {
	/**
	 * 添加实体
	 * @param entity 实体的类
	 */
	public Serializable add(Object entity);
	
	/**
	 * 
	 * @param entity
	 */
	public void delete(Object entity);
	
	/**
	 * 
	 * @param entityClass
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public void delete(Class entityClass, Serializable id);
	
	/**
	 * 
	 * @param entity
	 */
	public void modify(Object entity);
	

	@SuppressWarnings("unchecked")
	public Object query(Class entityClass, Serializable id);
	

	@SuppressWarnings("unchecked")
	public List query(String queryString);
	

	public long count(String hql);
	

	@SuppressWarnings("unchecked")
	public List list(String queryString, int FirstResult, int MaxResult);
	

	public JFreeChart getStatisticChart(String title, String hql);
}
