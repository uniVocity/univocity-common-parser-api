/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.record.*;

/**
 * A {@link ResultRecord} is a record that can be linked with one more more {@link ResultRecord}s.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see ResultRecordMetaData
 */
public interface ResultRecord<C extends Context> extends Record {

	/**
	 * Gets {@link Result}s with data for additional fields linked to the current record.
	 *
	 * @return the result linked to this {@link ResultRecord}
	 */
	Result<? extends ResultRecord, C> getLinkedFieldData();

	/**
	 * Gets all of the associated {@link Result} for this {@link ResultRecord}.
	 *
	 * @return all of the associated {@link Record}s.
	 */
	Results<? extends Result<? extends ResultRecord, C>> getLinkedEntityData();

	/**
	 * @return whether or not there is any linked results
	 */
	boolean hasLinkedData();

	/**
	 * Get the meta data associated with the {@link ResultRecord}
	 *
	 * @return the meta data associated with the record.
	 */
	ResultRecordMetaData getMetaData();

	/**
	 * Returns the parent record which contains this current record. Every {@link ResultRecord}
	 * obtained from method {@link #getLinkedEntityData()} should have a parent record. This
	 * record will have the initially parsed data from the input, before any {@link Nesting} operation
	 * is applied.
	 *
	 * @return the parent record if it exists.
	 */
	ResultRecord<C> getParentRecord();
}
