package org.eclipse.graph;


/**
 * This graph visitor examines all adjacent pairs of nodes and determines if 
 * swapping the two nodes provides improved graph aesthetics.
 * @author Daniel Lee
 */
public class LocalOptimizer extends GraphVisitor {

private boolean shouldSwap(Node current, Node next) {
	int crossCount = 0;
	int invertedCrossCount = 0;
	
	EdgeList currentIncoming = current.incoming;
	EdgeList nextIncoming = next.incoming;
	for (int i = 0; i < currentIncoming.size(); i++) {
		Edge currentEdge = currentIncoming.getEdge(i);
		for (int j = 0; j < nextIncoming.size(); j++) {
			if (nextIncoming.getEdge(j).getIndexForRank(current.rank - 1)
				< currentEdge.getIndexForRank(current.rank - 1))
				crossCount++;
			if (nextIncoming.getEdge(j).getIndexForRank(current.rank - 1) 
				> currentEdge.getIndexForRank(current.rank - 1))
				invertedCrossCount++;
		}
	}
	
	EdgeList currenteOutgoing = current.outgoing;
	EdgeList nextOutgoing = next.outgoing;
	for (int i = 0; i < currenteOutgoing.size(); i++) {
		Edge currentEdge = currenteOutgoing.getEdge(i);
		for (int j = 0; j < nextOutgoing.size(); j++) {
			if (nextOutgoing.getEdge(j).getIndexForRank(current.rank + 1) 
				< currentEdge.getIndexForRank(current.rank + 1))
				crossCount++;	
			if (nextOutgoing.getEdge(j).getIndexForRank(current.rank + 1) 
				> currentEdge.getIndexForRank(current.rank + 1))
				invertedCrossCount++;
		}
	}
	if (invertedCrossCount < crossCount)		
		return true;
	return false;
}

private void swapNodes(Node current, Node next, Rank rank) {
	int index = rank.indexOf(current);
	rank.set(index + 1, current);
	rank.set(index, next);
	index = current.index;
	current.index = next.index;
	next.index = index;
}

/**
 * @see org.eclipse.graph.GraphVisitor#visit(org.eclipse.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	boolean flag;
	do {
		flag = false;
		for (int r = 0; r < g.ranks.size(); r++) {
			Rank rank = g.ranks.getRank(r);
			for (int n = 0; n < rank.count() - 1; n++) {
				Node currentNode = rank.getNode(n);
				Node nextNode = rank.getNode(n + 1);
				if (shouldSwap(currentNode, nextNode)) {
					swapNodes(currentNode, nextNode, rank);
					flag = true;
					n = Math.max(0, n - 2);
				}
			}
		}
	} while (flag);
}

}
