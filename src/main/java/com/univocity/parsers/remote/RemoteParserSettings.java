/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.io.*;
import com.univocity.api.net.*;
import com.univocity.api.statistics.*;
import com.univocity.parsers.common.*;

import java.io.*;

/**
 * Base configuration class of a parser that can connect to a remote location, obtain data to parse and produce records
 * for one or more entities. The settings available in a {@code RemoteParserSettings} configure the remote content access,
 * the parsing process, and provide default configuration options that individual implementations of
 * {@link RemoteEntitySettings} can override.
 *
 * Entities are managed from an {@link RemoteEntityList} implementation.
 *
 * @param <S> an internal configuration object that extends from {@link CommonParserSettings}, and is used to
 *            manage configuration of elements shared with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>
 * @param <L> the {@link RemoteEntityList} implementation supported by an {@link EntityParserInterface}.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class RemoteParserSettings<S extends CommonParserSettings, L extends RemoteEntityList> extends EntityParserSettings<S, L> {

	private String emptyValue;

	protected Paginator paginator;

	private FileProvider downloadContentDirectory;
	private String fileNamePattern;

	private DataTransferListener<UrlReaderProvider, File> dataTransferListener;


	/**
	 * Creates a new instance with a supplied {@link RemoteEntityList} implementation. The
	 * {@link RemoteEntityList} is used to manage {@link RemoteEntitySettings} for each entity whose records
	 * will be parsed.
	 *
	 * @param entityList the list of entities whose records will be generated by the parser, and their configuration
	 */
	public RemoteParserSettings(L entityList) {
		super(entityList);
	}


	/**
	 * Configures the parser to store a local copy of the remote content in the filesystem.
	 * If the downloaded content is text, it will be stored using the system default encoding
	 *
	 * @param path the path to the target directory. It can contain system variables enclosed within { and }
	 *             (e.g. {@code {user.home}/Downloads"}). Subdirectories that doesn't exist will be created if required.
	 */
	public final void setDownloadContentDirectory(String path) {
		downloadContentDirectory = new FileProvider(path);
	}

	/**
	 * Configures the parser to store a local copy of the remote content in the filesystem.
	 * If the downloaded content is text, it will be stored using the system default encoding
	 *
	 * @param directory the target directory. Subdirectories that doesn't exist will be created if required.
	 */
	public final void setDownloadContentDirectory(File directory) {
		downloadContentDirectory = new FileProvider(directory);
	}

	/**
	 * Returns the directory where downloaded content should be stored
	 *
	 * @return a {@link FileProvider} pointing to the configured download content directory
	 */
	public final FileProvider getDownloadContentDirectory() {
		return downloadContentDirectory;
	}

	/**
	 * The pattern that names of downloaded files should follow. For example, setting the pattern as
	 * "/search/file{number}" will make pages stored in the search folder with the name "file1.html", "file2.html"
	 * etc. Note that the file extension will be automatically added if it is known.
	 *
	 * The following patterns are recognized:
	 * <ul>
	 * <li>number, &lt;padding&gt; prints a sequential number, starting from {@code 1}.
	 * Numbers can be padded with leading zeros if the optional padding number is provided.
	 *
	 * Examples:
	 * <ul>
	 * <li>/tmp/page{number, 4}: prints /tmp/page0001.html, /tmp/page0321.html, etc</li>
	 * <li>/tmp/page{number}: prints /tmp/page1.html, /tmp/page543.html, etc</li>
	 * <li>/tmp/page{number, 2}: prints /tmp/page01.html, /tmp/page89.html, /tmp/page289.html, etc</li>
	 * </ul>
	 * </li>
	 * <li>date, &lt;mask&gt; prints the current date as a timestamp. A date mask can be provided to configure
	 * how the date should be displayed (refer to {@link java.text.SimpleDateFormat} for valid patterns).
	 *
	 * Examples:
	 * <ul>
	 * <li>/tmp/file_{date, yyyy-MMM-dd}: prints /tmp/file_2016-Dec-25.pdf, /tmp/file_2020-Feb-28.html, etc</li>
	 * <li>/tmp/file_{date}: prints /tmp/file_23423423423.pdf, /tmp/file_234234324231.html, etc</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 *
	 * <i>defaults to @code{file_{number}}</i>
	 *
	 * @param pattern the pattern used to generate file names for downloaded content.
	 */
	public final void setFileNamePattern(String pattern) {
		fileNamePattern = pattern;
	}

	/**
	 * Returns the pattern that names of downloaded files should follow. For example, setting the pattern as
	 * "/search/file{number}" will make pages stored in the search folder with the name "file1.html", "file2.html"
	 * etc. Note that the file extension will be automatically added if it is known.
	 *
	 * The following patterns are recognized:
	 * <ul>
	 * <li>number, &lt;padding&gt; prints a sequential number, starting from {@code 1}.
	 * Numbers can be padded with leading zeros if the optional padding number is provided.
	 *
	 * Examples:
	 * <ul>
	 * <li>/tmp/page{number, 4}: prints /tmp/page0001.html, /tmp/page0321.html, etc</li>
	 * <li>/tmp/page{number}: prints /tmp/page1.html, /tmp/page543.html, etc</li>
	 * <li>/tmp/page{number, 2}: prints /tmp/page01.html, /tmp/page89.html, /tmp/page289.html, etc</li>
	 * </ul>
	 * </li>
	 * <li>date, &lt;mask&gt; prints the current date as a timestamp. A date mask can be provided to configure
	 * how the date should be displayed (refer to {@link java.text.SimpleDateFormat} for valid patterns).
	 *
	 * Examples:
	 * <ul>
	 * <li>/tmp/file_{date, yyyy-MMM-dd}: prints /tmp/file_2016-Dec-25.pdf, /tmp/file_2020-Feb-28.html, etc</li>
	 * <li>/tmp/file_{date}: prints /tmp/file_23423423423.pdf, /tmp/file_234234324231.html, etc</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * <i>defaults to @code{file_{number}}</i>
	 *
	 * @return the pattern used to generate file names for downloaded content.
	 */
	public final String getFileNamePattern() {
		if (fileNamePattern == null) {
			return "file_{number}";
		}
		return fileNamePattern;
	}

	/**
	 * Configures a {@link Paginator} to handle multiple pages of remote content that needs to parsed.
	 *
	 * @return a {@link Paginator} associated with the current {@code RemoteParserSettings}
	 */
	public Paginator getPaginator() {
		if (paginator == null) {
			paginator = newPaginator();
		}
		return paginator;
	}

	/**
	 * Creates an instance of a concrete implementation of {@link Paginator}
	 *
	 * @return a new {@link Paginator} instance
	 */
	protected abstract Paginator newPaginator();

	/**
	 * Returns the value to be used when the content parsed for a field of some record evaluates to an empty {@code String}
	 *
	 * <i>Defaults to {@code null}</i>
	 *
	 * @return the value to be used instead of empty {@code String} (i.e. "") when the content of a field is empty.
	 */
	public final String getEmptyValue() {
		return emptyValue;
	}

	/**
	 * Defines the value to be used when the content parsed for a field of some record evaluates to an empty {@code String}
	 *
	 * <i>Defaults to {@code null}</i>
	 *
	 * @param emptyValue the value to be used instead of empty {@code String} (i.e. "") when the content of a field is empty.
	 */
	public final void setEmptyValue(String emptyValue) {
		this.emptyValue = emptyValue;
	}

	/**
	 * Identifies whether fields should be reordered when field selection methods of an entity's {@link EntitySettings}
	 * (such as {@link EntitySettings#selectFields(String...)}) are used.
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
		return globalSettings.isColumnReorderingEnabled();
	}

	/**
	 * Defines whether fields should be reordered when field selection methods of an entity's {@link EntitySettings}
	 * (such as {@link EntitySettings#selectFields(String...)}) are used.
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
		globalSettings.setColumnReorderingEnabled(columnReorderingEnabled);
	}

	public DataTransferListener<UrlReaderProvider, File> getDataTransferListener() {
		return dataTransferListener == null ? NoopDataTransferListener.instance : dataTransferListener;
	}

	public void setDataTransferListener(DataTransferListener<UrlReaderProvider, File> dataTransferListener) {
		this.dataTransferListener = dataTransferListener;
	}
}
