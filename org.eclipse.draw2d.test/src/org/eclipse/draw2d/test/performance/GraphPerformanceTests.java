/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test.performance;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.eclipse.test.performance.Dimension;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.internal.graph.InitialRankSolver;

/*
 * The different tests in this class have been taken from
 * org.eclipse.draw2d.examples.GraphTests.
 */
public class GraphPerformanceTests extends BasePerformanceTestCase {

static Random rand = new Random(90);

/**
 * @param conns
 */
private static void shuffleConnections(int[] conns) {
	for (int i = 0; i < conns.length; i += 2) {
		int swap = (int) (rand.nextFloat() * conns.length) % conns.length / 2;
		swap *= 2;
		int temp = conns[i];
		conns[i] = conns[swap];
		conns[swap] = temp;

		temp = conns[i + 1];
		conns[i + 1] = conns[swap + 1];
		conns[swap + 1] = temp;
	}
}

private void addNodes(NodeList nodes, Node[] row) {
	for (int i = 0; i < row.length; i++)
		if (row[i] != null)
			nodes.add(row[i]);
}

protected void bug90228() {
	Node s11, s10, s9, s8, s7, s6, s5, s4, s3, s2, s1;
	Node h11, h10, h9, h8, h7, h6, h5, h4, h3, h2, h1;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(s11 = createNode("S11.cpp"));
	nodes.add(s10 = createNode("S10.cpp"));
	nodes.add(s9 = createNode("S9.cpp"));
	nodes.add(s8 = createNode("S8.cpp"));
	nodes.add(s7 = createNode("S7.cpp"));
	nodes.add(s6 = createNode("S6.cpp"));
	nodes.add(s5 = createNode("S5.cpp"));
	nodes.add(s4 = createNode("S4.cpp"));
	nodes.add(s3 = createNode("S3.cpp"));
	nodes.add(s2 = createNode("S2.cpp"));
	nodes.add(s1 = createNode("S1.cpp"));
	nodes.add(h11 = createNode("H11.h"));
	nodes.add(h10 = createNode("H10.h"));
	nodes.add(h9 = createNode("H9.h"));
	nodes.add(h8 = createNode("H8.h"));
	nodes.add(h7 = createNode("H7.h"));
	nodes.add(h6 = createNode("H6.h"));
	nodes.add(h5 = createNode("H5.h"));
	nodes.add(h4 = createNode("H4.h"));
	nodes.add(h3 = createNode("H3.h"));
	nodes.add(h2 = createNode("H2.h"));
	nodes.add(h1 = createNode("H1.h"));

	edges.add(createEdge(s11, h11));
	edges.add(createEdge(h11, h10));
	edges.add(createEdge(s10, h10));
	edges.add(createEdge(h10, h9));
	edges.add(createEdge(s9, h9));
	edges.add(createEdge(h9, h8));
	edges.add(createEdge(s8, h8));
	edges.add(createEdge(h8, h7));
	edges.add(createEdge(s7, h7));
	edges.add(createEdge(h7, h6));
	edges.add(createEdge(s6, h6));
	edges.add(createEdge(h6, h5));
	edges.add(createEdge(s5, h5));
	edges.add(createEdge(h5, h4));
	edges.add(createEdge(s4, h4));
	edges.add(createEdge(h4, h3));
	edges.add(createEdge(s3, h3));
	edges.add(createEdge(h3, h2));
	edges.add(createEdge(s2, h2));
	edges.add(createEdge(h2, h1));
	edges.add(createEdge(s1, h1));

	connectNonConnectedSubgraphs(nodes, edges);
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;

	new DirectedGraphLayout().visit(graph);
}

private void connectNonConnectedSubgraphs(List nodes, List edges) {
	Node ghostNode = new Node();
	ghostNode.width = 1;
	ghostNode.height = 1;
	ghostNode.setPadding(new Insets(0));

	nodes.add(ghostNode);

	ListIterator ni = nodes.listIterator();
	while (ni.hasNext()) {
		Node n = (Node) ni.next();

		if (n == ghostNode)
			continue;

		// if node has no incoming connectors then assume it is not connected to
		// the main graph
		if (n.incoming.isEmpty()) {
			Edge e = new Edge(ghostNode, n);
			e.weight = 0;
			edges.add(e);
		}
	}
}

private Edge createEdge(Node start, Node end) {
	Edge edge = new Edge(start, end);
	edge.delta += 1.5;
	return edge;
}

private Node createNode(String name) {
	Node node = new Node(name);
	node.setPadding(new Insets(30));
	return node;
}

protected void graph1() {
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	Node row[], firstRow[];
	firstRow = new Node[3];
	firstRow[1] = new Node("h1");
	firstRow[2] = new Node("h2");
	addNodes(nodes, firstRow);

	row = joinRows(nodes, edges, firstRow, new int[] { 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1,
			6, 2, 7, 2, 8, 2, 9, 2, 10 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 2, 2, 3, 3, 1, 4, 4, 5, 5, 6,
			6, 7, 7, 8, 8, 9, 9, 9, 10, 10, 11 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 1, 2, 2, 3, 2, 6, 3, 6, 4, 4, 5,
			5, 6, 7, 7, 6, 8, 8, 9, 6, 10, 9, 10, 10, 11, 11 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 1, 2, 1, 3, 2, 3, 3, 4, 4, 5, 5,
			6, 7, 3, 8, 7, 9, 8, 10, 9, 10, 10, 11, 11 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 2, 4, 3, 5, 4, 6, 5, 7, 6, 8,
			7, 9, 7, 10, 7, 11, 7 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 2, 3, 4, 4, 4, 5, 3, 6, 4, 7,
			4, 7, 5 });

	row = joinRows(nodes, edges, row, new int[] { 1, 2, 2, 1, 3, 2, 4, 2, 4, 3, 5, 4 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 1, 3, 2, 4, 2, 4, 3, 4, 4 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 1, 3, 2, 4, 2, 4, 3, 4, 4 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 1, 4, 2 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 2, 2, 3 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 2 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 2,
			6, 2, 7, 2, 8, 2, 9, 2, 10 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 3, 1, 5, 1, 7, 2, 9, 2, 4, 3, 6,
			3, 7, 3, 3, 3, 4, 2, 8, 4, 9, 4, 10, 4 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 1, 3, 2, 4, 2, 4, 3, 4, 4 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 1, 4, 2, 1, 3, 4, 3 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 2, 2, 1, 3, 2, 3 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 2,
			6, 2, 7, 2, 8, 2, 9, 2, 10 });

	row = joinRows(nodes, edges, row, new int[] { 1, 1, 3, 1, 5, 1, 7, 2, 9, 2, 4, 3, 6,
			3, 7, 3, 3, 3, 4, 2, 8, 4, 9, 4, 10, 4, 10, 5, 10, 6, 10, 7 });

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;

	new DirectedGraphLayout().visit(graph);
}

private Node[] joinRows(NodeList nodes, EdgeList edges, Node[] firstRow, int[] conns) {
	shuffleConnections(conns);
	Node secondRow[] = new Node[20];
	Node head, tail;
	for (int i = 0; i < conns.length; i += 2) {
		head = firstRow[conns[i]];
		tail = secondRow[conns[i + 1]];
		if (tail == null) {
			tail = secondRow[conns[i + 1]] = new Node("node" + conns[i + 1]);
			tail.width = 78;
		}
		edges.add(new Edge(head, tail));
	}
	addNodes(nodes, secondRow);
	return secondRow;
}


public void testGraphPerformance() {
	tagAsGlobalSummary("Directed Graph Layout", Dimension.CPU_TIME);

	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		if (i >= warmupRuns)
			startMeasuring();

		graph1();
		bug90228();
		graph1();
		bug90228();

		if (i >= warmupRuns)
			stopMeasuring();
	}
	commitMeasurements();
	assertPerformance();
}

}