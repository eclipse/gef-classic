package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Point;

/**
 * A directed Edge joining a source and target Node.  Edges indicate the dependencies
 * between nodes.  An Edge provides the information needed to perform a graph layout, and
 * it stores the result of the layout in its various field. Therefore, it functions
 * both as input and output.  The input data consists of:
 * <UL>
 *   <LI>{@link #source} - the source Node
 *   <LI>{@link #target} - the target Node
 *   <LI>{@link #delta} - the minimum number of rows the edge should span
 *   <LI>{@link #weight} - a hint indicating this edge's importance
 *   <LI>{@link #width} - the edge's width
 *   <LI>[{@link #offsetSource}] - the edge's attachment point at the source node
 *   <LI>[{@link #offsetTarget}] - the edge's attachment point at the target node
 * </UL>
 * <P>The output of a layout consists of bending longer edges, and potentially inverting
 * edges to remove cycles in the graph.  The output consists of:
 * <UL>
 *   <LI>{@link #vNodes} - the virtual nodes (if any) which make up the bendpoints
 *   <LI>{@link #isFeedback} - <code>true</code> if the edge points backwards
 * </UL>
 * 
 * @author hudsonr
 * @since 2.1
 */
public class Edge {

/**
 * For internal use only. Represents the edge's cut value in the network simplex loop.
 */
public int cut;

/**
 * An arbitrary data field for use by clients.
 */
public Object data;

/**
 * The minimum rank separation between the source and target nodes.
 */
public int delta = 1;

/**
 * For internal use only. Used during layout.
 */
public boolean flag;

/**
 * Internal field, used to determine if edge source and target should be swapped.
 */
public boolean isFeedback = false;

/**
 * The edge's attachment point at the <em>source</em> node. The default value is -1, which
 * indicates that the edge should use the node's default {@link Node#getOffsetOutgoing()
 * outgoing} attachment point.
 */
public int offsetSource = -1;
/**
 * The edge's attachment point at the <em>target</em> node. The default value is -1, which
 * indicates that the edge should use the node's default {@link Node#getOffsetIncoming()
 * incoming} attachment point.
 */
public int offsetTarget = -1;

/**
 * The source Node.
 */
public Node source;

/**
 * The target Node.
 */
public Node target;

public Point start;
public Point end;

/**
 * For internal use only. Field used during layout.
 */
public boolean tree;

/**
 * The virtual nodes used to bend edges which go across one or more ranks.  Each virtual
 * node is just a regular node which occupies some small amount of space on a row. It's
 * width is equivalent to the edge's width.  Clients should use each virtual node's
 * location (x, y, width, and height) as the way to position an edge which spans 1 or more
 * rows.
 */
public NodeList vNodes;

/**
 * A hint indicating how straight and short the edge should be relative to
 * other edges in the graph.  The default value is <code>1</code>.
 */
public int weight = 1;

/**
 * The width occupied by the edge itself.  The default value is <code>1</code>.
 */
public int width = 1;

/**
 * Constructs a new edge with the given source and target nodes.  All other fields will
 * have their default values.
 * @param source the source Node
 * @param target the target Node
 */
public Edge(Node source, Node target) {
	this(null, source, target);
}

/**
 * Constructs a new edge with the given source, target, delta, and weight.
 * @param source the source Node
 * @param target the target Node
 * @param delta the minimum edge span
 * @param weight the weight hint
 */
public Edge(Node source, Node target, int delta, int weight) {
	this(source, target);
	this.delta = delta;
	this.weight = weight;
}

/**
 * Constructs a new edge with the given data object, source, and target node.
 * @param data an arbitrary data object
 * @param source the source node
 * @param target the target node
 */
public Edge(Object data, Node source, Node target) {
	this.data = data;
	this.source = source;
	this.target = target;
	source.outgoing.add(this);
	target.incoming.add(this);
}

/**
 * For internal use only. Returns the index of the {@link Node} (or {@link VirtualNode})
 * on this edge at the given rank.  If this edge doesn't have a node at the given rank, -1
 * is returned.
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

/**
 * For internal use only. Returns the target node's row minus the source node's row.
 * @return the distance from the source to target ranks
 */
public int getLength() {
	return (target.rank - source.rank);
}

/**
 * For internal use only. Returns the amount of slack in the edge.
 * @return the edge's slack
 */
public int getSlack() {
	return (target.rank - source.rank) - delta;
}

/**
 * Returns the effective source offset for this edge.  The effective source offset is
 * either the {@link #offsetSource} field, or the source node's default outgoing offset if
 * that field's value is -1.
 * @return the source offset
 */
public int getSourceOffset() {
	if (offsetSource != -1)
		return offsetSource;
	return source.getOffsetOutgoing();
}

/**
 * Returns the effective target offset for this edge.  The effective target offset is
 * either the {@link #offsetTarget} field, or the target node's default incoming offset if
 * that field's value is -1.
 * @return the target offset
 */
public int getTargetOffset() {
	if (offsetTarget != -1)
		return offsetTarget;
	return target.getOffsetIncoming();
}

/**
 * For internal use only.  Returns the node opposite the given node on this edge.
 * @param end one end
 * @return the other end
 */
public Node opposite(Node end) {
	if (source == end)
		return target;
	return source;
}

/**
 * Inverts this edge. (Source becomes target, target becomes source).
 */
public void invert() {
	source.outgoing.remove(this);
	target.incoming.remove(this);
	
	Node oldTarget = target;
	target = source;
	source = oldTarget;

	int temp = offsetSource;
	offsetSource = offsetTarget;
	offsetTarget = temp;
	
	target.incoming.add(this);
	source.outgoing.add(this);
	
	if (start != null) {
		Point pt = start;
		start = end;
		end = pt;
	}
	
	if (vNodes != null) {
		NodeList newVNodes = new NodeList();
		for (int j = vNodes.size() - 1; j >= 0; j--) {
			newVNodes.add(vNodes.getNode(j));
		}
		vNodes = newVNodes;
	}
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "{" + source + "} -> {" + target + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

}
