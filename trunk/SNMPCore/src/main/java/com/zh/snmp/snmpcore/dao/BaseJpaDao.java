/*
 *  *  Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Sonrisa Informatikai Kft. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sonrisa.
 *
 * SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.zh.snmp.snmpcore.dao;

import com.zh.snmp.snmpcore.entities.BaseEntity;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.queries.QueryByExamplePolicy;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.queries.ReportQueryResult;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @param <T>
 * @author cserepj
 */
public abstract class BaseJpaDao<T extends BaseEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseJpaDao.class);
    protected Class<T> clazz;

    @PersistenceContext
    protected EntityManager entityManager;

    //    @Autowired
    public BaseJpaDao() {
        ParameterizedType genericSuperclass = (ParameterizedType)getClass().getGenericSuperclass();
        this.clazz = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    public T createNewInstance() {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, clazz + " does not have emty constructor.", e);
        }
    }
    
    public List<T> find(T filterEntity, String sort, int start, int count) {
        return find(filterEntity, getDefaultPolicy(), sort, start, count);
    }
    
    public T findExampleEntity(T filterEntity) {
        return findExampleEntity(filterEntity, null);
    }
    
    protected List<T> find(T filterEntity, QueryByExamplePolicy policy, String sort, int start, int count) {
        ReadAllQuery q = new ReadAllQuery(filterEntity, policy);
        q.setFirstResult(start);
        if (count >= 0) {
            q.setMaxRows(count);            
        }
        return (List<T>)getSession().executeQuery(q);
    }

    protected T findExampleEntity(T filterEntity, QueryByExamplePolicy policy) {
        ReadObjectQuery query = new ReadObjectQuery(filterEntity, policy);
        /*
        query.setExampleObject(filterEntity);
        if (policy != null) {
            query.setQueryByExamplePolicy(policy);
        }
         * 
         */
        return (T)getSession().executeQuery(query);
    }

    public int count(T filterEntity) {
        return count(filterEntity, getDefaultPolicy());
    }

    public boolean delete(T entity) {
        T indb = load(entity.getId());
        if (indb != null)  {
            entityManager.remove(indb);
            return true;
        } else {
            return false;
        }
    }

    protected int count(T filterEntity, QueryByExamplePolicy policy) {
        ReportQuery rq = new ReportQuery();
        rq.addCount();
        rq.setExampleObject(filterEntity);
        rq.setQueryByExamplePolicy(policy);
        List<ReportQueryResult> res = (List<ReportQueryResult>)getSession().executeQuery(rq);
        return (Integer)res.get(0).get("COUNT");
    }

    protected QueryByExamplePolicy getDefaultPolicy() {
        return null;
    }

    public T load(Long id) {
        return entityManager.find(clazz, id);
    }

    public T save(T object) {
        if (object.getId() != null) {
            return entityManager.merge(object);
        } else {
            entityManager.persist(object);
            return object;
        }
    }

    public final EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(JpaEntityManager e) {
        entityManager = e;
    }

    public Class<T> getEntityClass() {
        return clazz;
    }

    protected Session getSession() {
        return JpaHelper.getEntityManager(entityManager).getActiveSession();
        //return ((JpaEntityManager)entityManager.getDelegate()).getSession();
    }
    public void flush() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Flushing");
        }
        entityManager.flush();
    }
    /*
    protected T merge(T object) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Merging object: " + object);
        }
        return entityManager.merge(object);
    }

    protected void remove(T object) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Removing object: " + object);
        }
        entityManager.remove(object);
    }

    protected List<T> find(String query, Object... params) {
        Query q = entityManager.createQuery(query);
        for (int i=1; i<= params.length; i++) {
            q.setParameter(i, params[i]);
        }
        return q.getResultList();
    }

    protected void persist(T object) {
        if (LOGGER.isDebugEnabled()) {
        }
        entityManager.persist(object);
        LOGGER.debug("Persisting object: " + object.getId() + "; " + entityManager + "; " + object);
    }

    protected void flush() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Flushing");
        }
        entityManager.flush();
    }
*/

/*
    public void batchInsert(Collection<T> objects) {
        //logger.info("------------>>>batchInsert:");
        batchInsertNative(objects);
    }
    protected void refresh(T entity) {
        entityManager.refresh(entity);
    }

    private void batchInsertNative(Collection<T> objects) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("YO batchInsertNative entityManager:" + entityManager);
        }
        Session session = getSession();
        session.getLogin().useBatchWriting();
        session.getLogin().bindAllParameters();
        session.getLogin().cacheAllStatements();
        session.getLogin().setMaxBatchWritingSize(objects.size());
        EntityTransaction trans = entityManager.getTransaction();
        trans.begin();
        UnitOfWork uow = session.acquireUnitOfWork();
        uow.registerAllObjects(objects);
        uow.commit();
        uow.release();
        trans.commit();
        entityManager.clear();
        entityManager.close();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("YO batchInsertNative commited");
        }
    }
*/
}
