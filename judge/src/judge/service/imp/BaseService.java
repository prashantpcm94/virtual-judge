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

import judge.bean.Problem;
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
	
	public int toggleAccess(int id){
		Problem problem = (Problem) this.query(Problem.class, id);
		System.out.println("title = " + problem.getTitle());
		problem.setHidden(1 - problem.getHidden());
		this.modify(problem);
		return problem.getHidden();
	}

}
