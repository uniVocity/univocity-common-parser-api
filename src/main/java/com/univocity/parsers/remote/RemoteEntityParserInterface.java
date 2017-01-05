/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;
import com.univocity.parsers.common.record.*;

/**
 * //TODO: javadoc
 *
 * @param <C> the pagination context implementation supported by the parser.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface RemoteEntityParserInterface<P extends PaginationContext, R extends Record, C extends Context, T extends Result<R, C>> extends EntityParserInterface<R, C, T> {

	/**
	 * Returns the {@link PaginationContext} object with information collected for the configured {@link Paginator}, if
	 * any. The information returned comes from the last input processed, and might have been modified by a
	 * {@link PaginationHandler} if it has been associated with the {@link Paginator}
	 * using {@link Paginator#setPaginationHandler(PaginationHandler)}.
	 *
	 * @return the current {@link PaginationContext} with pagination information captured after parsing a given input.
	 */
	P getPaginationContext();
}
