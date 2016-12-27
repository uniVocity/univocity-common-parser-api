/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.net.*;

public abstract class RemoteLinkFollower<S extends RemoteEntitySettings, T extends RemoteEntityList<S>, R extends RemoteParserSettings> {
	protected T entityList;
	protected R parserSettings;
	protected S parentEntitySettings;
	private UrlReaderProvider baseUrl;

	private boolean ignoreLinkFollowingErrors = true;

	/**
	 * Creates a new LinkFollower
	 */
	protected RemoteLinkFollower(S parentEntitySettings) {
		entityList = (T) parentEntitySettings.getParentEntityList().clone();
		this.parentEntitySettings = entityList.addEntitySettings(parentEntitySettings);
		parserSettings = (R) entityList.getParserSettings();
	}

	public final T getEntityList() {
		return entityList;
	}

	public final S addEntity(String entityName) {
		return entityList.configureEntity(entityName);
	}

	/**
	 * Returns the settings object associated with the link follower. This configuration object is used to configure the
	 * link follower when it parses a linked page.
	 *
	 * @return the link follower's associated settings object
	 */
	public final R getParserSettings() {
		return parserSettings;
	}

	public final UrlReaderProvider getBaseUrl() {
		return baseUrl;
	}

	public final void setBaseUrl(UrlReaderProvider baseUrlReaderProvider) {
		this.baseUrl = baseUrlReaderProvider;
	}

	//FIXME: javadoc here (if this is to stay)
	public void setIgnoreLinkFollowingErrors(boolean ignoreLinkFollowingErrors) {
		this.ignoreLinkFollowingErrors = ignoreLinkFollowingErrors;
	}

	//FIXME: javadoc here (if this is to stay)
	public boolean isIgnoreLinkFollowingErrors() {
		return ignoreLinkFollowingErrors;
	}

	@Override
	public String toString() {
		return "RemoteLinkFollower{" +
				"entityList=" + entityList +
				", parserSettings=" + parserSettings +
				", parentEntitySettings=" + parentEntitySettings +
				", baseUrl=" + baseUrl +
				", ignoreLinkFollowingErrors=" + ignoreLinkFollowingErrors +
				'}';
	}
}
