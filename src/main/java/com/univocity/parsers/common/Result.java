/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.processor.core.*;
import com.univocity.parsers.common.record.*;

import java.io.*;
import java.util.*;

/**
 * An interface used to store the result of parsing some input with a parser implementing {@link EntityParserInterface}.
 * The fields that are captured and the order that they appear in each row depend on how they were configured using
 * {@link EntitySettings} before parsing.
 *
 * @param <R> the type of {@link Record} that this result stores.
 * @param <C> the type of {@link Context} used when the results where parsed.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see EntityParserInterface
 * @see EntitySettings
 * @see Record
 */
public interface Result<R extends Record, C extends Context> extends Closeable {

	/**
	 * Get the name of the entity that this result belongs to.
	 *
	 * @return the name of the entity that the result is for.
	 */
	String getEntityName();

	/**
	 * @return the context used when creating the result.
	 */
	C getContext();

	/**
	 * @return the meta data of the record
	 */
	RecordMetaData getRecordMetaData();

	/**
	 * Get an array of the headers of each field in the order that they were created and match the order
	 * that they appear in the methods that return a {@code String[]} for each row. e.g. {@link #getRows()}
	 * and {@link #iterateRows()}.
	 *
	 * @return the headers of each field for the result.
	 */
	String[] getHeaders();

	/**
	 * Get a {@link List} of {@code String[]} where each array is a row of results. The order of fields
	 * in each row matches the order they were added to the {@link EntityList} and the order they appear
	 * in {@link #getHeaders()}.
	 *
	 * @return a {@link List} of {@code String[]} for each row of the result.
	 */
	List<String[]> getRows();

	/**
	 * Get an {@link Iterable} of {@code String[]} that iterates over each row.
	 *
	 * @return an {@link Iterable} over each row.
	 */
	Iterable<String[]> iterateRows();

	/**
	 * Get a {@link List} of {@link Record} where each {@link Record} represents a row in the results.
	 * Each {@link Record} contains the {@link String} captured for each field and also many convenience methods
	 * for further data manipulation.
	 *
	 * @return a {@link List} of {@link Record} for each row of the result.
	 */
	List<R> getRecords();

	/**
	 * Get an {@link Iterable} of {@link Record} that iterates over each row.
	 *
	 * @return an {@link Iterable} over each row
	 */
	Iterable<R> iterateRecords();


	/**
	 * Uses the provided {@link Processor} to process each row of the result using a
	 * default {@link NoopProcessorErrorHandler} to  not handle any errors and simply rethrow them.
	 *
	 * @param processor the {@link Processor} used to process each row of the result.
	 */
	void process(Processor<C> processor);

	/**
	 * Uses the provided {@link Processor} to process each row of the result using the {@link ProcessorErrorHandler} to
	 * handle any errors that occur.
	 *
	 * @param processor    the {@link Processor} used to process each row of the result.
	 * @param errorHandler the {@link ProcessorErrorHandler} used to handle any error in a specific way
	 *                     that occurs during processing.
	 */
	void process(Processor<C> processor, ProcessorErrorHandler<C> errorHandler);

	/**
	 * Using the {@link Class} {@code beanType} that has to contain one or more annotations from
	 * {@link com.univocity.parsers.annotations}, the fields of this {@link Result} are parsed into the matching fields
	 * of the {@code beanType}. Each row of this {@link Result}, if successfully parsed, will be returned as an object
	 * in a {@link List} of the specified type {@code <T>}. A null value for a field linked to a property of the bean
	 * encountered during parsing results in the default value for primitives and a null value for objects being set.
	 *
	 * The default {@link NoopProcessorErrorHandler} will be used as a {@link ProcessorErrorHandler} to not handle any
	 * errors.
	 *
	 * @param beanType the type of the bean that is created using the fields of this {@link Result}.
	 * @param <T>      the type of the bean that is returned.
	 *
	 * @return a {@link List} that contains {@code <T>} for each bean successfully parsed from a row.
	 */
	<T> List<T> getBeans(Class<T> beanType);

	/**
	 * Using the {@link Class} {@code beanType} that has to contain one or more annotations from
	 * {@link com.univocity.parsers.annotations}, the fields of this {@link Result} are parsed into the matching fields
	 * of the {@code beanType}. Each row of this {@link Result}, if successfully parsed, will be returned as an object
	 * in a {@link List} of the specified type {@code <T>}. A null value for a field linked to a property of the bean
	 * encountered during parsing results in the default value for primitives and a null value for objects being set.
	 *
	 * Any errors during parsing will be handled by the {@code errorHandler}.
	 *
	 * @param beanType     the type of the bean that is created using the fields of this {@link Result}.
	 * @param <T>          the type of the bean that is returned.
	 * @param errorHandler the {@link ProcessorErrorHandler} to handle any errors that occur during the processing
	 *                     of beans.
	 *
	 * @return a {@link List} that contains {@code <T>} for each bean successfully parsed from a row.
	 */
	<T> List<T> getBeans(Class<T> beanType, ProcessorErrorHandler<C> errorHandler);

