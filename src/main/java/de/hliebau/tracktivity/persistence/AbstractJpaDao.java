package de.hliebau.tracktivity.persistence;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.hliebau.tracktivity.domain.AbstractEntity;

public abstract class AbstractJpaDao<T extends AbstractEntity> implements CommonDao<T> {

	protected Class<T> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public AbstractJpaDao() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	public void delete(T t) {
		entityManager.remove(t);
	}

	public T findById(long id) {
		try {
			return entityManager.find(entityClass, id);
		} catch (NoResultException e) {
			return null;
		}
	}

	public long getCount() {
		TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(*) FROM " + entityClass.getName(), Long.class);
		return query.getSingleResult();
	}

	public void save(T t) {
		entityManager.persist(t);
	}

	public T update(T t) {
		return entityManager.merge(t);
	}

}
