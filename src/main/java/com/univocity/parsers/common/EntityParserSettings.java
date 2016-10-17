/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.common.*;

import java.util.*;

/**
 * Base configuration class of a parser that produces records for multiple entities. The settings available in
 * an {@code EntityParserSettings} configure the parsing process and provide default configuration options
 * that individual implementations of {@link EntitySettings} can override.
 *
 * Entities are managed from an {@link EntityList} implementation.
 *
 * @param <S> an internal configuration object that extends from {@link CommonParserSettings}, and is used to
 *            manage configuration of elements shared with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>
 * @param <L> the {@link EntityList} implementation supported by an {@link EntityParserInterface}.
 */
public abstract class EntityParserSettings<S extends CommonParserSettings, L extends EntityList> {

	protected final Set<String> entitiesToRead = new TreeSet<String>();
	protected final Set<String> entitiesToSkip = new TreeSet<String>();
	protected final S globalSettings = createGlobalSettings();
	private final L entityList;

	/**
	 * Creates a new configuration object for an implementation of {@link EntityParserInterface}, which will process
	 * an input to produce records the the entities defined in the given {@link EntityList}
	 *
	 * @param entityList the list of entities whose records will be generated from the parser
	 */
	public EntityParserSettings(L entityList) {
		Args.notNull(entityList, "Entity list");
		this.entityList = entityList;
		entityList.setGlobalSettings(this);
	}

	/**
	 * Utility method to create new, empty instances of {@link CommonParserSettings}. For internal use only.
	 *
	 * @return a new instance of a {@link CommonParserSettings} class.
	 */
	protected static final CommonParserSettings createEmptyGlobalSettings() {
		return EntitySettings.createEmptyParserSettings();
	}

	/**
	 * Forces concrete implementations of this class to produce an instance of {@link CommonParserSettings}, which
	 * will be used to configure elements shared with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>
	 *
	 * @return a new instance of some class that extends {@link CommonParserSettings}
	 */
	protected abstract S createGlobalSettings();

	private static String[] toArray(Collection<String> collection) {
		return collection == null ? ArgumentUtils.EMPTY_STRING_ARRAY : collection.toArray(new String[0]);
	}


	/**
	 * Adds one or more entities to the existing selection of entities to read.
	 * The parser will will only process records for the entities in the set of entities to read.
	 * Any any other entity won't have records produced/processed for them.
	 *
	 * @param entitiesToRead names of the entities to be read.
	 */
	public final void addEntitiesToRead(Collection<String> entitiesToRead) {
		addEntitiesToRead(toArray(entitiesToRead));
	}

	/**
	 * Adds one or more entities to the existing selection of entities to read.
	 * The parser will will only process records for the entities in the set of entities to read.
	 * Any any other entity won't have records produced/processed for them.
	 *
	 * @param entitiesToRead names of the entities to be read.
	 */
	public final void addEntitiesToRead(String... entitiesToRead) {
		for (String entity : entitiesToRead) {
			ArgumentUtils.noNulls("Name of entity to be read", entity);
			this.entitiesToRead.add(entity);
		}
	}

	/**
	 * Adds one or more entities to the existing selection of of entities to skip.
	 * The parser will not produce any records or will simply discard records produced for a given entity.
	 *
	 * @param entitiesToSkip names of the entities to be skipped.
	 */
	public final void addEntitiesToSkip(Collection<String> entitiesToSkip) {
		setEntitiesToSkip(toArray(entitiesToSkip));
	}

	/**
	 * Returns the unmodifiable set of entities to read. Any previous selection will be discarded.
	 * The parser will will only process records for the given entities. Any any other entity won't have records
	 * produced/processed for them.
	 *
	 * @return set with names of entities to be read.
	 */
	public final Set<String> getEntitiesToRead() {
		return Collections.unmodifiableSet(entitiesToRead);
	}

