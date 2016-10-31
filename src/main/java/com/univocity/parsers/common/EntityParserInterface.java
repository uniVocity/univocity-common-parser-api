/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.io.*;
import com.univocity.parsers.common.processor.core.*;
import com.univocity.parsers.common.record.*;
import com.univocity.parsers.remote.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

/**
 * An interface used by parsers that support multiple entities. All parsers should work with a concrete implementation
 * of a {@link EntityParserSettings} object that provides configuration options for the given parser. The definition of
 * entities and their configuration should be managed by an implementation of {@link EntityList}, which provides
 * individual {@link EntitySettings} to allow user to configure how the records of each individual entity should be
 * handled.
 *
 * At a minimum, all parser implementations must provide the following ways to handle records parsed from a given input:
 *
 * <ul>
 * <li>Using the <b>parseAll*</b> methods which parse the input in its entirety and accumulate all records collected
 * for all entities in memory. These methods return a map of entity names and their corresponding lists of simple
 * {@code String} arrays.</li>
 * <li>The <b>parseAllRecords*</b> methods work in a similar fashion as the previous method, but lists of more useful
 * {@link Record} objects are provided, which provide convenient data manipulation operations</li>
 * <li>Finally, the <b>parse*</b> methods will stream every record of every entity to an associated {@link Processor}
 * implementation as soon as it the record is generated. This won't accumulate results in memory (unless the
 * {@link Processor} itself does so) and is the preferred way to process larger inputs as fast as possible.</li>
 * </ul>
 *
 * If a {@link Processor} is assigned to a given entity, it will <b>always</b> be triggered for each parsed record,
 * regardless of which one of the aforementioned parsing methods are used.
 *
 * Concrete parser implementations may provide additional operations.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see EntityList
 * @see EntityParserSettings
 * @see EntitySettings
 * @see ReaderProvider
 * @see FileProvider
 * @see Record
 */
public interface EntityParserInterface {

	/**
	 * Given an input, made available from a {@link ReaderProvider}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * @param readerProvider an input provider with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(ReaderProvider readerProvider);

	/**
	 * Given an input file, made available from a {@link FileProvider}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * @param fileProvider the input file with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(FileProvider fileProvider);

	/**
	 * Given a {@link java.io.Reader}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * @param reader the input with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(Reader reader);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * <i>The default system encoding will be used to read text from the given input.</i>
	 *
	 * @param inputStream the input with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(InputStream inputStream);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * @param inputStream the input with content to be parsed
	 * @param encoding    the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(InputStream inputStream, Charset encoding);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * @param inputStream the input with content to be parsed
	 * @param encoding    the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(InputStream inputStream, String encoding);


	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * <i>The default system encoding will be used to read text from the given input.</i>
	 *
	 * @param file the input with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(File file);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * @param file     the input with content to be parsed
	 * @param encoding the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(File file, Charset encoding);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of rows produced for that entity.
	 *
	 * @param file     the input with content to be parsed
	 * @param encoding the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<String[]>> parseAll(File file, String encoding);


	/**
	 * Given an input, made available from a {@link ReaderProvider}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * @param readerProvider an input provider with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(ReaderProvider readerProvider);

	/**
	 * Given an input file, made available from a {@link FileProvider}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * @param fileProvider an input file with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(FileProvider fileProvider);

	/**
	 * Given a {@link java.io.Reader}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * @param reader the input with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(Reader reader);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * <i>The default system encoding will be used to read text from the given input.</i>
	 *
	 * @param inputStream the input with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(InputStream inputStream);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * @param inputStream the input with content to be parsed
	 * @param encoding    the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(InputStream inputStream, Charset encoding);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * @param inputStream the input with content to be parsed
	 * @param encoding    the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(InputStream inputStream, String encoding);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map. Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * <i>The default system encoding will be used to read text from the given input.</i>
	 *
	 * @param file the input with content to be parsed
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(File file);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map.  Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * @param file     the input with content to be parsed
	 * @param encoding the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(File file, Charset encoding);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, and returns them in a map.  Keys are the entity names
	 * and values are lists of {@link Record} produced for that entity.
	 *
	 * @param file     the input with content to be parsed
	 * @param encoding the encoding to be used when reading text from the given input.
	 *
	 * @return a map of entity names and the corresponding records extracted from the given input.
	 */
	Map<String, List<Record>> parseAllRecords(File file, String encoding);

	/**
	 * Given an input, made available from a {@link ReaderProvider}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * @param readerProvider an input provider with content to be parsed
	 */
	void parse(ReaderProvider readerProvider);

	/**
	 * Given an input, made available from a {@link FileProvider}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * @param fileProvider the input file with content to be parsed
	 */
	void parse(FileProvider fileProvider);

	/**
	 * Given a {@link java.io.Reader}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * @param reader the input with content to be parsed
	 */
	void parse(Reader reader);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * <i>The default system encoding will be used to read text from the given input.</i>
	 *
	 * @param inputStream the input with content to be parsed
	 */
	void parse(InputStream inputStream);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * @param inputStream the input with content to be parsed
	 * @param encoding    the encoding to be used when reading text from the given input.
	 */
	void parse(InputStream inputStream, Charset encoding);

	/**
	 * Given an {@link java.io.InputStream}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * @param inputStream the input with content to be parsed
	 * @param encoding    the encoding to be used when reading text from the given input.
	 */
	void parse(InputStream inputStream, String encoding);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * <i>The default system encoding will be used to read text from the given input.</i>
	 *
	 * @param file the input with content to be parsed
	 */
	void parse(File file);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * @param file     the input with content to be parsed
	 * @param encoding the encoding to be used when reading text from the given input.
	 */
	void parse(File file, Charset encoding);

	/**
	 * Given a {@link java.io.File}, parses all records of all entities
	 * defined in the {@link EntityList} of this parser, submitting them to the {@link Processor} implementation
	 * associated with each entity (through {@link EntitySettings#setProcessor(Processor)}. The {@link Processor}
	 * implementation will handle the rows as they come, in its {@link Processor#rowProcessed(String[], Context)} method
	 * which can accumulate/transform the rows on demand. The behavior and way to collect results is determined by
	 * the {@link Processor} implementation used.
	 *
	 * @param file     the input with content to be parsed
	 * @param encoding the encoding to be used when reading text from the given input.
	 */
	void parse(File file, String encoding);

	/**
	 * Returns the {@link PaginationContext} object with information collected for the configured {@link Paginator}, if
	 * any. The information returned comes from the last input processed, and might have been modified by a
	 * {@link PaginationHandler} if it has been associated with the {@link Paginator}
	 * using {@link Paginator#setPaginationHandler(PaginationHandler)}.
	 *
	 * @return the current {@link PaginationContext} with pagination information captured after parsing a given input.
	 */
	PaginationContext getPaginationContext();
}
