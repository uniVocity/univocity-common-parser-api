/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.common.*;
import com.univocity.parsers.common.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class RemoteParserSettings<S extends CommonParserSettings, L extends RemoteEntityList> extends EntityParserSettings<S, L> {

	//Does not exist in CommonParserSettings.
	private String emptyValue;

	protected Paginator paginator;

	protected FileProvider downloadContentDirectory;
	protected int downloadThreads;
	protected String fileNamePattern;
	protected final TreeMap<String, String> requestParameters = new TreeMap<String, String>();


	/**
	 * Creates a new HtmlParserSettings with a supplied {@link HtmlEntityList}. The {@link HtmlEntityList} is used to
	 * store {@link HtmlEntity}'s which define the specific HTML elements that will be parsed.
	 *
	 * @param
	 */
	public RemoteParserSettings(L entityList) {
		super(entityList);
	}

	/**
	 * Sets the number of threads that will be created to download content (e.g. images) after a page finishes parsing.
	 * If not set or set to a number <= 0, 4 threads will be used.
	 *
	 * @param downloadThreads the number of download threads that will be used to download content
	 */
	public void setDownloadThreads(int downloadThreads) {
		this.downloadThreads = downloadThreads;
	}

	/**
	 * Returns the amount of threads that the parser will use to download content
	 *
	 * @return number of download threads set
	 */
	public int getDownloadThreads() {
		return downloadThreads;
	}

	/**
	 * Sets the file directory where downloaded content will be stored using the system default encoding
	 *
	 * @param fileName the directory that stores downloaded content as a String
	 */
	public void setDownloadContentDirectory(String fileName) {
		downloadContentDirectory = new FileProvider(fileName);
	}

	/**
	 * Sets the file directory where downloaded content will be stored
	 *
	 * @param fileName the directory that stores downloaded content as a String
	 * @param encoding the encoding of the directory as a Charset
	 */
	public void setDownloadContentDirectory(String fileName, Charset encoding) {
		downloadContentDirectory = new FileProvider(fileName, encoding);
	}

	/**
	 * Sets the file directory where downloaded content will be stored
	 *
	 * @param fileName the directory that stores downloaded content as a String
	 * @param encoding the encoding of the directory as a String
	 */
	public void setDownloadContentDirectory(String fileName, String encoding) {
		downloadContentDirectory = new FileProvider(fileName, encoding);
	}

	/**
	 * Sets the file directory where downloaded content will be stored and use the default system encoding.
	 *
	 * @param file the directory that stores downloaded content
	 */
	public void setDownloadContentDirectory(File file) {
		downloadContentDirectory = new FileProvider(file);
	}

	/**
	 * Sets the file directory where downloaded content will be stored
	 *
	 * @param file     the directory that stores downloaded content
	 * @param encoding the encoding the directory as a Charset
	 */
	public void setDownloadContentDirectory(File file, Charset encoding) {
		downloadContentDirectory = new FileProvider(file, encoding);
	}

	/**
	 * Sets the file directory where downloaded content will be stored
	 *
	 * @param file     The directory that stores downloaded content
	 * @param encoding the encoding of the directory as a String
	 */
	public void setDownloadContentDirectory(File file, String encoding) {
		downloadContentDirectory = new FileProvider(file, encoding);
	}

	/**
	 * Returns the file directory where downloaded content is stored in
	 *
	 * @return a FileProvider which contains the download content directory
	 */
	public FileProvider getDownloadContentDirectory() {
		return downloadContentDirectory;
	}

	/**
	 * The pattern that the names of pages downloaded will follow. For example, setting the pattern as
	 * "/search/page{pageNumber}" will make pages stored in the search folder with the name "page1.html", "page2.html"
	 * etc. Note: The html extension is automatically added and does not need to be specified.
	 *
	 * @param pattern the pattern of file names
	 */
	public void setFileNamePattern(String pattern) {
		fileNamePattern = pattern;
	}

	/**
	 * Returns the file name pattern used for names when saving pages
	 *
	 * @return the pattern of file names
	 */
	public String getFileNamePattern() {
		return fileNamePattern;
	}

	/**
	 * Returns a map of request parameter names and values.
	 *
	 * @return request parameter names and values as a map
	 */
	public final Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	/**
	 * Creates a new request parameter.
	 *
	 * @param parameterName the name of the parameter
	 * @param value         the value associated with the parameter
	 */
	public final void setRequestParameter(String parameterName, String value) {
		requestParameters.put(parameterName, value);

	}

	public Paginator configurePaginator() {
		if (paginator == null) {
			paginator = newPaginator();
		}
		return paginator;
	}

	protected abstract Paginator newPaginator();

	public Paginator getPaginator() {
		return paginator;
	}

	public String getEmptyValue() {
		return emptyValue;
	}

	public void setEmptyValue(String emptyValue) {
		this.emptyValue = emptyValue;
	}

	public boolean isColumnReorderingEnabled() {
		return globalSettings.isColumnReorderingEnabled();
	}

	public void setColumnReorderingEnabled(boolean columnReorderingEnabled) {
		globalSettings.setColumnReorderingEnabled(columnReorderingEnabled);
	}
}
