package org.eclipse.graph.demo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graph.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * @since 2.1
 */
public class GraphDemo
	extends AbstractExample
{

static boolean buildPrimeGraph;

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


static public Figure buildGraph(DirectedGraph graph) {
	Figure contents = new Panel();
		
//	contents.setFont(new Font(null, "Tahoma", 10, 0));
	contents.setBackgroundColor(ColorConstants.white);
	contents.setLayoutManager(new XYLayout());
	
	Label label;
	
	for (int i=0; i<graph.nodes.size(); i++) {
		Node node = graph.nodes.getNode(i);
		label = new Label();
		label.setBackgroundColor(ColorConstants.lightGray);
		label.setOpaque(true);
		label.setBorder(new LineBorder());
		if (node.incoming.isEmpty())
			label.setBorder(new LineBorder(2));
		String text = node.data.toString();// + "(" + node.min + ", " + node.max + ")";
		label.setText(text);//+"("+node.index + ','+(int)(node.sortValue*10)+")");
		if (text.indexOf("Java") >= 0 || text.indexOf("JDI") >= 0)
			label.setBackgroundColor(ColorConstants.lightGreen);
		if (text.indexOf("Graph") >= 0
			|| text.indexOf("2d") >= 0
			|| text.indexOf("Dependen") >= 0
			|| text.indexOf("GEF")>= 0)
			label.setBackgroundColor(ColorConstants.cyan);
		node.data = label;
		contents.add(label,
			new Rectangle(node.x, node.y, node.width, node.height));
	}
	
	for (int i=0; i<graph.edges.size(); i++) {
		Edge edge = graph.edges.getEdge(i);
		PolylineConnection conn = connection(edge);
		conn.setForegroundColor(ColorConstants.lightGray);
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
//			conn.setLineStyle(Graphics.LINE_DOT);
//			Label l = new Label (Integer.toString(edge.getSlack()));
//			l.setOpaque(true);
//			conn.add(l, new ConnectionEndpointLocator(conn, false));
		}
		Figure s = (Figure)edge.source.data;
		Figure t = (Figure)edge.target.data;
		conn.setSourceAnchor(new TopOrBottomAnchor(s));
		conn.setTargetAnchor(new TopOrBottomAnchor(t));
		contents.add(conn);
	}
	if (buildPrimeGraph)
		buildPrimeGraph(graph.gPrime, contents);
	return contents;
}

static public void buildPrimeGraph(DirectedGraph graph, Figure panel) {
	for (int i = 0; i < graph.edges.size(); i++) {
		Edge e = graph.edges.getEdge(i);
		PolylineConnection line = new PolylineConnection();

		if (e.tree) {
			PolygonDecoration dec = new PolygonDecoration();
			dec.setLineWidth(2);
			if (e.head() == e.target)
				line.setSourceDecoration(dec);
			else
				line.setTargetDecoration(dec);

			line.setLineWidth(3);
			Label l = new Label (e.cut+","+e.getSlack());
			l.setOpaque(true);
			line.add(l, new ConnectionLocator(line));
		}
		else {
			line.setLineStyle(Graphics.LINE_DOT);
//			Label l = new Label ("("+e.getSlack()+")");
//			l.setOpaque(true);
//			line.add(l, new ConnectionLocator(line));
		}
		panel.add(line);
		Node n1;
		Node n2 = (Node)e.target.data;
		if (e.source.data instanceof NodePair) {
			NodePair pair = (NodePair)e.source.data;
			n1 = pair.n1;
			if (n1 == n2)
				n1 = pair.n2;
		} else {
			n1 = (Node)e.source.data;
		}
		ConnectionAnchor sa, ta;
		sa = new XYAnchor(new Point(n1.x, n1.y+20));
		ta = new XYAnchor(new Point(n2.x, n2.y+20));
		if (n1.rank == n2.rank) {
			if (n1.data instanceof Figure)
				sa = new TopOrBottomAnchor((Figure)n1.data);
			if (n2.data instanceof Figure)
				ta = new TopOrBottomAnchor((Figure)n2.data);
		} else {
			sa = new XYAnchor(new Point((n1.x+n2.x)/2-15, (n1.y+n2.y)/2 + 20));
			if (n2.data instanceof Figure)
				ta = new TopOrBottomAnchor((Figure)n2.data);
		}
		line.setSourceAnchor(sa);
		line.setTargetAnchor(ta);
	}
}

static PolylineConnection connection(Edge e) {
	PolylineConnection conn = new PolylineConnection();
	conn.setConnectionRouter(new BendpointConnectionRouter());
	List bends = new ArrayList();
	NodeList nodes = e.vNodes;
	if (nodes != null) {
		for (int i=0; i<nodes.size(); i++){
			Node n = nodes.getNode(i);
			int x = n.x;
			int y = n.y;
			bends.add(new AbsoluteBendpoint(x, y));
			bends.add(new AbsoluteBendpoint(x, y+40));
		}
	}
	conn.setRoutingConstraint(bends);
	return conn;
}

public static void main(String[] args) {
	new GraphDemo().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	DirectedGraph graph = GraphTests.graph3();
	buildPrimeGraph = true;

	Figure contents = buildGraph(graph);
	
	return contents;
}

}
