/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.parsers.common.fields.*;

/**
 * A {@link FieldSelector} that allows selecting fields from a given entity.
 *
 * @param <T> the type of the reference information used to uniquely identify a field (e.g. references to field indexes would use Integer, while references to field names would use String).
 */
class EntityFieldSelector<T> extends EntityFieldSet<T> implements FieldSelector {

	/**
	 * Creates a new field selector for an entity, which will determined only after a call to the {@link #of(String)} method.
	 *
	 * @param fieldSet a {@link FieldSet} that manages the actual selection of fields of an entity.
	 */
	EntityFieldSelector(FieldSet<T> fieldSet) {
		super(fieldSet);
	}

	@Override
	protected final void validate() {
		if (entity == null) {
			throw new IllegalStateException("No entity associated with " + super.describe() +
					". Please use method 'of()' after selecting your columns names/indexes" +
					" (e.g. 'settings.selectFields(...).of(<entity_name>)')");
		}
	}

	@Override
	public final int[] getFieldIndexes(String[] headers) {
		return getWrappedFieldSelector().getFieldIndexes(headers);
	}
}
