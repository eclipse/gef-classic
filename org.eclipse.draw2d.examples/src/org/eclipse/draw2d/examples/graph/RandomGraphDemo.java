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

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;

/**
 * @author delee
 */
public class RandomGraphDemo extends DirectedGraphDemo {

/** Random seed */
public Random random = new Random(0);
/** Maximum number of rows that will appear in the random graph */
public int numberOfRows = 10;
/** Number of nodes in the graph */
public int numberOfNodes = 10;

/**
 * @see org.eclipse.draw2d.examples.graph.DirectedGraphDemo#main(java.lang.String[])
 */
public static void main(String[] args) {
	new RandomGraphDemo().run();
}

/**
 * @see org.eclipse.draw2d.examples.graph.AbstractGraphDemo#hookShell()
 */
protected void hookShell() {
	Composite composite = new Composite(shell, 0);
	composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
	
	composite.setLayout(new GridLayout());	
	
	final org.eclipse.swt.widgets.Label rowLabel 
				= new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
	rowLabel.setText("Maximum Rank");
	final Text rowsText = new Text(composite, SWT.BORDER);

	final org.eclipse.swt.widgets.Label nodesLabel 
			= new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
	nodesLabel.setText("Number of Nodes");
	final Text nodesText = new Text(composite, SWT.BORDER);

	final org.eclipse.swt.widgets.Label seedLabel 
			= new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
	seedLabel.setText("Random seed");
	final Text seedText = new Text(composite, SWT.BORDER);
	
	Button button = new Button(composite, SWT.PUSH);
	button.setText("Generate");
	button.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			try {
				int rows = Integer.parseInt(rowsText.getText());
				if (rows > 1)
					numberOfRows = rows;
				else
					rowsText.setText(Integer.toString(numberOfRows));
			
				int nodes = Integer.parseInt(nodesText.getText());
				if (nodes > 0)
					numberOfNodes = nodes;
				else
					nodesText.setText(Integer.toString(numberOfNodes));
					random = new Random(Integer.parseInt(seedText.getText()));
					getFigureCanvas().setContents(getContents());
				} catch (NumberFormatException exception) {
					Display.getCurrent().beep();
				}
		}
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	});
	
	final org.eclipse.swt.widgets.Label primeLabel 
			= new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
	primeLabel.setText("Build Prime Graph");
	final Button primeGraphButton = new Button(composite, SWT.CHECK);
	
	primeGraphButton.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			try {
				buildPrime = !buildPrime;
				random = new Random(Integer.parseInt(seedText.getText()));
				getFigureCanvas().setContents(getContents());
			} catch (NumberFormatException exception) {
				Display.getCurrent().beep();
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	});
	
	rowsText.setText("10");
	nodesText.setText("10");
	seedText.setText("0");
}

private DirectedGraph generateRandomGraph() {
	DirectedGraph randomGraph = new DirectedGraph();
	
	// Create the nodes
	for (int i = 0; i < numberOfNodes; i++) {
		Node newNode = new Node("Node " + i);
		newNode.width = 100;
		randomGraph.nodes.add(newNode);
	}
	
	// Assign each node to a random row
	for (int i = 0; i < numberOfNodes; i++) {
		((Node)(randomGraph.nodes.get(i))).rank = random.nextInt(numberOfRows);
	}
	
 	NodeList unreachableNodes = new NodeList();
 	unreachableNodes = randomGraph.nodes;
 	
	NodeList reachableNodes = new NodeList();
	EdgeList allEdges = new EdgeList();
	
	// Connect the graph
	while (unreachableNodes.size() > 0) {
		Node unreachableNode =
			unreachableNodes.getNode(random.nextInt(unreachableNodes.size()));
			if (reachableNodes.size() > 0) {
				Node randomNode =
					reachableNodes.getNode(
						random.nextInt(reachableNodes.size()));
				// If ranks are the same, re-pick to prohibit cycles
				while (randomNode.rank == unreachableNode.rank) {
					randomNode =
						reachableNodes.getNode(
							random.nextInt(reachableNodes.size()));
					unreachableNode =
						unreachableNodes.getNode(
							random.nextInt(unreachableNodes.size()));
				}
				
				Edge newEdge;
				if (randomNode.rank > unreachableNode.rank)
					newEdge = new Edge(unreachableNode, randomNode);
				else
					newEdge = new Edge(randomNode, unreachableNode);
				allEdges.add(newEdge);
			}
			findAllReachableNodesFrom(unreachableNode, reachableNodes, unreachableNodes);
	}
	
	randomGraph.nodes = reachableNodes;
	randomGraph.edges = allEdges;

	new DirectedGraphLayout().visit(randomGraph);
	return randomGraph;
}

private void findAllReachableNodesFrom(Node root, NodeList reached, NodeList unreached) {
	reached.add(root);
	unreached.remove(root);
	EdgeList incoming = root.incoming;
	EdgeList outgoing = root.outgoing;
	
	for (int i = 0; i < incoming.size(); i++) {
		Node incomingNode = incoming.getEdge(i).source;
		if (!reached.contains(incomingNode))
			findAllReachableNodesFrom(incomingNode, reached, unreached);
	}
	
	for (int i = 0; i < outgoing.size(); i++) {
			Node outgoingNode = outgoing.getEdge(i).source;
			if (!reached.contains(outgoingNode))
				findAllReachableNodesFrom(outgoingNode, reached, unreached);
	}
	
	
}

/**
 * @see org.eclipse.graph.AbstractExample#getContents()
 */
protected IFigure getContents() {
	DirectedGraph graph = generateRandomGraph();
	Figure contents = buildGraph(graph);
	return contents;
}

/**
 * @see org.eclipse.draw2d.examples.graph.AbstractGraphDemo#getGraphMethods()
 */
protected String[] getGraphMethods() {
	String[] graphMethods = new String[1];
	graphMethods[0] = "Random";
	return graphMethods;
}


private static void printGraphVisNodes(EdgeList edges) {
	for (int i = 0; i < edges.size(); i++) {
		System.out.println(
			"\""
				+ edges.getEdge(i).source.data
				+ "\" -> \""
				+ edges.getEdge(i).target.data
				+ "\"");
	}
}

}
