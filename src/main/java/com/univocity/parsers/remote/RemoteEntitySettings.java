/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;
import com.univocity.parsers.common.processor.*;

import java.util.*;

/**
 * Manages configuration options for individual entities of a {@link RemoteEntityList}. Settings that also exist in
 * the parent {@link RemoteParserSettings} will be used by default but can be overridden for an individual entity.
 *
 * @param <C> the type of {@link Context} implementation supported by {@link com.univocity.parsers.common.processor.core.Processor}s of this entity.
 * @param <S> an internal configuration object that extends from {@link CommonParserSettings}, and is used to
 *            manage configuration of elements shared with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>
 * @param <G> type of the global configuration class (an instance of {@link RemoteEntitySettings}, used to configure
 *            the parser (a concrete implementation of {@link EntityParserInterface}) and its entities.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class RemoteEntitySettings<C extends Context, S extends CommonParserSettings, G extends RemoteParserSettings, T extends RemoteLinkFollower> extends EntitySettings<C, S, G> {

	private boolean localEmptyValue;
	private String emptyValue = null;

	private boolean localColumnReorderingEnabled;
	protected Set<String> requestParameters = new LinkedHashSet<String>();
	protected Map<String, T> linkFollowers = new HashMap<String, T>();

	private Boolean combineLinkFollowingRows;


	protected RemoteEntitySettings(String entityName, S entitySettings) {
		super(entityName, entitySettings);
	}

	/**
	 * Indicates whether or not rows parsed from link followers associated with this entity will be combined with each "parent" row. The way that
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
	 * Defaults to the parser's {@link RemoteParserSettings#isCombineLinkFollowingRows()} setting.
	 *
	 * @return a flag indicating whether the parser should join original rows with their corresponding linked rows
	 */
	public final boolean isCombineLinkFollowingRows() {
		if(combineLinkFollowingRows == null){
			combineLinkFollowingRows = parserSettings.isCombineLinkFollowingRows();
		}
		return combineLinkFollowingRows;
	}

	/**
	 * Sets whether or not rows parsed from link followers associated with this entity will be combined with each "parent" row. The way that
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
	 * Defaults to the parser's {@link RemoteParserSettings#isCombineLinkFollowingRows()} setting.
	 *
	 * @param combineLinkFollowingRows flag indicating whether the parser should join original rows with their corresponding linked rows
	 */
	public final void setCombineLinkFollowingRows(boolean combineLinkFollowingRows) {
		this.combineLinkFollowingRows = combineLinkFollowingRows;
	}

	@Override
	protected S getInternalSettings() {
		return super.getInternalSettings();
	}

	/**
	 * Returns the name of all fields associated with a remote entity. The definition of fields and how they are populated
	 * is delegated to concrete implementations of this class.
	 *
	 * @return a unmodifiable, ordered {@link LinkedHashSet} of field names available from this entity.
	 */
	public abstract Set<String> getFieldNames();

	/**
	 * Removes a field from the entity. Removed fields will not be used by the parser and any configuration associated
	 * with it will be lost.
	 *
	 * @param fieldName name of the field that should be be removed.
	 */
	public abstract void removeField(String fieldName);

	/**
	 * Identifies whether fields should be reordered when field selection methods such as
	 * {@link EntitySettings#selectFields(String...)} are used.
	 *
	 * <p>When <b>enabled</b>, each parsed record will contain values only for the selected columns.
	 * The values will be ordered according to the selection.
	 * <p>When <b>disabled</b>, each parsed record will contain values for all columns, in their original sequence.
	 * Fields which were not selected will contain null values, as defined in {@link EntitySettings#getNullValue()}.
	 *
	 * <i>Defaults to {@code true}</i>
	 *
	 * @return a flag indicating whether or not selected fields should be reordered
	 */
	public final boolean isColumnReorderingEnabled() {
		if (localColumnReorderingEnabled || parserSettings == null) {
			return getInternalSettings().isColumnReorderingEnabled();
		}
		return parserSettings.isColumnReorderingEnabled();
	}

	/**
	 * Defines whether fields should be reordered when field selection methods such as
	 * {@link EntitySettings#selectFields(String...)} are used.
	 *
	 * <p>When <b>enabled</b>, each parsed record will contain values only for the selected columns.
	 * The values will be ordered according to the selection.
	 * <p>When <b>disabled</b>, each parsed record will contain values for all columns, in their original sequence.
	 * Fields which were not selected will contain null values, as defined in {@link EntitySettings#getNullValue()}.
	 *
	 * <i>Defaults to {@code true}</i>
	 *
	 * @param columnReorderingEnabled the flag indicating whether or not selected fields should be reordered
	 */
	public final void setColumnReorderingEnabled(boolean columnReorderingEnabled) {
		this.localColumnReorderingEnabled = true;
		this.getInternalSettings().setColumnReorderingEnabled(columnReorderingEnabled);
	}

	/**
	 * Returns the value to be used when the content parsed for a field of some record evaluates to an empty {@code String}
	 *
	 * <i>Defaults to {@code null}</i>
	 *
	 * @return the value to be used instead of empty {@code String} (i.e. "") when the content of a field is empty.
	 */
	public final String getEmptyValue() {
		if (localEmptyValue || parserSettings == null) {
			return emptyValue;
		}
		return parserSettings.getEmptyValue();
	}

	/**
	 * Defines the value to be used when the content parsed for a field of some record evaluates to an empty {@code String}
	 *
	 * <i>Defaults to {@code null}</i>
	 *
	 * @param emptyValue the value to be used instead of empty {@code String} (i.e. "") when the content of a field is empty.
	 */
	public final void setEmptyValue(String emptyValue) {
		localEmptyValue = true;
		this.emptyValue = emptyValue;
	}

	public Map<String, T> getLinkFollowers() {
		return Collections.unmodifiableMap(linkFollowers);
	}

	protected RemoteEntityList getParentEntityList() {
		return (RemoteEntityList) super.getParentEntityList();
	}


	@Override
	protected EntitySettings<C, S, G> clone() {
		RemoteEntitySettings<C, S, G, T> out = (RemoteEntitySettings<C, S, G, T>) super.clone();
		out.linkFollowers = new HashMap<String, T>();
		requestParameters = new LinkedHashSet<String>();
		return out;
	}
}