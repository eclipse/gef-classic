package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;

/**
 * CompoundBreakCycles
 * @author Daniel Lee
 */
public class CompoundBreakCycles extends GraphVisitor {

private NodeList graphNodes;
private NodeList sR = new NodeList();

private boolean allFlagged(NodeList nodes) {
	boolean allflagged = false;
	for (int i = 0; i < nodes.size(); i++) {
		if (nodes.getNode(i).flag = false)
			return false;
	}
	return true;
}

private void cycleRemove(NodeList children, NodeList result) {
	do {
		// put all sinks in sR
		NodeList sinks = new NodeList();
		for(int i = 0; i < graphNodes.size(); i++) {
			Node node = graphNodes.getNode(i);
			if (node.flag == false && isSink(graphNodes.getNode(i)))
				sinks.add(node);
		}
		while (!sinks.isEmpty()) {
			Node sink = sinks.getNode(sinks.size() - 1);
			sink.flag = true;
			sR.add(sink);
			sinks.remove(sink);
			for (int i = 0; i < sink.incoming.size(); i++) {
				Node source = sink.incoming.getEdge(i).source;
				setOutDegree(source, getOutDegree(source) - 1);
				if (isSink(source))
					sinks.add(source);
			}		
		}
		
		// put all sources in result	
		NodeList sources = new NodeList();
		for(int i = 0; i < graphNodes.size(); i++) {
			Node node = graphNodes.getNode(i);
			if (node.flag == false && isSource(graphNodes.getNode(i)))
				sources.add(node);
		}
		while (!sources.isEmpty()) {
			Node source = sources.getNode(sinks.size() - 1);
			source.flag = true;
			result.add(source);
			sources.remove(source);
			for (int i = 0; i < source.outgoing.size(); i++) {
				Node target = source.outgoing.getEdge(i).target;
				setInDegree(target, getInDegree(target) - 1);
				if (isSource(source))
					sources.add(source);
			}		
		}
		
		// all sinks and sources added, find node with highest 
		// outDegree - inDegree
		Node max = findNodeWithMaxDegree(children);
		//remove the node
		if (max instanceof Subgraph)
			cycleRemove(((Subgraph)max).members, result);
	} while (!allFlagged(children));

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

private int getInDegree(Node n) {
	return n.workingInts[1];
}

private int getOutDegree(Node n) {
	return n.workingInts[2];
}

private void initializeDegrees(DirectedGraph g) {
	g.nodes.resetFlags();
	for (int i = 0; i < g.nodes.size(); i++) {
		Node n = g.nodes.getNode(i);
		setInDegree(n, n.incoming.size());
		setOutDegree(n, n.outgoing.size());
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

private void setInDegree(Node n, int deg) {
	n.workingInts[1] = deg;
}

private void setOutDegree(Node n, int deg) {
	n.workingInts[2] = deg;
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
	cycleRemove(elders, new NodeList());
}

}
