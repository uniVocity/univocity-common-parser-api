/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.api.common.remote;

/**
 * Created by anthony on 20/07/16.
 */
public abstract class RemoteResourceLinkFollower<C extends RemoteResourceEntity> {
	protected C entity;
	protected static String entityName = "*itemFollower*";
	protected int itemCount;
	protected boolean joinRows;
	protected int linkNum;


	/**
	 * Creates a new RemoteResourceLinkFollower
	 */
	public RemoteResourceLinkFollower() {
		entity = newEntity();
		itemCount = 0;
		joinRows = false;
		linkNum = 1;
	}

	/**
	 * Returns the name associated with the RemoteResourceLinkFollower
	 *
	 * @return the RemoteResourceLinkFollower's name
	 */
	static public final String getEntityName() {
		return  entityName;
	}

	protected abstract C newEntity();


	/**
	 * Returns the field names associated with the linkFollower
	 *
	 * @return a string array of field names
	 */
	public final String[] getFieldNames() {
		return entity.getFieldNames();
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
