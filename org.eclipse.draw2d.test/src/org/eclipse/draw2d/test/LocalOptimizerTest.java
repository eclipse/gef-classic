/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Rank;

/**
 * Tests the swapping of adjacent nodes in a directed graph. since 3.0
 * 
 */
public class LocalOptimizerTest extends TestCase {

	private DirectedGraph graph;
	private Node a, b, c, d, e, f, g, h, i, j, k, l;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		graph = new DirectedGraph();

		// create nodes and populate their ranks
		a = createNode("a");
		b = createNode("b");
		c = createNode("c");
		d = createNode("d");
		e = createNode("e");
		f = createNode("f");
		g = createNode("g");
		h = createNode("h");
		i = createNode("i");
		j = createNode("j");
		k = createNode("k");
		l = createNode("l");

		rankNodes(new Node[] { a, b, c, d }, 0);
		rankNodes(new Node[] { e, f, g, h }, 1);
		rankNodes(new Node[] { i, j, k, l }, 2);

		createDirectedGraphLayoutWithSelectedStepOnly(
				new String[] { "org.eclipse.draw2d.graph.PopulateRanks" })
				.visit(graph);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testIncomingSwapNeeded() {
		/*
		 * A B C D |\| \ | | X \| | |\ | E F G H
		 */
		createEdge(a, e);
		createEdge(b, f);
		createEdge(a, g);
		createEdge(c, h);
		createEdge(d, h);

		createDirectedGraphLayoutWithSelectedStepOnly(
				new String[] { "org.eclipse.draw2d.graph.LocalOptimizer" })
				.visit(graph);

		checkResults(new Node[][] { new Node[] { a, b, c, d },
				new Node[] { e, g, f, h } });
	}

	public void testOutgoingSwapNeeded() {
		/*
		 * A BC D \ | X \|/ \ E F G H
		 */
		createEdge(a, f);
		createEdge(b, f);
		createEdge(d, f);
		createEdge(c, h);

		createDirectedGraphLayoutWithSelectedStepOnly(
				new String[] { "org.eclipse.draw2d.graph.LocalOptimizer" })
				.visit(graph);

		checkResults(new Node[][] { new Node[] { a, b, d, c },
				new Node[] { e, f, g, h } });
	}

	public void testIncomingOffsetSwapNeeded() {
		/*
		 * A B C (C should swap twice to first position) \ \ / \ X X \ / \ \
		 * [-----E----] F G H \ / / \ / / X / / \ / I J K L (Should not swap)
		 */
		createEdge(a, e);
		createEdge(b, e).offsetTarget = 30;
		createEdge(c, e).offsetTarget = 20;

		createEdge(e, k).offsetSource = 20;
		createEdge(e, j).offsetSource = 30;
		createEdge(f, k);

		createDirectedGraphLayoutWithSelectedStepOnly(
				new String[] { "org.eclipse.draw2d.graph.LocalOptimizer" })
				.visit(graph);

		checkResults(new Node[][] { new Node[] { c, a, b, d },
				new Node[] { e, f, g, h }, new Node[] { i, j, k, l } });
	}

	public void testBidirectionalSwapNeeded() {
		/*
		 * A B C D \ |\ \| \ E F G H (F and G should swap) X| / X | |\ I J K L
		 * 
		 * 
		 * A B C D (Which causes A&B on previous rank to swap) \ /| X | / \| E G
		 * F H |\ \ | \ \ I J K L
		 */
		createEdge(a, f);
		createEdge(b, f);
		createEdge(b, g);
		createEdge(f, l);
		createEdge(g, j);
		createEdge(g, k);

		createDirectedGraphLayoutWithSelectedStepOnly(
				new String[] { "org.eclipse.draw2d.graph.LocalOptimizer" })
				.visit(graph);

		checkResults(new Node[][] { new Node[] { b, a, c, d },
				new Node[] { e, g, f, h }, new Node[] { i, j, k, l } });
	}

	/**
	 * LocalOptimizer and other GraphVisitors are package private, so we cannot
	 * instantiate them directly. Instead, we use a DirectedGraphLayout and
	 * remove all other steps from it.
	 * 
	 * @return A DirectedGraphLayout containing only the selected steps.
	 */
	private DirectedGraphLayout createDirectedGraphLayoutWithSelectedStepOnly(
			String[] graphVisitorClassNames) {
		try {
			DirectedGraphLayout layout = new DirectedGraphLayout();
			Field stepsField = DirectedGraphLayout.class
					.getDeclaredField("steps");
			stepsField.setAccessible(true);
			ArrayList steps = (ArrayList) stepsField.get(layout);
			ArrayList filteredSteps = new ArrayList();
			for (int i = 0; i < steps.size(); i++) {
				Object graphVisitor = steps.get(i);
				for (int j = 0; j < graphVisitorClassNames.length; j++) {
					if (graphVisitorClassNames[j].equals(graphVisitor
							.getClass().getName())) {
						filteredSteps.add(graphVisitor);
					}
				}
			}
			assertEquals(graphVisitorClassNames.length, filteredSteps.size());
			stepsField.set(layout, filteredSteps);
			return layout;
		} catch (Exception e) {
			fail(e.getMessage());
			return null;
		}
	}

	private Node createNode(String label) {
		Node node = new Node(label);
		graph.nodes.add(node);
		return node;
	}

	private Edge createEdge(Node n1, Node n2) {
		Edge edge = new Edge(n1, n2);
		graph.edges.add(edge);
		return edge;
	}

	private void rankNodes(Node[] nodes, int rank) {
		for (int i = 0; i < nodes.length; i++) {
			Node node = nodes[i];
			try {
				// node.rank = rank;
				Field rankField = Node.class.getDeclaredField("rank");
				rankField.setAccessible(true);
				rankField.set(node, new Integer(rank));
				// node.index = i;
				Field indexField = Node.class.getDeclaredField("index");
				indexField.setAccessible(true);
				indexField.set(node, new Integer(i));
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}
	}

	private void checkResults(Node[][] nodes) {
		for (int r = 0; r < nodes.length; r++) {
			Node[] row = nodes[r];
			Rank rank = graph.ranks.getRank(r);
			assertEquals(rank.size(), row.length);
			for (int n = 0; n < row.length; n++) {
				Node node = row[n];
				assertEquals("Unexpected node encountered at:" + r + "," + n,
						node, rank.getNode(n));
			}
		}
	}
}
