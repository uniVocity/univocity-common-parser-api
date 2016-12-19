/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import java.util.*;

/**
 * Created by anthony on 20/07/16.
 */
public abstract class RemoteLinkFollower<S extends RemoteEntitySettings, T extends RemoteEntityList<S>, R extends RemoteParserSettings> {
	protected T entityList;
	protected S defaultEntitySettings;
	protected R parserSettings;
	public static String ENTITY_NAME = "*linkFollower*";
	protected int itemCount;
	protected int linkNum;


	/**
	 * Creates a new LinkFollower
	 */
	public RemoteLinkFollower() {
		entityList = newEntityList();
		defaultEntitySettings = entityList.configureEntity(ENTITY_NAME);
		parserSettings = newParserSettings();
		itemCount = 0;
		linkNum = 1;
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

	/**
	 * Sets the number of times the link follower has followed a link
	 *
	 * @param itemCount LinkFollower's followed link count
	 */
	public final void setFollowedLinkCount(int itemCount) {
		this.itemCount = itemCount;
	}

	/**
	 * Returns how many times the LinkFollower has followed a link.
	 *
	 * @return the number of links the LinkFollower has followed
	 */
	public final int getFollowedLinkCount() {
		return itemCount;
	}

	public final S getDefaultEntitySettings() {
		return defaultEntitySettings;
	}

	public final T getEntityList() {
		return entityList;
	}

	public Map<String, ? extends RemoteLinkFollower<S,T,R>> getLinkFollowers() {
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
}
