/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.*;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;

/**
 * @author hudsonr
 */
public class PlaceEndpoints extends GraphVisitor {

/**
 * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	for (int i = 0; i < g.edges.size(); i++) {
		Edge edge = (Edge)g.edges.get(i);
		edge.start = new Point(
			edge.getSourceOffset() + edge.source.x,
			edge.source.y + edge.source.height);
		if (edge.source instanceof SubgraphBoundary) {
			SubgraphBoundary boundary = (SubgraphBoundary)edge.source;
			if (boundary.getParent().head == boundary)
				edge.start.y = boundary.getParent().y + boundary.getParent().insets.top;
		}
		edge.end = new Point(
			edge.getTargetOffset() + edge.target.x,
			edge.target.y);
	}
}

}