	/**
	 * Sets one or more entities as the list of entities to read. Any previous selection will be discarded.
	 * The parser will will only process records for the given entities. Any any other entity won't have records
	 * produced/processed for them.
	 *
	 * @param entitiesToRead names of the entities to be read.
	 */
	public final void setEntitiesToRead(Collection<String> entitiesToRead) {
		setEntitiesToRead(toArray(entitiesToRead));
	}

	/**
	 * Sets one or more entities as the list of entities to read. Any previous selection will be discarded.
	 * The parser will will only process records for the given entities. Any any other entity won't have records
	 * produced/processed for them.
	 *
	 * @param entitiesToRead names of the entities to be read.
	 */
	public final void setEntitiesToRead(String... entitiesToRead) {
		this.entitiesToRead.clear();
		addEntitiesToRead(entitiesToRead);
	}

	/**
	 * Returns an unmodifiable set of names of entities to be skipped.
	 * The parser will not produce any records or will simply discard records produced for the given entity.
	 *
	 * @return names of the entities to be skipped.
	 */
	public final Set<String> getEntitiesToSkip() {
		return Collections.unmodifiableSet(entitiesToSkip);
	}

	/**
	 * Sets one or more entities as the list of entities to skip. Any previous selection will be discarded.
	 * The parser will not produce any records or will simply discard records produced for a given entity.
	 *
	 * @param entitiesToSkip names of the entities to be skipped.
	 */
	public final void setEntitiesToSkip(String... entitiesToSkip) {
		this.entitiesToSkip.clear();
		addEntitiesToSkip(entitiesToSkip);
	}

	/**
	 * Adds one or more entities to the list of entities to skip. The parser will not produce any records or will simply
	 * records produced for a given entity.
	 *
	 * @param entitiesToSkip names of the entities to be skipped.
	 */
	public final void addEntitiesToSkip(String... entitiesToSkip) {
		for (String entity : entitiesToSkip) {
			ArgumentUtils.noNulls("Name of entity to be skipped", entity);
			this.entitiesToSkip.add(entity);
		}
	}

	/**
	 * Sets one or more entities as the list of entities to skip. Any previous selection will be discarded.
	 * The parser will not produce any records or will simply discard records produced for a given entity.
	 *
	 * @param entitiesToSkip names of the entities to be skipped.
	 */
	public final void setEntitiesToSkip(Collection<String> entitiesToSkip) {
		setEntitiesToSkip(toArray(entitiesToSkip));
	}

	/**
	 * Tests whether a given entity should be skipped, so the parser can ignore/not process its records.
	 *
	 * Entities to be skipped are those that are either added to the internal set of  {@link #entitiesToSkip} or
	 * not in the set of {@link #entitiesToRead}, if it is not empty
	 *
	 * @param entityName name of the entity
	 *
	 * @return {@code true} if the entity should be skipped, otherwise {@code false}
	 */
	public final boolean shouldSkip(String entityName) {
		return entitiesToSkip.contains(entityName) || (!entitiesToRead.isEmpty() && !entitiesToRead.contains(entityName));
	}

	/**
	 * Tests whether a given entity should be processed (i.e. whether or not the parser should generate records for it)
	 *
	 * Entities to be read are those that were not added to the internal set of {@link #entitiesToSkip} or
	 * are in the set of {@link #entitiesToRead}, if it is not empty
	 *
	 * @param entityName name of entity
	 *
	 * @return {@code true} if the entity should be read, otherwise {@code false}
	 */
	public final boolean shouldRead(String entityName) {
		return !entitiesToRead.isEmpty() && entitiesToRead.contains(entityName);
	}

	/**
	 * Returns the entity names contained in the associated {@link EntityList} as a set of Strings.
	 *
	 * @return the entity names
	 */
	public final Set<String> getEntityNames() {
		return entityList.getEntityNames();
	}

	/**
	 * Returns the {@link EntityList} provided in the constructor of this class
	 *
	 * @return the entity list.
	 */
	public final L getEntityList() {
		return entityList;
	}
}