package judge.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
	
	/**
	 * 添加/修改实体
	 * @param entity 实体的类
	 * @return 返回实体添加后的ID
	 */
	public void addOrModify(Object entity);

	/**
	 * 添加/修改实体集合
	 * @param entity 实体的类
	 */
	@SuppressWarnings("unchecked")
	public void addOrModify(Collection entity);

	
	@SuppressWarnings("unchecked")
	public Object query(Class entityClass, Serializable id);
	

	@SuppressWarnings("unchecked")
	public List query(String queryString);
	

	public long count(String hql);
	

	@SuppressWarnings("unchecked")
	public List list(String queryString, int FirstResult, int MaxResult);
	
	public int toggleAccess(int id);

}
