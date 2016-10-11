/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;


/**
 * This class is used to determine how the next page will be accessed using pagination. To see a concrete implementation
 * see {@link com.univocity.parsers.common.DefaultParsingContext}
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 *
 * @see HtmlPaginator
 * @see PaginationContext
 */
public interface PaginationHandler {

	/**
	 * A method used to prepare the call to the next page.
	 *
	 * @param paginationContext a {@link PaginationContext} used to get information from the pagination process
	 */
	void prepareCallToNextPage(PaginationContext paginationContext);



}