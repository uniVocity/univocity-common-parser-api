/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.io.*;
import com.univocity.api.net.*;

/**
 * Contains information about the current results obtained by the parser, including the remote HTTP response obtained,
 * and the next HTTP request to be performed by the parser. This is made available to the user through the
 * {@link NextInputHandler} callback.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RemoteFollower
 * @see Paginator
 * @see NextInputHandler
 * @see UrlReaderProvider
 */
public interface RemoteContext {

	/**
	 * Returns the value parsed from the input that indicates the current page, or {@code null} if not available.
	 *
	 * @return the content extracted from the input that provides information about the current page.
	 */
	String getCurrentPage();

	/**
	 * Returns the value parsed from the input that indicates the current page number. If undefined, and
	 * {@link #getCurrentPage()} returns a valid numeric {@code String} it will be converted to an {@code int}
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
	 * Returns the value parsed from the input, converted to a valid {@code int} number that indicates the next
	 * page number. If undefined, and {@link #getNextPage()} returns a valid numeric {@code String} it will be converted
	 * to an {@code int} number that indicates the next page. If the information is unparseable/unavailable,
	 * {@code -1} will be returned.
	 *
	 * @return the next page number if available from the input, {@code -1} otherwise.
	 */
	int getNextPageNumber();

	/**
	 * Returns a flag indicating whether a next page is available from the current page. It tests if
	 * {@link #getNextPage()} returns a non-null value OR {@link #getNextPageNumber()} returns a non-negative number and
	 * returns {@code true}. If no information about a next page is available, {@code false} will be returned
	 *
	 * @return {@code true} if information about a next page is available, otherwise {@code false}
	 */
	boolean hasMorePages();

	/**
	 * Returns the number of the pages visited so far by the current {@link Paginator}.
	 *
	 * @return the current page count.
	 */
	int getPageCount();

	/**
	 * Returns the {@link HttpResponse} object with all information returned by the remote server
	 * in its HTTP response message (which generated the current page).
	 *
	 * @return the {@link HttpResponse} received for the current page. Will be {@code null} if the
	 * process is running over local files.
	 */
	HttpResponse getCurrentResponse();

	/**
	 * Returns the {@link UrlReaderProvider} prepared by the parser to access the next URL. It inherits all configuration
	 * options defined in the call to the current page. Cookies set in the {@link HttpResponse} of the current page
	 * are automatically set into this request.
	 * You can alter its configuration before its {@link HttpRequest} is executed to
	 * fetch the next page.
	 *
	 * @return the {@link UrlReaderProvider} which will be used to execute a new HTTP request. Will be {@code null} if the
	 * process is running over local files.
	 */
	UrlReaderProvider getNextRequest();

	/**
	 * Stops the attempt to read the next input page.
	 */
	void stop();

	/**
	 * Returns the {@link RateLimiter} used by the parser to prevent multiple concurrent requests against the same
	 * server if {@link RemoteParserSettings#getRemoteInterval()} returns a positive number. Otherwise returns {@code null}
	 *
	 * You can decrease or increase the wait time with {@link RateLimiter#increaseWaitTime(long)} and
	 * {@link RateLimiter#decreaseWaitTime(long)}
	 *
	 * @return the active {@link RateLimiter} or {@code null} if {@link RemoteParserSettings#getRemoteInterval()}
	 * is {@code <= 0}
	 */
	RateLimiter getRateLimiter();
}
