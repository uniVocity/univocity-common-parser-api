/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.common;

import com.univocity.api.common.*;

import java.util.*;

/**
 * A map of "Entity Name" to {@link Result} returned from parsing with {@link EntityParserInterface#parse}
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public final class Results<R extends Result> extends LinkedHashMap<String, R> {

	private final Map<String, R> copyOfOriginalKeys = new LinkedHashMap<String, R>();

	/**
	 * Joins the results of the entity {@code entityToLink} to the results of the entity {@code masterEntity}
	 *
	 * The optional {@code otherEntitiesToLink} list other entities to join to the {@code masterEntity}
	 *
	 * @param masterEntity        the name of the entity to join all other specified entities to.
	 * @param entityToLink        the name of the entity to join to the {@code masterEntity}
	 * @param otherEntitiesToLink optional, other entities to join to the {@code masterEntity}
	 *
	 * @return the result of joining the {@code entityToLink}, and {@code otherEntitiesToLink} to {@code masterEntity}
	 */
	public final R join(String masterEntity, String entityToLink, String... otherEntitiesToLink) {
		R master = get(masterEntity);
		R linked = get(entityToLink);
		R out = (R) master.join(linked);
		for (String entityName : otherEntitiesToLink) {
			out = (R) out.join(get(entityName));
		}
		return out;
	}

	/**
	 * Links the results of the entity {@code entityToLink} to the results of the entity {@code masterEntity}
	 *
	 * The optional {@code otherEntitiesToLink} list other entities to link to the {@code masterEntity}
	 *
	 * @param masterEntity        the name of the entity to link all other specified entities to.
	 * @param entityToLink        the name of the entity to link to the {@code masterEntity}
	 * @param otherEntitiesToLink optional, other entities to link to the {@code masterEntity}
	 */
	public final void link(String masterEntity, String entityToLink, String... otherEntitiesToLink) {
		R master = get(masterEntity);
		R linked = get(entityToLink);
		master.link(linked);
		for (String entityName : otherEntitiesToLink) {
			master.link(get(entityName));
		}
	}

	private String getValidatedKey(Object entityName) {
		Args.notNull(entityName, "Entity name");

		String key = entityName.toString().trim();
		Args.notBlank(key, "Entity name");

		return key.toLowerCase();
	}

	/**
	 * Puts the {@code result} into the map, associated with {@code entityName}
	 *
	 * @param entityName the name of the entity associated with the {@code result}
	 * @param result     an object that is cast to {@code <R>}
	 *
	 * @return the previous {@code result} associated with {@code entityName}
	 */
	public final R put(String entityName, Object result) {
		return this.put(entityName, (R) result);
	}

	public final R put(String entityName, R result) {
		super.put(getValidatedKey(entityName), result);
		return copyOfOriginalKeys.put(entityName, result);
	}

	@Override
	public final R get(Object entityName) {
		return super.get(getValidatedKey(entityName));
	}

	@Override
	public final R remove(Object entityName) {
		R out = super.remove(getValidatedKey(entityName));

		if (out != null) {
			Iterator<String> it = copyOfOriginalKeys.keySet().iterator();
			while (it.hasNext()) {
				String value = it.next();
				if (value.equalsIgnoreCase(entityName.toString())) {
					it.remove();
					break;
				}
			}
		}

		return out;
	}

	@Override
	public final boolean containsValue(Object entityName) {
		return super.containsValue(getValidatedKey(entityName));
	}

	@Override
	public final boolean containsKey(Object entityName) {
		return super.containsKey(getValidatedKey(entityName));
	}

	@Override
	public Set<String> keySet() {
		return Collections.unmodifiableSet(copyOfOriginalKeys.keySet());
	}

	@Override
	public final Set<Map.Entry<String, R>> entrySet() {
		return Collections.unmodifiableSet(copyOfOriginalKeys.entrySet());
	}

	@Override
	public final Collection<R> values() {
		return Collections.unmodifiableCollection(super.values());
	}
}

