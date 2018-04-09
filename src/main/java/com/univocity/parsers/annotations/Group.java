/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */
package com.univocity.parsers.annotations;

import java.lang.annotation.*;
import java.util.*;

/**
 * Links an attribute of type {@link Map} to the the results of an entity available in a {@link com.univocity.parsers.common.Results} instance.
 *
 * A {@link Linked} annotation is required along this {@code Group} annotation, to configure how the values of the map should be populated.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:parsers@univocity.com">parsers@univocity.com</a>
 * @see Linked
 * @see com.univocity.parsers.common.Results
 * @see com.univocity.parsers.common.Result
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Group {

	/**
	 * The concrete type of the keys put into this map. Instances will be created from the records of the linked entity.
	 *
	 * @return the type of key to be added to the map.
	 */
	Class key() default Object.class;

	/**
	 * Defines the concrete type of the map to be instantiated. By default, if the attribute type is not a concrete map type,
	 * a {@link HashMap} will be instantiated and assigned to the attribute.
	 *
	 * @return the type of map to be created for the annotated attribute.
	 */
	Class<? extends java.util.Map> container() default java.util.Map.class;

}
