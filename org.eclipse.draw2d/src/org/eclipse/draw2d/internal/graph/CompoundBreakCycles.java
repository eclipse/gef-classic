package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;

/**
 * CompoundBreakCycles
 * @author Daniel Lee
 */
public class CompoundBreakCycles extends GraphVisitor {

private NodeList graphNodes;
private NodeList sL = new NodeList();

private boolean allFlagged(NodeList nodes) {
	for (int i = 0; i < nodes.size(); i++) {
		if (nodes.getNode(i).flag == false)
			return false;
	}
	return true;
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

private void cycleRemove(NodeList children) {
	NodeList sR = new NodeList();
	do {
		// put all sinks in sR
		NodeList sinks = new NodeList();
		for(int i = 0; i < graphNodes.size(); i++) {
			Node node = graphNodes.getNode(i);
			if (node.flag == false
				&& isSink(graphNodes.getNode(i))
				&& getChildCount(node) == 0) {
					sinks.add(node);
					node.flag = true;
				}		
		}
		while (!sinks.isEmpty()) {
			Node sink = sinks.getNode(sinks.size() - 1);
			sR.add(sink);
			sinks.remove(sink);
			removeIncomingEdges(sink);
			if (sink.getParent() != null) {
				setChildCount(sink.getParent(), getChildCount(sink.getParent()) - 1);
				if (isSink(sink.getParent()) && sink.getParent().flag == false && getChildCount(sink.getParent()) == 0) {
					sinks.add(sink.getParent());
					sink.getParent().flag = true;
				}
			}
			for (int i = 0; i < sink.incoming.size(); i++) {
				Edge e = sink.incoming.getEdge(i);
				if (isSink(e.source) && !e.source.flag && getChildCount(e.source) == 0) {
					sinks.add(e.source);
					e.source.flag = true;
				}
			}
		}
		
		// put all sources in result	
		NodeList sources = new NodeList();
		for(int i = 0; i < graphNodes.size(); i++) {
			Node node = graphNodes.getNode(i);
			if (node.flag == false
				&& isSource(graphNodes.getNode(i))
				&& getChildCount(node) == 0) {
					sources.add(node);
					node.flag = true;
				}
		}
		while (!sources.isEmpty()) {
			Node source = sources.getNode(sources.size() - 1);
			sL.add(source);
			sources.remove(source);
			removeOutgoingEdges(source);
			if (source.getParent() != null) {
				setChildCount(source.getParent(), getChildCount(source.getParent()) - 1);
				if (isSource(source.getParent()) && source.getParent().flag == false && getChildCount(source.getParent()) == 0) {
					sources.add(source.getParent());
					source.getParent().flag = true;
				}

			}
			for (int i = 0; i < source.outgoing.size(); i++) {
				Edge e = source.outgoing.getEdge(i);
				if (isSource(e.target) && !e.target.flag && getChildCount(e.target) == 0){
					sources.add(e.target);
					e.target.flag = true;
				}	
			}		
		}
		
		// all sinks and sources added, find node with highest 
		// outDegree - inDegree
		Node max = findNodeWithMaxDegree(children);
		if (max != null)
			remove(max);
	} while (!allFlagged(children));
	while (!sR.isEmpty())
		sL.add(sR.remove(sR.size() - 1));
	
}

private Node findNodeWithMaxDegree(NodeList nodes) {
	int max = Integer.MIN_VALUE;
	Node maxNode = null;
	
	for (int i = 0; i < nodes.size(); i++) {
		Node node = nodes.getNode(i);
		if (getOutDegree(node) - getInDegree(node) >= max && node.flag == false) {
			max = getOutDegree(node) - getInDegree(node);
			maxNode = node;
		}
	}
	return maxNode;
}

private int getChildCount(Node n) {
	return n.workingInts[3];
}	

private int getInDegree(Node n) {
	int result = n.workingInts[1];
	if (n instanceof Subgraph) {
		Subgraph s = (Subgraph)n;
		for (int i=0; i<s.members.size(); i++)
			if (!s.members.getNode(i).flag)
				result += getInDegree(s.members.getNode(i));
	}
	return result;
}

private int getOrderIndex(Node n) {
	return n.workingInts[0];
}

private int getOutDegree(Node n) {
	int result = n.workingInts[2];
	if (n instanceof Subgraph) {
		Subgraph s = (Subgraph)n;
		for (int i=0; i<s.members.size(); i++)
			if (!s.members.getNode(i).flag)
				result += getOutDegree(s.members.getNode(i));
	}
	return result;
}

private void initializeDegrees(DirectedGraph g) {
	g.nodes.resetFlags();
	g.edges.resetFlags();
	for (int i = 0; i < g.nodes.size(); i++) {
		Node n = g.nodes.getNode(i);
		setInDegree(n, n.incoming.size());
		setOutDegree(n, n.outgoing.size());
		if (n instanceof Subgraph)
			setChildCount(n, ((Subgraph)n).members.size());
		else
			setChildCount(n, 0);
	}
}

private void invertEdges(DirectedGraph g) {
	// Assign order indexes
	int orderIndex = 0;
	for (int i = 0; i < sL.size(); i++) {
		setOrderIndex(sL.getNode(i), orderIndex++);
	}
	
	for (int i = 0; i < g.edges.size(); i++) {
		Edge e = g.edges.getEdge(i);
		if (getOrderIndex(e.source) > getOrderIndex(e.target)) {
			e.invert();
			e.isFeedback = true;
		}	
	}
}

private boolean isSink(Node n) {
	if (getOutDegree(n) != 0)
		return false;
	if (n.getParent() == null)
		return true;
	return isSink(n.getParent());	
}

private boolean isSource(Node n) {
	if (getInDegree(n) != 0)
		return false;
	if (n.getParent() == null)
		return true;
	return isSource(n.getParent());
}

private void remove(Node n) {
	n.flag = true;
	if (n.getParent() != null)
		setChildCount(n.getParent(), getChildCount(n.getParent()) - 1);
	removeIncomingEdges(n);
	removeOutgoingEdges(n);
	sL.add(n);
	if (n instanceof Subgraph) {
		Subgraph s = (Subgraph)n;
		remove(s, s);
		cycleRemove(s.members);
	}
}

private void remove(Subgraph s, Node n) {
	Edge e = null;
	for (int i = 0; i < n.incoming.size(); i++) {
		e = n.incoming.getEdge(i);
		if (!s.isNested(e.source) && !e.flag) {
			//Remove e
			e.flag = true;
			setOutDegree(e.source, getOutDegree(e.source) - 1);
			setInDegree(e.target, getInDegree(e.target) - 1);
		}
	}
	for (int i = 0; i < n.outgoing.size(); i++) {
		e = n.outgoing.getEdge(i);
		if (!s.isNested(e.target) && !e.flag) {
			//Remove e
			e.flag = true;
			setOutDegree(e.source, getOutDegree(e.source) - 1);
			setInDegree(e.target, getInDegree(e.target) - 1);
		}
	}

	if (n instanceof Subgraph) {
		NodeList members = ((Subgraph)n).members;
		for (int i = 0; i < members.size(); i++)
			remove(s, members.getNode(i));
	}	
}

private void removeIncomingEdges(Node n) {
	for(int i = 0; i < n.incoming.size(); i++) {
		Edge e = n.incoming.getEdge(i);
		if (!e.flag) {
			e.flag = true;
			setOutDegree(e.source, getOutDegree(e.source) - 1);
		}
	}
}

private void removeOutgoingEdges(Node n) {
	for (int i = 0; i < n.outgoing.size(); i++) {
		Edge e = n.outgoing.getEdge(i);
		if (!e.flag) {
			e.flag = true;
			setInDegree(e.target, getInDegree(e.target) - 1);
		}
	}
}

private void setChildCount(Node n, int count) {
	n.workingInts[3] = count;
}

private void setInDegree(Node n, int deg) {
	n.workingInts[1] = deg;
}

private boolean changeInDegree(Node n, int delta) {
	return (n.workingInts[1] += delta) == 0;
}

private boolean changeOutDegree(Node n, int delta) {
	return (n.workingInts[2] += delta) == 0;
}

private void setOutDegree(Node n, int deg) {
	n.workingInts[2] = deg;
}

private void setOrderIndex(Node n, int index) {
	n.workingInts[0] = index;
}

/**
 * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	initializeDegrees(g);
	graphNodes = g.nodes;
	
	NodeList elders = new NodeList();
	for (int i = 0; i < graphNodes.size(); i++) {
		if(graphNodes.getNode(i).getParent() == null)
			elders.add(graphNodes.getNode(i));
	}				
	buildNestingTreeIndices(elders, 0);
	cycleRemove(elders);
	invertEdges(g);
}

}
