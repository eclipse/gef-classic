package org.eclipse.graph;

/**
 * This visitor will attempt to adjust rank assigment wherever the cut-values are zero.
 * The spanning tree will be traversed depth-first. When an edge whose cut-value is zero
 * is encountered, a weighted-median slack of all incoming and outgoing edges to that
 * subtree will be calculated, including the edge itself.  If this value is positive, the 
 * edge is lengthened by that amount, shifting the subtree.
 * @author hudsonr
 * Created on Jul 7, 2003
 */
public class BalanceRankAssigmentsMedianTechnique extends GraphVisitor {

	/**
	 * @see org.eclipse.graph.GraphVisitor#visit(org.eclipse.graph.DirectedGraph)
	 */
	public void visit(DirectedGraph g) {
		//$TODO

	}

}
