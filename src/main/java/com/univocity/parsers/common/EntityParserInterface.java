/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.io.*;
import com.univocity.parsers.common.processor.core.*;
import com.univocity.parsers.common.record.*;

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
public interface EntityParserInterface<R extends Record, C extends Context, T extends Result<R, C>> {

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
	Map<String, T> parse(ReaderProvider readerProvider);

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
	Map<String, T> parse(FileProvider fileProvider);

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
	Map<String, T> parse(Reader reader);

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
	Map<String, T> parse(InputStream inputStream);

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
	Map<String, T> parse(InputStream inputStream, Charset encoding);

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
	Map<String, T> parse(InputStream inputStream, String encoding);

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
	Map<String, T> parse(File file);

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
	Map<String, T> parse(File file, Charset encoding);

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
	Map<String, T> parse(File file, String encoding);

}
