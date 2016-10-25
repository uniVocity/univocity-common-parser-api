/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.net.*;

import java.util.*;

/**
 * Contains information about the pagination process managed by a {@link Paginator} and made available to the user
 * through the {@link PaginationHandler} callback.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see Paginator
 * @see PaginationHandler
 * @see UrlReaderProvider
 */
public interface PaginationContext {

	/**
	 * Returns the value parsed from the input that points to the next page.
	 *
	 * @return the content extracted from the input that provides information about the next page.
	 */
	String getNextPage();

	/**
	 * Returns the value parsed from the input that points to the previous page.
	 *
	 * @return the content extracted from the input that provides information about the previous page.
	 */
	String getPreviousPage();

	/**
	 * Returns the value parsed from the input that points to the first page.
	 *
	 * @return the content extracted from the input that provides information about the first page.
	 */
	String getFirstPage();

	/**
	 * Returns the value parsed from the input that points to the last page.
	 *
	 * @return the content extracted from the input that provides information about the last page.
	 */
	String getLastPage();


	/**
	 * Returns the value parsed from the input that indicates the current page size.
	 *
	 * @return the content extracted from the input that provides information about the current page size.
	 */
	String getPageSize();

	/**
	 * Returns the value parsed from the input that indicates the number of items available in the current page.
	 *
	 * @return the content extracted from the input that provides information about the number of items available
	 * in the current page.
	 */
	String getItemCount();

	/**
	 * Returns the value parsed from the input and associated with a user provided field of the {@link Paginator}.
	 *
	 * @param fieldName name of the user-provided field associated with the {@link Paginator}
	 *
	 * @return the content extracted from the input for the given field name.
	 */
	String readField(String fieldName);

	/**
	 * Returns the available field names available from the current {@link Paginator} implementation.
	 *
	 * @return a set of field names bound to the paginator.
	 */
	Set<String> getFieldNames();

	/**
	 * Returns the number of the page being visited by current {@link Paginator}.
	 *
	 * @return the current page number.
	 */
	int getCurrentPageNumber();

}
