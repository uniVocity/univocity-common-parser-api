package com.univocity.api.common.remote;

import com.univocity.api.common.*;
import com.univocity.parsers.common.*;
import com.univocity.parsers.common.Format;
import com.univocity.parsers.common.processor.core.*;

import java.io.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;

/**
 * Created by anthony on 21/07/16.
 */
public abstract class RemoteResourceSettings<T extends RemoteResourceEntityList> extends AbstractEntityParserSettings{

	protected Processor<Context> processor;
	protected final T entityList;
	protected FileProvider downloadContentDirectory;
	protected int downloadThreads;
	protected String fileNamePattern;


	/**
	 * Creates a new HtmlParserSettings with a supplied {@link HtmlEntityList}. The {@link HtmlEntityList} is used to
	 * store {@link HtmlEntity}'s which define the specific HTML elements that will be parsed.
	 *
	 * @param
	 */
	public RemoteResourceSettings(T entityList) {
		this.entityList = entityList;
	}

	/**
	 * Returns the {@link HtmlEntityList} associated with the settings.
	 *
	 * @return the HtmlEntityList
	 */
	public T getEntityList() {
		return entityList;
	}

	/**
	 * Sets the global {@link Processor}. All rows parsed will be delegated to the processor. Does not
	 * need to be set for the {@link HtmlParser} to function.
	 *
	 * @param processor the {@link Processor} that will be set as the processor
	 */
	public void setGlobalProcessor(Processor<Context> processor) {
		this.processor = processor;
	}

	/**
	 * Returns the processor if previously set. If the processor was not set, returns a
	 * {@link NoopProcessor}, which is a {@link Processor} that does nothing.
	 *
	 * @return the {@link Processor} previously set, or a {@link NoopProcessor} if never set.
	 */
	public Processor<Context> getGlobalProcessor() {
		return processor == null ? NoopProcessor.instance : processor;
	}

	@Override
	protected Format createDefaultFormat() {
		return new Format() {
			@Override
			protected TreeMap<String, Object> getConfiguration() {
				return new TreeMap<String, Object>();
			}
		};
	}

	/**
	 * Returns the entity names contained in the associated {@link HtmlEntityList} as a set of Strings.
	 * @return the entity names
	 */
	public Set<String> getEntityNames() {
		return entityList.getEntityNames();
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
	 * @param file the directory that stores downloaded content
	 * @param encoding the encoding the directory as a Charset
	 */
	public void setDownloadContentDirectory(File file, Charset encoding) {
		downloadContentDirectory = new FileProvider(file, encoding);
	}

	/**
	 * Sets the file directory where downloaded content will be stored
	 *
	 * @param file The directory that stores downloaded content
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
	 * @return the pattern of file names
	 */
	public String getFileNamePattern() {
		return fileNamePattern;
	}


}