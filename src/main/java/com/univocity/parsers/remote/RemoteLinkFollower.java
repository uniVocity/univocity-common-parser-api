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
public abstract class RemoteLinkFollower<S extends RemoteEntitySettings, T extends RemoteEntityList<S>> {
	protected T entityList;
	protected S defaultEntitySettings;
	public static String ENTITY_NAME = "*linkFollower*";
	protected int itemCount;
	protected int linkNum;


	/**
	 * Creates a new LinkFollower
	 */
	public RemoteLinkFollower() {
		entityList = newEntityList();
		defaultEntitySettings = entityList.configureEntity(ENTITY_NAME);
		itemCount = 0;
		linkNum = 1;
	}

	protected abstract T newEntityList();

	/**
	 * Returns the name associated with the LinkFollower
	 *
	 * @return the LinkFollower's name
	 */
	static public final String getEntityName() {
		return ENTITY_NAME;
	}


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

	public Map<String, ? extends RemoteLinkFollower> getLinkFollowers() {
		return defaultEntitySettings.getLinkFollowers();
	}

	public S addEntity(String entityName) {
		return entityList.configureEntity(entityName);
	}
}
