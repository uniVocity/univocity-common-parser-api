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
	 * Returns the value parsed from the input that indicates the current page, or {@code null} if not available.
	 *
	 * @return the content extracted from the input that provides information about the current page.
	 */
	String getCurrentPage();

	/**
	 * Returns the value parsed from the input from {@link #getCurrentPage()}, and converted to a valid {@code int}
	 * number that indicates the current page. If the information is unparseable/unavailable, {@code -1} will be
	 * returned.
	 *
	 * @return the actual page number available from the input, or {@code -1} if unavailable.
	 */
	int getCurrentPageNumber();

	/**
	 * Returns the value parsed from the input that points to the next page. This is usually a link to the next page
	 * or simply a numeric value representing the next page.
	 *
	 * @return the content extracted from the input that provides information about the next page.
	 */
	String getNextPage();

	/**
	 * Returns the value parsed from the input from {@link #getNextPage()}, and converted to a valid {@code int}
	 * number that indicates the current page. If the information is unparseable/unavailable, {@code -1} will be
	 * returned.
	 *
	 * @return the next page number, available from the input, or {@code -1} if unavailable.
	 */
	int getNextPageNumber();

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
	 * @return the sequence of field names bound to the paginator.
	 */
	String[] getFieldNames();

	/**
	 * Returns the names of all request parameters configured in the current {@link Paginator} implementation.
	 *
	 * @return a sequence of request field names bound to the paginator.
	 */
	String[] getRequestParameterNames();

	/**
	 * Returns the request parameters collected by the paginator as a map of request parameter names and values.
	 * Note that request parameters can have multiple values assigned to the same name.
	 *
	 * @return a map of request parameter names and their values.
	 */
	Map<String, String[]> getRequestParameters();

	/**
	 * Returns a {@link Record} with the values parsed from the input and associated with the given user provided fields
	 * and request parameters of the {@link Paginator}.
	 *
	 * @return the content extracted from the input as a {@link Record}
	 */
	Record getRecord();

	/**
	 * Returns the output of {@link #getCurrentPageNumber()} if evaluates to a positive number, otherwise
	 * returns the number of the pages visited so far by the current {@link Paginator}.
	 *
	 * @return the current page count.
	 */
	int getPageCount();

	/**
	 * Returns the local file which will contain the data of the next page to be visited,
	 * if reading from a remote location. If the parsing process is running on a set of already downloaded files,
	 * returns the file that is expected to contain information of the next page.
	 *
	 * @return the file with the next page of data.
	 */
	File getNextPageFile();

	/**
	 * Stops the pagination and prevents attempts to read more pages.
	 */
	void stopPagination();


}
