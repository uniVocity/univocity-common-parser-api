/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;

import java.util.*;

/**
 * An abstract class used by Parsers to provide configuration options for use during the parsing process.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RemoteEntityList
 * @see EntityParserSettings
 */
public abstract class RemoteEntitySettings<C extends Context, S extends CommonParserSettings, G extends RemoteParserSettings> extends EntitySettings<C, S, G> {

	private boolean localEmptyValue;
	private String emptyValue = null;

	private boolean localColumnReorderingEnabled;

	public RemoteEntitySettings(String entityName, S entitySettings) {
		super(entityName, entitySettings);
	}

	@Override
	protected S getInternalSettings() {
		return super.getInternalSettings();
	}

	/**
	 * Returns the name of all fields associated with the RemoteResourceEntity. Fields are associated when any of the field adding
	 * methods are run.
	 *
	 * @return a String array of the field names
	 */
	public abstract Set<String> getFieldNames();


	/**
	 * Removes a field from the RemoteResourceEntity. Removed fields will not be used by the parser.
	 *
	 * @param fieldName the name of the field that will be removed.
	 */
	public abstract void removeField(String fieldName);

	public boolean isColumnReorderingEnabled() {
		if(localColumnReorderingEnabled || parserSettings == null) {
			return getInternalSettings().isColumnReorderingEnabled();
		}
		return parserSettings.isColumnReorderingEnabled();
	}

	public void setColumnReorderingEnabled(boolean columnReorderingEnabled) {
		this.localColumnReorderingEnabled = true;
		this.getInternalSettings().setColumnReorderingEnabled(columnReorderingEnabled);
	}

	public String getEmptyValue() {
		if(localEmptyValue || parserSettings == null) {
			return emptyValue;
		}
		return parserSettings.getEmptyValue();
	}

	public void setEmptyValue(String emptyValue) {
		localEmptyValue = true;
		this.emptyValue = emptyValue;
	}
}