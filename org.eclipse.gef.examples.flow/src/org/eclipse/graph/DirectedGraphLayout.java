package org.eclipse.graph;

/**
 * 
 * @author hudsonr
 * Created on Jul 20, 2003
 */
public class DirectedGraphLayout extends GraphVisitor {

public void visit(DirectedGraph graph) {
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
	new HorizontalPlacement()
		.visit(graph);
}

}
