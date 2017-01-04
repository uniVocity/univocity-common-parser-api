/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.record.*;

import java.util.*;

/**
 * //TODO: javadoc
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface Result<T extends Record, C extends Context> {

	Map<String, List<String[]>> getRows();

	List<String[]> getRows(String entityName);

	Map<String, IterableResult<String[], C>> iterateRows();

	IterableResult<String[], C> iterateRows(String entityName);

	Map<String, List<T>> getRecords();

	List<T> getRecords(String entityName);

	Map<String, IterableResult<T, C>> iterateRecords();

	IterableResult<T, C> iterateRecords(String entityName);

	C getParsingContext();
}
