package org.eclipse.draw2d.graph;

/**
 * @author hudsonr
 * @since 2.1
 */
public class Edge {

/**
 * Internal field used during layout.
 */
public int cut;
/**
 * Arbitrary data field.
 */
public Object data;

/**
 * The minimum rank separation between the source and target nodes.
 */
public int delta = 1;
/**
 * Internal field used during layout.
 */
public boolean flag;

/**
 * Internal field, used to determine if edge source and target should be swapped.
 */
public boolean isBackward = false;

public int offsetSource = -1;
public int offsetTarget = -1;

public Node source;
public Node target;
/**
 * Internal field used during layout.
 */
public boolean tree;
/**
 * The virtual nodes used to bend edges which go across one or more ranks.
 */
public NodeList vNodes;
/**
 * A weighting factor indicating how straight and short the edge should be relative to
 * other edges.
 */
public int weight = 1;

/**
 * The width occupied by the edge itself.
 */
public int width = 1;

public Edge(Node source, Node target) {
	this(null, source, target);
}

public Edge(Node source, Node target, int delta, int weight) {
	this(source, target);
	this.delta = delta;
	this.weight = weight;
}

public Edge(Object data, Node source, Node target) {
	this.data = data;
	this.source = source;
	this.target = target;
	source.outgoing.add(this);
	target.incoming.add(this);
}

/**
 * Returns the index of the {@link Node} (or {@link VirtualNode}) on this edge at the 
 * given rank.  If this edge doesn't have a node at the given rank, -1 is returned.
 * @param rank the rank
 * @return the edges index at the given rank
 */
public int getIndexForRank(int rank) {
	if (source.rank == rank)
		return source.index;
	if (target.rank == rank)
		return target.index;
	if (vNodes != null)
		return vNodes.getNode(rank - source.rank - 1).index;
	return -1;
}

public int getLength() {
	return (target.rank - source.rank);
}

public int getSlack() {
	return (target.rank - source.rank) - delta;
}

public int getSourceOffset() {
	if (offsetSource != -1)
		return offsetSource;
	return source.getOffsetOutgoing();
}

public int getTargetOffset() {
	if (offsetTarget != -1)
		return offsetTarget;
	return target.getOffsetIncoming();
}

public Node opposite(Node end){
	if (source == end)
		return target;
	return source;
}

/**
 * Inverts this edge. (Source becomes target, target becomes source).
 */
public void invert() {	
	Node oldTarget = target;
	
	source.outgoing.remove(this);
	target.incoming.remove(this);
	
	target = source;
	source = oldTarget;
	
	target.incoming.add(this);
	source.outgoing.add(this);
	
	if (vNodes != null) {
		NodeList newVNodes = new NodeList();
		for (int j = vNodes.size() - 1; j >= 0; j--) {
			newVNodes.add(vNodes.getNode(j));
		}
		vNodes = newVNodes;
	}
}


int span() {
	return target.rank - source.rank;
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "{" + source + "} -> {" + target + "}" ;
}

}
