/*
 * Copyright (c) 2013 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;


/**
 * Callback class used to determine how the remote content should be accessed.
 *
 * Used to provide information about pagination and link following processes, provided by a {@link RemoteContext},
 * through the invocation of the {@link #prepareNextCall(RemoteContext)} method. Users can for example manipulate
 * the remote request to control how the next page should be fetched by modifying the HTTP request configuration before
 * accessing the next page of data.
 *
 * @author Univocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RemoteContext
 * @see RemoteFollower
 * @see Paginator
 * @see PaginationContext
 */
public interface NextInputHandler<C extends RemoteContext> {

	/**
	 * Method invoked by the parser when a new HTTP request to fetch a remote resource must be performed. Users can
	 * use this method to prepare the remote call and to obtain more information about the process.
	 *
	 * @param remoteContext the {@link RemoteContext} used to get information from the current and the next remote HTTP request.
	 */
	void prepareNextCall(C remoteContext);
}
