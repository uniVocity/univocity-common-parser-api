/*
 * Copyright (c) 2013 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.*;

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
 *
 * @param <C> the {@link Context} implementation which provides information about the entity parser and its execution.
 * @author Univocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class EntityParserSettings<S extends CommonParserSettings, L extends EntityList, C extends Context> implements Cloneable {

	protected Set<String> entitiesToRead = new TreeSet<String>();
	protected Set<String> entitiesToSkip = new TreeSet<String>();
	protected S globalSettings = createGlobalSettings();

	/**
	 * Creates a new configuration object for an implementation of {@link EntityParserInterface}, which will process
	 * an input to produce records for entities defined by an {@link EntityList}
	 */
	public EntityParserSettings() {
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
	 * Returns the custom error handler to be used to capture and handle errors that might happen while processing
	 * records with a {@link com.univocity.parsers.common.processor.core.Processor}
	 * (i.e. non-fatal {@link DataProcessingException}s).
	 *
	 * <p>The parsing process won't stop (unless the error handler rethrows the {@link DataProcessingException}
	 * or manually stops the process).</p>
	 *
	 * @return the callback error handler with custom code to manage occurrences of {@link DataProcessingException}.
	 */
	public final ProcessorErrorHandler<C> getProcessorErrorHandler() {
		return globalSettings.getProcessorErrorHandler();
	}

	/**
	 * Defines a custom error handler to be used to capture and handle errors that might happen while processing
	 * records with a {@link com.univocity.parsers.common.processor.core.Processor}
	 * (i.e. non-fatal {@link DataProcessingException}s).
	 *
	 * <p>The parsing process won't stop (unless the error handler rethrows the {@link DataProcessingException}
	 * or manually stops the process).</p>
	 *
	 * @param processorErrorHandler the callback error handler with custom code to manage occurrences of {@link DataProcessingException}.
	 */
	public final void setProcessorErrorHandler(ProcessorErrorHandler<C> processorErrorHandler) {
		globalSettings.setProcessorErrorHandler(processorErrorHandler);
	}

	/**
	 * Returns whether or not trailing whitespaces from values being read should be trimmed (defaults to {@code true})
	 *
	 * @return {@code true} if trailing whitespaces from values being read should be trimmed, {@code false} otherwise
	 */
	public final boolean getTrimTrailingWhitespaces() {
		return globalSettings.getIgnoreTrailingWhitespaces();
	}

	/**
	 * Defines whether or not trailing whitespaces from values being read should be trimmed (defaults to {@code true})
	 *
	 * @param trimTrailingWhitespaces flag indicating whether to remove trailing whitespaces from values being read
	 */
	@UI(order = 4)
	public final void setTrimTrailingWhitespaces(boolean trimTrailingWhitespaces) {
		globalSettings.setIgnoreTrailingWhitespaces(trimTrailingWhitespaces);
	}

	/**
	 * Returns whether or not leading whitespaces from values being read should be trimmed (defaults to {@code true})
	 *
	 * @return {@code true} if leading whitespaces from values being read should be trimmed, {@code false} otherwise
	 */
	public final boolean getTrimLeadingWhitespaces() {
		return globalSettings.getIgnoreLeadingWhitespaces();
	}

	/**
	 * Defines whether or not trailing whitespaces from values being read should be trimmed (defaults to {@code true})
	 *
	 * @param trimTrailingWhitespaces flag indicating whether to remove trailing whitespaces from values being read
	 */
	@UI(order = 4)
	public final void setTrimLeadingWhitespaces(boolean trimTrailingWhitespaces) {
		globalSettings.setIgnoreLeadingWhitespaces(trimTrailingWhitespaces);
	}

	/**
	 * Configures the parser to trim/keep leading and trailing whitespaces around values
	 * This has the same effect as invoking both {@link #setTrimLeadingWhitespaces(boolean)}
	 * and {@link #setTrimTrailingWhitespaces(boolean)} with the same value.
	 *
	 * @param trim a flag indicating whether whitespaces should be removed around values parsed.
	 */
	public final void trimValues(boolean trim) {
		globalSettings.trimValues(trim);
	}


	/**
	 * Configures the parser to limit the length of displayed contents being processed in the exception message when an error occurs
	 *
	 * <p>If set to {@code 0}, then no exceptions will include the content being manipulated in their attributes,
	 * and the {@code "<omitted>"} string will appear in error messages as the parsed content.</p>
	 *
	 * <p>defaults to {@code -1} (no limit)</p>.
	 *
	 * @return the maximum length of contents displayed in exception messages in case of errors while parsing.
	 */
	public final int getErrorContentLength() {
		return globalSettings.getErrorContentLength();
	}

	/**
	 * Configures the parser to limit the length of displayed contents being processed in the exception message when an error occurs
	 *
	 * <p>If set to {@code 0}, then no exceptions will include the content being manipulated in their attributes,
	 * and the {@code "<omitted>"} string will appear in error messages as the parsed content.</p>
	 *
	 * <p>defaults to {@code -1} (no limit)</p>.
	 *
	 * @param errorContentLength the maximum length of contents displayed in exception messages in case of errors while parsing.
	 */
	@Range(min = 0, max = 300)
	@UI(order = 10)
	public final void setErrorContentLength(int errorContentLength) {
		globalSettings.setErrorContentLength(errorContentLength);
	}

	/**
	 * Returns the {@code String} representation of a null value (defaults to {@code null})
	 * <p>When reading, if the parser does not read any character from the input for a particular value, the nullValue
	 * is used instead of an empty {@code String}</p>
	 *
	 * @return the String representation of a null value
	 */
	public final String getNullValue() {
		return globalSettings.getNullValue();
	}

	/**
	 * Defines the {@code String} representation of a null value (defaults to {@code null})
	 * <p>When reading, if the parser does not read any character from the input for a particular value, the nullValue
	 * is used instead of an empty {@code String}</p>
	 *
	 * @param nullValue the String representation of a null value
	 */
	@UI
	public final void setNullValue(String nullValue) {
		globalSettings.setNullValue(nullValue);
	}

	@Override
	protected EntityParserSettings<S, L, C> clone() {
		try {
			EntityParserSettings<S, L, C> out = (EntityParserSettings<S, L, C>) super.clone();

			out.entitiesToRead = new TreeSet<String>();
			out.entitiesToSkip = new TreeSet<String>();
			out.globalSettings = (S) globalSettings.clone(true);

			return out;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Unable to clone", e);
		}
	}
}