package org.eclipse.graph.demo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Font;

import org.eclipse.graph.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * @since 2.1
 */
public class CompoundGraphDemo
	extends AbstractExample
{

static class LeftOrRightAnchor extends ChopboxAnchor {
	public LeftOrRightAnchor(IFigure owner) {
		super(owner);
	}
	public Point getLocation(Point reference) {
		Point p;
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.x < p.x)
			p = getOwner().getBounds().getLeft();
		else
			p =getOwner().getBounds().getRight();
		getOwner().translateToAbsolute(p);
		return p;
	}
}

static class TopOrBottomAnchor extends ChopboxAnchor {
	public TopOrBottomAnchor(IFigure owner) {
		super(owner);
	}
	public Point getLocation(Point reference) {
		Point p;
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.y < p.y)
			p = getOwner().getBounds().getTop();
		else
			p =getOwner().getBounds().getBottom();
		getOwner().translateToAbsolute(p);
		return p;
	}
}

static boolean buildPrime;

static int HEIGHT = 100;

private static void buildEdgeFigure(Figure contents, Edge edge) {
			PolylineConnection conn = connection(edge);
			conn.setForegroundColor(ColorConstants.gray);
			conn.setLineWidth(2);
			
			
			if (edge.tree) {
	//			Label l = new Label ("(" + edge.cut + ")");
	//			l.setOpaque(true);
	//			conn.add(l, new ConnectionLocator(conn));
	//			conn.setLineWidth(3);
	//			PolygonDecoration dec = new PolygonDecoration();
	//			dec.setLineWidth(3);
	//			if (edge.head() == edge.target)
	//				conn.setSourceDecoration(dec);
	//			else
	//				conn.setTargetDecoration(dec);
			} else {
	//			conn.setLineStyle(Graphics.LINE_DASHDOT);
	//			Label l = new Label (Integer.toString(edge.getSlack()));
	//			l.setOpaque(true);
	//			conn.add(l, new ConnectionEndpointLocator(conn, false));
			}
			Node s = edge.source;
			Node t = edge.target;
			
			conn.setSourceAnchor(
				new XYAnchor(new Point(s.x + edge.getSourceOffset(), s.y + s.height)));
			conn.setTargetAnchor(
				new XYAnchor(new Point(t.x + edge.getTargetOffset(), t.y)));
//			conn.setSourceAnchor(new TopOrBottomAnchor(s));
//			conn.setTargetAnchor(new TopOrBottomAnchor(t));
			contents.add(conn);
}

static public Figure buildGraph(CompoundDirectedGraph graph) {
	Figure contents = new Panel();
	contents.setFont(new Font(null, "Tahoma", 10, 0));
	contents.setBackgroundColor(ColorConstants.white);
	contents.setLayoutManager(new XYLayout());
			
	for (int i = 0; i < graph.subgraphs.size(); i++) {
		Subgraph s = (Subgraph)graph.subgraphs.get(i);
		buildSubgraphFigure(contents, s);
	}
	
	for (int i=0; i<graph.nodes.size(); i++) {
		Node node = graph.nodes.getNode(i);
		buildNodeFigure(contents, node);
	}
	
	for (int i=0; i<graph.edges.size(); i++) {
		Edge edge = graph.edges.getEdge(i);
		buildEdgeFigure(contents, edge);
	}
	if (buildPrime)
		buildPrimeGraph(graph.gPrime, contents);
	return contents;
}

private static void buildNodeFigure(Figure contents, Node node) {
	Label label;
	label = new Label();
	label.setBackgroundColor(ColorConstants.lightGray);
	label.setOpaque(true);
	label.setBorder(new LineBorder());
	if (node.incoming.isEmpty())
		label.setBorder(new LineBorder(2));
	String text = node.data.toString();// + "(" + node.index +","+node.sortValue+ ")";
	label.setText(text);
	node.data = label;
	contents.add(label, new Rectangle(node.x, node.y, node.width, node.height));
}

static public void buildPrimeGraph(DirectedGraph graph, Figure panel) {
	for (int i = 0; i < graph.edges.size(); i++) {
		Edge e = graph.edges.getEdge(i);
		PolylineConnection line = buildPrimeEdge(e);
		panel.add(line);
		Node n1;
		Node n2 = (Node)e.target.data;
		boolean rankSeparator = false;
		if (e.source.data instanceof NodePair) {
			//edge connects Ne's to nodes.
			NodePair pair = (NodePair)e.source.data;
			n1 = pair.n1;
			if (n1 == n2)
				n1 = pair.n2;
		} else {
//			panel.remove(line);
			n1 = (Node)e.source.data;
			rankSeparator = true;
		}
		ConnectionAnchor sa, ta;
		sa = new XYAnchor(new Point(n1.x, n1.y + n1.height/2));
		ta = new XYAnchor(new Point(n2.x, n2.y + n2.height/2));
		if (rankSeparator) {
			if (n1.data instanceof Figure)
				sa = new LeftOrRightAnchor((Figure)n1.data);
			if (n2.data instanceof Figure)
				ta = new LeftOrRightAnchor((Figure)n2.data);
		} else {
			sa = new XYAnchor(new Point((n1.x+n2.x)/2-15, (n1.y + n1.height/2 + n2.height/2 + n2.y)/2));
			if (n2.data instanceof Figure)
				ta = new TopOrBottomAnchor((Figure)n2.data);
		}
		line.setSourceAnchor(sa);
		line.setTargetAnchor(ta);
	}
}

private static PolylineConnection buildPrimeEdge(Edge e) {
	PolylineConnection line = new PolylineConnection();
	
	if (e.tree) {
		PolygonDecoration dec = new PolygonDecoration();
		dec.setLineWidth(2);
		if (e.head() == e.target)
			line.setSourceDecoration(dec);
		else
			line.setTargetDecoration(dec);
	
		line.setLineWidth(3);
		Label l = new Label (e.cut+"");
		l.setOpaque(true);
		line.add(l, new ConnectionLocator(line));
	}
	else {
		line.setLineStyle(Graphics.LINE_DOT);
		Label l = new Label (e.getSlack()+"");
		l.setOpaque(true);
		line.add(l, new ConnectionLocator(line));
	}
	return line;
}

private static void buildSubgraphFigure(Figure contents, Subgraph s) {
	Figure figure = new Figure();	
	figure.setBorder(new LineBorder(ColorConstants.blue, s.insets.left));
	contents.add(figure, new Rectangle(s.x, s.y, s.width, s.height));
}

static PolylineConnection connection(Edge e) {
	PolylineConnection conn = new PolylineConnection();
	conn.setConnectionRouter(new BendpointConnectionRouter());
	List bends = new ArrayList();
	NodeList nodes = e.vNodes;
	if (nodes != null) {
		for (int i=0; i<nodes.size(); i++) {
			Node n = nodes.getNode(i);
			bends.add(new AbsoluteBendpoint(n.x, n.y));
			bends.add(new AbsoluteBendpoint(n.x, n.y + 40));
		}
	}
	conn.setRoutingConstraint(bends);
	return conn;
}

public static void main(String[] args) {
	new CompoundGraphDemo().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	CompoundDirectedGraph graph = CompoundGraphTests.test1();
	buildPrime = false;
	Figure contents = buildGraph(graph);
	
	return contents;
}

}
