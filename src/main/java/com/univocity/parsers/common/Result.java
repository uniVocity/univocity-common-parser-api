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
}
