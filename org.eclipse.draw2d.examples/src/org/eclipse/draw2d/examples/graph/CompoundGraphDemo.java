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

import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.draw2d.internal.graph.NodePair;

/**
 * @author hudsonr
 * @since 2.1
 */
public class CompoundGraphDemo
	extends AbstractGraphDemo
{

/**
 * Builds the graph, creates Draw2d figures for all graph components.
 * @param graph the graph to build
 * @return the Figure representing the graph
 */
public static Figure buildGraph(CompoundDirectedGraph graph) {
	Figure contents = new Panel();
	contents.setFont(new Font(null, "Tahoma", 10, 0));
	contents.setBackgroundColor(ColorConstants.white);
	contents.setLayoutManager(new XYLayout());
	
	for (int i = 0; i < graph.subgraphs.size(); i++) {
		Subgraph s = (Subgraph)graph.subgraphs.get(i);
		buildSubgraphFigure(contents, s);
	}
	
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
		sa = new XYAnchor(new Point(n1.x, n1.y + n1.height / 2));
		ta = new XYAnchor(new Point(n2.x, n2.y + n2.height / 2));
		if (rankSeparator) {
			if (n1.data instanceof Figure)
				sa = new LeftOrRightAnchor((Figure)n1.data);
			if (n2.data instanceof Figure)
				ta = new LeftOrRightAnchor((Figure)n2.data);
		} else {
			sa =
				new XYAnchor(
					new Point(
						(n1.x + n2.x) / 2 - 15,
						(n1.y + n1.height / 2 + n2.height / 2 + n2.y) / 2));
			if (n2.data instanceof Figure)
				ta = new TopOrBottomAnchor((Figure)n2.data);
		}
		line.setSourceAnchor(sa);
		line.setTargetAnchor(ta);
	}
}

private static void buildSubgraphFigure(Figure contents, Subgraph s) {
	Figure figure = new Figure();	
	figure.setBorder(new LineBorder(ColorConstants.blue, s.insets.left));
	contents.add(figure, new Rectangle(s.x, s.y, s.width, s.height));
}

/**
 * Run this demo
 * @param args command line args
 */
public static void main(String[] args) {
	new CompoundGraphDemo().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	CompoundDirectedGraph graph = null;
	try {
		graph =
			(CompoundDirectedGraph) (CompoundGraphTests
				.class
				.getMethod(graphMethod, null)
				.invoke(null, null)); 
	} catch (Exception e) {
		System.out.println("Could not build graph");
	}
	Figure contents = buildGraph(graph);
	return contents;
}

/**
 * @see org.eclipse.graph.demo.GraphDemo#getGraphMethods()
 */
protected String[] getGraphMethods() {
	Method[] methods = CompoundGraphTests.class.getMethods();
	String[] methodNames = new String[methods.length];
	
	int nameIndex = 0;
	for (int i = 0; i < methods.length; i++) {
		if (methods[i].getReturnType().equals(CompoundDirectedGraph.class)) {
			methodNames[nameIndex] = methods[i].getName();
			nameIndex++;
		}
	}
	return methodNames;
}

}
