package com.univocity.parsers.remote;

import java.lang.annotation.*;

/**
 * Defines a range of values that a method's parameters may take. Used for the GUI to define what values are selectable
 * by the user.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

	/**
	 * Defines the minimum value of method's parameter.
	 *
	 * @return the smallest acceptable value for the parameter
	 */
	int minSize();

	/**
	 * Defines the maximum value of method's parameter.
	 *
	 * @return the largest acceptable value for the parameter
	 */
	int maxSize();
}
