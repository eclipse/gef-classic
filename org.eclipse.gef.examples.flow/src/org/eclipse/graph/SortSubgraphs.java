package org.eclipse.graph;

import java.util.*;

/**
 * @author hudsonr
 */
public class SortSubgraphs extends GraphVisitor {

static class NestingTreeEntry {
	List contents = new ArrayList();
	Node subgraph;
	double sortValue;
	int size;
	
	boolean swap(int index) {
		Object left = contents.get(index);
		Object right = contents.get(index + 1);
		double iL = (left instanceof Node)
			? ((Node)left).sortValue
			: ((NestingTreeEntry)left).sortValue;
		double iR = (right instanceof Node)
			? ((Node)right).sortValue
			: ((NestingTreeEntry)right).sortValue;
		if (iL <= iR)
			return false;
		contents.set(index, right);
		contents.set(index+1, left);
		return true;
	}
	
	public void recursiveSort() {
		boolean change = false;
		//Use modified bubble sort for almost-sorted lists.
		do {
			change = false;
			for (int i = 0; i < contents.size() - 1; i++)
				change |= swap(i);
			if (change == false)
				break;
			change = false;
			for (int i = contents.size() - 2; i >= 0; i--)
				change |= swap(i);
		} while (change);
		for (int i=0; i<contents.size(); i++) {
			Object o = contents.get(i);
			if (o instanceof NestingTreeEntry)
				((NestingTreeEntry)o).recursiveSort();
		}
	}
	
	public void calculateSortValues() {
		int total = 0;
		for (int i = 0; i < contents.size(); i++) {
			Object o = contents.get(i);
			if (o instanceof NestingTreeEntry) {
				NestingTreeEntry e = (NestingTreeEntry)o;
				e.calculateSortValues();
				total += (int)(e.sortValue * e.size);
				size += e.size;
			} else {
				total += ((Node)o).index;
				size++;
			}
		}
		sortValue = (double)total / size;
	}
}

CompoundDirectedGraph g;

Set mappings = new HashSet();

Map nestingTreeMap = new HashMap();
NodePair pair = new NodePair();
NestingTreeEntry nestingTrees[];

/**
 * Calculates the average position P for each node and subgraph.  The average position is
 * stored in the sortValue for each node or subgraph.
 * 
 * Runs in approximately linear time with respect to the number of nodes, including
 * virtual nodes.
 */
private void assignSortValues() {
	RankList ranks = g.ranks;
	
	g.subgraphs.resetSortValues();
	g.subgraphs.resetIndices();
	
	/*
	 * For subgraphs, the sum of all positions is kept, along with the number of
	 * contributions, which is tracked in the subgraph's index field.
	 */
	for (int r = 0; r < ranks.size(); r++) {
		Rank rank = ranks.getRank(r);
		for (int j = 0; j < rank.size(); j++) {
			Node node = rank.getNode(j);
			node.x = 0;
			node.sortValue =
				(double)(node.index * 2 + 1) / (ranks.getRank(node.rank).size() * 2);
			Subgraph parent = node.getParent();
			while (parent != null) {
				parent.sortValue += node.sortValue;
				parent.index++;
				parent = parent.getParent();
			}
		}
	}
	
	/*
	 * For each subgraph, divide the sum of the positions by the number of contributions,
	 * to give the average position.
	 */
	for (int i = 0; i < g.subgraphs.size(); i++) {
		Subgraph subgraph = (Subgraph)g.subgraphs.get(i);
		subgraph.sortValue /= subgraph.index;
		subgraph.x = 0;
		System.out.println(subgraph + " average " + subgraph.sortValue);
	}
}

private void breakSubgraphCycles() {
	Collection allNodes = new ArrayList(); //The set of all nodes in the POset
	allNodes.addAll(g.nodes);
	allNodes.addAll(g.subgraphs);
	g.nodes.resetFlags(); //Flags will be set when node has been removed
	g.subgraphs.resetFlags();
	
	Stack noLefts = new Stack(); //The stack of nodes which have no unmarked incoming edges
	
	//Identify all initial nodes for removal
	for (Iterator iter = allNodes.iterator(); iter.hasNext();) {
		Node node = (Node)iter.next();
		if (node.x == 0)
			noLefts.push(node);
	}
	
	//Remove all leftmost nodes, updating the nodes to their right
	while (noLefts.size() > 0) {
		Node node = (Node)noLefts.pop();
		node.flag = true;
		allNodes.remove(node); //$TODO redundant
		if (node.toRight == null)
			continue;
		for (int i = 0; i < node.toRight.size(); i++) {
			Node right = node.toRight.getNode(i);
			right.x--;
			if (right.x == 0)
				noLefts.push(right);
		}
	}
}

private void addToNestingTree(NestingTreeEntry branch) {
	Subgraph s = branch.subgraph.getParent();
	NestingTreeEntry entry = (NestingTreeEntry)nestingTreeMap.get(s);
	if (entry == null) {
		entry = new NestingTreeEntry();
		entry.subgraph = s;
		nestingTreeMap.put(s, entry);
	}
	entry.contents.add(branch);
	if (s != null)
		addToNestingTree(s);
}


private void addToNestingTree(Node node) {
	Subgraph s = node.getParent();
	NestingTreeEntry entry = (NestingTreeEntry)nestingTreeMap.get(s);
	if (entry == null) {
		entry = new NestingTreeEntry();
		entry.subgraph = s;
		nestingTreeMap.put(s, entry);
	}
	entry.contents.add(node);
	if (s != null)
		addToNestingTree(entry);
}

private void buildSubgraphOrderingGraph(NestingTreeEntry entry) {
	
}

private void buildSubgraphOrderingGraph() {
	RankList ranks = g.ranks;
	for (int i = 0; i < ranks.size(); i++) {
		Rank rank = ranks.getRank(i);
		nestingTreeMap.clear();
		for (int j = 0; j < rank.size(); j++) {
			Node node = rank.getNode(j);
			addToNestingTree(node);
		}
		NestingTreeEntry entry = (NestingTreeEntry)nestingTreeMap.get(null);
		nestingTrees[i] = entry;
		entry.calculateSortValues();
		entry.recursiveSort();
		buildSubgraphOrderingGraph(entry);

//		for (int j = 0; j < rank.size() - 1; j++) {
//			Node left = rank.getNode(j);
//			Node right = rank.getNode(j+1);
//			Subgraph ancestor = GraphUtilities.getCommonAncestor(left, right);
//			while (left.getParent() != ancestor)
//				left = left.getParent();
//			while (right.getParent() != ancestor)
//				right = right.getParent();
//			if (left == right)
//				continue;
//			pair.n1 = left;
//			pair.n2 = right;
//			if (mappings.contains(pair))
//				continue;
//			mappings.add(pair);
//			pair = new NodePair();
//			left.leftToRight(right);
//			right.x++; //Using x field to count predecessors.
//		}
	}
}

public void visit(DirectedGraph dg) {
	g = (CompoundDirectedGraph)dg;
	
	assignSortValues();
	buildSubgraphOrderingGraph();
	breakSubgraphCycles();
}

}
