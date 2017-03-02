package com.map.utils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
 
public abstract class GenericDAOImpl<T, PK extends Serializable> {
 
	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	private Class<T> type;

	public GenericDAOImpl() {
		this.type = getEntityClass();
	}

	public T findByPk(PK pk) throws Exception {
		try {
			return this.entityManager.find(this.type, pk);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public Object findByPk(@SuppressWarnings("rawtypes") Class objectType,
			Object pk) throws Exception {
		try {
			return this.entityManager.find(pk.getClass(), pk);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public T findInstanceByNamedQuery(String queryName) throws Exception {
		try {
			final Query q = this.entityManager.createNamedQuery(queryName);
//			q.setMaxResults(100);
			@SuppressWarnings("unchecked")
			final T result = (T) q.getSingleResult();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}

	public T merge(T object) throws Exception {
		T result = null;
		try {
			result = this.entityManager.merge(object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}

	public T persist(T object) throws Exception {
		try {
			this.entityManager.persist(object);
			return object;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public void remove(T object) throws Exception {
		try {
			Object obj = object;
			if (!this.entityManager.contains(object)) {
				// si el objeto no esta en el EM, se lo carga
				obj = this.entityManager.merge(object);
			}

			this.entityManager.remove(obj);
		} catch (final Exception e) {
			throw new Exception(e);
		}

	}

	public List<T> findByParameters(String query, Object... args)
			throws Exception {
		try {
			final Query q = this.entityManager.createQuery(query);

			if (args != null) {
				int position = 1;
				for (Object obj : args) {
					q.setParameter(position++, obj);
				}
			}
			//q.setMaxResults(100);
			@SuppressWarnings("unchecked")
			final List<T> result = q.getResultList();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}

	public List<T> findByParametersMax(String query, Object... args)
			throws Exception {
		try {
			final Query q = this.entityManager.createQuery(query);

			if (args != null) {
				int position = 1;
				for (Object obj : args) {
					q.setParameter(position++, obj);
				}
			}
			@SuppressWarnings("unchecked")
			final List<T> result = q.getResultList();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}

	public T findObjectByParameters(String query, Object... args)
			throws Exception {
		try {
			final Query q = this.entityManager.createQuery(query);
			if (args != null) {

				int position = 1;
				for (Object obj : args) {
					q.setParameter(position++, obj);
				}
			}
			q.setMaxResults(1);
			@SuppressWarnings("unchecked")
			final T result = (T) q.getSingleResult();

			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}

	public int countByNamedQuery(String queryName, Object... args)
			throws Exception {
		Query q = this.getAndCreateNamedQueryParameters(queryName, args);
		return ((Long) q.getSingleResult()).intValue();
	}

	public int countByQuery(String query, Object... args) throws Exception {
		Query q = this.entityManager.createQuery(query);
		this.populateQueryParameter(q, args);
		return ((Long) q.getSingleResult()).intValue();
	}

	public List<T> find(String query) throws Exception {
		try {
			final Query q = this.entityManager.createQuery(query);
			//q.setMaxResults(200);
			@SuppressWarnings("unchecked")
			final List<T> result = q.getResultList();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}
	
	public List<T> findForGeo(String query) throws Exception {
		try {
			final Query q = this.entityManager.createQuery(query);
			q.setMaxResults(98);
			@SuppressWarnings("unchecked")
			final List<T> result = q.getResultList();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}

	public List<T> findMax(String query) throws Exception {
		try {
			final Query q = this.entityManager.createQuery(query);
			@SuppressWarnings("unchecked")
			final List<T> result = q.getResultList();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}

	public List<T> findAll() throws Exception {
		try {
			Query q = this.entityManager.createQuery("select o from " + this.type.getSimpleName() + " o");
			@SuppressWarnings("unchecked")
			List<T> results = q.getResultList();
			return results;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public List<T> findArrangeAll(String order) throws Exception {
		try {
			Query q = this.entityManager.createQuery(
					"select o from " + this.type.getSimpleName() + " o "
					+ "order by " + order);
			@SuppressWarnings("unchecked")
			List<T> results = q.getResultList();
			return results;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public List<T> findAllLimited() throws Exception {
		try {
			Query q = this.entityManager.createQuery("select o from " + this.type.getSimpleName() + " o");
			//q.setMaxResults(100);
			@SuppressWarnings("unchecked")
			List<T> results = q.getResultList();
			return results;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public List<T> findArrangeAllLimited(String order) throws Exception {
		try {
			Query q = this.entityManager.createQuery(
					"select o from " + this.type.getSimpleName() + " o "
					+ "order by " + order);
			//q.setMaxResults(100);
			@SuppressWarnings("unchecked")
			List<T> results = q.getResultList();
			return results;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public List<T> findByNamedQuery(String queryName) throws Exception {
		try {
			final Query q = this.entityManager.createNamedQuery(queryName);
			@SuppressWarnings("unchecked")
			final List<T> result = q.getResultList();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}

	public List<T> findByNamedQueryParameter(String queryName, Object... args)
			throws Exception {
		Query q = this.getAndCreateNamedQueryParameters(queryName, args);
		//q.setMaxResults(100);
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByQueryParameter(String query, Object... args)
			throws Exception {
		Query q = this.entityManager.createQuery(query);
		//q.setMaxResults(100);
		this.populateQueryParameter(q, args);
		return q.getResultList();
	}

	public void clear() throws Exception {
		try {
			this.entityManager.clear();

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	public void flush() throws Exception {
		try {
			this.entityManager.flush();
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	public Query getAndCreateNamedQueryParameters(String queryName,
			Object... args) throws Exception {
		try {
			final Query q = this.entityManager.createNamedQuery(queryName);
			//q.setMaxResults(100);
			this.populateQueryParameter(q, args);
			return q;
		} catch (final Exception e) {
			throw new Exception(e);
		}

	}

	public void populateQueryParameter(Query q, Object... args) {
		if (args != null) {
			int position = 1;
			for (Object obj : args) {
				q.setParameter(position++, obj);
			}
		}
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Obtiene el tipo de los argumentos al momento de crear
	 * 
	 * @author Gabriel Eguiguren
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		Type type = getClass().getGenericSuperclass();
		Class<T> entityClass = null;

		if (type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;
			if (paramType.getActualTypeArguments().length == 2) {
				entityClass = (Class<T>) paramType.getActualTypeArguments()[0];
			}
		}
		return entityClass;
	}
}