package com.map.utils;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface GenericDAO<T, PK extends Serializable> {

	public abstract T findByPk(PK pk) throws Exception;


	public abstract T findInstanceByNamedQuery(String queryName)
			throws Exception;

	public abstract T merge(T object) throws Exception;

	public abstract T persist(T object) throws Exception;

	public abstract void remove(T object) throws Exception;

	public abstract List<T> findByParameters(String query, Object... args)
			throws Exception;

	public abstract List<T> findByParametersMax(String query, Object... args)
			throws Exception;

	public abstract T findObjectByParameters(String query, Object... args)
			throws Exception;

	public abstract int countByNamedQuery(String queryName, Object... args)
			throws Exception;

	public abstract int countByQuery(String query, Object... args)
			throws Exception;

	public abstract List<T> find(String query) throws Exception;

	public abstract List<T> findMax(String query) throws Exception;

	public abstract List<T> findAll() throws Exception;

	public abstract List<T> findArrangeAll(String order) throws Exception;

	public abstract List<T> findAllLimited() throws Exception;

	public abstract List<T> findArrangeAllLimited(String order)
			throws Exception;

	public abstract List<T> findByNamedQuery(String queryName) throws Exception;

	public abstract List<T> findByNamedQueryParameter(String queryName,
			Object... args) throws Exception;

	public abstract List<T> findByQueryParameter(String query, Object... args)
			throws Exception;

	public abstract void clear() throws Exception;

	public abstract void flush() throws Exception;

	public abstract Query getAndCreateNamedQueryParameters(String queryName,
			Object... args) throws Exception;

	public abstract void populateQueryParameter(Query q, Object... args);

	public abstract EntityManager getEntityManager();

	public abstract void setEntityManager(EntityManager entityManager);

	/**
	 * Obtiene el tipo de los argumentos al momento de crear
	 * 
	 * @author Gabriel Eguiguren
	 * @return
	 */
	public abstract Class<T> getEntityClass();

}