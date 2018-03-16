/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.common.*;
import com.univocity.api.net.*;
import com.univocity.parsers.common.*;

import java.util.*;

/**
 * An abstract class that allow parsers that use {@link RemoteParserSettings} and {@link RemoteEntityList} to access and
 * parse linked pages. Linked page locations are generated with link following fields based on the content of the page.
 *
 * @param <S> Type of entity stored in {@link RemoteEntityList} associated with the RemoteLinkFollower.
 * @param <T> The type of {@link RemoteEntityList} that contains the entities used to parse a linked page. A RemoteLinkFollower
 *            can be thought of as a special type of {@link RemoteEntityList} that is used on linked pages.
 * @param <R> The type of settings that configures a RemoteParser
 */
public abstract class RemoteFollower<S extends RemoteEntitySettings, T extends RemoteEntityList<S>, R extends RemoteParserSettings> implements Cloneable, CommonFollowerOptions {
	protected T entityList;
	protected R parserSettings;
	protected S entitySettings;
	protected final RemoteFollower parentLinkFollower;
	private UrlReaderProvider baseUrl;

	private Nesting nesting = null;
	private Boolean ignoreLinkFollowingErrors = null;
	protected NextInputHandler<RemoteContext> nextLinkHandler;
	boolean stopped;

	boolean parentHasDate;

	protected TreeMap<String, ValueGetter<?>> urlParameters;

	/**
	 * Creates a new LinkFollower
	 * Uses the {@code parentEntitySettings} as a basis for the LinkFollower settings
	 *
	 * @param parentEntitySettings the parent entity settings used as a basis for LinkFollower settings
	 */
	protected RemoteFollower(S parentEntitySettings) {
		ArgumentUtils.notEmpty("Parent of remote follower", parentEntitySettings);
		this.entityList = (T) parentEntitySettings.getParentEntityList().newInstance();
		this.entitySettings = entityList.addEntitySettings(parentEntitySettings);
		this.parserSettings = (R) entityList.getParserSettings();
		this.parentLinkFollower = (RemoteFollower) this.entitySettings.owner;
		this.entitySettings.owner = this;
		this.urlParameters = new TreeMap<String, ValueGetter<?>>();
		ParameterizedString paramString = parserSettings.getParameterizedFileName();
//		parentHasDate = paramString.contains("date");
		parserSettings.setFileNamePattern("{parent}/file_{follower}");
	}

	/**
	 * Returns the list of entities available from this remote follower
	 *
	 * @return the list of entities available from this remote follower
	 */
	public final T getEntityList() {
		return entityList;
	}

	/**
	 * Adds a new entity to this remote follower if it doesn't exist and returns its configuration. The global settings
	 * made for the parser will be used by default. You can configure your entity to use different settings if required.
	 *
	 * @param entityName name of the entity whose configuration that will be returned.
	 *
	 * @return an existing or new entity configuration associated with the given entity name
	 */
	public final S addEntity(String entityName) {
		S out = entityList.configureEntity(entityName, entitySettings);
		out.owner = this;
		return out;
	}

	/**
	 * Returns the settings object associated with the remote follower. This configuration object is used to configure the
	 * remote follower when it parses a linked page.
	 *
	 * @return the remote follower's associated settings object
	 */
	public final R getParserSettings() {
		return parserSettings;
	}

	/**
	 * Returns the URL the remote follower should work with. Used for processing links that point to other remote hosts, and
	 * to ensure that relative resource paths are resolved against the given base URL.
	 *
	 * @return the base URL to resolve the remote address to be accessed by this remote follower.
	 */
	public final UrlReaderProvider getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Defines a base URL for the remote follower to run. Used for processing links that point to other remote hosts, and
	 * to ensure that relative resource paths are resolved against the given base URL.
	 *
	 * @param baseUrlReaderProvider the new baseUrl for processing links.
	 */
	public final void setBaseUrl(UrlReaderProvider baseUrlReaderProvider) {
		this.baseUrl = baseUrlReaderProvider;
	}

	/**
	 * Assign the parameter in the Url with the name {@code parameterName} to the value {@code parameterValue}
	 *
	 * @param parameterName  name of the parameter
	 * @param parameterValue value the parameter should hold
	 *
	 * @return this {@link RemoteFollower} to allow for method chaining
	 */
	public RemoteFollower assigning(String parameterName, Object parameterValue) {
		Args.notNull(parameterValue, parameterName);
		baseUrl.getRequest().setUrlParameter(parameterName, parameterValue);
		return this;
	}

	/**
	 * Assign the parameter in the Url with the name {@code ParameterName} to the value supplied by the {@link ValueGetter}
	 *
	 * @param parameterName name of the parameter in the Url to assign to
	 * @param valueGetter   the {@link ValueGetter} that will provide the value to be used as the parameter
	 *
	 * @return this {@link RemoteFollower} to allow for method chaining
	 */
	public RemoteFollower assigning(String parameterName, ValueGetter<?> valueGetter) {
		this.urlParameters.put(parameterName, valueGetter);
		return this;
	}

	@Override
	public final void ignoreFollowingErrors(boolean ignoreLinkFollowingErrors) {
		this.ignoreLinkFollowingErrors = ignoreLinkFollowingErrors;
	}

	@Override
	public final boolean isIgnoreFollowingErrors() {
		if (ignoreLinkFollowingErrors == null) {
			if (parentLinkFollower != null) {
				return parentLinkFollower.isIgnoreFollowingErrors();
			}
			return entitySettings.isIgnoreFollowingErrors();

		}
		return ignoreLinkFollowingErrors;
	}

	@Override
	public final Nesting getNesting() {
		if (nesting == null) {
			if (parentLinkFollower != null) {
				return parentLinkFollower.getNesting();
			}
			return entitySettings.getNesting();
		}
		return nesting;
	}

	@Override
	public final void setNesting(Nesting nesting) {
		this.nesting = nesting;
	}

	@Override
	public String toString() {
		return ">>" + entitySettings.getEntityName();
	}

	/**
	 * Gets the next link handler.
	 * Which will have the {@link NextInputHandler#prepareNextCall(RemoteContext)} method called before the
	 * next remote page is fetched.
	 *
	 * @return the next link handler.
	 */
	public NextInputHandler<RemoteContext> getNextLinkHandler() {
		return nextLinkHandler;
	}

	/**
	 * Sets the next link handler which will have the {@link NextInputHandler#prepareNextCall(RemoteContext)} method
	 * called before the next remote page is fetched.
	 * For example this could be used to modify the HTTP request configuration.
	 *
	 * @param nextLinkHandler the new link handler.
	 */
	public void setNextLinkHandler(NextInputHandler<RemoteContext> nextLinkHandler) {
		this.nextLinkHandler = nextLinkHandler;
	}


}
