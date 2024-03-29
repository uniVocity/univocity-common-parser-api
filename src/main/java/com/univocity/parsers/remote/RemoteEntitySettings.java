/*
 * Copyright (c) 2013 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;
import com.univocity.parsers.common.record.*;

import java.util.*;

/**
 * Manages configuration options for individual entities of a {@link RemoteEntityList}. Settings that also exist in
 * the parent {@link RemoteParserSettings} will be used by default but can be overridden for an individual entity.
 *
 * @param <C> the type of {@link Context} implementation supported by {@link com.univocity.parsers.common.processor.core.Processor}s of this entity.
 * @param <S> an internal configuration object that extends from {@link CommonParserSettings}, and is used to
 *            manage configuration of elements shared with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>
 * @param <G> type of the global configuration class (an instance of {@link RemoteEntitySettings}, used to configure
 *            the parser (a concrete implementation of {@link EntityParserInterface}) and its entities.
 *
 * @author Univocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class RemoteEntitySettings<C extends Context, S extends CommonParserSettings, G extends RemoteParserSettings, T extends RemoteFollower> extends EntitySettings<C, S, G> implements CommonFollowerOptions {

	private boolean localEmptyValue;
	private String emptyValue = null;

	private boolean localColumnReorderingEnabled;
	protected Set<String> requestParameters = new LinkedHashSet<String>();
	protected Map<String, T> followers = new HashMap<String, T>();

	private Boolean ignoreLinkFollowingErrors;
	private Nesting nesting;
	protected RemoteFollower owner;
	protected List<RecordFilter<? extends com.univocity.parsers.common.record.Record, C>> recordFilters = new ArrayList<RecordFilter<? extends com.univocity.parsers.common.record.Record, C>>(1);


	/**
	 * Internal constructor to be invoked the subclasses of {@code EntitySettings}
	 *
	 * @param entityName     the entity name, usually provided by the user
	 * @param entitySettings an internal implementation of a {@link CommonSettings}, used to manage configuration
	 *                       of elements shared with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>.
	 *                       Not meant to be exposed/accessed directly by users.
	 * @param parentEntity   parent entity to build settings on top of
	 */
	protected RemoteEntitySettings(String entityName, S entitySettings, RemoteEntitySettings parentEntity) {
		super(entityName, entitySettings, parentEntity);
	}

	@Override
	public final Nesting getNesting() {
		if (nesting == null) {
			if (parentEntity != null) {
				return ((RemoteEntitySettings) parentEntity).getNesting();
			}
			return parserSettings.getNesting();
		}
		return nesting;
	}

	@Override
	public final void setNesting(Nesting combineLinkFollowingRows) {
		this.nesting = combineLinkFollowingRows;
	}

	@Override
	public final void ignoreFollowingErrors(boolean ignoreLinkFollowingErrors) {
		this.ignoreLinkFollowingErrors = ignoreLinkFollowingErrors;
	}

	@Override
	public final boolean isIgnoreFollowingErrors() {
		if (ignoreLinkFollowingErrors == null) {
			if (parentEntity != null) {
				return ((RemoteEntitySettings) parentEntity).isIgnoreFollowingErrors();
			}
			return parserSettings.isIgnoreFollowingErrors();
		}
		return ignoreLinkFollowingErrors;
	}

	@Override
	protected S getInternalSettings() {
		return super.getInternalSettings();
	}

	/**
	 * Returns the name of all fields associated with a remote entity. The definition of fields and how they are populated
	 * is delegated to concrete implementations of this class.
	 *
	 * @return a unmodifiable, ordered {@link LinkedHashSet} of field names available from this entity.
	 */
	public abstract Set<String> getFieldNames();

	/**
	 * Removes a field from the entity. Removed fields will not be used by the parser and any configuration associated
	 * with it will be lost.
	 *
	 * @param fieldName name of the field that should be be removed.
	 */
	public abstract void removeField(String fieldName);

	/**
	 * Identifies whether fields should be reordered when field selection methods such as
	 * {@link EntitySettings#selectFields(String...)} are used.
	 *
	 * <p>When <b>enabled</b>, each parsed record will contain values only for the selected columns.
	 * The values will be ordered according to the selection.
	 * <p>When <b>disabled</b>, each parsed record will contain values for all columns, in their original sequence.
	 * Fields which were not selected will contain null values, as defined in {@link EntitySettings#getNullValue()}.
	 *
	 * <i>Defaults to {@code true}</i>
	 *
	 * @return a flag indicating whether or not selected fields should be reordered
	 */
	public final boolean isColumnReorderingEnabled() {
		if (localColumnReorderingEnabled || parserSettings == null) {
			return getInternalSettings().isColumnReorderingEnabled();
		}
		return parserSettings.isColumnReorderingEnabled();
	}

	/**
	 * Defines whether fields should be reordered when field selection methods such as
	 * {@link EntitySettings#selectFields(String...)} are used.
	 *
	 * <p>When <b>enabled</b>, each parsed record will contain values only for the selected columns.
	 * The values will be ordered according to the selection.
	 * <p>When <b>disabled</b>, each parsed record will contain values for all columns, in their original sequence.
	 * Fields which were not selected will contain null values, as defined in {@link EntitySettings#getNullValue()}.
	 *
	 * <i>Defaults to {@code true}</i>
	 *
	 * @param columnReorderingEnabled the flag indicating whether or not selected fields should be reordered
	 */
	public final void setColumnReorderingEnabled(boolean columnReorderingEnabled) {
		this.localColumnReorderingEnabled = true;
		this.getInternalSettings().setColumnReorderingEnabled(columnReorderingEnabled);
	}

	/**
	 * Returns the value to be used when the content parsed for a field of some record evaluates to an empty {@code String}
	 *
	 * <i>Defaults to {@code null}</i>
	 *
	 * @return the value to be used instead of empty {@code String} (i.e. "") when the content of a field is empty.
	 */
	public final String getEmptyValue() {
		if (localEmptyValue || parserSettings == null) {
			return emptyValue;
		}
		return parserSettings.getEmptyValue();
	}

	/**
	 * Defines the value to be used when the content parsed for a field of some record evaluates to an empty {@code String}
	 *
	 * <i>Defaults to {@code null}</i>
	 *
	 * @param emptyValue the value to be used instead of empty {@code String} (i.e. "") when the content of a field is empty.
	 */
	public final void setEmptyValue(String emptyValue) {
		localEmptyValue = true;
		this.emptyValue = emptyValue;
	}

	/**
	 * Returns a (unmodifiable) map of {@link RemoteFollower}s associated with this entity.
	 *
	 * @return the {@link RemoteFollower}s associated with this entity.
	 */
	public Map<String, T> getRemoteFollowers() {
		return Collections.unmodifiableMap(followers);
	}

	protected RemoteEntityList getParentEntityList() {
		return (RemoteEntityList) super.getParentEntityList();
	}

	/**
	 * Obtains a {@link RemoteFollower}s associated with this entity.
	 *
	 * @param followerName name of the {@link RemoteFollower} to return
	 *
	 * @return the {@link RemoteFollower} associated with this entity, or {@code null} if not found.
	 */
	public T getRemoteFollower(String followerName) {
		return followers.get(followerName);
	}


	@Override
	protected EntitySettings<C, S, G> clone() {
		RemoteEntitySettings<C, S, G, T> out = (RemoteEntitySettings<C, S, G, T>) super.clone();
		out.followers = new HashMap<String, T>();
		requestParameters = new LinkedHashSet<String>();
		return out;
	}

	/**
	 * Adds a filter to the entity to prevent unwanted rows to be processed.
	 * @param filter the callback {@link RecordFilter} to filter incoming records of this entity.
	 */
	public final void addRecordFilter(RecordFilter<? extends com.univocity.parsers.common.record.Record, C> filter) {
		recordFilters.add(filter);
	}

}