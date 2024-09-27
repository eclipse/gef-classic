/*******************************************************************************
 * Copyright 2005, 2024 CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.exampleStructures;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.interfaces.ContextListener;
import org.eclipse.zest.layouts.interfaces.EntityLayout;
import org.eclipse.zest.layouts.interfaces.ExpandCollapseManager;
import org.eclipse.zest.layouts.interfaces.GraphStructureListener;
import org.eclipse.zest.layouts.interfaces.LayoutContext;
import org.eclipse.zest.layouts.interfaces.LayoutListener;
import org.eclipse.zest.layouts.interfaces.NodeLayout;
import org.eclipse.zest.layouts.interfaces.PruningListener;
import org.eclipse.zest.layouts.interfaces.SubgraphLayout;

import org.eclipse.draw2d.EventListenerList;

/**
 * Create a very simple graph that can be used in the layout algorithms
 *
 * @author Casey Best
 * @author Chris Callendar
 */
public class SimpleGraph implements LayoutContext {

	Map<Object, SimpleNode> objectsToNodes;
	List<SimpleRelationship> relationships;
	LayoutAlgorithm algorithm;
	ExpandCollapseManager expandCollapseManager;
	EventListenerList listeners;
	DisplayIndependentRectangle bounds;

	public SimpleGraph() {
		objectsToNodes = new LinkedHashMap<>();
		relationships = new ArrayList<>();
		listeners = new EventListenerList();
		bounds = new DisplayIndependentRectangle();
	}

	/**
	 * Adds the node.
	 *
	 * @param node The node to add.
	 */
	public void addEntity(SimpleNode node) {
		objectsToNodes.put(node.getRealObject(), node);
	}

	/**
	 * Creates a LayoutEntity containing an object.
	 */
	public SimpleNode addObjectNode(Object object) {
		return objectsToNodes.computeIfAbsent(object, ignore -> new SimpleNode(object, this));
	}

	/**
	 * Add a relationship between two objects. Layout algorithms need to know
	 * whether a relationship is one way or bi-directional. This method assumes that
	 * all relationships are bi-direcional and have the same weight.
	 */
	public void addObjectRelationship(SimpleNode sourceNode, SimpleNode destinationNode) {
		addObjectRelationship(sourceNode, destinationNode, true, 1);
	}

	/**
	 * Add a relationship between two objects. Layout algorithms need to know
	 * whether a relationship is one way or bi-directional.
	 */
	public void addObjectRelationship(Object sourceObject, Object destinationObject, boolean bidirectional,
			int weight) {
		addObjectNode(sourceObject);
		addObjectNode(destinationObject);
		SimpleNode sourceNode = objectsToNodes.get(sourceObject);
		SimpleNode destinationNode = objectsToNodes.get(destinationObject);
		SimpleRelationship simpleRelationship = new SimpleRelationship(sourceNode, destinationNode, bidirectional,
				weight);
		relationships.add(simpleRelationship);
	}

	public void addRelationship(SimpleNode srcNode, SimpleNode destNode) {
		addRelationship(srcNode, destNode, true, 1);
	}

	public void addRelationship(SimpleNode srcNode, SimpleNode destNode, boolean bidirectional, int weight) {
		addEntity(srcNode);
		addEntity(destNode);
		SimpleRelationship rel = new SimpleRelationship(srcNode, destNode, bidirectional, weight);
		relationships.add(rel);
	}

	public void addRelationship(SimpleRelationship relationship) {
		relationships.add(relationship);
	}

	/**
	 * Returns a list of SimpleNodes that represent the objects added to this graph
	 * using addNode. Note that any manipulation to this graph was done on the
	 * SimpleNodes, not the real objects. You must still manipulate them yourself.
	 */
	@Override
	public SimpleNode[] getNodes() {
		return objectsToNodes.values().toArray(SimpleNode[]::new);
	}

	/**
	 * Returns a list of SimpleRelationships that represent the objects added to
	 * this graph using addRelationship.
	 */
	@Override
	public SimpleRelationship[] getConnections() {
		return relationships.toArray(SimpleRelationship[]::new);
	}

	@Override
	public SimpleRelationship[] getConnections(EntityLayout layoutEntity1, EntityLayout layoutEntity2) {
		List<SimpleRelationship> relationships = new ArrayList<>();
		for (SimpleRelationship relationship : this.relationships) {
			if (relationship.getSource() == layoutEntity1 && relationship.getTarget() == layoutEntity2) {
				relationships.add(relationship);
			}
		}
		return relationships.toArray(SimpleRelationship[]::new);
	}

	public void setBounds(double x, double y, double width, double height) {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
	}

	@Override
	public DisplayIndependentRectangle getBounds() {
		return bounds;
	}

	@Override
	public boolean isBoundsExpandable() {
		return false;
	}

	@Override
	public SubgraphLayout[] getSubgraphs() {
		return new SubgraphLayout[0];
	}

	@Override
	public SubgraphLayout createSubgraph(NodeLayout[] nodes) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPruningEnabled() {
		return false;
	}

	@Override
	public boolean isBackgroundLayoutEnabled() {
		return false;
	}

	@Override
	public void flushChanges(boolean animationHint) {
	}

	@Override
	public void setMainLayoutAlgorithm(LayoutAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public LayoutAlgorithm getMainLayoutAlgorithm() {
		return algorithm;
	}

	@Override
	public void setExpandCollapseManager(ExpandCollapseManager expandCollapseManager) {
		this.expandCollapseManager = expandCollapseManager;
	}

	@Override
	public ExpandCollapseManager getExpandCollapseManager() {
		return expandCollapseManager;
	}

	@Override
	public void addLayoutListener(LayoutListener listener) {
		listeners.addListener(LayoutListener.class, listener);
	}

	@Override
	public void removeLayoutListener(LayoutListener listener) {
		listeners.removeListener(LayoutListener.class, listener);
	}

	@Override
	public void addGraphStructureListener(GraphStructureListener listener) {
		listeners.addListener(GraphStructureListener.class, listener);
	}

	@Override
	public void removeGraphStructureListener(GraphStructureListener listener) {
		listeners.removeListener(GraphStructureListener.class, listener);
	}

	@Override
	public void addContextListener(ContextListener listener) {
		listeners.addListener(ContextListener.class, listener);
	}

	@Override
	public void removeContextListener(ContextListener listener) {
		listeners.removeListener(ContextListener.class, listener);
	}

	@Override
	public void addPruningListener(PruningListener listener) {
		listeners.addListener(PruningListener.class, listener);
	}

	@Override
	public void removePruningListener(PruningListener listener) {
		listeners.removeListener(PruningListener.class, listener);
	}

	@Override
	public SimpleNode[] getEntities() {
		return getNodes();
	}
}
