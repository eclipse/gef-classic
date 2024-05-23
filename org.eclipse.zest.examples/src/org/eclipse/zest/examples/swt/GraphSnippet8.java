/*******************************************************************************
 * Copyright 2005-2007, 2024, CHISEL Group, University of Victoria, Victoria,
 *                            BC, Canada. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.examples.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import org.eclipse.draw2d.ColorConstants;

/**
 * This snippet shows how to filter elements in the layout. The Data on the tree
 * connections are set to "False", meaning they won't be filtered.
 *
 * @author Ian Bull
 *
 */
public class GraphSnippet8 {
	private static Graph graph;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Shell shell = new Shell();
		Display display = shell.getDisplay();
		shell.setText("GraphSnippet8"); //$NON-NLS-1$
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		graph = new Graph(shell, SWT.NONE);

		GraphNode a = new GraphNode(graph, SWT.NONE, "Root"); //$NON-NLS-1$
		GraphNode b = new GraphNode(graph, SWT.NONE, "B"); //$NON-NLS-1$
		GraphNode c = new GraphNode(graph, SWT.NONE, "C"); //$NON-NLS-1$
		GraphNode d = new GraphNode(graph, SWT.NONE, "D"); //$NON-NLS-1$
		GraphNode e = new GraphNode(graph, SWT.NONE, "E"); //$NON-NLS-1$
		GraphNode f = new GraphNode(graph, SWT.NONE, "F"); //$NON-NLS-1$
		GraphNode g = new GraphNode(graph, SWT.NONE, "G"); //$NON-NLS-1$
		GraphNode h = new GraphNode(graph, SWT.NONE, "H"); //$NON-NLS-1$
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
		Filter filter = item -> {

			// Get the "Connection" from the Layout Item
			// and use this connection to get the "Graph Data"
			Object object = item.getGraphData();
			if (object instanceof GraphConnection connection1) {
				if (connection1.getData() instanceof Boolean boolVal) {
					// If the data is false, don't filter, otherwise, filter.
					return boolVal.booleanValue();
				}
				return true;
			}
			return false;
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
