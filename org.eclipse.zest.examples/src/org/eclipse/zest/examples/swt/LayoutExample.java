/*******************************************************************************
 * Copyright 2005-2007, 2024, CHISEL Group, University of Victoria, Victoria,
 *                            BC, Canada and others.
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

import org.eclipse.zest.core.widgets.ConstraintAdapter;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.constraints.BasicEdgeConstraints;

/**
 * This snippet shows how to use constraints. Nodes are attracted to the
 * {@code root} node and repelled by {@code non-root} nodes. The attraction is
 * proportional to the number of edges.
 *
 * @author Ian Bull
 *
 */
public class LayoutExample {
	private static Graph g;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell
		Shell shell = new Shell();
		Display d = shell.getDisplay();
		shell.setText("GraphSnippet1");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		g = new Graph(shell, SWT.NONE);
		GraphNode root = new GraphNode(g, SWT.NONE, "Root");
		for (int i = 0; i < 3; i++) {
			GraphNode n = new GraphNode(g, SWT.NONE, "1 - " + i);
			for (int j = 0; j < 3; j++) {
				GraphNode n2 = new GraphNode(g, SWT.NONE, "2 - " + j);
				new GraphConnection(g, SWT.NONE, n, n2);
			}
			new GraphConnection(g, SWT.NONE, root, n);
		}

		SpringLayoutAlgorithm.Zest1 springLayoutAlgorithm = new SpringLayoutAlgorithm.Zest1(LayoutStyles.NO_LAYOUT_NODE_RESIZING);

		ConstraintAdapter constraintAdapters = (object, constraint) -> {
			if (constraint instanceof BasicEdgeConstraints basicEdgeConstraints) {
				GraphConnection connection = (GraphConnection) object;
				if (connection.getSource().getText().equals("Root")) {
					basicEdgeConstraints.weight = 1;
				} else {
					basicEdgeConstraints.weight = -1;
				}
			}

		};

		g.addConstraintAdapter(constraintAdapters);
		g.setLayoutAlgorithm(springLayoutAlgorithm, true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}

}
