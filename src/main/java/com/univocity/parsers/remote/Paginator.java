/*
 * Copyright (c) 2013 Univocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.common.*;
import com.univocity.parsers.common.*;

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
 * @author Univocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RemoteParserSettings
 * @see PaginationContext
 * @see NextInputHandler
 */
public abstract class Paginator<E extends RemoteEntitySettings, P extends PaginationContext> {
	protected final E entitySettings;
	private int followCount = -1;
	private NextInputHandler<P> paginationHandler;
	private boolean urlTestingEnabled = false;

	/**
	 * Reserved field name used by the paginator to store itself as an entity
	 */
	public static final String ENTITY_NAME = "*paginator*";
	/**
	 * Reserved field name used by the paginator to store the current page matching entity
	 */
	public static final String CURRENT_PAGE = "currentPage";
	/**
	 * Reserved field name used by the paginator to store the current page number
	 */
	public static final String CURRENT_PAGE_NUMBER = "currentPageNumber";
	/**
	 * Reserved field name used by the paginator to store the next page matching entity
	 */
	public static final String NEXT_PAGE = "nextPage";
	/**
	 * Reserved field name used by the paginator to store the next page number
	 */
	public static final String NEXT_PAGE_NUMBER = "nextPageNumber";

	static Set<String> RESERVED_NAMES = Collections.unmodifiableSet(
			new TreeSet<String>(Arrays.asList(CURRENT_PAGE, CURRENT_PAGE_NUMBER, NEXT_PAGE, NEXT_PAGE_NUMBER)));

	/**
	 * Creates a new {@code Paginator}
	 *
	 * @param parserSettings the parser settings to use
	 */
	protected Paginator(RemoteParserSettings parserSettings) {
		entitySettings = newEntitySettings(parserSettings);
		entitySettings.setNesting(Nesting.LINK);
	}

	/**
	 * Internally, the {@code Paginator} uses an instance of {@link RemoteEntitySettings} that should allow
	 * the definition of fields specifically to control the available pagination elements found in the parsed content.
	 *
	 * @param parserSettings the parser settings to use in the new Entity settings
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
	 * Sets the {@link NextInputHandler} which is used to prepare the call to the next page when the parser runs.
	 * Users can provide their {@link NextInputHandler} to obtain information about the pagination process through a
	 * {@link PaginationContext}, and if required, to manipulate the remote call used to fetch the next page.
	 *
	 * @param paginationHandler the {@link NextInputHandler} that will be associated with this {@code Paginator}
	 */
	public final void setPaginationHandler(NextInputHandler<P> paginationHandler) {
		this.paginationHandler = paginationHandler;
	}

	/**
	 * Returns the {@link NextInputHandler} associated with thie {@code Paginator}.
	 * The {@link NextInputHandler} is used to prepare the call to the next page when the parser runs.
	 * Users can provide their {@link NextInputHandler} to obtain information about the pagination process through a
	 * {@link PaginationContext}, and if required, to manipulate the remote call used to fetch the next page.
	 *
	 * @return the {@link NextInputHandler} associated with this {@code Paginator}
	 */
	public NextInputHandler<P> getPaginationHandler() {
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
