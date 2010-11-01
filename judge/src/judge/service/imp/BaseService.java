// ========================================================================
// 文件名：BaseService.java
//
// 文件说明：
//     本文件主要实现各模块service层的公共功能，包括增、删、改、计算
//
// ========================================================================
package judge.service.imp;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import judge.bean.Problem;
import judge.bean.Submission;
import judge.dao.IBaseDao;
import judge.service.IBaseService;


@SuppressWarnings("unchecked")
public class BaseService implements IBaseService {

	static private IBaseDao baseDao;

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

	public void delete(Class entityClass, Serializable id) {
		BaseService.baseDao.delete(entityClass, id);
	}

	public void modify(Object entity) {
		BaseService.baseDao.modify(entity);
	}

	public void addOrModify(Object entity) {
		BaseService.baseDao.addOrModify(entity);
	}

	public void addOrModify(Collection entity) {
		BaseService.baseDao.addOrModify(entity);
	}

	public Object query(Class entityClass, Serializable id) {
		return BaseService.baseDao.query(entityClass, id);
	}

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
	
	public List list(String queryString, int FirstResult, int MaxResult) {
		return BaseService.baseDao.query(queryString, FirstResult, MaxResult);
	}

	public List list(String hql, Map parMap, int FirstResult, int MaxResult) {
		return BaseService.baseDao.query(hql, parMap, FirstResult, MaxResult);
	}
	
	public List query(String queryString, Map parMap) {
		return BaseService.baseDao.query(queryString, parMap);
	}
	
	public long count(String hql, Map parMap) {
		if(hql.contains("union")){
			String hql1 = "select count(*) from (" + hql + ")";
			long re;
			re = ((Number)this.query(hql1, parMap).get(0)).longValue();
			return re;
		}else{
			String hql1 = hql.substring(hql.indexOf("from"));
			hql1 = "select count(*) " + hql1;
			long re;
			//根据hql中是否有group by和distinct来选择计算方法
			if (hql.contains("group by")||hql.contains("distinct")){
				re = this.query(hql, parMap).size();
			}else{
				re = ((Number)this.query(hql1, parMap).get(0)).longValue();
			}
			return re;
		}
	}

	public int toggleAccess(int id){
		Problem problem = (Problem) this.query(Problem.class, id);
		System.out.println("title = " + problem.getTitle());
		problem.setHidden(1 - problem.getHidden());
		this.modify(problem);
		return problem.getHidden();
	}
	
	/**
	 * 根据提交ID查询结果
	 * @param id
	 * @return 0:ID 1:结果 2:内存 3:时间
	 */
	public Object[] getResult(int id){
		Object[] ret = new Object[4];
		Submission s = (Submission) query(Submission.class, id);
		ret[0] = id;
		ret[1] = s.getStatus();
		ret[2] = s.getMemory();
		ret[3] = s.getTime();
		return ret;
	}

}
