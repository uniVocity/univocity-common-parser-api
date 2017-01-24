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

	private boolean combineLinkFollowingRows;
	private boolean ignoreLinkFollowingErrors = true;

	/**
	 * Creates a new LinkFollower
	 */
	protected RemoteLinkFollower(S parentEntitySettings) {
		entityList = (T) parentEntitySettings.getParentEntityList().newInstance();
		this.parentEntitySettings = entityList.addEntitySettings(parentEntitySettings);
		parserSettings = (R) entityList.getParserSettings();

		combineLinkFollowingRows = parserSettings.isCombineLinkFollowingRows();
	}

	/**
	 * Indicates whether or not rows parsed from a link accessed by this link follower will be combined with a "parent" row. The way that
	 * the parser will join rows is by replacing the link following field by the contents collected from a linked result.
	 * If there are multiple rows parsed in the link, it will duplicate the original row to fit every link following row. For example:
	 *
	 * <hr><blockquote><pre>
	 * <table>
	 *     <tr>
	 *         <th>Rows from original page</th>
	 *         <th>Rows from linked page (linkedPage.com)</th>
	 *     </tr>
	 *     <tr>
	 *         <td>["17", "123 real street", "linkedPage.com"]</td>
	 *         <td>["mobile", "04 123 321"]</td>
	 *     </tr>
	 *     <tr>
	 *         <td></td>
	 *         <td>["home", "851 154 110"]</td>
	 *     </tr>
	 * </table>
	 * <hr></blockquote></pre>
	 *
	 * <p>The link following field can be seen in the original row with the value "linkedPage.com".  As 2 rows
	 * got parsed from the link, the original row will be duplicated to complete the join. The resulting output
	 * will be: </p>
	 *
	 * <hr><blockquote><pre>
	 *      ["17", "123 real street", "mobile", "04 123 321"]
	 *      ["17", "123 real street", "home", "851 154 110"]
	 * <hr></></blockquote></pre>
	 *
	 * Defaults to the parent entity's {@link RemoteEntitySettings#isCombineLinkFollowingRows()} setting.
	 *
	 * @return a flag indicating whether the parser should join original rows with their corresponding linked rows
	 */
	public final boolean isCombineLinkFollowingRows() {
		return combineLinkFollowingRows;
	}

	/**
	 * Sets whether or not rows parsed from by this link follower will be combined with a "parent" row. The way that
	 * the parser will join rows is by replacing the link following field by the contents collected from a linked result.
	 * If there are multiple rows parsed in the link, it will duplicate the original row to fit every link following row. For example:
	 *
	 *
	 * <hr><blockquote><pre>
	 * <table>
	 *     <tr>
	 *         <th>Rows from original page</th>
	 *         <th>Rows from linked page (linkedPage.com)</th>
	 *     </tr>
	 *     <tr>
	 *         <td>["17", "123 real street", "linkedPage.com"]</td>
	 *         <td>["mobile", "04 123 321"]</td>
	 *     </tr>
	 *     <tr>
	 *         <td></td>
	 *         <td>["home", "851 154 110"]</td>
	 *     </tr>
	 * </table>
	 * <hr></blockquote></pre>
	 *
	 * <p>The link following field can be seen in the original row with the value "linkedPage.com".  As 2 rows
	 * got parsed from the link, the original row will be duplicated to complete the join. The resulting output
	 * will be: </p>
	 *
	 * <hr><blockquote><pre>
	 *      ["17", "123 real street", "mobile", "04 123 321"]
	 *      ["17", "123 real street", "home", "851 154 110"]
	 * <hr></></blockquote></pre>
	 *
	 * Defaults to the parent entity's {@link RemoteEntitySettings#isCombineLinkFollowingRows()} setting.
	 *
	 * @param combineLinkFollowingRows flag indicating whether the parser should join original rows with their corresponding linked rows
	 */
	public final void setCombineLinkFollowingRows(boolean combineLinkFollowingRows) {
		this.combineLinkFollowingRows = combineLinkFollowingRows;
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
	public void ignoreLinkFollowingErrors(boolean ignoreLinkFollowingErrors) {
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
		return ">>" + parentEntitySettings.getEntityName();
	}
}
