/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.common.*;

import java.net.*;
import java.util.*;

/**
 * An abstract class that allows {@link com.univocity.parsers.common.EntityParserInterface} implementations that work
 * with {@link RemoteEntitySettings} to access multiple pages of remote content that needs to parsed.
 *
 * @param <E> type of {@link RemoteEntitySettings} of a parser with support for pagination. A paginator is essentially
 *            an entity specifically configured and used for the purpose of retrieving more content from a current
 *            state (or page), if available.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RemoteParserSettings
 * @see PaginationContext
 * @see PaginationHandler
 */
public abstract class Paginator<E extends RemoteEntitySettings> {
	protected final E entitySettings;
	private int followCount = -1;
	private PaginationHandler paginationHandler;
	private boolean urlTestingEnabled = false;

	public static final String ENTITY_NAME = "*paginator*";
	public static final String CURRENT_PAGE = "currentPage";
	public static final String CURRENT_PAGE_NUMBER = "currentPageNumber";
	public static final String NEXT_PAGE = "nextPage";
	public static final String NEXT_PAGE_NUMBER = "nextPageNumber";

	public static Set<String> RESERVED_NAMES = Collections.unmodifiableSet(
			new TreeSet<String>(Arrays.asList(CURRENT_PAGE, CURRENT_PAGE_NUMBER, NEXT_PAGE, NEXT_PAGE_NUMBER)));

	/**
	 * Creates a new {@code Paginator}
	 */
	protected Paginator(RemoteParserSettings parserSettings) {
		entitySettings = newEntitySettings(parserSettings);
	}

	/**
	 * Internally, the {@code Paginator} uses an instance of {@link RemoteEntitySettings} that should allow
	 * the definition of fields specifically to control the available pagination elements found in the parsed content.
	 *
	 * @return a new instance of a concrete implementation of {@link RemoteEntitySettings}, used to configure all
	 * pagination-related elements.
	 */
	protected abstract E newEntitySettings(RemoteParserSettings parserSettings);


	/**
	 * Sets the number of pages this {@code Paginator} can go up to.
	 *
	 * <i>Defaults to {@code -1} (no limit)</i>
	 *
	 * @param followCount the maximum number of pages that should be visited from a given starting point.
	 */
	public final void setFollowCount(int followCount) {
		Args.positiveOrZero(followCount, "Follow count");
		this.followCount = followCount;
	}

	/**
	 * Returns the number of pages this {@code Paginator} can go up to.
	 *
	 * <i>Defaults to {@code 0} (no limit)</i>
	 *
	 * @return the maximum number of pages that should be visited from a given starting point.
	 */
	public final int getFollowCount() {
		return followCount;
	}

	/**
	 * Sets the {@link PaginationHandler} which is used to prepare the call to the next page when the parser runs.
	 * Users can provide their {@link PaginationHandler} to obtain information about the pagination process through a
	 * {@link PaginationContext}, and if required, to manipulate the remote call used to fetch the next page.
	 *
	 * @param paginationHandler the {@link PaginationHandler} that will be associated with this {@code Paginator}
	 */
	public final void setPaginationHandler(PaginationHandler paginationHandler) {
		this.paginationHandler = paginationHandler;
	}

	/**
	 * Returns the {@link PaginationHandler} associated with thie {@code Paginator}.
	 * The {@link PaginationHandler} is used to prepare the call to the next page when the parser runs.
	 * Users can provide their {@link PaginationHandler} to obtain information about the pagination process through a
	 * {@link PaginationContext}, and if required, to manipulate the remote call used to fetch the next page.
	 *
	 * @return the {@link PaginationHandler} associated with this {@code Paginator}
	 */
	public final PaginationHandler getPaginationHandler() {
		return paginationHandler;
	}

	/**
	 * Returns the name of all fields associated with the paginator. The definition how this {@code Paginator} fields
	 * are populated is delegated to concrete implementations of this class.
	 *
	 * @return a unmodifiable, ordered {@link LinkedHashSet} of field names available from this {@code Paginator}.
	 */
	public final Set<String> getFieldNames() {
		return entitySettings.getFieldNames();
	}

	/**
	 * Indicates whether this paginator should test URLs pointing to the next page before actually retrieving it.
	 * Testing occurs every time {@link PaginationContext#getNextPage()} returns content that might be a relative or
	 * absolute URL pointing to the next page.
	 *
	 * URL testing consists of executing a {@code HEAD} {@link com.univocity.api.net.HttpRequest} call to the next page
	 * URL. If the response code returned by the remote server is {@code 200} (i.e. {@link HttpURLConnection#HTTP_OK})
	 * then the next page URL can be used, otherwise pagination will stop.
	 *
	 * <i>Defaults to {@code false}</i>
	 *
	 * @return a flag indicating whether the paginator will test URLs formed to capture the next page
	 * before actually attempting to fetch it.
	 */
	public boolean isUrlTestingEnabled() {
		return urlTestingEnabled;
	}

	/**
	 * Configures this paginator to test URLs pointing to the next page before actually retrieving it. Testing occurs
	 * every time {@link PaginationContext#getNextPage()} returns content that might be a relative or absolute URL
	 * pointing to the next page.
	 *
	 * URL testing consists of executing a {@code HEAD} {@link com.univocity.api.net.HttpRequest} call to the next page
	 * URL. If the response code returned by the remote server is {@code 200} (i.e. {@link HttpURLConnection#HTTP_OK})
	 * then the next page URL can be used, otherwise pagination will stop.
	 *
	 * <i>Defaults to {@code false}</i>
	 *
	 * @param urlTestingEnabled flag indicating whether the paginator should test URLs formed to capture the next page
	 *                          before actually attempting to fetch it.
	 */
	public void setUrlTestingEnabled(boolean urlTestingEnabled) {
		this.urlTestingEnabled = urlTestingEnabled;
	}
}
