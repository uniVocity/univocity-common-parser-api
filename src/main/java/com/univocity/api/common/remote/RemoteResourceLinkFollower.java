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


	public RemoteResourceLinkFollower() {
		entity = newEntity();
		itemCount = 0;
		joinRows = false;
		linkNum = 1;
	}

	static public final String getEntityName() {
		return  entityName;
	}

	protected abstract C newEntity();


	public final String[] getFieldNames() {
		return entity.getFieldNames();
	}

	public final void setFollowedLinkCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public final int getFollowedLinkCount() {
		return itemCount;
	}

	public final void setJoinRows(boolean joinRows) {
		this.joinRows = joinRows;
	}

	public final boolean getJoinRowsOption() {
		return joinRows;
	}


}
