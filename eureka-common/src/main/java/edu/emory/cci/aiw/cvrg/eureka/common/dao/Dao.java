package edu.emory.cci.aiw.cvrg.eureka.common.dao;

import java.util.List;

/**
 *
 * @param <T>
 * @param <PK>
 * @author hrathod
 */
public interface Dao<T, PK> {

	public T create(T entity);

	public T retrieve(PK uniqueId);

	public T update(T entity);

	public T remove(T entity);

	public T refresh(T entity);

	public List<T> getAll();
}
