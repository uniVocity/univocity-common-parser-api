package com.univocity.api.common.remote;

import com.univocity.api.common.*;

import java.util.*;

/**
 * Used by the {@link HtmlParser} to follow pages on a website.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 *
 * @see HtmlParser
 * @see PaginationContext
 * @see PaginationHandler
 */
public abstract class RemoteResourcePaginator<C extends RemoteResourceEntity> {
	protected C entity; //public temporarily
	protected int followCount;
	protected int idealPageSize;
	protected int currentPageNumber;
	protected PaginationHandler paginationHandler;
	protected static String entityName = "*paginator*";

	/**
	 * Creates a new HtmlPaginator and sets the currentPageNumber to 0
	 */
	public RemoteResourcePaginator() {
		currentPageNumber = 0;
		entity = newEntity();
	}

	public abstract C newEntity();


	/**
	 * Returns the name of the paginator
	 *
	 * @return the name of the paginator
	 */
	public String getEntityName() {
		return entity.getEntityName();
	}

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
	 *
	 * Sets a request parameter to a specfic value. This differs from {@link RemoteResourcePaginator#addRequestParameter(String)}
	 * as it does not create a field in the entity, therefore not returning a path. It is generally used during
	 * the parsing process as
	 *
	 * @param fieldName the name of the request parameter
	 * @param value the string that will be associated with the request parameter
	 */
	public void setRequestParameter(String fieldName, String value) {
		entity.setRequestParameter(fieldName, value);
	}

	/**
	 * Returns the request parameters as a Map<String, String>, where the key is the request parameter name and the value
	 * is the parsed String from the HTML page.
	 *
	 * @return the request parameters as a Map
	 */
	public Map<String, String> getRequestParameters() {
		return entity.getRequestParameters();
	}


	/**
	 * Returns the field names used by the Paginator
	 *
	 * @return a String array of field names
	 */
	public String[] getFieldNames() {
		return entity.getFieldNames();
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
	 * runs. If not set, will use DefaultPaginationHandler
	 *
	 * @param paginationHandler the {@link PaginationHandler} that will be associated with the paginator
	 */
	public void setPaginationHandler(PaginationHandler paginationHandler) {
		this.paginationHandler = paginationHandler;
	}

	public PaginationHandler getPaginationHandler() {
		return paginationHandler;
	}
}
