/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.annotations.*;
import com.univocity.parsers.common.fields.*;
import com.univocity.parsers.common.processor.core.*;

import java.util.*;


/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public abstract class EntitySettings<C extends Context, S extends CommonSettings, G extends EntityParserSettings> {

	private final S internalSettings;
	protected G globalSettings;

	private boolean localNullValue;
	private boolean localProcessorErrorHandler;
	private boolean localErrorContentLength;
	private boolean localTrimLeading;
	private boolean localTrimTrailing;

	protected final String name;
	protected Processor<C> processor;

	protected EntitySettings(String name, S internalSettings) {
		this.name = name;
		this.internalSettings = internalSettings;
	}

	protected void setGlobalSettings(G globalSettings) {
		this.globalSettings = globalSettings;
	}

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
	 * Returns the String representation of a null value (defaults to null)
	 * <p>When reading, if the parser does not read any character from the input, the nullValue is used instead of an empty string
	 * <p>When writing, if the writer has a null object to write to the output, the nullValue is used instead of an empty string
	 *
	 * @return the String representation of a null value
	 */
	public String getNullValue() {
		if (localNullValue || globalSettings == null) {
			return internalSettings.getNullValue();
		} else {
			return globalSettings.globalSettings.getNullValue();
		}
	}

	/**
	 * Sets the String representation of a null value (defaults to null)
	 * <p>When reading, if the parser does not read any character from the input, the nullValue is used instead of an empty string
	 * <p>When writing, if the writer has a null object to write to the output, the nullValue is used instead of an empty string
	 *
	 * @param nullValue the String representation of a null value
	 */
	public void setNullValue(String nullValue) {
		localNullValue = true;
		internalSettings.setNullValue(nullValue);
	}

	/**
	 * Selects a sequence of fields for reading/writing by their names.
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@link CommonParserSettings#setColumnReorderingEnabled(boolean)} with {@code false}.</p>
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
	public FieldSet<String> selectFields(String... fieldNames) {
		return internalSettings.selectFields(fieldNames);
	}

	/**
	 * Selects fields which will not be read/written, by their names
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@link CommonParserSettings#setColumnReorderingEnabled(boolean)} with {@code false}.</p>
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
	public FieldSet<String> excludeFields(String... fieldNames) {
		return internalSettings.excludeFields(fieldNames);
	}

	/**
	 * Selects a sequence of fields for reading/writing by their positions.
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@link CommonParserSettings#setColumnReorderingEnabled(boolean)} with {@code false}.</p>
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
	public FieldSet<Integer> selectIndexes(Integer... fieldIndexes) {
		return internalSettings.selectIndexes(fieldIndexes);
	}

	/**
	 * Selects columns which will not be read/written, by their positions
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@link CommonParserSettings#setColumnReorderingEnabled(boolean)} with {@code false}.</p>
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
	public FieldSet<Integer> excludeIndexes(Integer... fieldIndexes) {
		return internalSettings.excludeIndexes(fieldIndexes);
	}

	/**
	 * Selects a sequence of fields for reading/writing by their names
	 *
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@link CommonParserSettings#setColumnReorderingEnabled(boolean)} with {@code false}.</p>
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
	public FieldSet<Enum> selectFields(Enum... columns) {
		return internalSettings.selectFields(columns);
	}

	/**
	 * Selects columns which will not be read/written, by their names
	 *
	 * <p><b>When reading</b>, only the values of the selected columns will be parsed, and the content of the other columns ignored.
	 * The resulting rows will be returned with the selected columns only, in the order specified. If you want to
	 * obtain the original row format, with all columns included and nulls in the fields that have not been selected,
	 * set {@link CommonParserSettings#setColumnReorderingEnabled(boolean)} with {@code false}.</p>
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
	public FieldSet<Enum> excludeFields(Enum... columns) {
		return internalSettings.excludeFields(columns);
	}

	/**
	 * Indicates whether this settings object can automatically derive configuration options. This is used, for example, to define the headers when the user
	 * provides a {@link BeanWriterProcessor} where the bean class contains a {@link Headers} annotation, or to enable header extraction when the bean class of a
	 * {@link BeanProcessor} has attributes mapping to header names.
	 *
	 * <p>Defaults to {@code true}</p>
	 *
	 * @return {@code true} if the automatic configuration feature is enabled, false otherwise
	 */
	public final boolean isAutoConfigurationEnabled() {
		return internalSettings.isAutoConfigurationEnabled();
	}

	/**
	 * Indicates whether this settings object can automatically derive configuration options. This is used, for example, to define the headers when the user
	 * provides a {@link BeanWriterProcessor} where the bean class contains a {@link Headers} annotation, or to enable header extraction when the bean class of a
	 * {@link BeanProcessor} has attributes mapping to header names.
	 *
	 * @param autoConfigurationEnabled a flag to turn the automatic configuration feature on/off.
	 */
	public final void setAutoConfigurationEnabled(boolean autoConfigurationEnabled) {
		this.internalSettings.setAutoConfigurationEnabled(autoConfigurationEnabled);
	}

	/**
	 * Returns the custom error handler to be used to capture and handle errors that might happen while processing records with a {@link com.univocity.parsers.common.processor.core.Processor}
	 * or a {@link RowWriterProcessor} (i.e. non-fatal {@link DataProcessingException}s).
	 *
	 * <p>The parsing/writing process won't stop (unless the error handler rethrows the {@link DataProcessingException} or manually stops the process).</p>
	 *
	 * @param <T> the {@code Context} type provided by the parser implementation.
	 *
	 * @return the callback error handler with custom code to manage occurrences of {@link DataProcessingException}.
	 */
	public <T extends Context> ProcessorErrorHandler<T> getProcessorErrorHandler() {
		if (localProcessorErrorHandler || globalSettings == null) {
			return internalSettings.getProcessorErrorHandler();
		} else {
			return globalSettings.globalSettings.getProcessorErrorHandler();
		}
	}

	/**
	 * Defines a custom error handler to capture and handle errors that might happen while processing records with a {@link com.univocity.parsers.common.processor.core.Processor}
	 * or a {@link RowWriterProcessor} (i.e. non-fatal {@link DataProcessingException}s).
	 *
	 * <p>The parsing parsing/writing won't stop (unless the error handler rethrows the {@link DataProcessingException} or manually stops the process).</p>
	 *
	 * @param processorErrorHandler the callback error handler with custom code to manage occurrences of {@link DataProcessingException}.
	 */
	public void setProcessorErrorHandler(ProcessorErrorHandler<? extends Context> processorErrorHandler) {
		localProcessorErrorHandler = true;
		internalSettings.setProcessorErrorHandler(processorErrorHandler);
	}


	/**
	 * Returns a flag indicating whether or not a {@link ProcessorErrorHandler} has been defined through the use of method {@link #setProcessorErrorHandler(ProcessorErrorHandler)}
	 *
	 * @return {@code true} if the parser/writer is configured to use a {@link ProcessorErrorHandler}
	 */
	public boolean isProcessorErrorHandlerDefined() {
		if (localProcessorErrorHandler || globalSettings == null) {
			return internalSettings.isProcessorErrorHandlerDefined();
		} else {
			return globalSettings.globalSettings.isProcessorErrorHandlerDefined();
		}
	}

	final void autoConfigure() {
		if (!internalSettings.isAutoConfigurationEnabled()) {
			return;
		}

		runAutomaticConfiguration();
	}

	/**
	 * Configures the parser/writer to limit the length of displayed contents being parsed/written in the exception message when an error occurs
	 *
	 * <p>If set to {@code 0}, then no exceptions will include the content being manipulated in their attributes,
	 * and the {@code "<omitted>"} string will appear in error messages as the parsed/written content.</p>
	 *
	 * <p>defaults to {@code -1} (no limit)</p>.
	 *
	 * @return the maximum length of contents displayed in exception messages in case of errors while parsing/writing.
	 */
	public int getErrorContentLength() {
		if (localErrorContentLength || globalSettings == null) {
			return internalSettings.getErrorContentLength();
		} else {
			return globalSettings.globalSettings.getErrorContentLength();
		}
	}

	/**
	 * Configures the parser/writer to limit the length of displayed contents being parsed/written in the exception message when an error occurs.
	 *
	 * <p>If set to {@code 0}, then no exceptions will include the content being manipulated in their attributes,
	 * and the {@code "<omitted>"} string will appear in error messages as the parsed/written content.</p>
	 *
	 * <p
	 * >defaults to {@code -1} (no limit)</p>.
	 *
	 * @param errorContentLength maximum length of contents displayed in exception messages in case of errors while parsing/writing.
	 */
	public void setErrorContentLength(int errorContentLength) {
		localErrorContentLength = true;
		internalSettings.setErrorContentLength(errorContentLength);
	}

	protected void runAutomaticConfiguration() {
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

	public String toString() {
		return name;
	}

	public Processor<C> getProcessor() {
		return processor == null ? NoopProcessor.instance : processor;
	}

	public void setProcessor(Processor<C> processor) {
		this.processor = processor;
	}

	public boolean isTrimTrailingWhitespaces() {
		if(localTrimTrailing || globalSettings == null){
			return internalSettings.getIgnoreTrailingWhitespaces();
		}
		return globalSettings.globalSettings.getIgnoreTrailingWhitespaces();
	}

	public void setTrimTrailingWhitespaces(boolean ignoreTrailingWhitespaces) {
		localTrimTrailing = true;
		internalSettings.setIgnoreTrailingWhitespaces(ignoreTrailingWhitespaces);
	}

	public boolean isTrimLeadingWhitespaces() {
		if(localTrimLeading || globalSettings == null){
			return internalSettings.getIgnoreLeadingWhitespaces();
		}
		return globalSettings.globalSettings.getIgnoreLeadingWhitespaces();
	}

	public void setTrimLeadingWhitespaces(boolean ignoreLeadingWhitespaces) {
		localTrimLeading = true;
		internalSettings.setIgnoreLeadingWhitespaces(ignoreLeadingWhitespaces);
	}

	public void trimValues(boolean trim){
		localTrimTrailing = true;
		localTrimLeading = true;
		internalSettings.trimValues(trim);
	}

}
