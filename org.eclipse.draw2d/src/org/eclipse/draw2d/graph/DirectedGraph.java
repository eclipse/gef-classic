package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Insets;


/**
 * @author hudsonr
 * @since 2.1
 */
public class DirectedGraph {
public EdgeList cutEdges;

private Insets defaultPadding = new Insets(16);
public EdgeList edges = new EdgeList();

public DirectedGraph gPrime;
public NodeList nodes = new NodeList();
public RankList ranks;

public Insets getPadding(Node node) {
	Insets pad = node.getPadding();
	if (pad == null)
		return defaultPadding;
	return pad;
}

public void removeEdge(Edge edge) {
	edges.remove(edge);
	edge.source.outgoing.remove(edge);
	edge.target.incoming.remove(edge);
	if (edge.vNodes != null)
		for (int j = 0; j < edge.vNodes.size(); j++)
			removeNode(edge.vNodes.getNode(j));
}

public void removeNode(Node node) {
	nodes.remove(node);
	if (ranks != null)
		ranks.getRank(node.rank).remove(node);
}

public void setDefaultPadding(Insets insets) {
	defaultPadding = insets;
}

}
