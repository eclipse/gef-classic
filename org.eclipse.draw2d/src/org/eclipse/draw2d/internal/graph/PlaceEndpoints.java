package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.geometry.Point;
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
		edge.end = new Point(
			edge.getTargetOffset() + edge.target.x,
			edge.target.y);
	}
}

}
