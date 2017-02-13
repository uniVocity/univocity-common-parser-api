/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.common.*;

import java.util.*;

/**
 * FIXME: javadoc
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public final class Results<R extends Result> extends LinkedHashMap<String, R> {

	private final Set<String> originalKeyNames = new TreeSet<String>();

	public final R join(String masterEntity, String entityToLink, String... otherEntitiesToLink) {
		R master = get(masterEntity);
		R linked = get(entityToLink);
		R out = (R) master.join(linked);
		for (String entityName : otherEntitiesToLink) {
			out = (R) out.join(get(entityName));
		}
		return out;
	}

	public final R put(String entityName, R result) {
		Args.notEmpty(entityName, "Entity name");
		Args.notNull(result, "Result of entity '" + entityName + "'");
		originalKeyNames.add(entityName);
		return super.put(entityName.trim().toLowerCase(), result);
	}

	public final R get(String entityName) {
		Args.notEmpty(entityName, "Entity name");
		R results = super.get(entityName);
		if (results == null) {
			String normalized = String.valueOf(entityName).trim().toLowerCase();
			results = super.get(normalized);
			if (results == null) {
				throw new IllegalArgumentException("Entity name '" + entityName + "' does not exist. Available entities are: " + originalKeyNames);
			}
		}
		return results;
	}
}
