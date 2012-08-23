package de.hliebau.tracktivity.persistence;

import de.hliebau.tracktivity.domain.AbstractEntity;

public interface CommonDao<T extends AbstractEntity> {

	public void delete(T t);

	public T findById(long id);

	public long getCount();

	public void save(T t);

	public T update(T t);

}
