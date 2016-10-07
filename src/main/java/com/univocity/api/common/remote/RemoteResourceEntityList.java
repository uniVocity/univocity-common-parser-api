/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.api.common.remote;

import com.univocity.api.common.*;
import com.univocity.parsers.common.*;

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
	 * Returns the entity object associated with the given entityName. If there is no entity with that
	 * name, a new entity will be created and returned.
	 *
	 * @param entityName name of the entity that will be returned.
	 * @return an existing or new entity with the given name
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
	 * Returns the entity names stored in the HtmlEntityList as a set of type String. Returns empty Set if no
	 * entities configured.
	 *
	 * @return entity names stored as a set.
	 */
	public final Set<String> getEntityNames() {
		return new TreeSet<String>(originalEntityNames.keySet());
	}

	/**
	 * Returns all the entities stored in the entityList as a unmodifiable Collection
	 *
	 * @return a Collection of entities.
	 */
	public final Collection<T> getEntities() {
		return Collections.unmodifiableCollection(entities.values());
	}

	public final T getEntity(String entityName) {
		ArgumentUtils.notEmpty("Entity name", entityName);
		return entities.get(entityName.trim().toLowerCase());
	}

	public RemoteResourceLinkFollower configureLinkFollower() {
		if (linkFollower == null) {
			linkFollower = newLinkFollower();
		}
		return linkFollower;
	}

	/**
	 * Creates a new LinkFollower and returns it
	 *
	 * @return the newly created LinkFollower
	 */
	abstract protected RemoteResourceLinkFollower newLinkFollower();

	/**
	 * Returns the {@link RemoteResourceLinkFollower} associated with the Entity.
	 *
	 * @return the associated LinkFollower
	 */
	public RemoteResourceLinkFollower getLinkFollower() {
		return  linkFollower;
	}


	/**
	 * Removes an Entity from the list. A removed entity will not be used by the parser and any fields/configuration made
	 * in the removed entity will be lost.
	 *
	 * @param entityName the name of the entity that will be removed
	 */
	public void removeEntity(String entityName) {
		entities.remove(entityName);
		originalEntityNames.remove(entityName);
	}

	/**
	 * Removes an entity from the list. A removed entity will not be used by the parser and any fields/configuration
	 * associated with the removed entity will be lost.
	 *
	 * @param entity the entity object that will be removed
	 */
	public void removeEntity(T entity) {
		removeEntity(entity.getEntityName());
	}

}

