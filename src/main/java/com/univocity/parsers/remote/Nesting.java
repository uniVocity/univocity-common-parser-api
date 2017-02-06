/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

/**
 * Determines how data obtained from a given entity should be associated with the data of its parent.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public enum Nesting {

	/**
	 * Joins all data retrieved into the parent row. For example, if parent row is
	 * [a,b,c] and the data retrieved for [b] has [t,u] and [x,y], the resulting join will produce
	 * [a,b,t,u,c] and [a,b,x,y,c].
	 *
	 * Rows [t,u] and [x,y] will still be accessible through {@link RemoteResult#getLinkedFieldData(int)}
	 */
	JOIN,

	/**
	 * Joins all data retrieved into the parent row, replacing the source value used to produce the child rows.
	 * For example, if parent row is [a,b,c] and the data retrieved for [b] has [t,u] and [x,y], the resulting join will produce
	 * [a,t,u,c] and [a,x,y,c].
	 *
	 * Rows [t,u] and [x,y] will still be accessible through {@link RemoteResult#getLinkedFieldData(int)}
	 */
	REPLACE_JOIN,

	/**
	 * Links all data retrieved to a given field of the parent row. For example, if parent row is
	 * [a,b,c] and the data retrieved for [b] has [t,u] and [x,y], the parent row will remain as
	 * [a,b,c], while rows [t,u] and [x,y] will be accessible through {@link RemoteResult#getLinkedFieldData(int)}
	 */
	LINK,

	/**
	 * Links all data retrieved the parent row, removing the source value used to produce child rows. For example,
	 * if parent row is [a,b,c] and the data retrieved for [b] has [t,u] and [x,y], the parent row will become as
	 * [a,c], while rows [t,u] and [x,y] will be accessible through {@link RemoteResult#getLinkedFieldData(int)}
	 */
	REPLACE_LINK;

	/**
	 * Tests if this nesting option replaces the source value used to produce child rows.
	 *
	 * @return {@code true} if this nesting option replaces the source value used to produce child rows; {@code false} otherwise.
	 */
	public boolean replaces() {
		return this == REPLACE_LINK || this == REPLACE_JOIN;
	}

	/**
	 * Tests if this nesting option joins values of a linked entity with the values of a parent row.
	 *
	 * @return {@code true} if this nesting option replaces the source value used to produce child rows; {@code false} otherwise.
	 */
	public boolean joins() {
		return this == JOIN || this == REPLACE_JOIN;
	}

	/**
	 * Tests if this nesting option links values of a linked entity to the parent row.
	 *
	 * @return {@code true} if this  nesting option links values of a linked entity to the parent row; {@code false} otherwise.
	 */
	public boolean links() {
		return this == LINK || this == REPLACE_LINK;
	}
}
