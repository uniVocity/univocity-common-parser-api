package com.univocity.parsers.remote;


import java.lang.annotation.*;

/**
 * A annotation that allows configuration options to assist the graphical user interface in displaying components associated
 * with the method.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UIConfig {

	int DEFAULT_ORDER = 100;

	/**
	 * Defines if the method will be shown in the GUI.
	 *
	 * @return true if GUI will show method, false otherwise
	 */
	boolean show() default true;

	/**
	 * Defines the order in which the method will be shown in the GUI. A lower order number means that the method will be shown
	 * near the top of the interface, while a higher order number will be shown near the bottom.
	 *
	 * @return an int representing the position of the method
	 */
	int order() default DEFAULT_ORDER;
}
