/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.record.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface ResultRecord<C extends Context> extends Record {

	Result<? extends ResultRecord, C> getLinkedFieldData();

	Results<? extends Result<? extends ResultRecord, C>> getLinkedEntityData();
}
