/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.net.*;

import java.util.*;

/**
 * Created by anthony on 20/07/16.
 */
public abstract class RemoteLinkFollower<S extends RemoteEntitySettings, T extends RemoteEntityList<S>, R extends RemoteParserSettings> {
	protected T entityList;
	protected S defaultEntitySettings;
	protected R parserSettings;
	public static String ENTITY_NAME = "*linkFollower*";
	private UrlReaderProvider baseUrlReaderProvider;

	/**
	 * Creates a new LinkFollower
	 */
	protected RemoteLinkFollower() {
		entityList = newEntityList();
		defaultEntitySettings = entityList.configureEntity(ENTITY_NAME);
		parserSettings = newParserSettings();
	}

	protected abstract T newEntityList();

	protected abstract R newParserSettings();

	/**
	 * Returns the field names associated with the linkFollower
	 *
	 * @return a string array of field names
	 */
	public final Set<String> getFieldNames() {
		return defaultEntitySettings.getFieldNames();
	}

	public final S getDefaultEntitySettings() {
		return defaultEntitySettings;
	}

	public final T getEntityList() {
		return entityList;
	}

	public Map<String, ? extends RemoteLinkFollower<S, T, R>> getLinkFollowers() {
		return defaultEntitySettings.getLinkFollowers();
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

	public final UrlReaderProvider getBaseUrlReaderProvider() {
		return baseUrlReaderProvider;
	}

	public final void setBaseUrlReaderProvider(UrlReaderProvider baseUrlReaderProvider) {
		this.baseUrlReaderProvider = baseUrlReaderProvider;
	}

	@Override
	public String toString() {
		return "LinkFollower{" +
				"entities=" + entityList.getEntityNames() +
				", defaultEntitySettings=" + defaultEntitySettings +
				", baseUrlReaderProvider=" + baseUrlReaderProvider +
				'}';
	}
}
