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
package org.eclipse.gef.examples.pde;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;

/**
 * @author Daniel Lee
 */
public abstract class AbstractGraphDemo {

static boolean buildPrime = false;

static String graphMethod;

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
			p = getOwner().getBounds().getBottom();
		getOwner().translateToAbsolute(p);
		return p;
	}
}

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
			p = getOwner().getBounds().getRight();
		getOwner().translateToAbsolute(p);
		return p;
	}
}

/**
 * Builds a figure for the given edge and adds it to contents
 * @param contents the parent figure to add the edge to
 * @param edge the edge
 */
static void buildEdgeFigure(Figure contents, Edge edge) {
	PolylineConnection conn = connection(edge);
//	conn.setForegroundColor(ColorConstants.gray);
//	conn.setLineWidth(2);
	
	
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

/**
 * Builds a Figure for the given node and adds it to contents
 * @param contents the parent Figure to add the node to
 * @param node the node to add
 */
static void buildNodeFigure(Figure contents, Node node) {
	Label label;
	label = new Label();
	label.setBackgroundColor(ColorConstants.darkGray);
	label.setForegroundColor(ColorConstants.white);
	label.setOpaque(true);
	label.setBorder(new MarginBorder(3));
//	if (node.incoming.isEmpty())
//		label.setBorder(new LineBorder(2));
	String text = node.data.toString();// + "(" + node.index +","+node.sortValue+ ")";
	label.setText(text);
	node.data = label;
	contents.add(label, new Rectangle(node.x, node.y, node.width, node.height));
}

/**
 * Builds a Figure for the given prime edge
 * @param e the prime edge
 * @return the Figure for the prime edge
 */
static PolylineConnection buildPrimeEdge(Edge e) {
	PolylineConnection line = new PolylineConnection();
	
	if (e.tree) {
		PolygonDecoration dec = new PolygonDecoration();
		dec.setLineWidth(2);
	
		line.setLineWidth(3);
		Label l = new Label (e.cut + "");
		l.setOpaque(true);
		line.add(l, new ConnectionLocator(line));
	} else {
		line.setLineStyle(Graphics.LINE_DOT);
		Label l = new Label (e.getSlack() + "");
		l.setOpaque(true);
		line.add(l, new ConnectionLocator(line));
	}
	return line;
}

/**
 * Builds a connection for the given edge
 * @param e the edge
 * @return the connection
 */
static PolylineConnection connection(Edge e) {
	PolylineConnection conn = new PolylineConnection();
	conn.setConnectionRouter(new BendpointConnectionRouter());
	List bends = new ArrayList();
	NodeList nodes = e.vNodes;
	if (nodes != null) {
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.getNode(i);
			int x = n.x;
			int y = n.y;
			bends.add(new AbsoluteBendpoint(x, y));
			bends.add(new AbsoluteBendpoint(x, y + n.height));
		}
	}
	conn.setRoutingConstraint(bends);
	return conn;
}

}
