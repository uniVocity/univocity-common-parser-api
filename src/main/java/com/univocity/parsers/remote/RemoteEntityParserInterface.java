/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;

/**
 * //TODO: javadoc
 *
 * @param <C> the pagination context implementation supported by the parser.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface RemoteEntityParserInterface<C extends PaginationContext> extends EntityParserInterface {

	/**
	 * Returns the {@link PaginationContext} object with information collected for the configured {@link Paginator}, if
	 * any. The information returned comes from the last input processed, and might have been modified by a
	 * {@link PaginationHandler} if it has been associated with the {@link Paginator}
	 * using {@link Paginator#setPaginationHandler(PaginationHandler)}.
	 *
	 * @return the current {@link PaginationContext} with pagination information captured after parsing a given input.
	 */
	C getPaginationContext();
}
