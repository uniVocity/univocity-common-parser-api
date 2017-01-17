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
 * <p>
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

	private final Map<String, List<String>> linkedEntitiesMap = new HashMap<String, List<String>>();

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

	/**
	 * Links the supplied children entities to the given parent entity. When a {@link RemoteResult} for an entity is
	 * returned by the parser, it's linked entity results can be accessed via {@link RemoteResult#getLinkedEntityData(int)}.
	 *
	 * <p>
	 * Leaf entities (entities that are linked to but have no child links) need to have one field that matches the name
	 * of a field in each parent entity in the hierarchy. For example, there are 3 entities in a link hierarchy shown in
	 * the diagram below:
	 * </p>
	 *
	 * <pre>
	 *   Entities:  Day("Day") -> Station("Name") -> Fuel("Price", "Type")
	 *   (fields in brackets)
	 * </pre>
	 *
	 * <p>Fuel, as a leaf entity, <strong>must have</strong> fields that match a field from both Day and Station. Adding
	 * these fields result in the below diagram: </p>
	 *
	 * <pre>
	 *   Entities:  Day("Day") -> Station("Name") -> Fuel("Price", "Type", "Day", "Name")
	 * </pre>
	 *
	 * <p>When the parser runs, it will attempt to link rows where the leaf row 'linked fields' matches it's associated
	 * parents fields. For instance, assume that the rows parsed are: </p>
	 *
	 * <pre>
	 *     	                         Day("Day"): ["MON"], ["TUE"]
	 *                          Station("Name"): ["Station A"], ["Station B']
	 *     Fuel("Price", "Type", "Day", "Name"): ["$1.00", "UL", "MON", "Station A"],  ["$1.00", "UL", "TUE", "Station C"]
	 * </pre>
	 *
	 * <p>
	 *     The first row of Fuel {@code ["$1.00", "UL", "MON", "Station A"]} will be linked with the first row of Station
	 *     {@code ["Station A"]} as their 'Name' field values match. This Station row will then be linked to first row of day
	 *     {@code ["MON"]} as it matches the 'Day' field in the Fuel row. A diagram showing this linking can be seen
	 *     below. No other rows will be linked.
	 * </p>
	 *
	 * <pre>
	 *     Code: (a) RemoteResult dayEntityResult = parser.parse(file).get("Day");
	 *           (b) RemoteResult linkedStationResult = dayEntityResult.getLinkedEntityData(0).get("Station");
	 *           (c) RemoteResult linkedFuelResult = linkedStationResult.getLinkedEntityData(0).get("Fuel");
	 *
	 *     Result:
	 *           (a) ["Mon"] -> (b) ["Station A"] -> (c) ["$1.00", "UL", "MON", "Station A"]
	 * </pre>
	 *
	 *
	 * @param parent the entity that the children entities will link to
	 * @param firstChild the first entity that will be linked to the parent entity
	 * @param restOfChildren any other entities that will be linked to the parent
	 */
	public void linkEntities(S parent, S firstChild, S... restOfChildren) {
		ArgumentUtils.noNulls("Linked entities", parent, firstChild);
		if (isParentInChildren(parent, firstChild, restOfChildren)) {
			throw new IllegalArgumentException("Can not link entity: '" + parent + "' to itself");
		}

		if (linkedEntitiesMap.get(parent.getEntityName()) == null) {
			linkedEntitiesMap.put(parent.getEntityName(), new ArrayList<String>());
		}

		List<String> children = linkedEntitiesMap.get(parent.getEntityName());
		children.add(firstChild.getEntityName());
		for (S child : restOfChildren) {
			children.add(child.getEntityName());
		}
	}

	private boolean isParentInChildren(S parent, S firstChild, S... restOfChildren) {
		if (parent == firstChild) {
			return true;
		}

		for (S child : restOfChildren) {
			if (parent == child) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a map describing the linked entities where <strong>key = parent entity name</strong> and <strong>value = names of entities linked
	 * to the parent.</strong>
	 *
	 * @return a map where the values are the names of entities linked to the key (which is a name of a entity).
	 */
	public Map<String, List<String>> getLinkedEntitiesMap() {
		return Collections.unmodifiableMap(linkedEntitiesMap);
	}

}

