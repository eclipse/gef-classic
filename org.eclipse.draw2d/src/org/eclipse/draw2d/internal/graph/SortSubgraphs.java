/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal.graph;

import java.util.*;

import org.eclipse.draw2d.graph.*;

/**
 * @author hudsonr
 */
public class SortSubgraphs extends GraphVisitor {

static class NestingTreeEntry {
	List contents = new ArrayList();
	boolean isLeaf = true;
	int size;
	double sortValue;
	Node subgraph;
	
	public void calculateSortValues() {
		int total = 0;
		for (int i = 0; i < contents.size(); i++) {
			Object o = contents.get(i);
			if (o instanceof NestingTreeEntry) {
				isLeaf = false;
				NestingTreeEntry e = (NestingTreeEntry)o;
				e.calculateSortValues();
				total += (int)(e.sortValue * e.size);
				size += e.size;
			} else {
				Node n = (Node)o;
				n.sortValue = n.index;
				total += n.index;
				size++;
			}
		}
		sortValue = (double)total / size;
	}
	
	public void getSortValueFromSubgraph() {
		if (subgraph != null)
			sortValue = subgraph.sortValue;
		for (int i = 0; i < contents.size(); i++) {
			Object o = contents.get(i);
			if (o instanceof NestingTreeEntry)
				((NestingTreeEntry)o).getSortValueFromSubgraph();
		}
	}

	public void recursiveSort() {
		if (isLeaf)
			return;
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
		for (int i = 0; i < contents.size(); i++) {
			Object o = contents.get(i);
			if (o instanceof NestingTreeEntry)
				((NestingTreeEntry)o).recursiveSort();
		}
	}
	
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
		contents.set(index + 1, left);
		return true;
	}
	public String toString() {
		return "Nesting:" + subgraph; //$NON-NLS-1$
	}
}

CompoundDirectedGraph g;

Map nestingTreeMap = new HashMap();
NestingTreeEntry nestingTrees[];

Set OGedges = new HashSet();
Set OGmembers = new HashSet();
NodePair pair = new NodePair();

private void addToNestingTree(NestingTreeEntry branch) {
	Subgraph subgraph = branch.subgraph.getParent();
	NestingTreeEntry parent = (NestingTreeEntry)nestingTreeMap.get(subgraph);
	if (parent == null) {
		parent = new NestingTreeEntry();
		parent.subgraph = subgraph;
		nestingTreeMap.put(subgraph, parent);
		if (subgraph != null)
			addToNestingTree(parent);
	}
	parent.contents.add(branch);
}

private void addToNestingTree(Node child) {
	Subgraph subgraph = child.getParent();
	NestingTreeEntry parent = (NestingTreeEntry)nestingTreeMap.get(subgraph);
	if (parent == null) {
		parent = new NestingTreeEntry();
		parent.subgraph = subgraph;
		nestingTreeMap.put(subgraph, parent);
		if (subgraph != null)
			addToNestingTree(parent);
	}
	parent.contents.add(child);
}

private void breakSubgraphCycles() {
	//The stack of nodes which have no unmarked incoming edges
	List noLefts = new ArrayList();

	int index = 1;
	//Identify all initial nodes for removal
	for (Iterator iter = OGmembers.iterator(); iter.hasNext();) {
		Node node = (Node)iter.next();
		if (node.x == 0)
			sortedInsert(noLefts, node);
	}
	
	Node cycleRoot;
	do {
		//Remove all leftmost nodes, updating the nodes to their right
		while (noLefts.size() > 0) {
			Node node = (Node)noLefts.remove(noLefts.size() - 1);
			node.sortValue = index++;
			OGmembers.remove(node);
//			System.out.println("removed:" + node);
			NodeList rightOf = rightOf(node);
			if (rightOf == null)
				continue;
			for (int i = 0; i < rightOf.size(); i++) {
				Node right = rightOf.getNode(i);
				right.x--;
				if (right.x == 0)
					sortedInsert(noLefts, right);
			}
		}
		cycleRoot = null;
		double min = Double.MAX_VALUE;
		for (Iterator iter = OGmembers.iterator(); iter.hasNext();) {
			Node node = (Node)iter.next();
			if (node.sortValue < min) {
				cycleRoot = node;
				min = node.sortValue;
			}
		}
		if (cycleRoot != null) {
			//break the cycle;
			sortedInsert(noLefts, cycleRoot);
//			System.out.println("breaking cycle with:" + cycleRoot);
//			Display.getCurrent().beep();
			cycleRoot.x = -1; //prevent x from ever reaching 0
		} // else if (OGmembers.size() > 0)
			//System.out.println("FAILED TO FIND CYCLE ROOT"); //$NON-NLS-1$
	} while (cycleRoot != null);
}

