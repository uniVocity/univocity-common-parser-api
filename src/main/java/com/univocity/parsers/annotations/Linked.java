/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */


package com.univocity.parsers.annotations;

import java.lang.annotation.*;

/**
 * Links an attribute to the the results of an entity available in a {@link com.univocity.parsers.common.Results} instance.
 * Regular attributes of any type are allowed if the results associated are expected to contain a single value. If there
 * are multiple results the annotated attribute can be declared as an array or a collection type.
 *
 * If linking to a {@link java.util.Map}, you must add the {@link Group} annotation to the attribute to configure how the
 * keys should be populated. The configuration in this {@code Linked} annotation will be used to populate the values of the map.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:parsers@univocity.com">parsers@univocity.com</a>
 * @see Group
 * @see com.univocity.parsers.common.Results
 * @see com.univocity.parsers.common.Result
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD})
public @interface Linked {

	/**
	 * The name of the entity in the {@link com.univocity.parsers.common.Results} instance whose rows will be read
	 * to generate instances of the given {@link #type()}. If empty, the name of the attribute will be used as the
	 * entity name.
	 *
	 * @return name of the entity whose results should be linked to the parent instance.
	 */
	String entity() default "";

	/**
	 * The type of object that should be created for each {@link com.univocity.parsers.common.Result} associated with
	 * the given {@link #entity()} name. Defaults to the actual attribute type. A type must be provided explicitly if
	 * the attribute is a collection.
	 *
	 * @return the type to be instantiated for each row linked to the parent instance.
	 */
	Class type() default Object.class;

	/**
	 * Provides the type of the collection to be instantiated for this linked attribute (if applicable). The following
	 * defaults apply if the type of this attribute is not a concrete type:
	 *
	 * <ul>
	 * <li>Attribute of type {@link java.util.Set} or {@link java.util.Collection} initialized with a new {@link java.util.HashSet}</li>
	 * <li>Attribute of type {@link java.util.List} initialized with a new {@link java.util.ArrayList}</li>
	 * </ul>
	 *
	 * @return the concrete collection type to create for storing linked results of the given {@link #type()}
	 */
	Class container() default Object.class;

}
