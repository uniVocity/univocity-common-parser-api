/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import java.util.*;

public abstract class EntityParserSettings<S extends CommonParserSettings, L extends EntityList> {

	protected final Set<String> entitiesToRead = new TreeSet<String>();
	protected final Set<String> entitiesToSkip = new TreeSet<String>();
	protected final S globalSettings = createGlobalSettings();
	private final L entityList;

	public EntityParserSettings(L entityList) {
		this.entityList = entityList;
		entityList.setGlobalSettings(this);
	}

	protected static final CommonParserSettings createEmptyGlobalSettings() {
		return EntitySettings.createEmptyParserSettings();
	}

	protected abstract S createGlobalSettings();

	private static String[] toArray(Collection<String> collection) {
		return collection == null ? ArgumentUtils.EMPTY_STRING_ARRAY : collection.toArray(new String[0]);
	}

	public final void addEntitiesToRead(Collection<String> entitiesToRead) {
		addEntitiesToRead(toArray(entitiesToRead));
	}

	public final void addEntitiesToRead(String... entitiesToRead) {
		for (String entity : entitiesToRead) {
			ArgumentUtils.noNulls("Name of entity to be read", entity);
			this.entitiesToRead.add(entity);
		}
	}

	public final void addEntitiesToSkip(Collection<String> entitiesToSkip) {
		setEntitiesToSkip(toArray(entitiesToSkip));
	}

	public final void setEntitiesToSkip(Collection<String> entitiesToSkip) {
		setEntitiesToSkip(toArray(entitiesToSkip));
	}

	public final Set<String> getEntitiesToRead() {
		return Collections.unmodifiableSet(entitiesToRead);
	}

	public final void setEntitiesToRead(Collection<String> entitiesToRead) {
		setEntitiesToRead(toArray(entitiesToRead));
	}

	public final void setEntitiesToRead(String... entitiesToRead) {
		this.entitiesToRead.clear();
		addEntitiesToRead(entitiesToRead);
	}

	public final Set<String> getEntitiesToSkip() {
		return Collections.unmodifiableSet(entitiesToSkip);
	}

	public final void setEntitiesToSkip(String... entitiesToSkip) {
		this.entitiesToSkip.clear();
		addEntitiesToSkip(entitiesToSkip);
	}

	public final void addEntitiesToSkip(String... entitiesToSkip) {
		for (String entity : entitiesToSkip) {
			ArgumentUtils.noNulls("Name of entity to be skipped", entity);
			this.entitiesToSkip.add(entity);
		}
	}

	public final boolean shouldSkip(String entityName) {
		return entitiesToSkip.contains(entityName) || (!entitiesToRead.isEmpty() && !entitiesToRead.contains(entityName));
	}

	public final boolean shouldRead(String entityName) {
		return !entitiesToRead.isEmpty() && entitiesToRead.contains(entityName);
	}

	/**
	 * Returns the entity names contained in the associated {@link HtmlEntityList} as a set of Strings.
	 *
	 * @return the entity names
	 */
	public final Set<String> getEntityNames() {
		return entityList.getEntityNames();
	}

	public final L getEntityList(){
		return entityList;
	}
}