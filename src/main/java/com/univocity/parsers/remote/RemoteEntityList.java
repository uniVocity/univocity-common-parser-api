/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.parsers.remote;

import com.univocity.parsers.common.*;

import java.util.*;

/**
 * A list of remote entities to be parsed by some implementation of {@link EntityParserInterface},
 * and their specific configurations.
 *
 * The configuration applied over individual {@link RemoteEntitySettings} elements override their counterparts in the
 * global parser settings, usually a subclass of {@link RemoteParserSettings}
 *
 * @param <S> the type of {@link RemoteEntitySettings} managed by this list.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 * @see RemoteEntitySettings
 * @see RemoteParserSettings
 * @see EntityParserInterface
 */
public abstract class RemoteEntityList<S extends RemoteEntitySettings> extends EntityList<S> {

	private Map<String, List<String>> linkedEntitiesMap = new HashMap<String, List<String>>();
	/**
	 * Creates a new, empty {@code RemoteEntityList}, applying the global configuration object, used by the
	 * {@link EntityParserInterface} implementation, to all entity-specific settings in this list.
	 *
	 * @param globalSettings the global parser settings whose configuration may provide defaults for all entities
	 *                       defined in this list.
	 */
	public RemoteEntityList(RemoteParserSettings globalSettings) {
		super(globalSettings);
	}


	protected final S addEntitySettings(S settings) {
		return super.addEntitySettings(settings);
	}

	public RemoteParserSettings getParserSettings() {
		return (RemoteParserSettings) super.getParserSettings();
	}

	@Override
	protected abstract RemoteEntityList<S> newInstance();

	public void linkEntities(S parent, S firstChild, S... restOfChildren) {
		if (linkedEntitiesMap.get(parent.getEntityName()) == null) {
			linkedEntitiesMap.put(parent.getEntityName(), new ArrayList<String>());
		}

		List<String> children = linkedEntitiesMap.get(parent.getEntityName());
		children.add(firstChild.getEntityName());
		for (S child : restOfChildren) {
			children.add(child.getEntityName());
		}
		parent.linkedEntities.addAll(children);
	}

	public Map<String, List<String>> getLinkedEntitiesMap() {
		return Collections.unmodifiableMap(linkedEntitiesMap);
	}

}

