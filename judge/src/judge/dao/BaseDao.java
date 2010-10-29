package judge.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

@SuppressWarnings("unchecked")
public class BaseDao extends HibernateDaoSupport implements IBaseDao {
	
	public Serializable add(Object entity) {
		return this.getHibernateTemplate().save(entity);
	}

	public void delete(Object entity) {
		this.getHibernateTemplate().merge(entity);
		this.getHibernateTemplate().delete(entity);
	}

	public void delete(Class entityClass, Serializable id) {
		Object entity = (Object) this.getHibernateTemplate().get(entityClass, id);
		this.getHibernateTemplate().merge(entity);
		this.getHibernateTemplate().delete(entity);
	}

	public void modify(Object entity) {
		this.getHibernateTemplate().update(entity);
	}

	public void modify(Class entityClass, Serializable id) {
		Object entity = (Object) this.getHibernateTemplate().get(entityClass, id);
		this.getHibernateTemplate().update(entity);
	}

	public void addOrModify(Object entity) {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}
	
	public void addOrModify(Collection entity) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		tx.begin();
		try {
			for (Object o : entity){
				session.saveOrUpdate(o);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	public List query(String queryString) {
		return this.getHibernateTemplate().find(queryString);
	}

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

	public Object query(Class entityClass, Serializable id) {
		Object entity = (Object) this.getHibernateTemplate().get(entityClass, id);
		if (entity != null)
			this.getHibernateTemplate().refresh(entity);
		return entity;
	}
}
