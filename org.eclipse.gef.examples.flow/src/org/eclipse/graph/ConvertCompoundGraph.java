package org.eclipse.graph;

import org.eclipse.draw2d.geometry.Insets;

/**
 * @author hudsonr
 * Created on Jul 2, 2003
 */
public class ConvertCompoundGraph extends GraphVisitor {

private void addContainmentEdges(CompoundDirectedGraph graph) {
	//For all nested nodes, connect to head and/or tail of containing subgraph if present
	for (int i = 0; i < graph.nodes.size(); i++) {
		Node node = (Node)graph.nodes.getNode(i);
		Subgraph parent = node.getParent();
		if (parent == null)
			continue;
		if (node instanceof Subgraph) {
			Subgraph sub = (Subgraph)node;
			connectHead(graph, sub.head, parent);
			connectTail(graph, sub.tail, parent);
		} else {
			connectHead(graph, node, parent);
			connectTail(graph, node, parent);
		}
	}
}

int buildNestingTreeIndices(NodeList nodes, int base) {
	for (int i = 0; i < nodes.size(); i++) {
		Node node = (Node)nodes.get(i);
		if (node instanceof Subgraph) {
			Subgraph s = (Subgraph)node;
			s.nestingTreeMin = base;
			base = buildNestingTreeIndices(s.members, base);
		}
		node.nestingIndex = base++;
	}
	return base++;
}

private void connectHead(CompoundDirectedGraph graph, Node node, Subgraph parent) {
	boolean connectHead = true;
	for (int j = 0; connectHead && j < node.incoming.size(); j++) {
		Node ancestor = (Node)node.incoming.getEdge(j).source;
		if (parent.isNested(ancestor))
			connectHead = false;
	}
	if (connectHead) {
		Edge e = new Edge(parent.head, node);
		graph.edges.add(e);
		graph.containment.add(e);
	}
}

private void connectTail(CompoundDirectedGraph graph, Node node, Subgraph parent) {
	boolean connectTail = true;
	for (int j = 0; connectTail && j < node.outgoing.size(); j++) {
		Node ancestor = (Node)node.outgoing.getEdge(j).target;
		if (parent.isNested(ancestor))
			connectTail = false;
	}
	if (connectTail) {
		Edge e = new Edge(node, parent.tail);
		graph.edges.add(e);
		graph.containment.add(e);
	}
}

private void convertSubgraphEndpoints(CompoundDirectedGraph graph) {
	for (int i = 0; i < graph.edges.size(); i++) {
		Edge edge = (Edge)graph.edges.get(i);
		if (edge.source instanceof Subgraph) {
			edge.source.outgoing.remove(edge);
			Node tail = ((Subgraph)edge.source).tail;
			edge.source = tail;
			tail.outgoing.add(edge);
		}
		if (edge.target instanceof Subgraph) {
			edge.target.incoming.remove(edge);
			Node head = ((Subgraph)edge.target).head;
			edge.target = head;
			head.incoming.add(edge);
		}
	}
}

private void replaceSubgraphsWithBoundaries(CompoundDirectedGraph graph) {
	for (int i = 0; i < graph.subgraphs.size(); i++) {
		Subgraph s = (Subgraph)graph.subgraphs.get(i);
		graph.nodes.add(s.head);
		graph.nodes.add(s.tail);
		graph.nodes.remove(s);
	}
}

/**
 * @see org.eclipse.graph.GraphVisitor#visit(org.eclipse.graph.DirectedGraph)
 */
public void visit(DirectedGraph dg) {
	CompoundDirectedGraph graph = (CompoundDirectedGraph)dg;

	NodeList roots = new NodeList();
	//Find all subgraphs and root subgraphs
	for (int i = 0; i < graph.nodes.size(); i++) {
		Object node = graph.nodes.get(i);
		if (node instanceof Subgraph) {
			Subgraph s = (Subgraph)node;
			Insets padding = dg.getPadding(s);
			s.head = new SubgraphBoundary(s, padding, 0);
			s.tail = new SubgraphBoundary(s, padding, 2);
			
			graph.subgraphs.add(s);
			if (s.getParent() == null)
				roots.add(s);
			if (s.members.size() == 2) //The 2 being the head and tail only
				graph.edges.add(new Edge(s.head, s.tail));
		}
	}

	buildNestingTreeIndices(roots, 0);
	convertSubgraphEndpoints(graph);
	addContainmentEdges(graph);
	replaceSubgraphsWithBoundaries(graph);
}

}
