package org.eclipse.graph;

/**
 * @author hudsonr
 */
abstract class SpanningTreeVisitor extends GraphVisitor {

Edge getParentEdge(Node node) {
	return (Edge)node.workingData[1];
}

EdgeList getSpanningTreeChildren(Node node) {
	return (EdgeList)node.workingData[0];
}

protected Node getTreeHead(Edge edge) {
	if (getParentEdge(edge.source) == edge)
		return edge.target;
	return edge.source;
}

Node getTreeParent(Node node) {
	Edge e = getParentEdge(node);
	if (e == null)
		return null;
	return e.opposite(node);
}

protected Node getTreeTail(Edge edge) {
	if (getParentEdge(edge.source) == edge)
		return edge.source;
	return edge.target;
}

void setParentEdge(Node node, Edge edge) {
	node.workingData[1] = edge;
}

}
