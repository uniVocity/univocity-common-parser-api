/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;
import com.univocity.parsers.common.record.*;

import java.util.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface RemoteRecord<R extends RemoteRecord, C extends Context> extends Record {

	RemoteResult<R, C> getLinkedFieldData();

	Map<String, RemoteResult<R, C>> getLinkedEntityData();
}
