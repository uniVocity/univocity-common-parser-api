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
public final class Results<R extends Result> implements Map<String, R> {

	private final Map<String, R> normalizedKeyMap = new HashMap<String, R>();
	private final Map<String, R> originalKeyMap = new LinkedHashMap<String, R>();

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
		normalizedKeyMap.put(getValidatedKey(entityName), (R) result);
		return originalKeyMap.put(entityName, (R) result);
	}

	public final R put(String entityName, R result) {
		normalizedKeyMap.put(getValidatedKey(entityName), result);
		return originalKeyMap.put(entityName, result);
	}

	@Override
	public final R get(Object entityName) {
		return normalizedKeyMap.get(getValidatedKey(entityName));
	}

	@Override
	public final R remove(Object entityName) {
		R out = normalizedKeyMap.remove(getValidatedKey(entityName));

		if (out != null) {
			Iterator<String> it = originalKeyMap.keySet().iterator();
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
		return normalizedKeyMap.containsValue(getValidatedKey(entityName));
	}

	@Override
	public final boolean containsKey(Object entityName) {
		return normalizedKeyMap.containsKey(getValidatedKey(entityName));
	}

	@Override
	public Set<String> keySet() {
		return Collections.unmodifiableSet(originalKeyMap.keySet());
	}

	@Override
	public final Set<Map.Entry<String, R>> entrySet() {
		return Collections.unmodifiableSet(originalKeyMap.entrySet());
	}

	@Override
	public final Collection<R> values() {
		return Collections.unmodifiableCollection(originalKeyMap.values());
	}

	@Override
	public int size() {
		return originalKeyMap.size();
	}

	@Override
	public boolean isEmpty() {
		return originalKeyMap.isEmpty();
	}

	@Override
	public void putAll(Map<? extends String, ? extends R> m) {
		for (Map.Entry e : m.entrySet()) {
			this.put(String.valueOf(e.getKey()), (R) e.getValue());
		}
	}

	@Override
	public void clear() {
		normalizedKeyMap.clear();
		originalKeyMap.clear();
	}
}

