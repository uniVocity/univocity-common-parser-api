/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.common.*;
import com.univocity.parsers.common.record.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

/**
 * A interface used by parsers that support multiple entities.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface EntityParserInterface {

	Map<String, List<String[]>> parseAll(ReaderProvider readerProvider);

	Map<String, List<String[]>> parseAll(FileProvider fileProvider);

	Map<String, List<String[]>> parseAll(Reader reader);

	Map<String, List<String[]>> parseAll(InputStream inputStream);

	Map<String, List<String[]>> parseAll(InputStream inputStream, Charset encoding);

	Map<String, List<String[]>> parseAll(InputStream inputStream, String encoding);

	Map<String, List<Record>> parseAllRecords(ReaderProvider readerProvider);

	Map<String, List<Record>> parseAllRecords(FileProvider fileProvider);

	Map<String, List<Record>> parseAllRecords(Reader reader);

	Map<String, List<Record>> parseAllRecords(InputStream inputStream);

	Map<String, List<Record>> parseAllRecords(InputStream inputStream, Charset encoding);

	Map<String, List<Record>> parseAllRecords(InputStream inputStream, String encoding);

	void parse(ReaderProvider readerProvider);

	void parse(FileProvider fileProvider);

	void parse(Reader reader);

	void parse(InputStream inputStream);

	void parse(InputStream inputStream, Charset encoding);

	void parse(InputStream inputStream, String encoding);
}
