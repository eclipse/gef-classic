package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.*;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;

/**
 * @author hudsonr
 */
public class RestoreCompoundGraph extends GraphVisitor {

public void visit(DirectedGraph g) {
	for (int i = 0; i < g.edges.size(); i++) {
		Edge e = g.edges.getEdge(i);
		if (e.source instanceof SubgraphBoundary) {
			e.source.outgoing.remove(e);
			e.source = e.source.getParent();
		}
		if (e.target instanceof SubgraphBoundary) {
			e.target.incoming.remove(e);
			e.target= e.target.getParent();
		}
	}
}

}
