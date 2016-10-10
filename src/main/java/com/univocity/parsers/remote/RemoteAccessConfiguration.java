/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import java.util.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class RemoteAccessConfiguration {

	protected final TreeMap<String, String> requestParameters = new TreeMap<String, String>();


	public final void setRequestParameter(String paramName, String value) {
		requestParameters.put(paramName, value);
	}

	/**
	 * Returns the request parameters as a Map<String, String>, where the key is the request parameter name and the value
	 * is the parsed String from the HTML page.
	 *
	 * @return the request parameters as a Map
	 */
	public final Map<String, String> getRequestParameters() {
		return requestParameters;
	}
}
