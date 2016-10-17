/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.annotations.*;
import com.univocity.parsers.common.fields.*;
import com.univocity.parsers.common.processor.*;
import com.univocity.parsers.common.processor.core.*;

import java.util.*;


/**
 * Manages configuration options for individual entities of an {@link EntityList}. Settings that also exist in
 * the parent {@link EntityParserSettings} will be used by default but can be overridden for an individual entity.
 *
 * @param <C> the type of {@link Context} implementation supported by {@link Processor}s of this entity.
 * @param <S> internal implementation of a {@link CommonSettings}, used to manage configuration of elements shared
 *            with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>
 * @param <G> type of the global configuration class (an instance of {@link EntityParserSettings}, used to configure
 *            the parser (a concrete implementation of {@link EntityParserInterface}) and its entities.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see EntityList
 * @see EntityParserInterface
 * @see EntityParserSettings
 */
public abstract class EntitySettings<C extends Context, S extends CommonSettings, G extends EntityParserSettings> {

	private final S internalSettings;
	protected G parserSettings;

	private boolean localNullValue;
	private boolean localProcessorErrorHandler;
	private boolean localErrorContentLength;
	private boolean localTrimLeading;
	private boolean localTrimTrailing;

	protected final String name;
	protected Processor<C> processor;

	/**
	 * Internal constructor to be invoked the subclasses of {@code EntitySettings}
	 *
	 * @param name             the entity name, usually provided by the user
	 * @param internalSettings an internal implementation of a {@link CommonSettings}, used to manage configuration
	 *                         of elements shared with <a href="http://www.univocity.com/pages/about-parsers">univocity-parsers</a>.
	 *                         Not meant to be exposed/accessed directly by users.
	 */
	protected EntitySettings(String name, S internalSettings) {
		this.name = name;
		this.internalSettings = internalSettings;
	}

	/**
	 * Used to "inherit" default settings of a parent {@link EntityParserSettings}. Used internally in
	 * in the constructor of {@link EntityParserSettings} (which is the global setting object itself).
	 *
	 * @param parserSettings the {@link EntityParserSettings} that "owns" all entities and their {@code EntitySettings}
	 */
	protected final void setParserSettings(G parserSettings) {
		this.parserSettings = parserSettings;
	}

	/**
	 * Utility method to create new, empty instances of {@link CommonParserSettings}. For internal use only.
	 *
	 * @return a new instance of a {@link CommonParserSettings} class.
	 */
	protected static final CommonParserSettings createEmptyParserSettings() {
		return new CommonParserSettings() {
			@Override
			protected Format createDefaultFormat() {
				return new Format() {
					@Override
					protected TreeMap<String, Object> getConfiguration() {
						return new TreeMap<String, Object>();
					}
				};
			}
		};
	}

	/**
	 * Returns the {@code String} representation of a null value (defaults to {@link EntityParserSettings#globalSettings#getNullValue()})
	 * <p>When reading, if the parser does not read any character from the input for a particular value, the nullValue
	 * is used instead of an empty {@code String}</p>
	 * <p>When writing, if the writer has a {@code null} object to write to the output, the nullValue is used instead
	 * of an empty {@code String}</p>
	 *
	 * @return the String representation of a null value
	 */
	public final String getNullValue() {
		if (localNullValue || parserSettings == null) {
			return internalSettings.getNullValue();
		} else {
			return parserSettings.globalSettings.getNullValue();
		}
	}

	/**
	 * Sets the {@code String} representation of a null value (defaults to {@link EntityParserSettings#globalSettings#getNullValue()})
	 * <p>When reading, if the parser does not read any character from the input for a particular value, the nullValue
	 * is used instead of an empty {@code String}</p>
	 * <p>When writing, if the writer has a {@code null} object to write to the output, the nullValue is used instead
	 * of an empty {@code String}</p>
	 *
	 * @param nullValue the String representation of a null value
	 */
	public final void setNullValue(String nullValue) {
		localNullValue = true;
		internalSettings.setNullValue(nullValue);
	}

	/**
	 * Selects a sequence of fields for reading/writing by their names.
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@code setColumnReorderingEnabled(boolean)} with {@code false} if available.</p>
	 *
	 * <p><b>When writing</b>, the sequence provided represents the expected format of the input rows. For example,
	 * headers can be "H1,H2,H3", but the input data is coming with values for two columns and in a different order,
	 * such as "V_H3, V_H1". Selecting fields "H3" and "H1" will allow the writer to write values in the expected
	 * locations. Using the given example, the output row will be generated as: "V_H1,null,V_H3"</p>
	 *
	 * @param fieldNames The field names to read/write
	 *
	 * @return the (modifiable) set of selected fields
	 */
	public final FieldSet<String> selectFields(String... fieldNames) {
		return internalSettings.selectFields(fieldNames);
	}

