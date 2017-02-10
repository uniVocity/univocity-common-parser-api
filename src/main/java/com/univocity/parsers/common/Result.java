/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.processor.core.*;
import com.univocity.parsers.common.record.*;

import java.util.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface Result<R extends Record, C extends Context> {

	String getEntityName();

	C getContext();

	RecordMetaData getRecordMetaData();

	String[] getHeaders();

	List<String[]> getRows();

	Iterable<String[]> iterateRows();

	List<R> getRecords();

	Iterable<R> iterateRecords();

	void process(Processor<C> processor);

	void process(Processor<C> processor, ProcessorErrorHandler<C> errorHandler);

	<T> List<T> getBeans(Class<T> beanType);

	<T> List<T> getBeans(Class<T> beanType, ProcessorErrorHandler<C> errorHandler);

	<T> Iterable<T> iterateBeans(Class<T> beanType);

	<T> Iterable<T> iterateBeans(Class<T> beanType, ProcessorErrorHandler<C> errorHandler);

	boolean isComplete();

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
	Result<R, C> joinWith(Result<R, C> result, String... fieldNames);

}
