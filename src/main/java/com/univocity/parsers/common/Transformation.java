/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

/**
 * A value transformation operation. Transforms a given input value to an output.
 *
 * @param <I> The input type to be converted to the output type <b>O</b>
 * @param <O> The type of output produced by a conversion applied to the input <b>I</b>.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public interface Transformation<I, O> {

	/**
	 * Converts an input value of type <b>I</b> to an output value of type <b>O</b>.
	 *
	 * @param input the input of type <b>I</b> to be converted to an object of type <b>O</b>
	 *
	 * @return the conversion result.
	 */
	O transform(I input);
}
