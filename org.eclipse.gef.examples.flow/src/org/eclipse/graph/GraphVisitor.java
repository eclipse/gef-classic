package org.eclipse.graph;

import java.util.Random;

/**
 * @author hudsonr
 * @since 2.1
 */
public abstract class GraphVisitor {
//Random rand = new Random(8333);
public abstract void visit(DirectedGraph g);
}
