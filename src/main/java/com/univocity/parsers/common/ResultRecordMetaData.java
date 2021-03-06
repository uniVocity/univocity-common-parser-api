/*
 * Copyright (c) 2013 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.record.*;

/**
 * {@link RecordMetaData} with the name of the {@link EntitySettings} used.
 *
 * @author Univocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RecordMetaData
 */
public interface ResultRecordMetaData<C extends Context> extends RecordMetaData {

	/**
	 * Get the name of the entity that contains the current results.
	 *
	 * @return the name of the entity.
	 */
	String entityName();
}
