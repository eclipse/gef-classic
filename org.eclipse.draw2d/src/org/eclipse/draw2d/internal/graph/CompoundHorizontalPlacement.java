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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Rank;
import org.eclipse.draw2d.graph.RankList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.draw2d.graph.SubgraphBoundary;

/**
 * Calculates the X-coordinates for nodes in a compound directed graph. 
 * @author Randy Hudson
 * @since 2.1.2
 */
public class CompoundHorizontalPlacement extends HorizontalPlacement {

class LeftRight {
	//$TODO Delete and use NodePair class, equivalent
	Object left, right;
	LeftRight(Object l, Object r) {
		left = l; right = r;
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		LeftRight entry = (LeftRight)obj;
		return entry.left.equals(left) && entry.right.equals(right);
	}
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}
}

Set entries = new HashSet();

/**
 * @see org.eclipse.graph.HorizontalPlacement#applyGPrime()
 */
void applyGPrime() {
	super.applyGPrime();
	NodeList subgraphs = ((CompoundDirectedGraph)graph).subgraphs;
	for (int i = 0; i < subgraphs.size(); i++) {
		Subgraph s = (Subgraph)subgraphs.get(i);
		s.x = s.left.x;
		s.width = s.right.x + s.right.width - s.x;
	}
}

/**
 * @see org.eclipse.graph.HorizontalPlacement#buildRankSeparators(org.eclipse.graph.RankList)
 */
void buildRankSeparators(RankList ranks) {
	CompoundDirectedGraph g = (CompoundDirectedGraph)graph;
	
	Rank rank;
	for (int row = 0; row < g.ranks.size(); row++) {
		rank = g.ranks.getRank(row);
		Node n = null, prev = null;
		for (int j = 0; j < rank.size(); j++) {
			n = rank.getNode(j);
			if (prev == null) {
				addSeparatorsLeft(n, null);
			} else {
				Subgraph s = GraphUtilities.getCommonAncestor(prev, n);
				Node left = addSeparatorsRight(prev, s);
				Node right = addSeparatorsLeft(n, s);
				createEdge(left, right);
			}
			prev = n;
		}
		if (n != null)
			addSeparatorsRight(n, null);
	}
}

void createEdge(Node left, Node right) {
	LeftRight entry = new LeftRight(left, right);
	if (entries.contains(entry))
		return;
	entries.add(entry);
	int separation = left.width + Math.max(
		graph.getPadding(left).right,
		graph.getPadding(right).left);
	prime.edges.add(new Edge(
		getPrime(left), getPrime(right), separation, 0
	));
}

//void addEdge(Node u, Node v, Edge e, int weight) {
//	Node uLeft = u, uRight = u, vLeft = v, vRight = v;
//	boolean special = false;
//	// If U is a boundary
//	if (u instanceof SubgraphBoundary && u.getParent().tail == u) {
//		uLeft = u.getParent().left;
//		uRight = u.getParent().right;
//		special = true;
//	}
//	
//	if (v instanceof SubgraphBoundary && v.getParent().head == v) {
//		vLeft = v.getParent().left;
//		vRight = v.getParent().right;
//		special = true;
//	}
//	
//	if (special) {
//		super.addEdge(uLeft, vLeft, e, weight);
//		super.addEdge(uRight, vRight, e, weight);
//	} else
//		super.addEdge(u, v, e, weight);
//}

Node addSeparatorsLeft(Node n, Subgraph graph) {
	Subgraph parent = n.getParent();
	while (parent != graph && parent != null) {
		createEdge(getLeft(parent), n);
		n = parent.left;
		parent = parent.getParent();
	}
	return n;
}

Node addSeparatorsRight(Node n, Subgraph graph) {
	Subgraph parent = n.getParent();
	while (parent != graph && parent != null) {
		createEdge(n, getRight(parent));
		n = parent.right;
		parent = parent.getParent();
	}
	return n;
}

Node getLeft(Subgraph s) {
	if (s.left == null) {
		s.left = new SubgraphBoundary(s, graph.getPadding(s), 1);
		s.left.rank = (s.head.rank + s.tail.rank) / 2;

		Node head = getPrime(s.head);
		Node tail = getPrime(s.tail);
		Node left = getPrime(s.left);
		Node right = getPrime(getRight(s));
		prime.edges.add(new Edge(left, right, s.width, 0));
		prime.edges.add(new Edge(left, head, 0, 1));
		prime.edges.add(new Edge(head, right, 0, 1));
		prime.edges.add(new Edge(left, tail, 0, 1));
		prime.edges.add(new Edge(tail, right, 0, 1));
	}
	return s.left;
}

Node getRight(Subgraph s) {
	if (s.right == null) {
		s.right = new SubgraphBoundary(s, graph.getPadding(s), 3);
		s.right.rank = (s.head.rank + s.tail.rank) / 2;
	}
	return s.right;
}

Node getPrime(Node n) {
	Node nPrime = get(n);
	if (nPrime == null) {
		nPrime = new Node(n);
		prime.nodes.add(nPrime);
		map(n, nPrime);
	}
	return nPrime;
}

public void visit(DirectedGraph g) {
	super.visit(g);
}

}
