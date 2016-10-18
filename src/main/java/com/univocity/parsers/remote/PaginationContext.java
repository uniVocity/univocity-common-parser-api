/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.common.*;

import java.util.*;

/**
 *  Contains information about the pagination process.
 *
 *  @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 *
 *  @see HtmlPaginator
 *  @see UrlReaderProvider
 */
public interface PaginationContext {

	/**
	 * Returns the value parsed from the next page path.
	 *
	 * @return String from html element specified by the next page path
	 */
	String getNextPage();

	/**
	 * Returns the value parsed from the last page path.
	 *
	 * @return String from html element specified by the last page path
	 */
	String getLastPage();

	/**
	 * Returns the value parsed from the previous page path.
	 *
	 * @return String from html element specified by the previous page path
	 */
	String getPreviousPage();

	/**
	 * Returns the value parsed from the page size path.
	 *
	 * @return String from html element specified by the page size path
	 */
	String getPageSize();

	/**
	 * Returns the value parsed from the item count path.
	 *
	 * @return String from html element specified by the first page path
	 */
	String getItemCount();

	/**
	 * Returns the value parsed from the first page path.
	 *
	 * @return String from html element specified by the first page path
	 */
	String getFirstPage();

	String getField(String fieldName);

	Set<String> getRequestParameterNames();

	/**
	 * Returns the ideal page size that the paginator is set at.
	 *
	 * @return a number defining the ideal page size
	 */
	int getIdealPageSize();

	/**
	 * Sets the page size that the paginator will set the web page size to.
	 *
	 * @param idealPageSize
	 */
	void setIdealPageSize(int idealPageSize);

	/**
	 * Returns the associated {@link UrlReaderProvider}
	 *
	 * @return the associated {@link UrlReaderProvider}
	 */
	ReaderProvider getReaderProvider();

	/**
	 * Returns a String array of field names used by the paginator. Field names are set via setting requestParameters or
	 * using the other setters such as {@link HtmlPaginator#setNextPage()}
	 *
	 * @return a string array of field names.
	 */
	Set<String> getFieldNames();

	int getCurrentPageNumber();

}
