package org.eclipse.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudsonr
 * @since 2.1
 */
public class TightSpanningTreeSolver extends GraphVisitor {

protected DirectedGraph graph;
protected EdgeList candidates = new EdgeList();
protected NodeList members = new NodeList();

public void visit(DirectedGraph graph) {
	this.graph = graph;
	solve();
}

Node addEdge(Edge edge) {
	int delta = edge.getSlack();
	edge.tree = true;
	Node node;
	if (edge.target.flag) {
		delta = -delta;
		node = edge.source;
		node.spanTreeParent = edge;
		edge.target.spanTreeChildren.add(edge);
	} else {
		node = edge.target;
		node.spanTreeParent = edge;
		edge.source.spanTreeChildren.add(edge);
	}
	members.adjustRank(delta);
	addNode(node);
	return node;
}

void addNode(Node node) {
	node.flag = true;
	EdgeList list = node.incoming;
	Edge e;
	for (int i = 0; i < list.size(); i++) {
		e = list.getEdge(i);
		if (!e.source.flag) {
			if (!candidates.contains(e))
				candidates.add(e);
		} else
			candidates.remove(e);
	}

	list = node.outgoing;
	for (int i = 0; i < list.size(); i++) {
		e = list.getEdge(i);
		if (!e.target.flag) {
			if (!candidates.contains(e))
				candidates.add(e);
		} else
			candidates.remove(e);
	}
	members.add(node);
}

protected void solve() {
	graph.edges.resetFlags();
	graph.nodes.resetFlags();
	
	Node root = graph.nodes.getNode(0);
	root.spanTreeParent = null;
	addNode(root);
	List nonMembers = new ArrayList(graph.nodes);
	while (members.size() < graph.nodes.size()) {
		if (candidates.size() == 0)
			throw new RuntimeException("graph is not fully connected");//$NON-NLS-1$
		int minSlack = Integer.MAX_VALUE, slack;
		Edge minEdge = null, edge;
		for (int i=0; i<candidates.size() && minSlack > 0; i++) {
			edge = candidates.getEdge(i);
			slack = edge.getSlack();
			if (slack < minSlack) {
				minSlack = slack;
				minEdge = edge;
			}
		}
		Node added = addEdge(minEdge);
		nonMembers.remove(added);
	}
	graph.nodes.normalizeRanks();
}

}
