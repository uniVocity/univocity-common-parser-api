/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;


import com.univocity.api.common.*;

/**
 * Callback class used by a {@link Paginator} to determine how the next page of content should be accessed.
 *
 * Information about the pagination process is provided by a {@link PaginationContext}, through the invocation of
 * the {@link #prepareCallToNextPage(UrlReaderProvider, PaginationContext)} method. Users can manipulate the remote request to
 * control how the next page should be fetched by modifying the {@link UrlReaderProvider} configuration.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see Paginator
 * @see PaginationContext
 */
public interface PaginationHandler {

	/**
	 * Method invoked by the parser internally when a {@link Paginator} is defined and new pages of content have been
	 * identified in the input. Users can use this method to prepare the call to the next page and to obtain more
	 * information about the pagination process
	 *
	 * @param urlReaderProvider a {@link UrlReaderProvider} prepared by the parser to access the next page.
	 *                          Users can alter its configuration before the {@link HttpRequest} is processed.
	 * @param paginationContext the {@link PaginationContext} used to get information from the pagination process.
	 */
	void prepareCallToNextPage(UrlReaderProvider urlReaderProvider, PaginationContext paginationContext);
}
