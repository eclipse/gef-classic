package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.*;

/**
 * @author hudsonr
 */
public class CompoundPopulateRanks extends PopulateRanks {

/**
 * @see org.eclipse.graph.PopulateRanks#createVirtualNode(org.eclipse.graph.Edge, int)
 */
VirtualNode createVirtualNode(Edge e, int i) {
	VirtualNode n = super.createVirtualNode(e, i);
	Subgraph s = GraphUtilities.getCommonAncestor(e.source, e.target);
	if (s != null) {
		n.setParent(s);
		s.members.add(n);
		n.nestingIndex = s.nestingIndex;
	}
	return n;
}

}
