/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.net.*;
import com.univocity.parsers.common.record.*;

import java.io.*;
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
	 * Returns the value parsed from the input and associated with a user provided fields and request parameters of
	 * the {@link Paginator}.
	 *
	 * @param fieldName name of the user-provided field associated with the {@link Paginator}
	 *
	 * @return the content extracted from the input for the given field name.
	 */
	String readField(String fieldName);

	/**
	 * Returns the values parsed from the input and associated with the given user provided fields
	 * and request parameters of the {@link Paginator}.
	 *
	 * @param fieldNames names of the user-provided field associated with the {@link Paginator}. If empty
	 *                   the values of all fields will be returned.
	 *
	 * @return the content extracted from the input for the given field names.
	 */
	String[] readFields(String... fieldNames);

	/**
	 * Returns the available field names available from the current {@link Paginator} implementation.
	 *
	 * @return a set of field names bound to the paginator.
	 */
	Set<String> getFieldNames();


	/**
	 * Returns a {@link Record} with the values parsed from the input and associated with the given user provided fields
	 * and request parameters of the {@link Paginator}.
	 *
	 * @return the content extracted from the input as a {@link Record}
	 */
	Record getRecord();

	/**
	 * Returns the number of the page being visited by current {@link Paginator}.
	 *
	 * @return the current page number.
	 */
	int getCurrentPageNumber();

	/**
	 * Returns the local file which will contain the data of the next page to be visited,
	 * if reading from a remote location. If the parsing process is running on a set of already downloaded files,
	 * returns the file that is expected to contain the information of the next page.
	 *
	 * @return the file with the next page of data.
	 */
	File getNextPageFile();

	/**
	 * Stops the pagination and prevents attempts to read more pages.
	 */
	void stopPagination();

}
