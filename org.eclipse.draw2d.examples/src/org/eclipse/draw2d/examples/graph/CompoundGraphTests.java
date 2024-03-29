/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.graph;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.CompoundDirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;

/**
 * A collection of compound graph tests.
 *
 * @author hudsonr
 * @since 2.1
 */
public class CompoundGraphTests {

	private static final String EDITOR = "Editor"; //$NON-NLS-1$
	private static final String SUBGRAPH_2 = "Subgraph 2"; //$NON-NLS-1$
	private static final String SUBGRAPH_1 = "Subgraph 1"; //$NON-NLS-1$

	public static CompoundDirectedGraph aaaapull(int direction) {
		Subgraph s1, s2;
		Node a, b, e, j, m;
		Node r, t;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		s1 = new Subgraph(SUBGRAPH_1);
		s2 = new Subgraph(SUBGRAPH_2);

		nodes.add(r = new Node("r", s2)); //$NON-NLS-1$
		nodes.add(t = new Node("t", s2)); //$NON-NLS-1$

		nodes.add(a = new Node("a", s1)); //$NON-NLS-1$
		nodes.add(b = new Node("b", s1)); //$NON-NLS-1$
		nodes.add(e = new Node("e", s1)); //$NON-NLS-1$
		nodes.add(j = new Node("j", s1)); //$NON-NLS-1$
		nodes.add(m = new Node("m", s1)); //$NON-NLS-1$

		edges.add(new Edge(a, b));

		edges.add(new Edge(b, e));

		edges.add(new Edge(e, j));

		edges.add(new Edge(m, t));
		edges.add(new Edge(j, r));
		edges.add(new Edge(a, r));
		edges.add(new Edge(s1, s2));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;
		graph.nodes.add(s1);
		graph.nodes.add(s2);

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph aaaflowEditor(int direction) {
		Subgraph diagram, flow, subflow1, subflow2;
		Node a1, a2, a3;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		nodes.add(diagram = new Subgraph(EDITOR));
		nodes.add(flow = new Subgraph("Flow", diagram)); //$NON-NLS-1$
		nodes.add(subflow1 = new Subgraph("Sub1", flow)); //$NON-NLS-1$
		nodes.add(subflow2 = new Subgraph("Sub2", flow)); //$NON-NLS-1$

		nodes.add(a1 = new Node("a1", diagram)); //$NON-NLS-1$
		nodes.add(a2 = new Node("a2", subflow1)); //$NON-NLS-1$
		nodes.add(a3 = new Node("a3", subflow2)); //$NON-NLS-1$

		a1.width = a2.width = a3.width = 200;
		a1.outgoingOffset = 1;

		edges.add(new Edge(a1, flow));
		edges.add(new Edge(a1, a2));
		edges.add(new Edge(a2, a3));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph chains(int direction) {
		Subgraph s1, s2, s3, sb;
		Node nx, n0, n1, n2, n3, n4, n5, n6, na, nb;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		nodes.add(s1 = new Subgraph("S1")); //$NON-NLS-1$
		nodes.add(s2 = new Subgraph("S2")); //$NON-NLS-1$
		nodes.add(s3 = new Subgraph("S3")); //$NON-NLS-1$
		nodes.add(sb = new Subgraph("SB")); //$NON-NLS-1$

		s1.setPadding(new Insets(10));
		s1.innerPadding = new Insets(1);
		s1.insets = new Insets(9);
		edges.add(new Edge(s1, s2));
		edges.add(new Edge(s1, sb));
		edges.add(new Edge(sb, s3));
		edges.add(new Edge(s2, s3));

		nodes.add(n0 = new Node("0", s1)); //$NON-NLS-1$
		nodes.add(nx = new Node("x", s1)); //$NON-NLS-1$
		nodes.add(n1 = new Node("1", s1)); //$NON-NLS-1$
		nodes.add(n2 = new Node("2", s1)); //$NON-NLS-1$
		edges.add(new Edge(nx, n2));
		edges.add(new Edge(n0, n2));
		edges.add(new Edge(n1, n2));

		nodes.add(n3 = new Node("3", s2)); //$NON-NLS-1$
		nodes.add(n4 = new Node("4", s2)); //$NON-NLS-1$
		edges.add(new Edge(n3, n4));

		nodes.add(n5 = new Node("5", s3)); //$NON-NLS-1$
		nodes.add(n6 = new Node("6", s3)); //$NON-NLS-1$
		edges.add(new Edge(n5, n6));

		nodes.add(na = new Node("a", sb)); //$NON-NLS-1$
		nodes.add(nb = new Node("b", sb)); //$NON-NLS-1$
		edges.add(new Edge(na, nb));

		n1.width = 60;
		n2.width = na.width = 70;
		n3.width = 100;
		n5.width = n6.width = 64;
		n4.width = 150;

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;
		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph flowChart(int direction) {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		Subgraph diagram, s1, s2, s3, s4, s5, s6, s7, s8;

		nodes.add(diagram = new Subgraph("diagram")); //$NON-NLS-1$

		nodes.add(s1 = new Subgraph("s1", diagram)); //$NON-NLS-1$
		nodes.add(s2 = new Subgraph("s2", s1)); //$NON-NLS-1$
		nodes.add(s3 = new Subgraph("s3", s1)); //$NON-NLS-1$
		nodes.add(s4 = new Subgraph("s4", s1)); //$NON-NLS-1$
		nodes.add(s5 = new Subgraph("s5", s1)); //$NON-NLS-1$
		nodes.add(s6 = new Subgraph("s6", s1)); //$NON-NLS-1$
		nodes.add(s7 = new Subgraph("s7", s1)); //$NON-NLS-1$
		nodes.add(s8 = new Subgraph("s8", s1)); //$NON-NLS-1$

		Node outer1, outer2, outer3;
		nodes.add(outer1 = new Node("asdf", diagram)); //$NON-NLS-1$
		nodes.add(outer2 = new Node("asfasdf", diagram)); //$NON-NLS-1$
		nodes.add(outer3 = new Node("a3", diagram)); //$NON-NLS-1$

		edges.add(new Edge(s3, s6));
		edges.add(new Edge(s4, s7));
		edges.add(new Edge(s6, s8));

		edges.add(new Edge(outer1, outer3));
		edges.add(new Edge(outer3, s1));
		edges.add(new Edge(outer2, s1));

		Node s2a, s2b, s2c;
		nodes.add(s2a = new Node("BMW", s2)); //$NON-NLS-1$
		nodes.add(s2b = new Node("Hawking", s2)); //$NON-NLS-1$
		nodes.add(s2c = new Node("Smurfy", s2)); //$NON-NLS-1$
		edges.add(new Edge(s2a, s2b));
		edges.add(new Edge(s2a, s2c));

		Node s3a, s3b;
		nodes.add(s3a = new Node("Jammin", s3)); //$NON-NLS-1$
		nodes.add(s3b = new Node("This is it", s3)); //$NON-NLS-1$
		edges.add(new Edge(s3a, s3b));

		nodes.add(new Node("catDog", s4)); //$NON-NLS-1$
		Node s5a, s5b;
		nodes.add(s5a = new Node("a1", s5)); //$NON-NLS-1$
		nodes.add(s5b = new Node("a2", s5)); //$NON-NLS-1$
		edges.add(new Edge(s5a, s5b));

		Node s6a, s6b, s6c;
		nodes.add(s6a = new Node("Hoop it up", s6)); //$NON-NLS-1$
		nodes.add(s6b = new Node("Streeball", s6)); //$NON-NLS-1$
		nodes.add(s6c = new Node("Downtown", s6)); //$NON-NLS-1$
		edges.add(new Edge(s6b, s6c));
		edges.add(new Edge(s6a, s6b));

		Node s7a, s7b;
		nodes.add(s7a = new Node("Thing 1", s7)); //$NON-NLS-1$
		nodes.add(s7b = new Node("Thing 2", s7)); //$NON-NLS-1$
		edges.add(new Edge(s7a, s7b));

		Node s8a, s8b, s8c, s8d, s8e;
		nodes.add(s8a = new Node("a1", s8)); //$NON-NLS-1$
		nodes.add(s8b = new Node("a2", s8)); //$NON-NLS-1$
		nodes.add(s8c = new Node("a3", s8)); //$NON-NLS-1$
		nodes.add(s8d = new Node("a4", s8)); //$NON-NLS-1$
		nodes.add(s8e = new Node("a5", s8)); //$NON-NLS-1$
		edges.add(new Edge(s8a, s8c));
		edges.add(new Edge(s8a, s8d));
		edges.add(new Edge(s8b, s8c));
		edges.add(new Edge(s8b, s8e));
		edges.add(new Edge(s8c, s8e));

		Node inner1, inner2, inner3, inner4, inner5, inner6, inner7, inner8, inner9, inner10, inner11, inner12, inner13,
				inner14, inner15, inner16;

		nodes.add(inner1 = new Node("buckyball", s1)); //$NON-NLS-1$
		nodes.add(inner2 = new Node("d", s1)); //$NON-NLS-1$
		nodes.add(inner3 = new Node("cheese", s1)); //$NON-NLS-1$
		nodes.add(inner4 = new Node("dingleberry", s1)); //$NON-NLS-1$
		nodes.add(inner5 = new Node("dinosaur", s1)); //$NON-NLS-1$
		nodes.add(inner6 = new Node("foobar", s1)); //$NON-NLS-1$
		nodes.add(inner7 = new Node("t30", s1)); //$NON-NLS-1$
		nodes.add(inner8 = new Node("a21", s1)); //$NON-NLS-1$
		nodes.add(inner9 = new Node("katarina", s1)); //$NON-NLS-1$
		nodes.add(inner10 = new Node("zig zag", s1)); //$NON-NLS-1$
		nodes.add(inner11 = new Node("a16", s1)); //$NON-NLS-1$
		nodes.add(inner12 = new Node("a23", s1)); //$NON-NLS-1$
		nodes.add(inner13 = new Node("a17", s1)); //$NON-NLS-1$
		nodes.add(inner14 = new Node("a20", s1)); //$NON-NLS-1$
		nodes.add(inner15 = new Node("a19", s1)); //$NON-NLS-1$
		nodes.add(inner16 = new Node("a24", s1)); //$NON-NLS-1$
		edges.add(new Edge(inner1, inner3));
		edges.add(new Edge(inner2, inner4));
		edges.add(new Edge(inner2, inner3));
		edges.add(new Edge(inner3, inner5));
		edges.add(new Edge(inner4, inner5));
		edges.add(new Edge(inner4, inner6));
		edges.add(new Edge(inner6, s6));
		edges.add(new Edge(inner5, inner7));
		edges.add(new Edge(inner7, inner8));
		edges.add(new Edge(inner8, s5));
		edges.add(new Edge(s3, inner9));
		edges.add(new Edge(s4, inner9));
		edges.add(new Edge(inner9, inner10));
		edges.add(new Edge(s7, inner11));
		edges.add(new Edge(s7, inner12));
		edges.add(new Edge(inner11, inner13));
		edges.add(new Edge(inner11, inner14));
		edges.add(new Edge(inner11, inner15));
		edges.add(new Edge(inner12, inner15));
		edges.add(new Edge(inner12, inner16));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph flowEditor1(int direction) {
		Subgraph diagram, flow;
		Node a1, a2, a4, a5, a6, x, y;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		nodes.add(diagram = new Subgraph(EDITOR)); // $NON-NLS-1$
		nodes.add(flow = new Subgraph("Flow", diagram)); //$NON-NLS-1$

		nodes.add(a1 = new Node("a1", diagram)); //$NON-NLS-1$
		nodes.add(a2 = new Node("a2", diagram)); //$NON-NLS-1$
		nodes.add(a4 = new Node("a4", diagram)); //$NON-NLS-1$
		nodes.add(a5 = new Node("a5", diagram)); //$NON-NLS-1$
		nodes.add(a6 = new Node("a6", diagram)); //$NON-NLS-1$
		nodes.add(new Node("a7", diagram)); //$NON-NLS-1$
		nodes.add(new Node("a8", diagram)); //$NON-NLS-1$

		edges.add(new Edge(a1, a2));
		edges.add(new Edge(a2, a4));
		edges.add(new Edge(a2, a5));
		edges.add(new Edge(a2, a6));

		edges.add(new Edge(a6, flow));
		nodes.add(x = new Node("x", flow)); //$NON-NLS-1$
		nodes.add(y = new Node("y", flow)); //$NON-NLS-1$
		edges.add(new Edge(x, y));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph flowEditor2(int direction) {
		Subgraph diagram, flow;
		Node a1, a2, a3, a4;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		nodes.add(diagram = new Subgraph(EDITOR)); // $NON-NLS-1$
		nodes.add(flow = new Subgraph("Flow", diagram)); //$NON-NLS-1$

		nodes.add(a1 = new Node("a1", diagram)); //$NON-NLS-1$
		nodes.add(a2 = new Node("a2", diagram)); //$NON-NLS-1$
		nodes.add(a3 = new Node("a3", flow)); //$NON-NLS-1$
		nodes.add(a4 = new Node("a4", flow)); //$NON-NLS-1$

		edges.add(new Edge(a1, a2));
		edges.add(new Edge(a2, a4));
		edges.add(new Edge(a3, a2));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph ShortestPassCase(int direction) {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		Subgraph p = new Subgraph("parent"); //$NON-NLS-1$
		nodes.add(p);
		Node a = new Node("a", p); //$NON-NLS-1$
		nodes.add(a);
		Node b = new Node("b", p); //$NON-NLS-1$
		nodes.add(b);
		Node c = new Node("c", p); //$NON-NLS-1$
		nodes.add(c);
		Node d = new Node("d", p); //$NON-NLS-1$
		nodes.add(d);
		Node e = new Node("e", p); //$NON-NLS-1$
		nodes.add(e);

		edges.add(new Edge(a, d));
		edges.add(new Edge(a, c));
		edges.add(new Edge(b, c));
		edges.add(new Edge(b, d));
		edges.add(new Edge(b, e));
		edges.add(new Edge(c, d));
		edges.add(new Edge(c, e));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;

	}

	public static CompoundDirectedGraph tangledSubgraphs(int direction) {
		Subgraph A, B, C, D;
		Node a1, a2, b1, b2, c1, c2, d1, d2;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		nodes.add(A = new Subgraph("Subgraph A")); //$NON-NLS-1$
		nodes.add(B = new Subgraph("Subgraph B")); //$NON-NLS-1$
		nodes.add(C = new Subgraph("Subgraph C")); //$NON-NLS-1$
		nodes.add(D = new Subgraph("Subgraph D")); //$NON-NLS-1$

		// C.rowOrder = 2;
		// B.rowOrder = 3;

		nodes.add(a1 = new Node("a1", A)); //$NON-NLS-1$
		nodes.add(a2 = new Node("a2", A)); //$NON-NLS-1$
		edges.add(new Edge(a1, a2));

		nodes.add(b1 = new Node("b1", B)); //$NON-NLS-1$
		nodes.add(b2 = new Node("b2", B)); //$NON-NLS-1$
		edges.add(new Edge(b1, b2));

		nodes.add(c1 = new Node("c1", C)); //$NON-NLS-1$
		nodes.add(c2 = new Node("c2", C)); //$NON-NLS-1$
		edges.add(new Edge(c1, c2));

		nodes.add(d1 = new Node("d1", D)); //$NON-NLS-1$
		nodes.add(d2 = new Node("d2", D)); //$NON-NLS-1$
		edges.add(new Edge(d1, d2));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;

		edges.add(new Edge(a1, d2));
		edges.add(new Edge(d1, c2));
		edges.add(new Edge(d1, b2));

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph test1(int direction) {
		Subgraph s1, s2;
		Node n1, n2, n3, n4, n5, n6, n7;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		s1 = new Subgraph(SUBGRAPH_1);
		s2 = new Subgraph(SUBGRAPH_2);

		nodes.add(n1 = new Node("1", s1)); //$NON-NLS-1$
		nodes.add(n2 = new Node("2", s1)); //$NON-NLS-1$
		nodes.add(n3 = new Node("3", s1)); //$NON-NLS-1$
		nodes.add(n4 = new Node("4", s2)); //$NON-NLS-1$
		nodes.add(n5 = new Node("5", s2)); //$NON-NLS-1$
		nodes.add(n6 = new Node("6", s2)); //$NON-NLS-1$
		nodes.add(n7 = new Node("7", s2)); //$NON-NLS-1$

		n1.width = 60;
		n2.width = 70;
		n3.width = 100;
		n5.width = n6.width = 64;
		n7.width = n4.width = 90;

		edges.add(new Edge(n1, n3));
		edges.add(new Edge(n2, n3));
		edges.add(new Edge(n4, n5));
		edges.add(new Edge(n4, n6));
		edges.add(new Edge(n6, n7));
		edges.add(new Edge(n5, n7));

		edges.add(new Edge(n2, n5));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;
		graph.nodes.add(s1);
		graph.nodes.add(s2);

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static CompoundDirectedGraph test2(int direction) {
		Subgraph s1, s2, s1_1;
		Node n1, n2, n3, n4, n5, n6, n7, n8;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		s1 = new Subgraph(SUBGRAPH_1);
		s2 = new Subgraph(SUBGRAPH_2);
		s1_1 = new Subgraph("Subgraph 1.1", s1); //$NON-NLS-1$

		nodes.add(s1);
		nodes.add(s2);
		nodes.add(s1_1);

		nodes.add(n1 = new Node("1", s1)); //$NON-NLS-1$
		nodes.add(n2 = new Node("2", s1)); //$NON-NLS-1$
		nodes.add(n3 = new Node("3", s1)); //$NON-NLS-1$
		nodes.add(n4 = new Node("4", s2)); //$NON-NLS-1$
		nodes.add(n5 = new Node("5", s2)); //$NON-NLS-1$
		nodes.add(n6 = new Node("6", s2)); //$NON-NLS-1$
		nodes.add(n7 = new Node("7", s2)); //$NON-NLS-1$
		nodes.add(n8 = new Node("8", s1_1)); //$NON-NLS-1$

		n8.width = 80;
		n1.width = 60;
		n2.width = 70;
		n3.width = 100;
		n5.width = n6.width = 64;
		n7.width = n4.width = 90;

		edges.add(new Edge(n1, n2));
		edges.add(new Edge(n2, n3));
		// edges.add(new Edge(n1, n3));
		edges.add(new Edge(n1, n8));
		edges.add(new Edge(n1, n5));
		edges.add(new Edge(n8, n3));
		edges.add(new Edge(n4, n5));
		edges.add(new Edge(n4, n6));
		edges.add(new Edge(n6, n7));
		edges.add(new Edge(n5, n7));

		edges.add(new Edge(n2, n5));

		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.setDirection(direction);
		graph.nodes = nodes;
		graph.edges = edges;

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

}
