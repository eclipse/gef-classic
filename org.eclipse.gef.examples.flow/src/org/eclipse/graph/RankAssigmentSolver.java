package org.eclipse.graph;


/**
 * solves the rank assigment problem on a DirectedGraph containing an initial feasible
 * spanning tree.
 * @author hudsonr
 * @since 2.1
 */
public class RankAssigmentSolver extends GraphVisitor {

boolean searchDirection;
protected DirectedGraph graph;
int maxcount = 200;

int depthFirstCutValue(Edge edge, int count) {
	Node n = edge.tail();
	n.spanTreeMin = count;
	int cutvalue = 0;
	int multiplier = (edge.target == n) ? 1 : -1;
	EdgeList list;
	
	list = n.outgoing;
	Edge e;
	for (int i=0; i<list.size(); i++) {
		e = list.getEdge(i);
		if (e.tree && e != edge) {
			count = depthFirstCutValue(e, count);
			cutvalue += (e.cut - e.weight) * multiplier;
		} else {
			cutvalue -= e.weight * multiplier;
		}
	}
	list = n.incoming;
	for (int i=0; i<list.size(); i++) {
		e = list.getEdge(i);
		if (e.tree && e != edge) {
			count = depthFirstCutValue(e, count);
			cutvalue -= (e.cut - e.weight) * multiplier;
		} else {
			cutvalue += e.weight * multiplier;
		}
	}

	edge.cut = cutvalue;
	if (cutvalue < 0)
		graph.cutEdges.add(edge);
	n.spanTreeMax = count;
	return count + 1;
}

void initCutValues() {
	Node root = graph.nodes.getNode(0);
	graph.cutEdges = new EdgeList();
	Edge e;
	root.spanTreeMin = 1;
	root.spanTreeMax = 1;

	for (int i=0; i<root.outgoing.size(); i++) {
		e = root.outgoing.getEdge(i);
		if (!root.spanTreeChildren.contains(e))
			continue;
		root.spanTreeMax = depthFirstCutValue(e, root.spanTreeMax);
	}
	for (int i=0; i<root.incoming.size(); i++) {
		e = root.incoming.getEdge(i);
		if (!root.spanTreeChildren.contains(e))
			continue;
		root.spanTreeMax = depthFirstCutValue(e, root.spanTreeMax);
	}
}

Edge leave() {
	Edge result = null;
	Edge e;
	int minCut = 0;
	int weight = -1;
	for (int i=0; i<graph.cutEdges.size(); i++) {
		e = graph.cutEdges.getEdge(i);
		if (e.cut < minCut) {
			result = e;
			minCut = result.cut;
			weight = result.weight;
		} else if (e.cut == minCut && e.weight > weight) {
			result = e;
			weight = result.weight;
		}
	}
	return result;
}

/**
 * returns the Edge which should be entered.
 * @param branch * @return Edge */
Edge enter(Node branch) {
	Node n;
	Edge result = null;
	int minSlack = Integer.MAX_VALUE;
	boolean incoming = branch.spanTreeParent.target != branch;
//	searchDirection = !searchDirection;
	for (int i = 0; i < graph.nodes.size(); i++) {
		if (searchDirection)
			n = graph.nodes.getNode(i);
		else
			n = graph.nodes.getNode(graph.nodes.size() - 1 - i);
		if (branch.spanTreeContains(n)) {
			EdgeList edges;
			if (incoming)
				edges = n.incoming;
			else
				edges = n.outgoing;
			for (int j = 0; j < edges.size(); j++) {
				Edge e = edges.getEdge(j);
				if (!branch.spanTreeContains(e.opposite(n))
				  && !e.tree
				  && e.getSlack() < minSlack) {
					result = e;
					minSlack = e.getSlack();
				}
			}
		}
	}
	return result;
}

public void visit(DirectedGraph graph) {
	this.graph = graph;
	initCutValues();
	networkSimplexLoop();
	graph.nodes.normalizeRanks();
}

void networkSimplexLoop() {
	Edge leave, enter;
	int count = 0;
	while ((leave = leave()) != null && count < 88) {

		if (count == maxcount){
			System.out.println("failed to find optimal solution");
			break;
		}
		count++;
		
		Node leaveTail = leave.tail();
		Node leaveHead = leave.head();

		enter = enter(leaveTail);
		if (enter == null)
			break;
		
		//Break the "leave" edge from the spanning tree
		leaveHead.spanTreeChildren.remove(leave);
		leaveTail.spanTreeParent = null;
		leave.tree = false;
		graph.cutEdges.remove(leave);
		
		Node enterTail = enter.source;
		if (!leaveTail.spanTreeContains(enterTail))
			//Oops, wrong end of the edge
			enterTail = enter.target;
		Node enterHead = enter.opposite(enterTail);

		//Prepare enterTail by making it the root of its sub-tree
		updateSubgraph(enterTail);

		//Add "enter" edge to the spanning tree
		enterHead.spanTreeChildren.add(enter);
		enterTail.spanTreeParent = enter;
		enter.tree = true;

		repairCutValues(enter);

		Node commonAncestor = enterHead;

		while (!commonAncestor.spanTreeContains(leaveHead)) {
			repairCutValues(commonAncestor.spanTreeParent);
			commonAncestor = commonAncestor.getSpanTreeParent();
		}
		while (leaveHead != commonAncestor) {
			repairCutValues(leaveHead.spanTreeParent);
			leaveHead = leaveHead.getSpanTreeParent();
		}
		updateMinMax(commonAncestor, commonAncestor.spanTreeMin);
		tightenEdge(enter);
	}
}

void tightenEdge(Edge edge) {
	Node tail = edge.tail();
	int delta = edge.getSlack();
	if (tail == edge.target)
		delta = -delta;
	Node n;
	for (int i=0; i<graph.nodes.size(); i++){
		n = graph.nodes.getNode(i);
		if (tail.spanTreeContains(n))
			n.rank += delta;
	}
}

int updateMinMax(Node root, int count) {
	root.spanTreeMin = count;
	for (int i=0; i<root.spanTreeChildren.size(); i++)
		count = updateMinMax(root.spanTreeChildren.getEdge(i).tail(), count);
	root.spanTreeMax = count;
	return count+1;
}

void updateSubgraph(Node root) {
	Edge flip = root.spanTreeParent;
	if (flip != null) {
		Node rootParent = root.getSpanTreeParent();
		rootParent.spanTreeChildren.remove(flip);
		updateSubgraph(rootParent);
		root.spanTreeParent = null;
		rootParent.spanTreeParent = flip;
		repairCutValues(flip);
		root.spanTreeChildren.add(flip);
	}
}

void repairCutValues(Edge edge) {
	graph.cutEdges.remove(edge);
	Node n = edge.tail();
	int cutvalue = 0;
	int multiplier = (edge.target == n) ? 1 : -1;
	EdgeList list;
	
	list = n.outgoing;
	Edge e;
	for (int i=0; i<list.size(); i++) {
		e = list.getEdge(i);
		if (e.tree && e != edge)
			cutvalue += (e.cut - e.weight) * multiplier;
		else
			cutvalue -= e.weight * multiplier;
	}
	list = n.incoming;
	for (int i=0; i<list.size(); i++) {
		e = list.getEdge(i);
		if (e.tree && e != edge)
			cutvalue -= (e.cut - e.weight) * multiplier;
		else
			cutvalue += e.weight * multiplier;
	}

	edge.cut = cutvalue;
	if (cutvalue < 0)
		graph.cutEdges.add(edge);
}

}
