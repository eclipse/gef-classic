package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.*;

/**
 * 
 * @author hudsonr
 * Created on Jul 20, 2003
 */
public class CompoundVerticalPlacement extends VerticalPlacement {

/**  */
public void visit(DirectedGraph dg) {
	CompoundDirectedGraph g = (CompoundDirectedGraph)dg;
	super.visit(g);
	for (int i = 0; i < g.subgraphs.size(); i++) {
		Subgraph s = (Subgraph)g.subgraphs.get(i);
		s.y = s.head.y;
		s.height = s.tail.height + s.tail.y - s.y;
	}
}

}
