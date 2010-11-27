package judge.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public interface IBaseDao {
	public Serializable add(Object entity);
	public void delete(Object entity);
	public void delete(Class entityClass, Serializable id);
	public void modify(Object entity);
	public void modify(Class entityClass, Serializable id);
	public List query(String hql);
	public List query(String queryString, int FirstResult, int MaxResult);
	public Object query(Class entityClass, Serializable id);
	public void addOrModify(Object entity);
	public void addOrModify(Collection entity);
	public List query(String hql, Map parMap);
	public List query(String hql, Map parMap, int FirstResult, int MaxResult);
	public void execute(String statement);
	public void execute(String statement, Map parMap);

}
