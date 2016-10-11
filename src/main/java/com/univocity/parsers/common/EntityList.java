/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.common.*;

import java.util.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class EntityList<E extends EntitySettings> implements Iterable<E> {

	protected final Map<String, E> entities = new TreeMap<String, E>();
	protected final Map <String, String> originalEntityNames = new TreeMap<String, String>();
	private EntityParserSettings globalSettings;

	/**
	 * Creates a new, empty EntityList
	 */
	protected EntityList() {
	}

	final void setGlobalSettings(EntityParserSettings globalSettings){
		this.globalSettings = globalSettings;
		for(E entity : entities.values()){
			entity.setGlobalSettings(globalSettings);
		}
	}

	/**
	 * Returns the entity object associated with the given entityName. If there is no entity with that
	 * name, a new entity will be created and returned.
	 *
	 * @param entityName name of the entity that will be returned.
	 * @return an existing or new entity with the given name
	 */
	public final E configureEntity(String entityName) {
		Args.notBlank(entityName, "Entity name");
		String normalizedEntityName = entityName.trim().toLowerCase();
		if (entities.get(normalizedEntityName) == null) {
			E newEntity = newEntity(entityName);
			newEntity.setGlobalSettings(globalSettings);
			entities.put(normalizedEntityName, newEntity);
			originalEntityNames.put(entityName, normalizedEntityName);
		}
		E entitySettings = entities.get(normalizedEntityName);
		return entitySettings;
	}

	protected abstract E newEntity(String entityName);

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
	public final Collection<E> getEntities() {
		return Collections.unmodifiableCollection(entities.values());
	}

	public final E getEntity(String entityName) {
		ArgumentUtils.notEmpty("Entity name", entityName);
		return entities.get(entityName.trim().toLowerCase());
	}

	/**
	 * Removes an Entity from the list. A removed entity will not be used by the parser and any fields/configuration made
	 * in the removed entity will be lost.
	 *
	 * @param entityName the name of the entity that will be removed
	 */
	public final void removeEntity(String entityName) {
		Args.notBlank(entityName, "Entity name");
		entityName = entityName.trim().toLowerCase();
		entities.remove(entityName);
		originalEntityNames.remove(entityName);
	}

	/**
	 * Removes an entity from the list. A removed entity will not be used by the parser and any fields/configuration
	 * associated with the removed entity will be lost.
	 *
	 * @param entity the entity object that will be removed
	 */
	public final void removeEntity(E entity) {
		Args.notNull(entity, "Entity");
		removeEntity(entity.getEntityName());
	}

	@Override
	public Iterator<E> iterator() {
		return entities.values().iterator();
	}
}
