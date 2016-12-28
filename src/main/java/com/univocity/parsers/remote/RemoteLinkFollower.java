/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.net.*;

/**
 * An abstract class that allow parsers that use {@link RemoteParserSettings} and {@link RemoteEntityList} to access and
 * parse linked pages. Linked page locations are generated with link following fields based on the content of the page.
 *
 * @param <S> Type of entity stored in {@link RemoteEntityList} associated with the RemoteLinkFollower.
 * @param <T> The type of {@link RemoteEntityList} that contains the entities used to parse a linked page. A RemoteLinkFollower
 *            can be thought of as a special type of {@link RemoteEntityList} that is used on linked pages.
 * @param <R> The type of settings that configures a RemoteParser
 */
public abstract class RemoteLinkFollower<S extends RemoteEntitySettings, T extends RemoteEntityList<S>, R extends RemoteParserSettings> implements Cloneable {
	protected T entityList;
	protected R parserSettings;
	protected S parentEntitySettings;
	private UrlReaderProvider baseUrl;

	private boolean ignoreLinkFollowingErrors = true;

	/**
	 * Creates a new LinkFollower
	 */
	protected RemoteLinkFollower(S parentEntitySettings) {
		entityList = (T) parentEntitySettings.getParentEntityList().newInstance();
		this.parentEntitySettings = entityList.addEntitySettings(parentEntitySettings);
		parserSettings = (R) entityList.getParserSettings();

		/*
			we need to remove link followers or else previous link followers created in parent entity list
			will be run inside this link follower
		*/
//		removeLinkFollowers();
	}

	private void removeLinkFollowers() {
		for (RemoteEntitySettings entitySettings : entityList) {
			if (!entitySettings.getEntityName().equals(Paginator.ENTITY_NAME)) {
				entitySettings.clearLinkFollowers();
			}
		}

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

	/**
	 * If set to false, the parser will throw an Exception when the parser tries to follow a link that is invalid or
	 * malformed. Set to true, the parser will simply ignore following an invalid or malformed link.
	 *
	 * @param ignoreLinkFollowingErrors true if the parser will ignore errors when accessing linked page, false otherwise.
	 */
	public void setIgnoreLinkFollowingErrors(boolean ignoreLinkFollowingErrors) {
		this.ignoreLinkFollowingErrors = ignoreLinkFollowingErrors;
	}

	/**
	 * Returns if the parser will ignore invalid or malformed link following urls.
	 *
	 * @return if the parser is set to ignore errors when accessing linked page
	 */
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

	@Override
	protected RemoteLinkFollower<S,T,R> clone() {
		try {
			RemoteLinkFollower<S, T, R> out = (RemoteLinkFollower<S, T, R>) super.clone();
//			out.entityList = (T) entityList.clone();
			out.parserSettings = (R) parserSettings.clone();
//			out.parentEntitySettings = (S) parentEntitySettings.clone();
//			out.baseUrl = baseUrl.clone();
			return out;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}
}
