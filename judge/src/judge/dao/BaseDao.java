package judge.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

@SuppressWarnings("unchecked")
public class BaseDao extends HibernateDaoSupport implements IBaseDao {

	public void addOrModify(Object entity) {
		Session session = super.getSession();
		Transaction tx = session.beginTransaction();
		tx.begin();
		try {
			sessionAddOrModify(session, entity);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
		super.releaseSession(session);
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

	public void execute(String statement) {
		Session session = super.getSession();
		Transaction tx = session.beginTransaction();
		tx.begin();
		try {
			session.createQuery(statement).executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
		super.releaseSession(session);
	}

	public void execute(String statement, Map parMap) {
		Session session = super.getSession();
		Transaction tx = session.beginTransaction();
		tx.begin();
		try {
			Query query = session.createQuery(statement);
			if (parMap != null) {
				for (Iterator i = parMap.entrySet().iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					try {
						if (entry.getValue() instanceof List) {
							query.setParameterList((String) entry.getKey(), (List) entry.getValue());
						} else if (entry.getValue() instanceof String[]) {
							query.setParameterList((String) entry.getKey(), (String[]) entry.getValue());
						} else {
							query.setParameter((String) entry.getKey(), entry.getValue());
						}
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
			}
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
		super.releaseSession(session);
	}

	public Object query(Class entityClass, Serializable id) {
		Object entity = (Object) this.getHibernateTemplate().get(entityClass, id);
		if (entity != null)
			this.getHibernateTemplate().refresh(entity);
		return entity;
	}

	public List query(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public List query(String queryString, int FirstResult, int MaxResult) {
		Session session = super.getSession();
		Transaction t = session.beginTransaction();
		t.begin();
		List list = null;
		try {
			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(FirstResult);
			queryObject.setMaxResults(MaxResult);
			list = queryObject.list();
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
			t.rollback();
		}
		super.releaseSession(session);
		return list;
	}

	public List query(String hql, Map parMap) {
		Session session = super.getSession();
		Transaction t = session.beginTransaction();
		t.begin();
		List list = null;
		try {
			Query queryObject = session.createQuery(hql);
			if (parMap != null) {
				for (Iterator i = parMap.entrySet().iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					try {
						if (entry.getValue() instanceof List) {
							queryObject.setParameterList((String) entry.getKey(), (List) entry.getValue());
						} else if (entry.getValue() instanceof String[]) {
							queryObject.setParameterList((String) entry.getKey(), (String[]) entry.getValue());
						} else {
							queryObject.setParameter((String) entry.getKey(), entry.getValue());
						}
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
			}
			list = queryObject.list();
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
			t.rollback();
		}
		super.releaseSession(session);
		return list;
	}

	public List query(String hql, Map parMap, int FirstResult, int MaxResult) {
		Session session = super.getSession();
		Transaction t = session.beginTransaction();
		t.begin();
		List list = null;
		try {
			Query queryObject = session.createQuery(hql);
			if (parMap != null) {
				for (Iterator i = parMap.entrySet().iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					try {
						if (entry.getValue() instanceof List) {
							queryObject.setParameterList((String) entry.getKey(), (List) entry.getValue());
						} else if (entry.getValue() instanceof String[]) {
							queryObject.setParameterList((String) entry.getKey(), (String[]) entry.getValue());
						} else {
							queryObject.setParameter((String) entry.getKey(), entry.getValue());
						}
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
			}
			queryObject.setFirstResult(FirstResult);
			queryObject.setMaxResults(MaxResult);
			list = queryObject.list();
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
			t.rollback();
		}
		super.releaseSession(session);
		return list;
	}
	
	
	///////////////////////////////////////////////////

	private void sessionAddOrModify(Session session, Object data) {
		if (data instanceof Collection){
			Collection data1 = (Collection) data;
			for (Object object : data1) {
				sessionAddOrModify(session, object);
			}
		} else {
			session.saveOrUpdate(data);
		}
	}

	public Session createSession() {
		return super.getSession();
	}

	public void closeSession(Session session) {
		super.releaseSession(session);
	}

	
	

}
