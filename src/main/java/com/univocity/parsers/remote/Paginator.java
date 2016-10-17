/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import java.util.*;

/**
 * A abstract class that allows {@link RemoteResourceParser}'s to access and parse a sequence of pages
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RemoteResourceParser
 * @see PaginationContext
 * @see PaginationHandler
 */
public abstract class Paginator<E extends RemoteEntitySettings> {
	protected final E entitySettings;
	protected int followCount;
	protected int idealPageSize;
	protected int currentPageNumber;
	protected PaginationHandler paginationHandler;
	public static String ENTITY_NAME = "*paginator*";


	/**
	 * Creates a new HtmlPaginator and sets the currentPageNumber to 0
	 */
	protected Paginator() {
		currentPageNumber = 0;
		entitySettings = newEntitySettings();
	}

	protected abstract E newEntitySettings();

	/**
	 * Sets the ideal page size. The ideal page size is a number that the paginator will try to set the page size to.
	 *
	 * @param pageSize a number that is used to define the ideal page size
	 */
	public void setIdealPageSize(int pageSize) {
		this.idealPageSize = pageSize;
	}

	/**
	 * Returns the ideal page size. The ideal page size is a number that the paginator will try to set the page size to.
	 *
	 * @return the ideal page size
	 */
	public int getIdealPageSize() {
		return idealPageSize;
	}


	/**
	 * Sets the amount of times that the {@link HtmlParser} will go to the next page.
	 *
	 * @param followCount the number of pages that will be visited
	 */
	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}

	/**
	 * Returns the amount of times the {@link HtmlParser} will go to the next page.
	 *
	 * @return the number of pages that will be visited
	 */
	public int getFollowCount() {
		return followCount;
	}


	/**
	 * Returns the page number that the Paginator is currently up to
	 *
	 * @return the current page number of the paginator
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	/**
	 * Sets what page number the Paginator is up to.
	 *
	 * @param currentPageNumber the page number that the paginator will be set to.
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * Sets the {@link PaginationHandler} which is used to prepare the call to the next page when the {@link HtmlParser}
	 * runs. If not set, will use DefaultPaginationHandler.
	 *
	 * @param paginationHandler the {@link PaginationHandler} that will be associated with the paginator
	 */
	public void setPaginationHandler(PaginationHandler paginationHandler) {
		this.paginationHandler = paginationHandler;
	}

	/**
	 * Returns the {@link PaginationHandler} associated with the Paginator
	 *
	 * @return the associated {@link PaginationHandler}
	 */
	public PaginationHandler getPaginationHandler() {
		return paginationHandler;
	}

	public Set<String> getFieldNames() {
		return entitySettings.getFieldNames();
	}
}
