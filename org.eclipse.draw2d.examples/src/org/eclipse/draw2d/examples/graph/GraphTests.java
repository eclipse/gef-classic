package org.eclipse.draw2d.examples.graph;

import java.util.Random;

import org.eclipse.draw2d.graph.*;
import org.eclipse.draw2d.internal.graph.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class GraphTests {
public static DirectedGraph offsetTest() {
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
	return graph;
}

static Random rand = new Random(90);

/**
 * @param nodes
 * @param row
 */
private static void addNodes(NodeList nodes, Node[] row) {
	for (int i=0; i<row.length; i++)
		if (row[i] != null)
			nodes.add(row[i]);
}

public static DirectedGraph anotherTour() {
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

	for (int i = 0; i < nodes.size(); i++) {
		Node node = nodes.getNode(i);
		node.index = i;
	}
	for (int i = 0; i < edges.size(); i++) {
		Edge edge = edges.getEdge(i);
		System.out.println("  " + edge.source.index + " -> "+ edge.target.index + ";");
	}

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
	
	return graph;
}

public static DirectedGraph balanceThis1() {
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
//	edges.add(new Edge(b, g));
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
	return graph;
}

public static DirectedGraph balanceThis2() {
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
	return graph;
}
	
public static DirectedGraph balanceThis3() {
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
	return graph;


}

public static DirectedGraph fourLevelBinaryTree() {
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
	
	for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.getNode(i);
			node.index = i;
		}
	for (int i = 0; i < edges.size(); i++) {
		Edge edge = edges.getEdge(i);
//		if (edge.source.index == 0)
//			edge.weight = 1;
//		else if (edge.source.index == 1 || edge.source.index == 2)
//			edge.weight = 2;
//		else
//			edge.weight = 4;
		
		System.out.println("  " + edge.source.index + " -> "+ edge.target.index + ";");
	}
	
	new DirectedGraphLayout()
		.visit(graph);
	
	return graph;
}

public static DirectedGraph graph1() {
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
	
	return graph;
}

public static DirectedGraph graph2() {
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

	for (int i = 0; i < nodes.size(); i++) {
		Node node = nodes.getNode(i);
		node.index = i;
	}
//	for (int i = 0; i < edges.size(); i++) {
//		Edge edge = edges.getEdge(i);
//		System.out.println("  " + edge.source.index + " -> "+ edge.target.index + ";");
//	}

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
	
	return graph;
}

public static DirectedGraph graph3() {
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

	for (int i = 0; i < nodes.size(); i++) {
		Node node = nodes.getNode(i);
		node.index = i;
	}
//	for (int i = 0; i < edges.size(); i++) {
//		Edge edge = edges.getEdge(i);
//		System.out.println("  " + edge.source.index + " -> "+ edge.target.index + ";");
//	}

	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
	
	return graph;
}


/**
 * @param nodes
 * @param edges
 * @param firstRow
 * @param is
 * @return
 */
private static Node[] joinRows(NodeList nodes, EdgeList edges, Node[] firstRow, int[] conns) {
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

public static DirectedGraph simpleGraph() {
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
//	nodes.add(j = new Node("node j"));

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
//	f.incoming.getEdge(0).weight = 4;
	edges.add(new Edge(f, g));
	edges.add(new Edge(f, h));
	edges.add(new Edge(g, e));
	edges.add(new Edge(h, e));
	

	edges.add(new Edge(b, g));
	edges.add(new Edge(f, d));
//	edges.add(new Edge(c, h)); //This will force h onto the rank with d
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new InitialRankSolver()
		.visit(graph);
//	f.rank++;
//	g.rank++;
//	h.rank++;

	new DirectedGraphLayout()
		.visit(graph);
	return graph;
}

public static DirectedGraph test1() {
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
	return graph;
}

public static DirectedGraph test2() {
	Node a,b,c,d,e,f,g,h,i,j,k,l;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
	nodes.add(f = new Node("node f"));
//	nodes.add(g = new Node("node g"));
//	nodes.add(h = new Node("node h"));
//	nodes.add(i = new Node("node i"));
//	nodes.add(j = new Node("node j"));
//	nodes.add(k = new Node("node k"));
//	nodes.add(l = new Node("node l"));

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
	return graph;
}

public static DirectedGraph test3() {
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
	return graph;
}

public static DirectedGraph test4() {
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
	return graph;
}

public static DirectedGraph tinyGraph() {
	Node a,b,c,d,e,f,g;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(a = new Node("node a"));
	nodes.add(b = new Node("node b"));
	nodes.add(c = new Node("node c"));
	nodes.add(d = new Node("node d"));
	nodes.add(e = new Node("node e"));
//	nodes.add(f = new Node("node f"));
	nodes.add(g = new Node("node g"));

	edges.add(new Edge(a, d));
	edges.add(new Edge(c, b));
	edges.add(new Edge(b, g, 1, 3));
	edges.add(new Edge(c, d));
	edges.add(new Edge(d, e));
//	edges.add(new Edge(d, f));
	edges.add(new Edge(d, g));
//	edges.add(new Edge(c, g));	
	
	DirectedGraph graph = new DirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	new DirectedGraphLayout()
		.visit(graph);
	return graph;
}


public static DirectedGraph unstableGraph() {
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

//	edges.add(new Edge(ab, a0));
//	edges.add(new Edge(ab, b0));
//
//	edges.add(new Edge(ac, a0));
//	edges.add(new Edge(ac, c0));
//
//	edges.add(new Edge(bc, c0));
//	edges.add(new Edge(bc, b0));

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
	return graph;

}

}