private void buildSubgraphOrderingGraph() {
	RankList ranks = g.ranks;
	nestingTrees = new NestingTreeEntry[ranks.size()];
	for (int r = 0; r < ranks.size(); r++) {
		Rank rank = ranks.getRank(r);
		nestingTreeMap.clear();
		for (int j = 0; j < rank.count(); j++) {
			Node node = rank.getNode(j);
			addToNestingTree(node);
		}
		NestingTreeEntry entry = (NestingTreeEntry)nestingTreeMap.get(null);
		nestingTrees[r] = entry;
		entry.calculateSortValues();
		entry.recursiveSort();
	}

	for (int i = 0; i < nestingTrees.length; i++) {
		NestingTreeEntry entry = nestingTrees[i];
		buildSubgraphOrderingGraph(entry);
	}
}

private void buildSubgraphOrderingGraph(NestingTreeEntry entry) {
	NodePair pair = new NodePair();
	if (entry.isLeaf)
		return;
	for (int i = 0; i < entry.contents.size(); i++) {
		Object right = entry.contents.get(i);
		if (right instanceof Node)
			pair.n2 = (Node)right;
		else {
			pair.n2 = ((NestingTreeEntry)right).subgraph;
			buildSubgraphOrderingGraph((NestingTreeEntry)right);
		}
		if (pair.n1 != null && !OGedges.contains(pair)) {
			OGedges.add(pair);
			leftToRight(pair.n1, pair.n2);
			OGmembers.add(pair.n1);
			OGmembers.add(pair.n2);
			pair.n2.x++; //Using x field to count predecessors.
			pair = new NodePair(pair.n2, null);
		} else {
			pair.n1 = pair.n2;
		}
	}
}

/**
 * Calculates the average position P for each node and subgraph.  The average position is
 * stored in the sortValue for each node or subgraph.
 * 
 * Runs in approximately linear time with respect to the number of nodes, including
 * virtual nodes.
 */
private void calculateSortValues() {
	RankList ranks = g.ranks;
	
	g.subgraphs.resetSortValues();
	g.subgraphs.resetIndices();
	
	/*
	 * For subgraphs, the sum of all positions is kept, along with the number of
	 * contributions, which is tracked in the subgraph's index field.
	 */
	for (int r = 0; r < ranks.size(); r++) {
		Rank rank = ranks.getRank(r);
		for (int j = 0; j < rank.count(); j++) {
			Node node = rank.getNode(j);
			node.sortValue = node.index;
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
	}
}

private void repopulateRank(Rank r, NestingTreeEntry entry) {
	for (int i = 0; i < entry.contents.size(); i++) {
		Object o = entry.contents.get(i);
		if (o instanceof Node)
			r.add(o);
		else
			repopulateRank(r, (NestingTreeEntry)o);
	}
}

private void repopulateRanks() {
	for (int i = 0; i < nestingTrees.length; i++) {
		Rank rank = g.ranks.getRank(i);
		rank.clear();
		repopulateRank(rank, nestingTrees[i]);
	}
}

private NodeList rightOf(Node left) {
	return (NodeList)left.workingData[0];
}

private void leftToRight(Node left, Node right) {
	rightOf(left).add(right);
}

void sortedInsert(List list, Node node) {
	int insert = 0;
	while (insert < list.size()
	  && ((Node)list.get(insert)).sortValue > node.sortValue)
		insert++;
	list.add(insert, node);
}

private void topologicalSort() {
	for (int i = 0; i < nestingTrees.length; i++) {
		nestingTrees[i].getSortValueFromSubgraph();
		nestingTrees[i].recursiveSort();
	}
}

void init() {
	for (int r = 0; r < g.ranks.size(); r++) {
		Rank rank = g.ranks.getRank(r);
		for (int i = 0; i < rank.count(); i++) {
			Node n = (Node)rank.get(i);
			n.workingData[0] = new NodeList();
		}
	}
	for (int i = 0; i < g.subgraphs.size(); i++) {
		Subgraph s = (Subgraph)g.subgraphs.get(i);
		s.workingData[0] = new NodeList();
	}
}

public void visit(DirectedGraph dg) {
	g = (CompoundDirectedGraph)dg;
	
	init();
	buildSubgraphOrderingGraph();
	calculateSortValues();
	breakSubgraphCycles();
	topologicalSort();
	repopulateRanks();
}

}