	/**
	 * Selects fields which will not be read/written, by their names
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@code setColumnReorderingEnabled(boolean)} with {@code false} if available.</p>
	 *
	 * <p><b>When writing</b>, the sequence of non-excluded fields represents the expected format of the input rows. For example,
	 * headers can be "H1,H2,H3", but the input data is coming with values for two columns and in a different order,
	 * such as "V_H3, V_H1". Selecting fields "H3" and "H1" will allow the writer to write values in the expected
	 * locations. Using the given example, the output row will be generated as: "V_H1,null,V_H3"</p>
	 *
	 * @param fieldNames The field names to exclude from the parsing/writing process
	 *
	 * @return the (modifiable) set of ignored fields
	 */
	public final FieldSet<String> excludeFields(String... fieldNames) {
		return internalSettings.excludeFields(fieldNames);
	}

	/**
	 * Selects a sequence of fields for reading/writing by their positions.
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@code setColumnReorderingEnabled(boolean)} with {@code false} if available.</p>
	 *
	 * <p><b>When writing</b>, the sequence provided represents the expected format of the input rows. For example,
	 * headers can be "H1,H2,H3", but the input data is coming with values for two columns and in a different order,
	 * such as "V_H3, V_H1". Selecting indexes "2" and "0" will allow the writer to write values in the expected
	 * locations. Using the given example, the output row will be generated as: "V_H1,null,V_H3"</p>
	 *
	 * @param fieldIndexes The indexes to read/write
	 *
	 * @return the (modifiable) set of selected fields
	 */
	public final FieldSet<Integer> selectIndexes(Integer... fieldIndexes) {
		return internalSettings.selectIndexes(fieldIndexes);
	}

	/**
	 * Selects columns which will not be read/written, by their positions
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@code setColumnReorderingEnabled(boolean)} with {@code false} if available.</p>
	 *
	 * <p><b>When writing</b>, the sequence of non-excluded fields represents the expected format of the input rows. For example,
	 * headers can be "H1,H2,H3", but the input data is coming with values for two columns and in a different order,
	 * such as "V_H3, V_H1". Selecting fields by index, such as  "2" and "0" will allow the writer to write values in the expected
	 * locations. Using the given example, the output row will be generated as: "V_H1,null,V_H3"</p>
	 *
	 * @param fieldIndexes indexes of columns to exclude from the parsing/writing process
	 *
	 * @return the (modifiable) set of ignored fields
	 */
	public final FieldSet<Integer> excludeIndexes(Integer... fieldIndexes) {
		return internalSettings.excludeIndexes(fieldIndexes);
	}

	/**
	 * Selects a sequence of fields for reading/writing by their names
	 *
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@code setColumnReorderingEnabled(boolean)} with {@code false} if available.</p>
	 *
	 * <p><b>When writing</b>, the sequence provided represents the expected format of the input rows. For example,
	 * headers can be "H1,H2,H3", but the input data is coming with values for two columns and in a different order,
	 * such as "V_H3, V_H1". Selecting fields "H3" and "H1" will allow the writer to write values in the expected
	 * locations. Using the given example, the output row will be generated as: "V_H1,null,V_H3"</p>
	 *
	 * @param columns The columns to read/write
	 *
	 * @return the (modifiable) set of selected fields
	 */
	@SuppressWarnings("rawtypes")
	public final FieldSet<Enum> selectFields(Enum... columns) {
		return internalSettings.selectFields(columns);
	}

	/**
	 * Selects columns which will not be read/written, by their names
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@code setColumnReorderingEnabled(boolean)} with {@code false} if available.</p>
	 *
	 * <p><b>When writing</b>, the sequence of non-excluded fields represents the expected format of the input rows. For example,
	 * headers can be "H1,H2,H3", but the input data is coming with values for two columns and in a different order,
	 * such as "V_H3, V_H1". Selecting fields "H3" and "H1" will allow the writer to write values in the expected
	 * locations. Using the given example, the output row will be generated as: "V_H1,null,V_H3"</p>
	 *
	 * @param columns The columns to exclude from the parsing/writing process
	 *
	 * @return the (modifiable) set of ignored fields
	 */
	@SuppressWarnings("rawtypes")
	public final FieldSet<Enum> excludeFields(Enum... columns) {
		return internalSettings.excludeFields(columns);
	}

