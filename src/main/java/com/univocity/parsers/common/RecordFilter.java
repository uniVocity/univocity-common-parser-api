/*
 * Copyright (c) 2018 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.record.*;

/**
 * A filter of records captured by the parser. Allows discarding records,
 * before any {@link Nesting} operation is applied or any link following occurs
 *
 * @param <R> the type of record to filter.
 * @param <C> the a contextual object with the current parsing state
 */
public interface RecordFilter<R extends Record, C extends Context> {
	/**
	 * Determines whether the given record should be accepted by the parser or discarded.
	 * @param record the record to accept
	 * @param context the current state of the parser
	 * @return {@code true} if the record should be accepted by the parser for
	 * further processing, or {@false} if the record should be discarded
	 */
	boolean accept(R record, C context);
}
