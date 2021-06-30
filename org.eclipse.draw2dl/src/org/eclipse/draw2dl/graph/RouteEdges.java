/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.graph;

import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.PointList;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * @author Randy Hudson
 */
class RouteEdges extends org.eclipse.draw2dl.graph.GraphVisitor {

	/**
	 * @see org.eclipse.draw2dl.graph.GraphVisitor#visit(org.eclipse.draw2dl.graph.DirectedGraph)
	 */
	public void revisit(org.eclipse.draw2dl.graph.DirectedGraph g) {
		for (int i = 0; i < g.edges.size(); i++) {
			org.eclipse.draw2dl.graph.Edge edge = (org.eclipse.draw2dl.graph.Edge) g.edges.get(i);
			edge.start = new Point(edge.getSourceOffset() + edge.source.x,
					edge.source.y + edge.source.height);
			if (edge.source instanceof org.eclipse.draw2dl.graph.SubgraphBoundary) {
				org.eclipse.draw2dl.graph.SubgraphBoundary boundary = (org.eclipse.draw2dl.graph.SubgraphBoundary) edge.source;
				if (boundary.getParent().head == boundary)
					edge.start.y = boundary.getParent().y
							+ boundary.getParent().insets.top;
			}
			edge.end = new Point(edge.getTargetOffset() + edge.target.x,
					edge.target.y);

			if (edge.vNodes != null)
				routeLongEdge(edge, g);
			else {
				PointList list = new PointList();
				list.addPoint(edge.start);
				list.addPoint(edge.end);
				edge.setPoints(list);
			}
		}
	}

	static void routeLongEdge(Edge edge, DirectedGraph g) {
		org.eclipse.draw2dl.graph.ShortestPathRouter router = new ShortestPathRouter();
		org.eclipse.draw2dl.graph.Path path = new Path(edge.start, edge.end);
		router.addPath(path);
		Rectangle o;
		Insets padding;
		for (int i = 0; i < edge.vNodes.size(); i++) {
			org.eclipse.draw2dl.graph.VirtualNode node = (VirtualNode) edge.vNodes.get(i);
			Node neighbor;
			if (node.left != null) {
				neighbor = node.left;
				o = new Rectangle(neighbor.x, neighbor.y, neighbor.width,
						neighbor.height);
				padding = g.getPadding(neighbor);
				o.width += padding.right + padding.left;
				o.width += (edge.getPadding() * 2);
				o.x -= (padding.left + edge.getPadding());
				o.union(o.getLocation().translate(-100000, 2));
				router.addObstacle(o);
			}
			if (node.right != null) {
				neighbor = node.right;
				o = new Rectangle(neighbor.x, neighbor.y, neighbor.width,
						neighbor.height);
				padding = g.getPadding(neighbor);
				o.width += padding.right + padding.left;
				o.width += (edge.getPadding() * 2);
				o.x -= (padding.left + edge.getPadding());
				o.union(o.getLocation().translate(100000, 2));
				router.addObstacle(o);
			}
		}
		router.setSpacing(0);
		router.solve();
		edge.setPoints(path.getPoints());
	}

}
