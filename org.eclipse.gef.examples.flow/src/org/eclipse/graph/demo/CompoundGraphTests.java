package org.eclipse.graph.demo;

import org.eclipse.graph.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class CompoundGraphTests {

static CompoundDirectedGraph chains() {
	Subgraph s1, s2, s3, sb;
	Node nx,n0,n1, n2, n3, n4, n5, n6, na, nb;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	nodes.add(s1 = new Subgraph("S1"));
	nodes.add(s2 = new Subgraph("S2"));
	nodes.add(s3 = new Subgraph("S3"));
	nodes.add(sb = new Subgraph("SB"));

	edges.add(new Edge(s1, s2));
	edges.add(new Edge(s1, sb));
	edges.add(new Edge(sb, s3));
	edges.add(new Edge(s2, s3));

	nodes.add(n0 = new Node("0", s1));
	nodes.add(nx = new Node("x", s1));
	nodes.add(n1 = new Node("1", s1));
	nodes.add(n2 = new Node("2", s1));
	edges.add(new Edge(nx, n2));
	edges.add(new Edge(n0, n2));
	edges.add(new Edge(n1, n2));

	nodes.add(n3 = new Node("3", s2));
	nodes.add(n4 = new Node("4", s2));
	edges.add(new Edge(n3, n4));

	nodes.add(n5 = new Node("5", s3));
	nodes.add(n6 = new Node("6", s3));
	edges.add(new Edge(n5, n6));

	nodes.add(na = new Node("a", sb));
	nodes.add(nb = new Node("b", sb));
	edges.add(new Edge(na, nb));

	n1.width = 60;
	n2.width = na.width = 70;
	n3.width = 100;
	n5.width = n6.width = 64;
	n4.width = 150;

	CompoundDirectedGraph graph = new CompoundDirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	new CompoundDirectedGraphLayout().visit(graph);
	return graph;
}

static CompoundDirectedGraph pull() {
	Subgraph s1, s2;
	Node a,b,c,d,e,f,g,h,i,j,k,x,l,m,n,y;
	Node r,t;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	s1 = new Subgraph("Subgraph 1");
	s2 = new Subgraph("Subgraph 2");

	nodes.add(r = new Node("r", s2));
	nodes.add(t = new Node("t", s2));

	nodes.add(a = new Node("a", s1));
	nodes.add(b = new Node("b", s1));
	nodes.add(c = new Node("c", s1));
	nodes.add(d = new Node("d", s1));
	nodes.add(e = new Node("e", s1));
	nodes.add(f = new Node("f", s1));
	nodes.add(g = new Node("g", s1));
	nodes.add(h = new Node("h", s1));
	nodes.add(i = new Node("i", s1));
	nodes.add(j = new Node("j", s1));
	nodes.add(k = new Node("k", s1));
	nodes.add(l = new Node("l", s1));
	nodes.add(m = new Node("m", s1));
	nodes.add(x = new Node("x", s1));


	edges.add(new Edge(a, b));
	edges.add(new Edge(a, c));
	edges.add(new Edge(a, d));

	edges.add(new Edge(b, e));
	edges.add(new Edge(b, f));
	edges.add(new Edge(b, g));
	edges.add(new Edge(a, x));//Test long internal edge


	edges.add(new Edge(c, g));

	edges.add(new Edge(d, g));
	edges.add(new Edge(d, h));
	edges.add(new Edge(d, i));

	edges.add(new Edge(e, x));
	edges.add(new Edge(e, j));

	edges.add(new Edge(f, k));
	edges.add(new Edge(g, k));
	edges.add(new Edge(h, k));

	edges.add(new Edge(i, m));
	edges.add(new Edge(i, l));

	edges.add(new Edge(m, t));
	edges.add(new Edge(j, r));
	edges.add(new Edge(a,r));
	edges.add(new Edge(s1, s2));

	CompoundDirectedGraph graph = new CompoundDirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	graph.nodes.add(s1);
	graph.nodes.add(s2);

	new CompoundDirectedGraphLayout().visit(graph);
	return graph;
}

static CompoundDirectedGraph test1() {
	Subgraph s1, s2;
	Node n1, n2, n3, n4, n5, n6, n7;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	s1 = new Subgraph("Subgraph 1");
	s2 = new Subgraph("Subgraph 2");

	nodes.add(n1 = new Node("1", s1));
	nodes.add(n2 = new Node("2", s1));
	nodes.add(n3 = new Node("3", s1));
	nodes.add(n4 = new Node("4", s2));
	nodes.add(n5 = new Node("5", s2));
	nodes.add(n6 = new Node("6", s2));
	nodes.add(n7 = new Node("7", s2));

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
	graph.nodes = nodes;
	graph.edges = edges;
	graph.nodes.add(s1);
	graph.nodes.add(s2);

	new CompoundDirectedGraphLayout().visit(graph);
	return graph;
}

static CompoundDirectedGraph tangledSubgraphs() {
	Subgraph A, B, C, D;
	Node a1, a2, b1, b2, c1, c2, d1, d2;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();


	nodes.add(A = new Subgraph("Subgraph A"));
	nodes.add(B = new Subgraph("Subgraph B"));
	nodes.add(C = new Subgraph("Subgraph C"));
	nodes.add(D = new Subgraph("Subgraph D"));

	nodes.add(a1 = new Node("a1", A));
	nodes.add(a2 = new Node("a2", A));
	edges.add(new Edge(a1, a2));


	nodes.add(b1 = new Node("b1", B));
	nodes.add(b2 = new Node("b2", B));
	edges.add(new Edge(b1, b2));

	nodes.add(c1 = new Node("c1", C));
	nodes.add(c2 = new Node("c2", C));
	edges.add(new Edge(c1, c2));

	nodes.add(d1 = new Node("d1", D));
	nodes.add(d2 = new Node("d2", D));
	edges.add(new Edge(d1, d2));

	CompoundDirectedGraph graph = new CompoundDirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;
	
	edges.add(new Edge(a1, d2));
	edges.add(new Edge(d1, c2));
	edges.add(new Edge(d1, b2));

	new CompoundDirectedGraphLayout()
		.visit(graph);
	return graph;
}

static CompoundDirectedGraph test2() {
	Subgraph s1, s2, s1_1;
	Node n1, n2, n3, n4, n5, n6, n7, n8;
	NodeList nodes = new NodeList();
	EdgeList edges = new EdgeList();

	s1 = new Subgraph("Subgraph 1");
	s2 = new Subgraph("Subgraph 2");
	s1_1 = new Subgraph("Subgraph 1.1", s1);

	nodes.add(s1);
	nodes.add(s2);
	nodes.add(s1_1);

	nodes.add(n1 = new Node("1", s1));
	nodes.add(n2 = new Node("2", s1));
	nodes.add(n3 = new Node("3", s1));
	nodes.add(n4 = new Node("4", s2));
	nodes.add(n5 = new Node("5", s2));
	nodes.add(n6 = new Node("6", s2));
	nodes.add(n7 = new Node("7", s2));
	nodes.add(n8 = new Node("8", s1_1));

	n8.width = 80;
	n1.width = 60;
	n2.width = 70;
	n3.width = 100;
	n5.width = n6.width = 64;
	n7.width = n4.width = 90;

	edges.add(new Edge(n1, n2));
	edges.add(new Edge(n2, n3));
//	edges.add(new Edge(n1, n3));
	edges.add(new Edge(n1, n8));
	edges.add(new Edge(n1, n5));
	edges.add(new Edge(n8, n3));
	edges.add(new Edge(n4, n5));
	edges.add(new Edge(n4, n6));
	edges.add(new Edge(n6, n7));
	edges.add(new Edge(n5, n7));

	edges.add(new Edge(n2, n5));

	CompoundDirectedGraph graph = new CompoundDirectedGraph();
	graph.nodes = nodes;
	graph.edges = edges;

	new CompoundDirectedGraphLayout()
		.visit(graph);
	return graph;
}

}
