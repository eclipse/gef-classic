package org.eclipse.draw2d.graph;

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

public int nestingIndex = -1;

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

public final int incomingIndex(int i) {
	Edge e = incoming.getEdge(i);
	if (e.vNodes != null)
		return e.vNodes.getNode(e.vNodes.size() - 1).index;
	return e.source.index;
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