	/**
	 * Indicates whether this settings object can automatically derive configuration options. This is used, for example,
	 * to define the headers when the user provides a {@link BeanWriterProcessor} where the bean class contains a
	 * {@link Headers} annotation, or to enable header extraction when the bean class of a {@link BeanProcessor} has
	 * attributes mapping to header names.
	 *
	 * <p>Defaults to {@code true}</p>
	 *
	 * @return {@code true} if the automatic configuration feature is enabled, false otherwise
	 */
	public final boolean isAutoConfigurationEnabled() {
		return internalSettings.isAutoConfigurationEnabled();
	}

	/**
	 * Indicates whether this settings object can automatically derive configuration options. This is used, for example,
	 * to define the headers when the user provides a {@link BeanWriterProcessor} where the bean class contains a
	 * {@link Headers} annotation, or to enable header extraction when the bean class of a
	 * {@link BeanProcessor} has attributes mapping to header names.
	 *
	 * @param autoConfigurationEnabled a flag to turn the automatic configuration feature on/off.
	 */
	public final void setAutoConfigurationEnabled(boolean autoConfigurationEnabled) {
		this.internalSettings.setAutoConfigurationEnabled(autoConfigurationEnabled);
	}

	/**
	 * Returns the custom error handler to be used to capture and handle errors that might happen while processing
	 * records with a {@link com.univocity.parsers.common.processor.core.Processor}
	 * or a {@link RowWriterProcessor} (i.e. non-fatal {@link DataProcessingException}s).
	 *
	 * <p>The parsing/writing process won't stop (unless the error handler rethrows the {@link DataProcessingException}
	 * or manually stops the process).</p>
	 *
	 * @param <T> the {@code Context} type provided by the parser implementation.
	 *
	 * @return the callback error handler with custom code to manage occurrences of {@link DataProcessingException}.
	 */
	public final <T extends Context> ProcessorErrorHandler<T> getProcessorErrorHandler() {
		if (localProcessorErrorHandler || parserSettings == null) {
			return internalSettings.getProcessorErrorHandler();
		} else {
			return parserSettings.globalSettings.getProcessorErrorHandler();
		}
	}

	/**
	 * Defines a custom error handler to capture and handle errors that might happen while processing records with
	 * a {@link com.univocity.parsers.common.processor.core.Processor}
	 * or a {@link RowWriterProcessor} (i.e. non-fatal {@link DataProcessingException}s).
	 *
	 * <p>The parsing parsing/writing won't stop (unless the error handler rethrows the {@link DataProcessingException}
	 * or manually stops the process).</p>
	 *
	 * @param processorErrorHandler the callback error handler with custom code to manage occurrences of {@link DataProcessingException}.
	 */
	public final void setProcessorErrorHandler(ProcessorErrorHandler<? extends Context> processorErrorHandler) {
		localProcessorErrorHandler = true;
		internalSettings.setProcessorErrorHandler(processorErrorHandler);
	}


	/**
	 * Returns a flag indicating whether or not a {@link ProcessorErrorHandler} has been defined through the use of
	 * method {@link #setProcessorErrorHandler(ProcessorErrorHandler)}
	 *
	 * @return {@code true} if the parser/writer is configured to use a {@link ProcessorErrorHandler}
	 */
	public final boolean isProcessorErrorHandlerDefined() {
		if (localProcessorErrorHandler || parserSettings == null) {
			return internalSettings.isProcessorErrorHandlerDefined();
		} else {
			return parserSettings.globalSettings.isProcessorErrorHandlerDefined();
		}
	}

	final void autoConfigure() {
		if (!internalSettings.isAutoConfigurationEnabled()) {
			return;
		}

		runAutomaticConfiguration();
	}

	/**
	 * Configures the parser/writer to limit the length of displayed contents being parsed/written in
	 * the exception message when an error occurs
	 *
	 * <p>If set to {@code 0}, then no exceptions will include the content being manipulated in their attributes,
	 * and the {@code "<omitted>"} string will appear in error messages as the parsed/written content.</p>
	 *
	 * <p>defaults to {@code -1} (no limit)</p>.
	 *
	 * @return the maximum length of contents displayed in exception messages in case of errors while parsing/writing.
	 */
	public final int getErrorContentLength() {
		if (localErrorContentLength || parserSettings == null) {
			return internalSettings.getErrorContentLength();
		} else {
			return parserSettings.globalSettings.getErrorContentLength();
		}
	}

