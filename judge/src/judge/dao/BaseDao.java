package judge.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BaseDao extends HibernateDaoSupport implements IBaseDao {
	
	public Serializable add(Object entity) {
		return this.getHibernateTemplate().save(entity);
	}

	public void delete(Object entity) {
		this.getHibernateTemplate().merge(entity);
		this.getHibernateTemplate().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public void delete(Class entityClass, Serializable id) {
		Object entity = (Object) this.getHibernateTemplate().get(entityClass, id);
		this.getHibernateTemplate().merge(entity);
		this.getHibernateTemplate().delete(entity);
	}

	public void modify(Object entity) {
		this.getHibernateTemplate().update(entity);
	}

	@SuppressWarnings("unchecked")
	public void modify(Class entityClass, Serializable id) {
		Object entity = (Object) this.getHibernateTemplate().get(entityClass, id);
		this.getHibernateTemplate().update(entity);
	}

	@SuppressWarnings("unchecked")
	public List query(String queryString) {
		return this.getHibernateTemplate().find(queryString);
	}

	@SuppressWarnings("unchecked")
	public List query(String queryString, int FirstResult, int MaxResult) {

		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		Transaction t = session.beginTransaction();
		t.begin();
		Query queryObject = session.createQuery(queryString);
		queryObject.setFirstResult(FirstResult);
		queryObject.setMaxResults(MaxResult);
		List list = queryObject.list();
		t.commit();
		session.clear();
		session.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public Object query(Class entityClass, Serializable id) {
		Object entity = (Object) this.getHibernateTemplate().get(entityClass, id);
		if (entity != null)
			this.getHibernateTemplate().refresh(entity);
		return entity;
	}
}
