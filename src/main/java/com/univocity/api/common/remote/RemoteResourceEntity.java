/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.api.common.remote;


import java.util.*;

/**
 * Created by anthony on 21/07/16.
 */
public abstract class RemoteResourceEntity {
	protected final String entityName;

	protected final TreeMap<String, String> requestParameters = new TreeMap<String, String>();

	/**
	 * Creates a new RemoteResourceEntity with a null name.
	 */
	public RemoteResourceEntity() {
		this.entityName = null;
	}

	/**
	 * Creates a new RemoteResouceEntity and associates it with the specified name.
	 *
	 * @param entityName the name that will identify the entity
	 */
	public RemoteResourceEntity(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Returns the name of the HTMLEntity
	 *
	 * @return the name as a String
	 */
	public final String getEntityName() {
		return entityName;
	}

	/**
	 * Returns the name of all fields associated with the RemoteResourceEntity. Fields are associated when any of the field adding
	 * methods are run.
	 *
	 * @return a String array of the field names
	 */
	abstract public String[] getFieldNames();


	/**
	 * Returns a map of request parameter names and values.
	 *
	 * @return request parameter names and values as a map
	 */
	public final Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	/**
	 * Creates a new request parameter.
	 *
	 * @param parameterName the name of the parameter
	 * @param value the value associated with the parameter
	 */
	public final void setRequestParameter(String parameterName, String value) {
		requestParameters.put(parameterName, value);

	}

	/**
	 * Removes a field from the RemoteResourceEntity. Removed fields will not be used by the parser.
	 *
	 * @param fieldName the name of the field that will be removed.
	 */
	abstract public void removeField(String fieldName);

	public String toString() {
		return entityName;
	}
}

