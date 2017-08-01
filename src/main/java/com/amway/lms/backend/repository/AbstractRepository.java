package com.amway.lms.backend.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRepository<PK extends Serializable, T> {

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractRepository() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }

    public void persist(T entity) {
        getSession().persist(entity);
    }

    public void update(T entity) {
        getSession().update(entity);
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    protected Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }

    public Query createNamedQuery(String queryName, Integer firstResult, Integer maxResults, Object... parameters) {
     org.hibernate.Query query = getSession().getNamedQuery(queryName);
     if (parameters != null) {
      for (int i = 0; i < parameters.length; i++) {
       query.setParameter(i, parameters[i]);
      }
     }

        query.setFirstResult(firstResult == null || firstResult < 0 ? 0
                : firstResult);
        if (maxResults != null && maxResults > 0)
            query.setMaxResults(maxResults);

        return query;
    }
}
