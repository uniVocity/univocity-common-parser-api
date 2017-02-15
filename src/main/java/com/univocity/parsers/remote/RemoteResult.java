/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface RemoteResult<R extends RemoteRecord, C extends Context> extends Result<R, C> {

	RemoteResult<R, C> getLinkedFieldData(int rowIndex);

	Results<? extends RemoteResult<R, C>> getLinkedEntityData(int rowIndex);
}
