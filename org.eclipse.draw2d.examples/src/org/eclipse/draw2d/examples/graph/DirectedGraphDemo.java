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
package org.eclipse.draw2d.examples.graph;

import java.lang.reflect.Method;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.internal.graph.NodePair;

/**
 * @author hudsonr
 * @since 2.1
 */
public class DirectedGraphDemo
	extends AbstractGraphDemo
{

/**
 * Builds the graph, creates Draw2d figures for all graph components.
 * @param graph the graph to build
 * @return the Figure representing the graph
 */
public static Figure buildGraph(DirectedGraph graph) {
	Figure contents = new Panel();
		
//	contents.setFont(new Font(null, "Tahoma", 10, 0));
	contents.setBackgroundColor(ColorConstants.white);
	contents.setLayoutManager(new XYLayout());
	
	for (int i = 0; i < graph.nodes.size(); i++) {
		Node node = graph.nodes.getNode(i);
		buildNodeFigure(contents, node);
	}
	
	for (int i = 0; i < graph.edges.size(); i++) {
		Edge edge = graph.edges.getEdge(i);
		buildEdgeFigure(contents, edge);
	}
	if (buildPrime)
		buildPrimeGraph(graph.gPrime, contents);
	return contents;
}

/**
 * Builds the prime graph, creates Draw2d figures for all prime graph components.
 * @param graph the graph to build the prime graph on top of
 * @param panel the Figure containing the Draw2d graph representation
 */
public static void buildPrimeGraph(DirectedGraph graph, Figure panel) {
	for (int i = 0; i < graph.edges.size(); i++) {
		Edge e = graph.edges.getEdge(i);
		PolylineConnection line = new PolylineConnection();

		if (e.tree) {
			PolygonDecoration dec = new PolygonDecoration();
			dec.setLineWidth(2);
//			if (e.head() == e.target)
//				line.setSourceDecoration(dec);
//			else
//				line.setTargetDecoration(dec);

			line.setLineWidth(3);
			Label l = new Label (e.cut + "," + e.getSlack());
			l.setOpaque(true);
			line.add(l, new ConnectionLocator(line));
		} else {
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
		sa = new XYAnchor(new Point(n1.x, n1.y + 20));
		ta = new XYAnchor(new Point(n2.x, n2.y + 20));
		if (n1.rank == n2.rank) {
			if (n1.data instanceof Figure)
				sa = new TopOrBottomAnchor((Figure)n1.data);
			if (n2.data instanceof Figure)
				ta = new TopOrBottomAnchor((Figure)n2.data);
		} else {
			sa =
				new XYAnchor(
					new Point((n1.x + n2.x) / 2 - 15, (n1.y + n2.y) / 2 + 20));
			if (n2.data instanceof Figure)
				ta = new TopOrBottomAnchor((Figure)n2.data);
		}
		line.setSourceAnchor(sa);
		line.setTargetAnchor(ta);
	}
}

/**
 * Runs this demo
 * @param args command line args
 */
public static void main(String[] args) {
	new DirectedGraphDemo().run();
}

/**
 * @see org.eclipse.graph.demo.GraphDemo#getGraphMethods()
 */
protected String[] getGraphMethods() {
	Method[] methods = GraphTests.class.getMethods();
	String[] methodNames = new String[methods.length];
	
	int nameIndex = 0;
	for (int i = 0; i < methods.length; i++) {
		if (methods[i].getReturnType().equals(DirectedGraph.class)) {
			methodNames[nameIndex] = methods[i].getName();
			nameIndex++;
		}
	}
	return methodNames;
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	DirectedGraph graph = null;
	try {
		graph =
			(DirectedGraph) (GraphTests
				.class
				.getMethod(graphMethod, null)
				.invoke(null, null));
	} catch (Exception e) {
		System.out.println("Could not build graph");
		e.printStackTrace();
	}
	Figure contents = buildGraph(graph);
	return contents;
}

}
