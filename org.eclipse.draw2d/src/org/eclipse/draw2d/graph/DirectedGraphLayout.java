package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.internal.graph.*;
import org.eclipse.draw2d.internal.graph.*;

/**
 * 
 * @author hudsonr
 * Created on Jul 20, 2003
 */
public class DirectedGraphLayout extends GraphVisitor {

public void visit(DirectedGraph graph) {
	new BreakCycles()
		.visit(graph);
	new InitialRankSolver()
		.visit(graph);
	new TightSpanningTreeSolver()
		.visit(graph);
	new RankAssigmentSolver()
		.visit(graph);
	new PopulateRanks()
		.visit(graph);
	new VerticalPlacement()
		.visit(graph);
	new MinCross()
		.visit(graph);
	new LocalOptimizer()
		.visit(graph);
	new HorizontalPlacement()
		.visit(graph);
	new InvertEdges()
		.visit(graph);
}

}