	/**
	 * Configures the parser/writer to limit the length of displayed contents being parsed/written in the exception
	 * message when an error occurs.
	 *
	 * <p>If set to {@code 0}, then no exceptions will include the content being manipulated in their attributes,
	 * and the {@code "<omitted>"} string will appear in error messages as the parsed/written content.</p>
	 *
	 * <p
	 * >defaults to {@code -1} (no limit)</p>.
	 *
	 * @param errorContentLength maximum length of contents displayed in exception messages in case of errors
	 *                           while parsing/writing.
	 */
	public final void setErrorContentLength(int errorContentLength) {
		localErrorContentLength = true;
		internalSettings.setErrorContentLength(errorContentLength);
	}

	protected final void runAutomaticConfiguration() {
		internalSettings.runAutomaticConfiguration();
	}

	protected S getInternalSettings() {
		return internalSettings;
	}

	/**
	 * Returns the name of the HTMLEntity
	 *
	 * @return the name as a String
	 */
	public final String getEntityName() {
		return name;
	}

	public final String toString() {
		return name;
	}

	/**
	 * Returns the current implementation of {@link Processor} to be used to process rows generated for this
	 * entity. If no processor has been defined, a {@link NoopProcessor} will be returned.
	 *
	 * @return the current {@link Processor}
	 *
	 * @see com.univocity.parsers.common.processor
	 */
	public final Processor<C> getProcessor() {
		return processor == null ? NoopProcessor.instance : processor;
	}

	/**
	 * Defines an implementation of {@link Processor} to be used to process rows generated for this
	 * entity.
	 *
	 * @param processor the {@link Processor} implementation.
	 *
	 * @see com.univocity.parsers.common.processor
	 */
	public final void setProcessor(Processor<C> processor) {
		this.processor = processor;
	}

	/**
	 * Returns whether or not trailing whitespaces from values being read/written should be trimmed (defaults to {@code true})
	 *
	 * @return {@code true} if trailing whitespaces from values being read/written should be trimmed, {@code false} otherwise
	 */
	public final boolean getTrimTrailingWhitespaces() {
		if (localTrimTrailing || parserSettings == null) {
			return internalSettings.getIgnoreTrailingWhitespaces();
		}
		return parserSettings.globalSettings.getIgnoreTrailingWhitespaces();
	}

	/**
	 * Defines whether or not trailing whitespaces from values being read/written should be trimmed (defaults to {@code true})
	 *
	 * @param trimTrailingWhitespaces flag indicating whether to remove trailing whitespaces from values being read/written
	 */
	public final void setTrimTrailingWhitespaces(boolean trimTrailingWhitespaces) {
		localTrimTrailing = true;
		internalSettings.setIgnoreTrailingWhitespaces(trimTrailingWhitespaces);
	}

	/**
	 * Returns whether or not leading whitespaces from values being read/written should be trimmed (defaults to {@code true})
	 *
	 * @return {@code true} if leading whitespaces from values being read/written should be trimmed, {@code false} otherwise
	 */
	public final boolean getTrimLeadingWhitespaces() {
		if (localTrimLeading || parserSettings == null) {
			return internalSettings.getIgnoreLeadingWhitespaces();
		}
		return parserSettings.globalSettings.getIgnoreLeadingWhitespaces();
	}

	/**
	 * Defines whether or not leading whitespaces from values being read/written should be trimmed (defaults to {@code true})
	 *
	 * @param trimLeadingWhitespaces flag indicating whether to remove leading whitespaces from values being read/written
	 */
	public final void setTrimLeadingWhitespaces(boolean trimLeadingWhitespaces) {
		localTrimLeading = true;
		internalSettings.setIgnoreLeadingWhitespaces(trimLeadingWhitespaces);
	}

	/**
	 * Configures the parser/writer to trim/keep leading and trailing whitespaces around values
	 * This has the same effect as invoking both {@link #setTrimLeadingWhitespaces(boolean)}
	 * and {@link #setTrimTrailingWhitespaces(boolean)} with the same value.
	 *
	 * @param trim a flag indicating whether whitespaces should be removed around values parsed/written.
	 */
	public final void trimValues(boolean trim) {
		localTrimTrailing = true;
		localTrimLeading = true;
		internalSettings.trimValues(trim);
	}

}
