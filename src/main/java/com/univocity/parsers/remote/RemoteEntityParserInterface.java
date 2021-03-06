/*
 * Copyright (c) 2013 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;
import com.univocity.parsers.common.record.*;

import java.util.*;

/**
 * An extension of the {@link EntityParserInterface} for parsers that access remote content.
 *
 * All parsers should work with a concrete implementation of a {@link RemoteParserSettings} object that provides
 * configuration options for the given parser. The definition of entities and their configuration should be managed by
 * an implementation of {@link RemoteEntityList}, which provides individual {@link RemoteEntitySettings} to allow user
 * to configure how the records of each individual entity should be handled.
 *
 * Concrete parser implementations may provide additional operations.
 *
 * @param <C> the pagination context implementation supported by the parser, to support reading through records available
 *            remotely in multiple result pages.
 *
 * @author Univocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see EntityParserInterface
 * @see PaginationContext
 * @see Record
 * @see Context
 * @see Result
 */
public interface RemoteEntityParserInterface<R extends ResultRecord<C>, C extends Context, T extends Result<R, C>> extends EntityParserInterface<R, C, T> {

	/**
	 * Returns the {@link PaginationContext} object with information collected for the configured {@link Paginator}, if
	 * any. The information returned comes from the last input processed, and might have been modified by a
	 * {@link NextInputHandler} if it has been associated with the {@link Paginator}
	 * using {@link Paginator#setPaginationHandler(NextInputHandler)}.
	 *
	 * @return the current {@link PaginationContext} with pagination information captured after parsing a given input.
	 */
	PaginationContext getPaginationContext();

}
