/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.api.*;
import com.univocity.api.common.*;
import com.univocity.api.io.*;
import com.univocity.api.statistics.*;
import com.univocity.parsers.common.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

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
 * @param <C> the {@link Context} implementation which provides specific details about the the parsing process performed
 *            by this parser.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see DataTransfer
 * @see RemoteEntitySettings
 * @see RemoteEntityList
 * @see Paginator
 */
public abstract class RemoteParserSettings<S extends CommonParserSettings, L extends RemoteEntityList, C extends Context> extends EntityParserSettings<S, L, C> implements CommonFollowerOptions {

	private String emptyValue;
	protected Paginator paginator;

	private boolean downloadBeforeParsingEnabled = false;
	private FileProvider downloadContentDirectory;
	private ParameterizedString fileNamePattern;
	private boolean downloadOverwritingEnabled = true;

	private Nesting nesting = Nesting.LINK;
	private boolean ignoreLinkFollowingErrors = false;

	private DownloadListener downloadListener;
	private int downloadThreads = 8;

	private ExecutorService executorService;
	private long remoteInterval = 15L;

	/**
	 * Creates a new configuration object for an implementation of {@link EntityParserInterface}, which will process
	 * an input to produce records for entities defined by a {@link RemoteEntityList}.  The
	 * {@link RemoteEntityList} is used to manage {@link RemoteEntitySettings} for each entity whose records
	 * will be parsed.
	 */
	public RemoteParserSettings() {
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
	@UI(order = 1)
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
	 * Sets the pattern that names of downloaded files should follow. For example, setting the pattern as
	 * "/search/file{page}" will make pages stored in the search folder with the name "file1.html", "file2.html"
	 * etc. Note that the file extension will be automatically added if it is known.
	 *
	 * The following patterns are recognized:
	 * <ul>
	 * <li>{@code {page, <padding>}} prints the current page number from the paginator.
	 * Number can be padded with leading zeroes if the optional padding number is provided.
	 * Examples:
	 * <ul>
	 * <li>{@code /tmp/page{page, 4}}: prints {@code /tmp/page0001.html, /tmp/page0321.html}, etc.</li>
	 * <li>{@code /tmp/page{page}}: prints {@code /tmp/page1.html, /tmp/page2.html, /tmp/page543.html}, etc.</li>
	 * <li>{@code /tmp/page{page, 2}}: prints {@code /tmp/page01.html, /tmp/page89.html, /tmp/page289.html}, etc</li>
	 * </ul>
	 * </li>
	 * <li>{@code {date, <mask>}} prints the current date as a timestamp. A date mask can be provided to configure
	 * how the date should be displayed (refer to {@link java.text.SimpleDateFormat} for valid patterns).
	 * Examples:
	 * <ul>
	 * <li>{@code /tmp/file_{date, yyyy-MMM-dd}}: prints {@code /tmp/file_2016-Dec-25.pdf, /tmp/file_2020-Feb-28.html}, etc</li>
	 * <li>{@code /tmp/file_{date}}: prints {@code /tmp/file_23423423423.pdf, /tmp/file_234234324231.html}, etc</li>
	 * </ul>
	 * </li>
	 * <li>{@code {$query}} prints the value associated with the supplied query located in the HTML page's URL. Examples:
	 * <ul>
	 * <li>{@code /tmp/search_{$q}} on HTML page with url {@code 'http://google.com/search?q=cup'}: prints {@code /tmp/search_cup.html}</li>
	 * </ul>
	 * </li>
	 * <li>
	 * {@code {follower, <padding>}} prints the number of followers that have been parsed. Basically the same as {@code page}
	 * except for link followers.
	 * Examples:
	 * <ul>
	 * <li>{@code /tmp/file_{follower, 3}}: prints {@code /tmp/file_001, /tmp/file_014}, etc.</li>
	 * </ul>
	 * </li>
	 * <li>
	 * {@code {parent}} prints the name of the "parent" file without the extension. The "parent" file is the one that
	 * the "parent" entity saved to. In the case of a link follower the "parent" entity would be the entity that parsed
	 * the row which triggered the link follower to start parsing.
	 * Examples:
	 * <ul>
	 * <li>{@code {parent}/followedPage} with a parent entity saving to the file {@code /tmp/page_1.html} would print
	 * {@code /tmp/page_1/followedPage.html}</li>
	 * <li>
	 * {@code {parent}/file_{follower}} with a parent entity saving to the file {@code /tmp/page_4.html} would print
	 * {@code /tmp/page_4/file_1.html, /tmp/page_4/file_2.html}, etc.
	 * </li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * <i>Defaults to {@code file_{page}}</i>
	 *
	 * @param pattern the pattern used to generate file names for downloaded content.
	 */
	@UI(order = 2)
	public final void setFileNamePattern(String pattern) {
		fileNamePattern = new ParameterizedString(pattern);
	}

	/**
	 * Gets the pattern that names of downloaded files should follow. For example, setting the pattern as
	 * "/search/file{page}" will make pages stored in the search folder with the name "file1.html", "file2.html"
	 * etc. Note that the file extension will be automatically added if it is known.
	 *
	 * The following patterns are recognized:
	 * <ul>
	 * <li>{@code {page, <padding>}} prints the current page number from the paginator.
	 * Number can be padded with leading zeroes if the optional padding number is provided.
	 * Examples:
	 * <ul>
	 * <li>{@code /tmp/page{page, 4}}: prints {@code /tmp/page0001.html, /tmp/page0321.html}, etc.</li>
	 * <li>{@code /tmp/page{page}}: prints {@code /tmp/page1.html, /tmp/page2.html, /tmp/page543.html}, etc.</li>
	 * <li>{@code /tmp/page{page, 2}}: prints {@code /tmp/page01.html, /tmp/page89.html, /tmp/page289.html}, etc</li>
	 * </ul>
	 * </li>
	 * <li>{@code {date, <mask>}} prints the current date as a timestamp. A date mask can be provided to configure
	 * how the date should be displayed (refer to {@link java.text.SimpleDateFormat} for valid patterns).
	 * Examples:
	 * <ul>
	 * <li>{@code /tmp/file_{date, yyyy-MMM-dd}}: prints {@code /tmp/file_2016-Dec-25.pdf, /tmp/file_2020-Feb-28.html}, etc</li>
	 * <li>{@code /tmp/file_{date}}: prints {@code /tmp/file_23423423423.pdf, /tmp/file_234234324231.html}, etc</li>
	 * </ul>
	 * </li>
	 * <li>{@code {$query}} prints the value associated with the supplied query located in the HTML page's URL. Examples:
	 * <ul>
	 * <li>{@code /tmp/search_{$q}} on HTML page with url {@code 'http://google.com/search?q=cup'}: prints {@code /tmp/search_cup.html}</li>
	 * </ul>
	 * </li>
	 * <li>
	 * {@code {follower, <padding>}} prints the number of followers that have been parsed. Basically the same as {@code page}
	 * except for link followers.
	 * Examples:
	 * <ul>
	 * <li>{@code /tmp/file_{follower, 3}}: prints {@code /tmp/file_001, /tmp/file_014}, etc.</li>
	 * </ul>
	 * </li>
	 * <li>
	 * {@code {parent}} prints the name of the "parent" file without the extension. The "parent" file is the one that
	 * the "parent" entity saved to. In the case of a link follower the "parent" entity would be the entity that parsed
	 * the row which triggered the link follower to start parsing.
	 * Examples:
	 * <ul>
	 * <li>{@code {parent}/followedPage} with a parent entity saving to the file {@code /tmp/page_1.html} would print
	 * {@code /tmp/page_1/followedPage.html}</li>
	 * <li>
	 * {@code {parent}/file_{follower}} with a parent entity saving to the file {@code /tmp/page_4.html} would print
	 * {@code /tmp/page_4/file_1.html, /tmp/page_4/file_2.html}, etc.
	 * </li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * <i>Defaults to {@code file_{page}}</i>
	 *
	 * @return the pattern used to generate file names for downloaded content.
	 */
	public final String getFileNamePattern() {
		return getParameterizedFileName().applyParameterValues();
	}

	final ParameterizedString getParameterizedFileName() {
		if (fileNamePattern == null) {
			fileNamePattern = new ParameterizedString("file_{page}");
		}
		return fileNamePattern;
	}

	/**
	 * Sets the value of a parameter in the filename pattern defined in {@link #setFileNamePattern(String)}.
	 *
	 * @param parameterName  the name of the parameter
	 * @param parameterValue the value of the parameter
	 */
	public final void setFileNameParameter(String parameterName, Object parameterValue) {
		getParameterizedFileName().set(parameterName, parameterValue);
	}

	/**
	 * Gets the value of a parameter in the filename pattern defined in {@link #setFileNamePattern(String)}.
	 *
	 * @param parameterName the name of the parameter to get
	 *
	 * @return the value of the parameter
	 */
	public final Object getFileNameParameter(String parameterName) {
		return getParameterizedFileName().get(parameterName);
	}

	/**
	 * @return the set of parameter names in the filename pattern defined in {@link #setFileNamePattern(String)}.
	 */
	public final Set<String> getFileNameParameters() {
		return getParameterizedFileName().getParameters();
	}

	/**
	 * Clears all values from the filename pattern defined in {@link #setFileNamePattern(String)}
	 */
	public final void clearFileNameParameters() {
		this.getParameterizedFileName().clearValues();
	}

	/**
	 * Configures a {@link Paginator} to handle multiple pages of remote content that needs to parsed.
	 *
	 * @param paginator a {@link Paginator} to be associated with the current {@code RemoteParserSettings}
	 */
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	/**
	 * Configures a {@link Paginator} to handle multiple pages of remote content that needs to parsed.
	 *
	 * @return a {@link Paginator} associated with the current {@code RemoteParserSettings}
	 */
	public Paginator getPaginator() {
		if (paginator == null) {
			paginator = newPaginator(this);
		}
		return paginator;
	}

	/**
	 * Creates an instance of a concrete implementation of {@link Paginator}
	 *
	 * @param parserSettings the parser settings that should be used for the new paginator
	 *
	 * @return a new {@link Paginator} instance
	 */
	protected abstract Paginator newPaginator(RemoteParserSettings parserSettings);

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
	@UI
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


	/**
	 * Returns the {@link DownloadListener} associated with the parser and which will receive updates on the
	 * progress of downloads made by the parser.
	 *
	 * @return the current listener that should receive notifications regarding the progress of downloads
	 * performed by the parser. If undefined, a {@link NoopDataTransfer} will be returned.
	 */
	public DownloadListener getDownloadListener() {
		return downloadListener;
	}

	/**
	 * Associates a {@link DataTransfer} with the parser, which will receive updates on the progress of downloads
	 * made by the parser.
	 *
	 * @param downloadListener the listener that should receive notifications regarding the progress of downloads
	 *                         performed by the parser.
	 */
	public void setDownloadListener(DownloadListener downloadListener) {
		this.downloadListener = downloadListener;
	}

	/**
	 * Returns the default file extension to use when saving files to the directory specified by
	 * {@link #getDownloadContentDirectory()}, in case the the file name pattern taken from {@link #getFileNamePattern()}
	 * doesn't include a file extension.
	 *
	 * @return the default file extension to use when saving files
	 */
	public abstract String getDefaultFileExtension();

	/**
	 * Returns a flag indicating whether the parser will overwrite content already downloaded. If disabled, the parser
	 * will skip the download of contents already available in the filesystem, and use the content available locally.
	 *
	 * <i>Defaults to {@code true}</i>
	 *
	 * @return flag to indicate overwriting of downloaded content is enabled.
	 */
	public boolean isDownloadOverwritingEnabled() {
		return downloadOverwritingEnabled;
	}

	/**
	 * Configures the parser to overwrite content already downloaded. If disabled, the parser will skip the download
	 * of contents already available in the filesystem, and use the content available locally.
	 *
	 * <i>Defaults to {@code true}</i>
	 *
	 * @param downloadOverwritingEnabled flag to enable or disable overwriting of downloaded content.
	 */
	@UI
	public void setDownloadOverwritingEnabled(boolean downloadOverwritingEnabled) {
		this.downloadOverwritingEnabled = downloadOverwritingEnabled;
	}

	/**
	 * Verifies whether the parser will download the remote content before parsing it. If a directory to download
	 * c7ontent has been set (with {@link #setDownloadContentDirectory(String)}, this method will always return {@code true}
	 * and the parser will download the remote content into the given directory. If no directory has been defined,
	 * the contents will be downloaded into a temporary directory.
	 *
	 * <i>Defaults to {@code false}</i>
	 *
	 * @return a flag indicating whether any remote content should be downloaded into a local file before being parsed.
	 */
	public boolean isDownloadBeforeParsingEnabled() {
		return downloadBeforeParsingEnabled || downloadContentDirectory != null;
	}

	/**
	 * Instructs the parser to download the remote content before parsing it. If a directory to download
	 * content has been set (with {@link #setDownloadContentDirectory(String)}, this method has no effect
	 * and the parser will download the remote content into the given directory. If this flag is set to {@code true} and
	 * no directory has been defined, the contents will be downloaded into a temporary directory.
	 *
	 * <i>Defaults to {@code false}</i>
	 *
	 * @param downloadBeforeParsingEnabled flag enable the parser to download remote content into a local file before parsing it.
	 */
	@UI
	public void setDownloadBeforeParsingEnabled(boolean downloadBeforeParsingEnabled) {
		this.downloadBeforeParsingEnabled = downloadBeforeParsingEnabled;
	}

	/**
	 * Sets the number of threads that will be used to download remote content (e.g. images) that is associated with
	 * the parsed input
	 *
	 * <i>Defaults to 8</i>
	 *
	 * @param downloadThreads the maximum number of threads to be used for downloading content
	 */
	@Range(min = 1, max = 16)
	@UI(order = 3)
	public final void setDownloadThreads(int downloadThreads) {
		Args.positive(downloadThreads, "Number of threads for content download");
		this.downloadThreads = downloadThreads;
	}

	/**
	 * Sets the number of threads that will be used to download remote content (e.g. images) that is associated with
	 * the parsed input
	 *
	 * <i>Defaults to 8</i>
	 *
	 * @return the maximum number of threads to be used for downloading content
	 */
	public final int getDownloadThreads() {
		return downloadThreads <= 0 ? 8 : downloadThreads;
	}

	@Override
	public final Nesting getNesting() {
		return nesting;
	}

	@Override
	public final void setNesting(Nesting nesting) {
		if (nesting == null) {
			nesting = Nesting.LINK;
		}
		this.nesting = nesting;
	}

	/**
	 * Assigns an {@link ExecutorService} to be parser, which will be used to manage the multiple threads that can be
	 * started. These threads are used to parse/download data from a given input and any remote resources associated with it.
	 *
	 * <em>Defaults to:</em> {@code Executors.newCachedThreadPool();}
	 *
	 * @param executorService the executor service to be used by the parser for the creation of new threads.
	 */
	public final void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	/**
	 * Returns the {@link ExecutorService} to be used by the parser for managing the multiple threads that can be
	 * started. These threads are used to parse/download data from a given input and any remote resources associated with it.
	 *
	 * <em>Defaults to:</em> {@code Executors.newCachedThreadPool();}
	 *
	 * @return the executor service to be used by the parser for the creation of new threads.
	 */
	public final ExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newCachedThreadPool();
		}
		return this.executorService;
	}

	@Override
	public void ignoreFollowingErrors(boolean ignoreLinkFollowingErrors) {
		this.ignoreLinkFollowingErrors = ignoreLinkFollowingErrors;
	}

	@Override
	public boolean isIgnoreFollowingErrors() {
		return ignoreLinkFollowingErrors;
	}

	@Override
	protected RemoteParserSettings<S, L, C> clone() {
		RemoteParserSettings<S, L, C> out = (RemoteParserSettings) super.clone();
		out.paginator = null;
		out.fileNamePattern = null;
		return out;
	}

	/**
	 * Returns the minimum interval of time to wait between remote requests. This is required to prevent submitting
	 * multiple requests to the same server at the same time, which can easily happen when {@link RemoteFollower}s are
	 * used.
	 *
	 * <em>Defaults to 15 ms</em>
	 *
	 * @return the minimum time (in milliseconds) to wait between remote requests.
	 *         Values {@link <= 0} mean the internal {@link RateLimiter} is disabled.
	 */
	public final long getRemoteInterval() {
		return remoteInterval;
	}

	/**
	 * Defines the minimum interval of time to wait between remote requests. This is required to prevent submitting
	 * multiple requests to the same server at the same time, which can easily happen when {@link RemoteFollower}s are
	 * used.
	 *
	 * <em>Defaults to 15 ms</em>
	 *
	 * @param remoteInterval minimum time (in milliseconds) to wait between remote requests.
	 *                       Any value {@link <= 0} will disable the internal {@link RateLimiter}.
	 */
	public final void setRemoteInterval(long remoteInterval) {
		this.remoteInterval = remoteInterval;
	}
}
