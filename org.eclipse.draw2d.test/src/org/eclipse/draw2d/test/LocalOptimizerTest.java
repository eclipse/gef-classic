/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Rank;
import org.eclipse.draw2d.internal.graph.LocalOptimizer;
import org.eclipse.draw2d.internal.graph.PopulateRanks;

/**
 * Tests the swapping of adjacent nodes in a directed graph.
 * since 3.0
 */
public class LocalOptimizerTest extends TestCase {

DirectedGraph graph;
Node a, b, c, d, e, f, g, h, i, j, k, l;

/*
 * @see TestCase#tearDown()
 */
protected void tearDown() throws Exception {
	super.tearDown();
}

public void testIncomingSwapNeeded() {
	buildNodes();
	
	/*
	 *  A B C  D
	 *  |\|  \ |
	 *  | X   \|
	 *  | |\   |
	 *  E F G  H
	 */
	
	new Edge(a,e);
	new Edge(b,f);
	new Edge(a,g);
	new Edge(c,h);
	new Edge(d,h);
	new LocalOptimizer().visit(graph);
	
	checkResults(new Node[]   [] {
			new Node[] {a, b, c, d},
			new Node[] {e, g, f, h}});
}

public void testIncomingOffsetSwapNeeded() {
	buildNodes();
	
	/*
	 *  A  B  C  (C should swap twice to first position)
	 *   \ \ /
	 *    \ X
	 *     X \
	 *    / \ \
	 * [-----E----] F  G  H
	 *     \   /   / 
	 *      \ /   /  
	 *       X   /   
	 *      / \ /    
	 * I   J   K    L  (Should not swap)
	 */
	
	new Edge(a,e);
	new Edge(b,e).offsetTarget = 30;
	new Edge(c,e).offsetTarget = 20;

	new Edge(e,k).offsetSource = 20;
	new Edge(e,j).offsetSource = 30;
	new Edge(f,k);

	
	new LocalOptimizer().visit(graph);
	
	checkResults(new Node[][] {
			new Node[] {c, a, b, d},
			new Node[] {e, f, g, h},
			new Node[] {i, j, k, l}});
}

public void testOutgoingSwapNeeded() {
	buildNodes();
	
	/*
	 *  A  BC D
	 *   \ | X
	 *    \|/ \
	 *  E  F G H
	 */
	
	new Edge(a,f);
	new Edge(b,f);
	new Edge(d,f);
	new Edge(c,h);
	new LocalOptimizer().visit(graph);
	
	checkResults(new Node[][] {
			new Node[] {a, b, d, c},
			new Node[] {e, f, g, h}});
}

public void testBidirectionalSwapNeeded() {
	buildNodes();
	
	/*
	 *  A B C D
	 *  \ |\    
	 *   \| \   
	 *  E F  G H    (F and G should swap)
	 *      X|   
	 *     / X    
	 *    |  |\ 
	 *  I J  K L
	 * 
	 * 
	 *    A   B C D (Which causes A&B on previous rank to swap)
	 *     \ /| 
	 *      X |
	 *     / \|   
	 *  E G   F  H
	 *    |\  \ 
	 *    | \  \ 
	 *  I J  K  L
	 * 
	 */
	
	new Edge(a,f);
	new Edge(b,f);
	new Edge(b,g);
	new Edge(f,l);
	new Edge(g,j);
	new Edge(g,k);
	
	new LocalOptimizer().visit(graph);
	
	checkResults(new Node[][] {
			new Node[] {b, a, c, d},
			new Node[] {e, g, f, h},
			new Node[] {i, j, k, l}});
}

/**
 * @since 3.0
 * @param nodes
 */
private void checkResults(Node[][] nodes) {
	for (int r = 0; r < nodes.length; r++) {
		Node[] row = nodes[r];
		Rank rank = graph.ranks.getRank(r);
		assertEquals(rank.size(), row.length);
		for (int n = 0; n < row.length; n++) {
			Node node = row[n];
			assertEquals("Unexpected node encountered at:" + r + "," + n, node,
					rank.getNode(n));
		}
	}
}

/**
 * @since 3.0
 * 
 */
private void buildNodes() {
	graph = new DirectedGraph();
	NodeList nodes = new NodeList();
	graph.nodes = nodes;
	nodes.add(a = new Node("a"));
	nodes.add(b = new Node("b"));
	nodes.add(c = new Node("c"));
	nodes.add(d = new Node("d"));
	nodes.add(e = new Node("E"));
	nodes.add(f = new Node("F"));
	nodes.add(g = new Node("G"));
	nodes.add(h = new Node("H"));
	nodes.add(i = new Node("i"));
	nodes.add(j = new Node("j"));
	nodes.add(k = new Node("k"));
	nodes.add(l = new Node("l"));
	
	rankNodes(new Node[] {a, b, c, d}, 0);
	rankNodes(new Node[] {e, f, g, h}, 1);
	rankNodes(new Node[] {i, j, k, l}, 2);
	new PopulateRanks().visit(graph);
	for (int i = 0; i < graph.ranks.size(); i++)
		graph.ranks.getRank(i).assignIndices();
}

private void rankNodes(Node[] nodes, int rank) {
	for (int i = 0; i < nodes.length; i++) {
		Node node = nodes[i];
		node.rank = rank;
		node.index = i;
	}
}

}
