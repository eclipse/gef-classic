/*******************************************************************************
 * Copyright 2012, 2024 Zoltan Ujhelyi and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Zoltan Ujhelyi
 ******************************************************************************/
package org.eclipse.zest.core.widgets.gestures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.GestureListener;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Transform;

/**
 * A gesture listener for rotate gestures in Graph widgets.
 *
 * @author Zoltan Ujhelyi
 * @since 1.14
 */
public class RotateGestureListener implements GestureListener {

	Graph graph;
	double rotate;
	List<? extends GraphNode> nodes;
	List<Point> originalLocations;
	double xCenter, yCenter;

	private void storePosition() {
		originalLocations = new ArrayList<>();
		Transform t = new Transform();
		t.setTranslation(-xCenter, -yCenter);
		for (GraphNode node : nodes) {
			originalLocations.add(t.getTransformed(node.getLocation()));
		}
	}

	private void updatePositions(double rotation) {
		Transform t = new Transform();
		t.setRotation(rotation);
		t.setTranslation(xCenter, yCenter);
		for (int i = 0; i < nodes.size(); i++) {
			GraphNode node = nodes.get(i);
			Point p = originalLocations.get(i);
			Point rot = t.getTransformed(p);
			node.setLocation(rot.preciseX(), rot.preciseY());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.GestureListener#gesture(org.eclipse.swt.events
	 * .GestureEvent)
	 */
	@Override
	public void gesture(GestureEvent e) {
		if (!(e.widget instanceof Graph)) {
			return;
		}
		switch (e.detail) {
		case SWT.GESTURE_BEGIN:
			graph = (Graph) e.widget;
			rotate = 0.0;
			List<? extends GraphItem> selection = graph.getSelection();
			if (selection.isEmpty()) {
				nodes = graph.getNodes();
			} else {
				nodes = tryGetNodes(selection);
			}
			xCenter = 0;// e.x;
			yCenter = 0;// e.y;
			for (GraphNode node : nodes) {
				Point location = node.getLocation();
				xCenter += location.preciseX();
				yCenter += location.preciseY();
			}
			xCenter = xCenter / nodes.size();
			yCenter = yCenter / nodes.size();
			storePosition();
			break;
		case SWT.GESTURE_ROTATE:
			updatePositions(e.rotation / 2 / Math.PI);
			break;
		default:
			// Do nothing
		}
	}

	/**
	 * Converts the given list of graph items to a list of graph nodes, if possible.
	 * If the list only contains elements of type {@link GraphNode}, then calling
	 * this method behaves like {@code (List<GraphNode>) selection} (just with
	 * complexity O(n), rather than O(1)). If the list contains at least one
	 * non-node (e.g. a connection), then an empty list is returned.<br>
	 * In the context of this listener, it should not be possible to rotate a graph,
	 * if at least one connection is selected, as its location is determined by its
	 * source and destination node and can therefore not be rotated on its own.
	 */
	private static List<GraphNode> tryGetNodes(List<? extends GraphItem> selection) {
		List<GraphNode> nodes = new ArrayList<>();
		for (GraphItem item : selection) {
			if (!(item instanceof GraphNode node)) {
				// Both nodes and connections selected
				return Collections.emptyList();
			}
			nodes.add(node);
		}
		return nodes;
	}

}
