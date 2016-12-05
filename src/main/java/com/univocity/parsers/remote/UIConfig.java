package com.univocity.parsers.remote;


import java.lang.annotation.*;

/**
 * A annotation that allows configuration options to assist the graphical user interface in displaying components associated
 * with the method.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UIConfig {

	int DEFAULT_ORDER = Integer.MAX_VALUE;

	/**
	 * Specifies what will be displayed as the name of the method in the GUI. If no label is specified, the method's name
	 * (with better formatting) will be used.
	 *
	 * @return the string representation of what the GUI will use to identify the method.
	 */
	String label() default "";

	/**
	 * Defines the order in which the method will be shown in the GUI. A lower order number means that the method will be shown
	 * near the top of the interface, while a higher order number will be shown near the bottom.
	 *
	 * @return an int representing the position of the method
	 */
	int order() default DEFAULT_ORDER;
}
