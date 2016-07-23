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

	public RemoteResourceEntity() {
		this.entityName = null;
	}

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
	 * Returns the name of all fields associated with the HtmlEntity.
	 *
	 * @return a String array of the field names
	 */
	abstract public String[] getFieldNames();


	public final Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	public final void setRequestParameter(String parameterName, String value) {
		requestParameters.put(parameterName, value);

	}
}

