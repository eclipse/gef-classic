package org.eclipse.graph;

/**
 * @author hudsonr
 * @since 2.1
 */
public class VirtualNode extends Node {

Node prev;
Node next;

VirtualNode(Edge e, int i) {
	super(e);
	incoming.add(e);
	outgoing.add(e);
	width = e.width;
	height = 0;
	rank = i;
}

public Edge getEdge() {
	return (Edge)data;
}

/**
 * @see org.eclipse.graph.Node#medianDown()
 */
public double medianOutgoing() {
	return next.index;
}

/**
 * @see org.eclipse.graph.Node#medianUp()
 */
public double medianIncoming() {
	return prev.index;
}

/**
 * Returns the original edge weight multiplied by the omega value for the this node and
 * the node on the previous rank.
 * @return
 */
public int omega() {
	Edge e = (Edge)data;
	if (e.source.rank + 1 < rank  && rank < e.target.rank)
		return 8 * e.weight;
	return 2 * e.weight;
}

/**
 * @see org.eclipse.graph.Node#toString()
 */
public String toString() {
	Edge e = (Edge)data;
	return "VN[" + (e.vNodes.indexOf(this) + 1) + "]("+ data +")";
}

}
