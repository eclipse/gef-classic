package org.eclipse.graph;

import org.eclipse.draw2d.geometry.Insets;


/**
 * @author hudsonr
 * Created on Jul 2, 2003
 */
public class Subgraph extends Node {

public NodeList members = new NodeList();
public Node head, tail, left, right;
public int nestingTreeMin;
public Insets insets = new Insets(15);
public Insets innerPadding = NO_INSETS;

private static final Insets NO_INSETS = new Insets();

public Subgraph(Object data) {
	this(data, null);
}

public Subgraph(Object data, Subgraph parent) {
	super(data, parent);
}

public void addMember(Node n) {
	members.add(n);
}

/**
 * Returns <code>true</code> if the given node is properly contained inside this subgraph.
 * @param n the node in question
 * @return <code>true</code> if contained
 */
public boolean isNested(Node n) {
	return n.nestingIndex >= nestingTreeMin
		&& n.nestingIndex <= nestingIndex;
}

}
