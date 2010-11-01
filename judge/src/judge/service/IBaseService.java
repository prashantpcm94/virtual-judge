package judge.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
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
	public void addOrModify(Collection entity);

	
	public Object query(Class entityClass, Serializable id);
	

	public List query(String queryString);
	

	public long count(String hql);
	

	public List list(String queryString, int FirstResult, int MaxResult);
	
	public int toggleAccess(int id);
	
	/**
	 * 根据提交ID查询结果
	 * @param id
	 * @return 0:ID 1:结果 2:内存 3:时间
	 */
	public Object[] getResult(int id);

	public List query(String queryString, Map parMap) ;

	public List list(String hql, Map parMap, int FirstResult, int MaxResult);

	public long count(String hql, Map parMap);
}
