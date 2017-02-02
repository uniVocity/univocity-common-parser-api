/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
interface CommonLinkFollowerOptions {

	/**
	 * If set to {@code false}, the parser will throw an Exception when the parser tries to follow a link that is invalid or
	 * malformed. If {@code true}, the parser will simply ignore following an invalid or malformed link.
	 *
	 * Defaults to {@code true}
	 *
	 * @param ignoreLinkFollowingErrors true if the parser will ignore errors when accessing linked page, false otherwise.
	 */
	void ignoreLinkFollowingErrors(boolean ignoreLinkFollowingErrors);

	/**
	 * Returns if the parser will ignore invalid or malformed link following urls.
	 * Defaults to {@code true}
	 *
	 * @return {@code true} if the parser is set to ignore errors when accessing linked page
	 */
	boolean isIgnoreLinkFollowingErrors();

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
	boolean isCombineLinkFollowingRows();

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
	void setCombineLinkFollowingRows(boolean combineLinkFollowingRows);


	/**
	 * Gets whether or not fields in an entity used to link entities should be removed. Linking entity fields occur
	 * in a leaf entity as described in
	 * {@link RemoteEntityList#linkEntities(RemoteEntitySettings parent, RemoteEntitySettings child, RemoteEntitySettings[] restOfChildren)}.
	 * An example of removing entity fields can be seen below:
	 *
	 * <pre>
	 *      Person entity is linked to pet entity
	 *      Entities    : Person("Name", "Age")| Pet("Species", "Age", "Name")
	 *      Original Row: ["Bob", "12"]        | ["Dog", "3", "Bob"]
	 *
	 *      Result:
	 *         setRemoveLinkedEntityFields(true) : ["Bob", "12"] -> ["Dog", "3"]
	 *         setRemoveLinkedEntityFields(false): ["Bob", "12"] -> ["Dog", "3", "Bob"]
	 *
	 * </pre>
	 *
	 * @return a flag indicating if linked entity fields will be removed
	 */
	boolean isRemoveLinkedEntityFields();


	/**
	 * Sets whether or not fields in an entity used to link entities should be removed. Linking entity fields occur
	 * in a leaf entity as described in
	 * {@link RemoteEntityList#linkEntities(RemoteEntitySettings parent, RemoteEntitySettings child, RemoteEntitySettings[] restOfChildren)}.
	 * An example of removing entity fields can be seen below:
	 *
	 * <pre>
	 *      Person entity is linked to pet entity
	 *      Entities    : Person("Name", "Age")| Pet("Species", "Age", "Name")
	 *      Original Row: ["Bob", "12"]        | ["Dog", "3", "Bob"]
	 *
	 *      Result:
	 *         setRemoveLinkedEntityFields(true) : ["Bob", "12"] -> ["Dog", "3"]
	 *         setRemoveLinkedEntityFields(false): ["Bob", "12"] -> ["Dog", "3", "Bob"]
	 *
	 * </pre>
	 *
	 * @param removeLinkedEntityFields a flag indicating if linked entity fields will be removed
	 */
	void setRemoveLinkedEntityFields(boolean removeLinkedEntityFields);

}
