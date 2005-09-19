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
public class GraphPerformanceTests extends BasePerformanceTestCase
{

static Random rand = new Random(90);
	
/**
 * @param conns
 */
private static void shuffleConnections(int[] conns) {
	for (int i=0; i<conns.length; i+= 2){
		int swap = (int)(rand.nextFloat()* conns.length) % conns.length/2;
		swap *= 2;
		int temp = conns[i];
		conns[i] = conns[swap];
		conns[swap] = temp;
		
		temp = conns[i+1];
		conns[i+1] = conns[swap+1];
		conns[swap+1] = temp;
	}
}

private void addNodes(NodeList nodes, Node[] row) {
	for (int i=0; i<row.length; i++)
		if (row[i] != null)
			nodes.add(row[i]);
}

protected void anotherTour() {
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();
	
	Node row[], firstRow[];
	firstRow = new Node[4];
	firstRow[1] = new Node("h1");
	firstRow[2] = new Node("h2");
	firstRow[3] = new Node("h3");
	addNodes(nodes, firstRow);
	row = joinRows(nodes, edges, firstRow, new int[] {
		1,1,
		1,2,
		1,3,
		2,3,
		2,4,
		3,3,
		3,4
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,
		3,4,
		2,1,
		2,3,
		3,2,
		4,4
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,1,2,
		2,1,2,4,
		3,3,
		3,4,4,3,
		4,5
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,2,2,3,3,4,4,5,5,1,2,4,3,5,4
	});

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void balanceThis1() {
	Node a,b1, b2, b3, b4,b5,c,d,e,f,g;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(b1 = new Node("node b1"));
	nodes.add(b2 = new Node("node b2"));
	nodes.add(b3 = new Node("node b3"));
	nodes.add(b4 = new Node("node b4"));
	nodes.add(b5 = new Node("node b5"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
	nodes.add(f = new Node("node f"));
	nodes.add(g = new Node("node g"));
	nodes.add(a = new Node("node a"));

	b3.width = b2.width = 90;

	edges.add(new Edge(c, d));
	edges.add(new Edge(c, e));
	edges.add(new Edge(e, f));
	edges.add(new Edge(e, g));
	edges.add(new Edge(a, b1));
	edges.add(new Edge(a, b2));
	edges.add(new Edge(a, b3));
	edges.add(new Edge(a, b4));
	edges.add(new Edge(a, b5));

	edges.add(new Edge(b3, d));
	edges.add(new Edge(b4, f));

	edges.add(new Edge(a, c));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void balanceThis2() {
	Node a,b2, b3, b4,c,d,e,f,g;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(b2 = new Node("node b2"));
	nodes.add(b3 = new Node("node b3"));
	nodes.add(b4 = new Node("node b4"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
	nodes.add(f = new Node("node f"));
	nodes.add(g = new Node("node g"));
	nodes.add(a = new Node("node a"));

	edges.add(new Edge(d, c));
	edges.add(new Edge(e, c));
	edges.add(new Edge(f, e));
	edges.add(new Edge(g, e));
	edges.add(new Edge(b2, a));
	edges.add(new Edge(b3, a));
	edges.add(new Edge(b4, a, 1, 2));

	edges.add(new Edge(d, b3));
	edges.add(new Edge(f, b4));

	edges.add(new Edge(c, a));
//		edges.add(new Edge(b, g));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void balanceThis3() {
	Node top1, top2;
	Node a1, a2, a3, a4;
	Node b1, b2, b3, b4, b5, b6;
	Node c1, c2, c3, c4;
	Node bottom1, bottom2;
	
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(top1 = new Node("Top 1"));
	top1.width = 400;

	nodes.add(top2 = new Node("Top 2"));
	top2.width = 400;
	
	nodes.add(a1 = new Node("a1"));
	nodes.add(a2 = new Node("a2"));
	nodes.add(a3 = new Node("a3"));
	nodes.add(a4 = new Node("a4"));
	nodes.add(b1 = new Node("b1"));
	nodes.add(b2 = new Node("b2"));
	nodes.add(b3 = new Node("b3"));
	nodes.add(b4 = new Node("b4"));
	nodes.add(b5 = new Node("b5"));
	nodes.add(b6 = new Node("b6"));
	nodes.add(c1 = new Node("c1"));
	nodes.add(c2 = new Node("c2"));
	nodes.add(c3 = new Node("c3"));
	nodes.add(c4 = new Node("c4"));
	
	nodes.add(bottom1 = new Node("Bottom 1"));
	bottom1.width = 400;

	nodes.add(bottom2 = new Node("Bottom 2"));
	bottom2.width = 400;
	
	edges.add(new Edge(top1, a1));
	edges.add(new Edge(top2, a4));
	edges.add(new Edge(a1, b1));
	edges.add(new Edge(a2, b1));
	edges.add(new Edge(a2, b3));
	edges.add(new Edge(a3, b3));
	edges.add(new Edge(a3, b5));
	edges.add(new Edge(a4, b5));
	edges.add(new Edge(a4, b6));
	edges.add(new Edge(b1, c1));
	edges.add(new Edge(b2, c1));
	edges.add(new Edge(b2, c2));
	edges.add(new Edge(b4, c2));
	edges.add(new Edge(b4, c3));
	edges.add(new Edge(b6, c3));
	edges.add(new Edge(b6, c4));
	edges.add(new Edge(c1, bottom1));
	edges.add(new Edge(c4, bottom2));

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
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
	
	new DirectedGraphLayout()
		.visit(graph);
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

		// if node has no incoming connectors then assume it is not connected to the main graph
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

protected void doTest1() {
	Node a,b,c,d,e,f,g;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
	nodes.add(f = new Node("node f"));
	nodes.add(g = new Node("node g"));

	edges.add(new Edge(a, d));
	edges.add(new Edge(b, d));
	edges.add(new Edge(c, d));
	edges.add(new Edge(d, e));
	edges.add(new Edge(d, f));
	edges.add(new Edge(d, g));
	edges.add(new Edge(c, g));	
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}


protected void doTest2() {
	Node a,b,c,d,e,f;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
	nodes.add(f = new Node("node f"));

	edges.add(new Edge(a, b));
	edges.add(new Edge(a, c));
	edges.add(new Edge(d, b));

	edges.add(new Edge(a, f));
	edges.add(new Edge(e, b));

	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void doTest3() {
	Node a,b,c,d;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));

	a.width = 80;
	d.width = 75;

	edges.add(new Edge(a, b));
	edges.add(new Edge(a, c));
	edges.add(new Edge(c, d));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void doTest4() {
	Node a;
	Node b,c,d;
	Node e,f,g;
	Node h,i,j;
	Node k;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
	nodes.add(f = new Node("node f"));
	nodes.add(g = new Node("node g"));
	nodes.add(h = new Node("node h"));
	nodes.add(i = new Node("node i"));
	nodes.add(j = new Node("node j"));
	nodes.add(k = new Node("node k"));

	edges.add(new Edge(a, b));
	edges.add(new Edge(a, c));
	edges.add(new Edge(a, d));
	edges.add(new Edge(b, e));
	edges.add(new Edge(c, e));

	edges.add(new Edge(d, e));
	edges.add(new Edge(d, f));
	edges.add(new Edge(d, g));
	edges.add(new Edge(e, h));
	edges.add(new Edge(f, h));
	

	edges.add(new Edge(g, h));	
	edges.add(new Edge(g, i));	
	edges.add(new Edge(g, j));	
	edges.add(new Edge(h, k));	
	edges.add(new Edge(i, k));	
	edges.add(new Edge(j, k));	
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout().visit(graph);
}

protected void fourLevelBinaryTree() {
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();
	
	Node row[], firstRow[];
	firstRow = new Node[2];
	firstRow[1] = new Node("root");
	addNodes(nodes, firstRow);
	
	row = joinRows(nodes, edges, firstRow, new int[] {1,1, 1, 2});
	
	row = joinRows(nodes, edges, row, new int[] {1,1,1,2,2,3,2,4});
	
	row = joinRows(nodes, edges, row, new int[] {1,1,1,2,2,3,2,4,3,5,3,6,4,7,4,8});
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
		
	new DirectedGraphLayout()
		.visit(graph);
}

protected void graph1() {
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();
	
	Node row[], firstRow[];
	firstRow = new Node[3];
	firstRow[1] = new Node("h1");
	firstRow[2] = new Node("h2");
	addNodes(nodes, firstRow);
	row = joinRows(nodes, edges, firstRow, new int[] {
		1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 2, 7, 2, 8, 2, 9, 2, 10
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,2,2,2,3,3,1,4,4,5,5,6,6,7,7,8,8,9,9,9,10,10,11
	});

//Row 3->4
	row = joinRows(nodes, edges, row, new int[] {
		1,1,1,2,2,3,2,6,3,6,4,4,5,5,6,7,7,6,8,8,9,6,10,9,10,10,11,11
	});


//		Row 4->5
	row = joinRows(nodes, edges, row, new int[] {
		1,1,1,2,1,3,2,3,3,4,4,5,5,6,7,3,8,7,9,8,10,9,10,10,11,11
	});
//		Row 5->6
	row = joinRows(nodes, edges, row, new int[] {
		1,1,2,2,4,3,5,4,6,5,7,6,8,7,9,7,10,7,11,7
	});
//		Row 6->7
	row = joinRows(nodes, edges, row, new int[] {
		1,1,2,2,3,4,4,4,5,3,6,4,7,4,7,5
	});
//		Row 7->8
	row = joinRows(nodes, edges, row, new int[] {
		1,2,2,1,3,2,4,2,4,3,5,4
	});

//		Row 8->9
	row = joinRows(nodes, edges, row, new int[] {
		1,1,2,1,3,1,4,1
	});

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void graph2() {
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();
	
	Node row[], firstRow[];
	firstRow = new Node[5];
	firstRow[1] = new Node("h1");
	firstRow[2] = new Node("h2");
	firstRow[3] = new Node("h3");
	firstRow[4] = new Node("h4");
	firstRow[1].width = firstRow[2].width = firstRow[3].width = firstRow[4].width = 160;
	addNodes(nodes, firstRow);
	row = joinRows(nodes, edges, firstRow, new int[] {
		1,1,
		1,2,
		2,2,
		2,3,
		3,3,
		3,4,
		4,4,
		4,5
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,
		3,3,
		5,2
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,
		2,2
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,
		1,2,
		2,2,
		2,1
	});

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void graph3() {
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();
	
	Node row[], firstRow[];
	firstRow = new Node[4];
	firstRow[1] = new Node("h1");
	firstRow[2] = new Node("h2");
	firstRow[3] = new Node("h3");
	firstRow[1].width = firstRow[2].width = firstRow[3].width = 120;
	addNodes(nodes, firstRow);
	row = joinRows(nodes, edges, firstRow, new int[] {
		1,1,1,2,
		1,3,2,3,
		2,4,3,3,
		3,4
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,3,4,
		2,1,2,3,
		3,2,4,4
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,1,2,
		2,1,2,4,
		3,3,3,4,
		4,3,4,5
	});

	row = joinRows(nodes, edges, row, new int[] {
		1,1,2,2,
		4,4,5,5,
		1,2,4,3,
		5,4
	});

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

private Node[] joinRows(NodeList nodes, EdgeList edges, Node[] firstRow, int[] conns) {
	shuffleConnections(conns);
	Node secondRow[] = new Node[20];
	Node head, tail;
	for (int i = 0; i < conns.length; i += 2) {
		head = firstRow[conns[i]];
		tail = secondRow[conns[i+1]];
		if (tail == null) {
			tail = secondRow[conns[i+1]] = new Node("node" + conns[i+1]);
			tail.width = 78;
		}
		edges.add(new Edge(head, tail));
	}
	addNodes(nodes, secondRow);
	return secondRow;
}


protected void offsetTest() {
	Node head, a1, a2, a3, a4, b1, b2, b3, b4;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();


	nodes.add(head = new Node("Head"));
	head.width = 100;
	nodes.add(a1 = new Node("node a1"));
	nodes.add(a2 = new Node("node a2"));
	nodes.add(a3 = new Node("node a3"));
	nodes.add(a4 = new Node("node a4"));
	
	nodes.add(b1 = new Node("node b1"));
	nodes.add(b2 = new Node("node b2"));
	nodes.add(b3 = new Node("node b3"));
	nodes.add(b4 = new Node("node b4"));

	Edge e = new Edge(head, a1);
	e.offsetSource = 10;
	edges.add(e);
	
	e = new Edge(head, b1);
	e.offsetSource = 90;
	edges.add(e);
	
	a1.incomingOffset = 40;
	b1.incomingOffset = 10;
	a1.outgoingOffset = 10;
	a2.incomingOffset = 40;
	
	a3.outgoingOffset = 10;
	a4.incomingOffset = 40;
	
	edges.add(new Edge(b1, b2));
	edges.add(new Edge(b2, b3));
	edges.add(new Edge(b3, b4));

	edges.add(new Edge(a1, a2));
	edges.add(e = new Edge(a2, a3));
	e.offsetSource = 10;
	e.offsetTarget = 40;
	edges.add(new Edge(a3, a4));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void simpleGraph() {
	Node a,b,c,d,e,f,g,h,i,x;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(e = new Node("node e"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(x = new Node("node X"));
	nodes.add(f = new Node("node f"));
	nodes.add(g = new Node("node g"));
	nodes.add(h = new Node("node h"));
	nodes.add(i = new Node("node i"));

	edges.add(new Edge(a, i));
	edges.add(new Edge(i, g));
	edges.add(new Edge(i, h));
	edges.add(new Edge(c, i));
	
	
	edges.add(new Edge(a, b));
	edges.add(new Edge(b, c));
	edges.add(new Edge(c, d));
	edges.add(new Edge(d, x));
	edges.add(new Edge(x, e));
	edges.add(new Edge(a, f));
	edges.add(new Edge(f, g));
	edges.add(new Edge(f, h));
	edges.add(new Edge(g, e));
	edges.add(new Edge(h, e));
	

	edges.add(new Edge(b, g));
	edges.add(new Edge(f, d));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new InitialRankSolver()
		.visit(graph);

	new DirectedGraphLayout()
		.visit(graph);
}

public void testGraphPerformance() {
	tagAsGlobalSummary("Graphs", Dimension.CPU_TIME);

	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		if (i >= warmupRuns)
			startMeasuring();
		
		anotherTour();
		balanceThis1();
		balanceThis2();
		balanceThis3();
		bug90228();
		doTest1();
		doTest2();
		doTest3();
		doTest4();
		fourLevelBinaryTree();
		graph1();
		graph2();
		graph3();
		offsetTest();
		simpleGraph();
		tinyGraph();
		unstableGraph();
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	commitMeasurements();
	assertPerformance();
}

protected void tinyGraph() {
	Node a,b,c,d,e,g;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
	nodes.add(g = new Node("node g"));

	edges.add(new Edge(a, d));
	edges.add(new Edge(c, b));
	edges.add(new Edge(b, g, 1, 3));
	edges.add(new Edge(c, d));
	edges.add(new Edge(d, e));
	edges.add(new Edge(d, g));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}

protected void unstableGraph() {
	Node a0,b0,c0,a1,b1,c1,a2, b2, c2;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a0 = new Node("node a0"));
	nodes.add(b0 = new Node("node b0"));
	nodes.add(c0 = new Node("node c0"));
	nodes.add(a1 = new Node("node a1"));
	nodes.add(b1 = new Node("node b1"));
	nodes.add(c1 = new Node("node c1"));
	nodes.add(a2 = new Node("node a2"));
	nodes.add(b2 = new Node("node b2"));
	nodes.add(c2 = new Node("node c2"));

	edges.add(new Edge(a0, a1, 1, 9));
	edges.add(new Edge(a1, a2, 1, 9));

	edges.add(new Edge(b0, b1, 1, 9));
	edges.add(new Edge(b1, b2, 1, 9));

	edges.add(new Edge(c0, c1, 1, 9));
	edges.add(new Edge(c1, c2, 1, 9));

	edges.add(new Edge(a0, b2));
	edges.add(new Edge(b0, c2));
	edges.add(new Edge(c0, a2));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
}


}