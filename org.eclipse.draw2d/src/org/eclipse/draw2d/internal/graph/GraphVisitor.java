package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.DirectedGraph;

/**
 * @author hudsonr
 * @since 2.1
 */
public abstract class GraphVisitor {
//Random rand = new Random(8333);
public abstract void visit(DirectedGraph g);
}