	/**
	 * Using the {@link Class} {@code beanType} that has to contain one or more annotations from
	 * {@link com.univocity.parsers.annotations}, the fields of this {@link Result} are parsed into the matching fields
	 * of the {@code beanType}.
	 *
	 * Returns an {@link IterableResult} of the {@link Class} {@code beanType} which is just like an {@link Iterator}
	 * except it has the added {@link Context} for the parsing session.
	 *
	 * A default {@link NoopProcessorErrorHandler} will be used as a {@link ProcessorErrorHandler} to not handle any
	 * errors.
	 *
	 * @param beanType the {@link Class} of the bean to create using fields of this {@link Result}.
	 * @param <T>      the type of bean to iterate over.
	 *
	 * @return an {@link IterableResult} iterating over beans of the type {@code beanType}.
	 */
	<T> IterableResult<T, C> iterateBeans(Class<T> beanType);

	/**
	 * Using the {@link Class} {@code beanType} that has to contain one or more annotations from
	 * {@link com.univocity.parsers.annotations}, the fields of this {@link Result} are parsed into the matching fields
	 * of the {@code beanType}.
	 *
	 * Returns an {@link IterableResult} of the {@link Class} {@code beanType} which is just like an {@link Iterator}
	 * except it has the added {@link Context} for the parsing session.
	 *
	 * Any errors during parsing will be handled by the {@code errorHandler}.
	 *
	 * @param beanType     the {@link Class} of the bean to create using fields of this {@link Result}.
	 * @param errorHandler the {@link ProcessorErrorHandler} to handle any errors during the parsing of the bean.
	 * @param <T>          the type of bean to iterate over.
	 *
	 * @return an {@link IterableResult} iterating over beans of the type {@code beanType}.
	 */
	<T> IterableResult<T, C> iterateBeans(Class<T> beanType, ProcessorErrorHandler<C> errorHandler);

	/**
	 * Whether or not all input has been parsed and no more row entries are to be added.
	 * If this is {@code true} then no more rows will be added.
	 *
	 * @return whether or not no more rows will be added.
	 */
	boolean isComplete();

	/**
	 * Pauses the processing of rows until all rows are captured and the queue is closed.
	 *
	 * @throws InterruptedException if the thread is interrupted
	 */
	void waitForCompletion() throws InterruptedException;

	/**
	 * Joins rows of a given result with the rows of the current one. Rows are joined by matching values of
	 * a given set of field names that are common to both {@link Result} instances.
	 *
	 * @param result     the input result whose rows will be associated with the current.
	 * @param fieldNames fields common to the given result and the current, whose values will be used to identify which
	 *                   rows should be associated. If none provided the fields that are common to current and given
	 *                   results will be used.
	 *
	 * @return a {@link Result} with the associated data.
	 */
	Result<R, C> join(Result<R, C> result, String... fieldNames);

	/**
	 * Links rows of a given result with the rows of the current one. Rows are linked by matching values of a given
	 * set of field names that are common to both {@link Result} instances.
	 * The linked data is then accessible using {@link #getLinkedEntityData(int)}
	 *
	 * @param result     the other {@link Result} whose rows will be added to this {@link Result}'s linked entity data.
	 * @param fieldNames the common field names used to determine which fields to link. If none provided then all
	 *                   fields that are common will be used.
	 */
	void link(Result<R, C> result, String... fieldNames);

	/**
	 * Gets {@link Result}s with data for additional fields linked to the record at {@code rowIndex}.
	 *
	 * @param rowIndex which row to get the linked field data from
	 *
	 * @return a {@link Result} linked to the row at {@code rowIndex}
	 */
	Result<R, C> getLinkedFieldData(int rowIndex);

	/**
	 * Gets all of the {@link Result} associated with the specific {@code rowIndex}
	 * in a {@link Results} list.
	 *
	 * @param rowIndex which row to get the linked entity data from
	 *
	 * @return a {@link Results} linked to the row at {@code rowIndex}
	 */
	Results<? extends Result<R, C>> getLinkedEntityData(int rowIndex);

	/**
	 * Closes the row processing so no more rows will be added to the {@link Result}
	 */
	void close();
}
