package org.eclipse.graph;

import org.eclipse.draw2d.geometry.Insets;

/**
 * @author hudsonr
 * @since 2.1
 */
public class Node {

public Object workingData[] = new Object[3];
public int workingInts[] = new int[4];
static boolean flipflop;

/**
 * Arbitrary client data
 */
public Object data;

/**
 * A flag for use by the various solving visitors.
 */
public boolean flag;

/**
 * The node's height.
 */
public int height = 40;

/**
 * The edges for which this node is the target.
 */
public EdgeList incoming = new EdgeList();
public int incomingOffset = -1;
/**
 * The node's index in the row it belongs.
 */
public int index;

int nestingIndex = -1;

/**
 * The edges for which this node is the source.
 */
public EdgeList outgoing = new EdgeList();
public int outgoingOffset = -1;

public Insets padding;

private Subgraph parent;

/**
 * The horizontal row to which this node belongs.
 */
public int rank;

/**
 * A value used to sort the node within its rank.
 */
public double sortValue;

/**
 * The node's width.
 */
public int width = 50;

/**
 * The node's final coordinates location
 */
public int x,y;
public Node() { }

public Node(Object data) {
	this.data = data;
}

public Node(Object data, Subgraph parent) {
	this.data = data;
	this.parent = parent;
	if (parent != null)
		parent.addMember(this);
}

public int getOffsetIncoming() {
	if (incomingOffset == -1)
		return width / 2;
	return incomingOffset;
}

public int getOffsetOutgoing() {
	if (outgoingOffset == -1)
		return width / 2;
	return outgoingOffset;
}

public Insets getPadding() {
	return padding;
}

public Subgraph getParent() {
	return parent;
}

private int incomingIndex(int i) {
	Edge e = incoming.getEdge(i);
	if (e.vNodes != null)
		return e.vNodes.getNode(e.vNodes.size() - 1).index;
	return e.source.index;
}

public double medianIncoming() {
	boolean change = false;
	do {
		change = false;
		for (int i = 0; i < incoming.size() - 1; i++) {
			if (incomingIndex(i) > incomingIndex(i+1)) {
				Edge e = incoming.getEdge(i);
				incoming.set(i, incoming.get(i+1));
				incoming.set(i+1, e);
				change = true;
			}
		}
	} while (change);
	
	int n = incoming.size();
	if (n == 0)
		return -1;
	if (n % 2 == 1)
		return incomingIndex(n / 2);
	if (n == 2) {
//		flipflop = !flipflop;
//		if (!flipflop)
//			return (incomingIndex(0) * 3 + 2* incomingIndex(1))/5.0;
//		return (incomingIndex(0) * 2 + 3 * incomingIndex(1))/5.0;
		return (incomingIndex(0) + incomingIndex(1)) / 2.0;
	}
	int l = incomingIndex(n / 2 - 1);
	int r = incomingIndex(n / 2);
	int dl = l - incomingIndex(0);
	int dr = incomingIndex(n - 1) - r;
	if (dl < dr)
		return l;
	if (dl > dr)
		return r;
	return
		(l + r) / 2.0;
}

/**
 * Calculates the new median value for this node based on outgoing edges.
 * @return the median value
 */
public double medianOutgoing() {
	boolean change = false;
	
	/* 
	 * First, SORT all of the outgoing edges based on their target node's current index. A
	 * bubble-sort is sufficient, because the list is almost sorted.
	 */
	do {
		change = false;
		for (int i = 0; i < outgoing.size() - 1; i++) {
			if (outgoingIndex(i) > outgoingIndex(i+1)) {
				Edge e = outgoing.getEdge(i);
				outgoing.set(i, outgoing.get(i+1));
				outgoing.set(i+1, e);
				change = true;
			}
		}
	} while (change);

	int n = outgoing.size();
	if (n == 0)
		return -1;
	if (n % 2 == 1)
		return outgoingIndex(n / 2);
	if (n == 2) {
//		flipflop = !flipflop;
//		if (flipflop)
//			return (outgoingIndex(0) * 3 + 2 * outgoingIndex(1)) / 5.0;
//		return (outgoingIndex(0) * 2 + 3 * outgoingIndex(1)) / 5.0;
		return (outgoingIndex(0) + outgoingIndex(1)) / 2.0;
	}
	int l = outgoingIndex(n / 2 - 1);
	int r = outgoingIndex(n / 2);
	int dl = l - outgoingIndex(0);
	int dr = outgoingIndex(n - 1) - r;
	if (dl < dr)
		return l;
	return r;
}

private int outgoingIndex(int i) {
	Edge e = outgoing.getEdge(i);
	if (e.vNodes != null)
		return e.vNodes.getNode(0).index;
	return e.target.index;
}

public void setPadding(Insets padding) {
	this.padding = padding;
}

public void setParent(Subgraph parent) {
	this.parent = parent;
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "N("+data+")";
}

}
