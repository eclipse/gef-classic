/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.tests.swt;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.LayoutItem;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

/**
 * This snippet shows how to use the findFigureAt to get the figure under the mouse
 * 
 * @author Ian Bull
 * 
 */
public class GraphSnippet8 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("GraphSnippet8");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		final Graph graph = new Graph(shell, SWT.NONE);

		GraphNode a = new GraphNode(graph, SWT.NONE, "Root");
		GraphNode b = new GraphNode(graph, SWT.NONE, "B");
		GraphNode c = new GraphNode(graph, SWT.NONE, "C");
		GraphNode d = new GraphNode(graph, SWT.NONE, "D");
		GraphNode e = new GraphNode(graph, SWT.NONE, "E");
		GraphNode f = new GraphNode(graph, SWT.NONE, "F");
		GraphNode g = new GraphNode(graph, SWT.NONE, "G");
		GraphNode h = new GraphNode(graph, SWT.NONE, "H");
		GraphConnection connection = new GraphConnection(graph, SWT.NONE, a, b);
		connection.setData(Boolean.FALSE);
		connection = new GraphConnection(graph, SWT.NONE, a, c);
		connection.setData(Boolean.FALSE);
		connection = new GraphConnection(graph, SWT.NONE, a, c);
		connection.setData(Boolean.FALSE);
		connection = new GraphConnection(graph, SWT.NONE, a, d);
		connection.setData(Boolean.FALSE);
		connection = new GraphConnection(graph, SWT.NONE, b, e);
		connection.setData(Boolean.FALSE);
		connection = new GraphConnection(graph, SWT.NONE, b, f);
		connection.setData(Boolean.FALSE);
		connection = new GraphConnection(graph, SWT.NONE, c, g);
		connection.setData(Boolean.FALSE);
		connection = new GraphConnection(graph, SWT.NONE, d, h);
		connection.setData(Boolean.FALSE);
		
		connection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, b, c);
		connection.setLineColor(ColorConstants.red);
		connection.setLineWidth(3);
		connection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, c, d);
		connection.setLineColor(ColorConstants.red);
		connection.setLineWidth(3);
		connection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, e, f);
		connection.setLineColor(ColorConstants.red);
		connection.setLineWidth(3);
		connection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, f, g);
		connection.setLineColor(ColorConstants.red);
		connection.setLineWidth(3);
		
		connection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, h, e);
		connection.setLineColor(ColorConstants.red);
		connection.setLineWidth(3);
		
		
		
		TreeLayoutAlgorithm treeLayoutAlgorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		Filter filter = new Filter() {
			public boolean isObjectFiltered(LayoutItem item) {
	
				Object object = item.getGraphData();
				if  (object instanceof GraphConnection ) {
					GraphConnection connection = (GraphConnection) object;
					if ( connection.getData() != null && connection.getData() instanceof Boolean ) {
						return ((Boolean)connection.getData()).booleanValue();
					}
					return true;
				}
				return false;
			}
			
		};
		treeLayoutAlgorithm.setFilter(filter);
		graph.setLayoutAlgorithm(treeLayoutAlgorithm, true);
		

		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
