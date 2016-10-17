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
public class PaginationContext {

	private final Paginator paginator;
	private final ReaderProvider readerProvider;

	/**
	 * Creates a new PaginationContext with a {@link HtmlPaginator} and a {@link UrlReaderProvider}.
	 * @param paginator
	 * @param urlReaderProvider
	 */
	public PaginationContext(Paginator paginator, ReaderProvider readerProvider) {
		this.paginator = paginator;
		this.readerProvider = readerProvider;
	}

	/**
	 * Returns the value parsed from the first page path.
	 *
	 * @return String from html element specified by the first page path
	 */
	public String getFirstPage() {
		return getField("firstPage");
	}

	/**
	 * Returns the value parsed from the next page path.
	 *
	 * @return String from html element specified by the next page path
	 */
	public String getNextPage() {
		return getField("nextPage");
	}

	/**
	 * Returns the value parsed from the last page path.
	 *
	 * @return String from html element specified by the last page path
	 */
	public String getLastPage() {
		return getField("lastPage");
	}

	/**
	 * Returns the value parsed from the previous page path.
	 *
	 * @return String from html element specified by the previous page path
	 */
	public String getPreviousPage() {
		return getField("previousPage");
	}

	/**
	 * Returns the value parsed from the page size path.
	 *
	 * @return String from html element specified by the page size path
	 */
	public String getPageSize() {
		return getField("pageSize");
	}

	/**
	 * Returns the value parsed from the item count path.
	 *
	 * @return String from html element specified by the first page path
	 */
	public String getItemCount() {
		return getField("itemCount");
	}

	private String getField(String fieldName) {
//
//		Map<String,String> map = paginator.getRequestParameters();
//		return  map.get(fieldName); //FIXME
		throw new IllegalStateException("fix me");
	}

	/**
	 * Returns the ideal page size that the paginator is set at.
	 *
	 * @return a number defining the ideal page size
	 */
	public int getIdealPageSize() {
		return paginator.getIdealPageSize();
	}

	/**
	 * Sets the page size that the paginator will set the web page size to.
	 *
	 * @param idealPageSize
	 */
	public void setIdealPageSize(int idealPageSize) {
		paginator.setIdealPageSize(idealPageSize);
	}

	/**
	 * Returns the associated {@link UrlReaderProvider}
	 *
	 * @return the associated {@link UrlReaderProvider}
	 */
	public ReaderProvider getReaderProvider() {
		return readerProvider;
	}

	/**
	 * Returns a String array of field names used by the paginator. Field names are set via setting requestParameters or
	 * using the other setters such as {@link HtmlPaginator#setNextPage()}
	 *
	 * @return a string array of field names.
	 */
	public Set<String> getFieldNames() {
		return paginator.getFieldNames();
	}

	/**
	 * Returns the number of the page that the paginator is currently up to
	 * @return the current page number
	 */
	public int getCurrentPageNumber() {
		return paginator.getCurrentPageNumber();
	}

}
