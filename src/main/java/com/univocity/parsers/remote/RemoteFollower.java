/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.net.*;
import com.univocity.parsers.common.*;

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

	/**
	 * Creates a new LinkFollower
	 */
	protected RemoteFollower(S parentEntitySettings) {
		ArgumentUtils.notEmpty("Parent of remote follower", parentEntitySettings);
		this.entityList = (T) parentEntitySettings.getParentEntityList().newInstance();
		this.entitySettings = entityList.addEntitySettings(parentEntitySettings);
//		this.entitySettings.
		this.parserSettings = (R) entityList.getParserSettings();
		this.parentLinkFollower = (RemoteFollower) this.entitySettings.owner;
		this.entitySettings.owner = this;

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
	 * @return the base URL to resolve the remote address to be accessed by this remote follower.
	 */
	public final void setBaseUrl(UrlReaderProvider baseUrlReaderProvider) {
		this.baseUrl = baseUrlReaderProvider;
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
}