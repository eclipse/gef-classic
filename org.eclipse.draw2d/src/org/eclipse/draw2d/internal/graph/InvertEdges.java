package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;

/**
 * Graph visitors run prior to this visitor may have inverted edges to remove cycles
 * in the graph. This visitor flips all such edges back to their original orientation.
 * 
 * @author Daniel Lee
 */
public class InvertEdges extends GraphVisitor {

/**
 * @see org.eclipse.graph.GraphVisitor#visit(org.eclipse.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	for (int i = 0; i < g.edges.size(); i++) {
		Edge e = g.edges.getEdge(i);
		if (e.isBackward)
			e.invert();
	}
}

}
