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
public abstract class LinkFollower<S extends RemoteEntitySettings> extends RemoteAccessConfiguration {
	protected S entitySettings;
	public static String ENTITY_NAME = "*itemFollower*";
	protected int itemCount;
	protected boolean joinRows;
	protected int linkNum;


	/**
	 * Creates a new LinkFollower
	 */
	public LinkFollower() {
		entitySettings = newEntitySettings();
		itemCount = 0;
		joinRows = false;
		linkNum = 1;
	}

	/**
	 * Returns the name associated with the LinkFollower
	 *
	 * @return the LinkFollower's name
	 */
	static public final String getEntityName() {
		return ENTITY_NAME;
	}

	protected abstract S newEntitySettings();


	/**
	 * Returns the field names associated with the linkFollower
	 *
	 * @return a string array of field names
	 */
	public final Set<String> getFieldNames() {
		return entitySettings.getFieldNames();
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

	/**
	 * Sets if parsed rows from a linked page will be joined with the parsed rows of the original page. If this is set to
	 * false, any data parsed in the linked page will generate new rows.
	 *
	 * @param joinRows
	 */
	public final void setJoinRows(boolean joinRows) {
		this.joinRows = joinRows;
	}

	/**
	 * Returns true if the joinrows option was set to true, otherwise, returns false.
	 *
	 * @return the state of the join rows opton
	 */
	public final boolean getJoinRowsOption() {
		return joinRows;
	}


}
