package com.univocity.api.common.remote;

import com.univocity.api.common.*;

import java.util.*;

/**
 * Created by anthony on 21/07/16.
 */
public abstract class RemoteResourceEntityList<T extends RemoteResourceEntity> {
	protected final Map<String,T> entities = new TreeMap<String,T>();
	protected final Map <String, String> originalEntityNames = new TreeMap<String, String>();
	protected RemoteResourcePaginator paginator;
	protected RemoteResourceLinkFollower linkFollower;

	/**
	 * Creates a new, empty HtmlEntityList
	 */
	public RemoteResourceEntityList() {
	}


	/**
	 * Returns the {@link HtmlEntity} associated with the given entityName. If there is not a {@link HtmlEntity} with that
	 * name, it creates it and returns it.
	 *
	 * @param entityName the name of the {@link HtmlEntity} that will be returned.
	 * @return the {@link HtmlEntity} with the given entityName
	 */
	public final T configureEntity(String entityName) {
		Args.notBlank(entityName, "Entity name");
		String normalizedEntityName = entityName.trim().toLowerCase();
		if (entities.get(normalizedEntityName) == null) {
			entities.put(normalizedEntityName, newEntity(entityName));
			originalEntityNames.put(entityName, normalizedEntityName);
		}
		return entities.get(normalizedEntityName);
	}

	protected abstract T newEntity(String entityName);

	public RemoteResourcePaginator configurePaginator() {
		if (paginator == null) {
			paginator = newPaginator();
		}
		return paginator;
	}


	protected abstract RemoteResourcePaginator newPaginator();

	public RemoteResourcePaginator getPaginator() {
		return paginator;
	}

	/**
	 * Returns the entity names stored in the HtmlEntityList as a set of type String
	 *
	 * @return entity names stored as a set.
	 */
	public final Set<String> getEntityNames() {
		return new TreeSet<String>(originalEntityNames.keySet());
	}

	/**
	 * Returns all the entities stored in the entityList as a unmodifiable Collection
	 *
	 * @return a Collection of {@link HtmlEntity}s.
	 */
	public final Collection<T> getEntities() {
		return Collections.unmodifiableCollection(entities.values());
	}

	public final T getEntityByName(String entityName) {
		return entities.get(entityName);
	}

	public RemoteResourceLinkFollower configureLinkFollower() {
		if (linkFollower == null) {
			linkFollower = newLinkFollower();
		}
		return linkFollower;
	}

	abstract protected RemoteResourceLinkFollower newLinkFollower();

	public RemoteResourceLinkFollower getLinkFollower() {
		return  linkFollower;
	}

}

