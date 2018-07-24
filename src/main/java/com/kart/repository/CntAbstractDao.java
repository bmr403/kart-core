package com.kart.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kart.exception.DataAccessLayerException;

@SuppressWarnings(value = { "all" })
public abstract class CntAbstractDao {

	@Autowired
	protected SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//Save data
	protected Object saveOrUpdate(Object obj) {
		Session session = sessionFactory.openSession();
		try {
			logger.info("before save");
			session.saveOrUpdate(obj);
			logger.info("after save object");
			session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
			handleException(e);
			logger.info("message");
		} finally {
			session.close();
		}
		return obj;
	}
	protected void delete(Object obj) {
		Session session = sessionFactory.openSession();
		try {
			logger.info("before delete");
			session.delete(obj);
			logger.info("after delete");
			session.flush();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
	}
	protected Object getUniqueObject(Class clazz,
			   DetachedCriteria objectCriteria) {
			  Object obj = null;
			  Session session = sessionFactory.openSession();
			  try {
			   Criteria crit = objectCriteria.getExecutableCriteria(session);
			   List list = crit.list();
			   logger.info("List Size : "+list.size());
			   if (list.size() != 0) {
			    obj = list.get(0);
			   }
			  } catch (HibernateException e) {
			   handleException(e);
			  } finally {
			   session.close();
			  }
			  return obj;
			 }


	protected Object find(Class clazz, Integer id) {
		Object obj = null;
		Session session = sessionFactory.openSession();
		try {
			obj = session.get(clazz, id);
		} catch (HibernateException e) {
			handleException(e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return obj;
	}
    
	//Get All List
	protected List getAllObjectList(Class clazz, DetachedCriteria objectCriteria) {
		List list = null;
		Session session = sessionFactory.openSession();
		try {
			//logger.debug("before of detachedcriteria");
			
			Criteria crit = objectCriteria.getExecutableCriteria(session);
			list = crit.list();
			logger.warn("list of obj :: " + list.size());
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}
	
    protected List getObjectListByRange(Class clazz,DetachedCriteria objectCriteria,int start,int limit)
    {
    	 List list = null;
    	 Session session = sessionFactory.openSession();
         try {
            
        	 Criteria crit = objectCriteria.getExecutableCriteria(session);
             crit.setFirstResult(start);
             crit.setMaxResults(limit);
             list =  crit.list();
            
         } catch (HibernateException e) {
             handleException(e);
         } finally {
         	session.close();
         }
         return list;    	
    	
    }
    
	protected int getObjectListCount(Class clazz, DetachedCriteria objectCriteria) {
		Integer resultTotal = 0;
		List rowlist = null;
		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			crit.setProjection(Projections.rowCount());
			rowlist = crit.list();
			if (!rowlist.isEmpty()) {
				Long resultTotalValue = (Long) rowlist.get(0);
				resultTotal = new Integer(resultTotalValue.intValue());
				logger.info("Total records: " + resultTotal);
			}

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return resultTotal.intValue();

	}
    
	protected void saveBatch(List objList) throws Exception {

		logger.info(" ------------------------------");
		logger.info("Save Batch Size: " + objList.size());

		Session session = sessionFactory.openSession();

		try {

			session.beginTransaction();
			int i = 0;
			for (Object obj : objList) {
				i++;
				session.saveOrUpdate(obj);
				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
			session.flush();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				session.close();
				session = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
	

	
		protected void handleException(HibernateException e)
			throws DataAccessLayerException {

		throw new DataAccessLayerException(e);
	}

}
