/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.common.*;

import java.util.*;

/**
 * A list of entities to be parsed by some implementation of {@link EntityParserInterface},
 * and their specific configurations.
 *
 * The configuration applied over individual {@link EntitySettings} elements override their counterparts in the
 * global parser settings, usually a subclass of {@link EntityParserSettings}
 *
 * @param <E> the type of {@link EntitySettings} managed by this list.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see EntitySettings
 * @see EntityParserSettings
 * @see EntityParserInterface
 */
public abstract class EntityList<E extends EntitySettings> implements Iterable<E> {

	protected Map<String, E> entities = new TreeMap<String, E>();
	protected Map<String, String> originalEntityNames = new TreeMap<String, String>();
	private EntityParserSettings globalSettings;

	/**
	 * Creates a new, empty {@code EntityList}, applying the global configuration object, used by the
	 * {@link EntityParserInterface} implementation, to all entity-specific settings in this list.
	 *
	 * @param globalSettings the global parser settings whose configuration may provide defaults for all entities
	 *                       defined in this list.
	 */
	protected EntityList(EntityParserSettings globalSettings) {
		Args.notNull(globalSettings, "Parser settings");
		this.globalSettings = globalSettings;
	}

	/**
	 * Returns the configuration object associated with the given entityName. If there is no entity with that
	 * name, a new configuration will be created and returned. The global settings made for the parser will be used
	 * by default. You can configure your entity to use different settings if required.
	 *
	 * @param entityName name of the entity whose configuration that will be returned.
	 *
	 * @return an existing or new entity configuration associated with the given entity name
	 */
	public final E configureEntity(String entityName) {
		return configureEntity(entityName, null);
	}

	/**
	 * Returns the configuration object associated with the given entityName. If there is no entity with that
	 * name, a new configuration will be created and returned. The global settings made for the parser will be used
	 * by default. You can configure your entity to use different settings if required.
	 *
	 * @param entityName   name of the entity whose configuration that will be returned.
	 * @param parentEntity the "parent" entity (which can be {@code null}), whose settings will
	 *                     be passed on to the new entity.
	 *
	 * @return an existing or new entity configuration associated with the given entity name
	 */
	protected E configureEntity(String entityName, E parentEntity) {
		Args.notBlank(entityName, "Entity name");
		String normalizedEntityName = entityName.trim().toLowerCase();
		if (entities.get(normalizedEntityName) == null) {
			E newEntity = newEntity(entityName, parentEntity);
			newEntity.setParent(this);
			entities.put(normalizedEntityName, newEntity);
			originalEntityNames.put(entityName, normalizedEntityName);
		}
		E entitySettings = entities.get(normalizedEntityName);
		entitySettings.setParent(this);

		return entitySettings;
	}

	/**
	 * Creates a new configuration object for the given entity name
	 *
	 * @param entityName   name of the new entity
	 * @param parentEntity the "parent" entity (which can be {@code null}), whose settings will
	 *                     be passed on to the new entity.
	 *
	 * @return new configuration object to be used by the new entity.
	 */
	protected abstract E newEntity(String entityName, E parentEntity);

	/**
	 * Returns the entity names stored in the {@code EntityList} as a set of {@code String}s. Returns an empty set if no
	 * entities configured.
	 *
	 * @return all entity names in a set.
	 */
	public final Set<String> getEntityNames() {
		return new TreeSet<String>(originalEntityNames.keySet());
	}

	/**
	 * Returns all the entity configurations stored in this {@code EntityList} as a unmodifiable Collection
	 *
	 * @return the collection of all entity settings currently in use.
	 */
	public final Collection<E> getEntities() {
		return Collections.unmodifiableCollection(entities.values());
	}

	/**
	 * Returns the configuration of a an existing entity or {@code null} if there's no entity with the given name.
	 *
	 * @param entityName name of the entity whose configuration will be returned
	 *
	 * @return an instance of {@link EntitySettings} which manages the configuration of the given entity,
	 * or {@code null} if no such entity exist.
	 */
	public final E getEntity(String entityName) {
		ArgumentUtils.notEmpty("Entity name", entityName);
		return entities.get(entityName.trim().toLowerCase());
	}

	/**
	 * Removes an entity from this {@code EntityList}. A removed entity will not be used by the parser and any
	 * fields/configuration made for the removed entity configuration will be lost.
	 *
	 * @param entityName name of the entity that will be removed.
	 */
	public final void removeEntity(String entityName) {
		Args.notBlank(entityName, "Entity name");
		entityName = entityName.trim().toLowerCase();
		entities.remove(entityName);
		originalEntityNames.remove(entityName);
	}

	/**
	 * Removes an entity from this {@code EntityList}. A removed entity will not be used by the parser and any
	 * fields/configuration made for the removed entity configuration will be lost.
	 *
	 * @param entity the entity object that should be be removed
	 */
	public final void removeEntity(E entity) {
		Args.notNull(entity, "Entity");
		removeEntity(entity.getEntityName());
	}

	/**
	 * Iterates over the entity configurations managed by this {@code EntityList}.
	 *
	 * @return a new {@code Iterator} of {@link EntitySettings} objects stored in this {@code EntityList}.
	 */
	@Override
	public final Iterator<E> iterator() {
		return entities.values().iterator();
	}


	/**
	 * Associates an entity setting configuration to a given entity. If the entity does not exist, it will be created.
	 * The <em>general</em> settings will be copied into a new configuration object, while any input-specific setting
	 * will be lost.
	 *
	 * @param settings the configuration to be associated with the given entity.
	 */
	protected E addEntitySettings(E settings) {
		Args.notNull(settings, "Entity settings");

		String entityName = settings.getEntityName();
		configureEntity(entityName);
		String normalizedEntityName = entityName.trim().toLowerCase();

		E config = (E) settings.clone();
		this.entities.put(normalizedEntityName, config);
		config.setParent(this);

		return config;
	}

	/**
	 * Returns the global parser settings whose configuration may provide defaults for all entities
	 * defined in this list.
	 *
	 * @return the parent parser settings object.
	 */
	public EntityParserSettings getParserSettings() {
		return globalSettings;
	}

	protected abstract EntityList newInstance();
}
