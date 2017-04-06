/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;

/**
 * The basic follower options that all followers should implement;
 * Ignoring errors and setting {@link Nesting}
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
interface CommonFollowerOptions {

	/**
	 * Configures the parser to ignore (or not) invalid, malformed or unavailable links when following urls
	 * to collect additional data associated to a current result.
	 *
	 * If set to {@code false}, the parser will throw an Exception when attempting to follow a link that is invalid,
	 * malformed or unavailable. If {@code true}, the parser will simply ignore the error and proceed.
	 *
	 * Defaults to {@code true}
	 *
	 * @param ignoreLinkFollowingErrors true if the parser will ignore errors when accessing linked page, false otherwise.
	 */
	void ignoreFollowingErrors(boolean ignoreLinkFollowingErrors);

	/**
	 * Returns a flag indicating whether the parser will ignore invalid, malformed or unavailable links when following urls
	 * to collect additional data associated to a current result.
	 *
	 * Defaults to {@code true}
	 *
	 * @return {@code true} if the parser is set to ignore errors when accessing linked page
	 */
	boolean isIgnoreFollowingErrors();

	/**
	 * Returns the nesting strategy to apply to rows associated to a "parent" row, such as results parsed from a
	 * link accessed by a {@link RemoteFollower}.
	 *
	 * Defaults to the parent entity's {@link RemoteEntitySettings#getNesting()} or if undefined,
	 * the {@link RemoteParserSettings#getNesting()} setting.
	 *
	 * @return the nesting strategy to use when processing results associated with a parent row.
	 */
	Nesting getNesting();

	/**
	 * Configures the nesting strategy to apply to rows associated to a "parent" row, such as results parsed from a
	 * link accessed by a {@link RemoteFollower}.
	 *
	 * Defaults to the parent entity's {@link RemoteEntitySettings#getNesting()} or if undefined,
	 * the {@link RemoteParserSettings#getNesting()} setting.
	 *
	 * @param nesting the nesting strategy to use when processing results associated with a parent row.
	 */
	void setNesting(Nesting nesting);
}
