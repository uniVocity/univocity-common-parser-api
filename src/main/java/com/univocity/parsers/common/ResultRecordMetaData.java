/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.record.*;

/**
 * {@link RecordMetaData} with the name of the {@link EntitySettings} used.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RecordMetaData
 */
public interface ResultRecordMetaData<C extends Context> extends RecordMetaData {

	/**
	 * Get the name of the {@link EntitySettings} that was used to create the results.
	 *
	 * @return the name of the {@link EntitySettings} used.
	 */
	String entityName();
}
