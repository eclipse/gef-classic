/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylyn.zest.tests.swt;

import org.eclipse.mylyn.zest.core.widgets.ConstraintAdapter;
import org.eclipse.mylyn.zest.core.widgets.Graph;
import org.eclipse.mylyn.zest.core.widgets.GraphConnection;
import org.eclipse.mylyn.zest.core.widgets.GraphNode;
import org.eclipse.mylyn.zest.layouts.LayoutStyles;
import org.eclipse.mylyn.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.constraints.BasicEdgeConstraints;
import org.eclipse.mylyn.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This snippet shows how to use the findFigureAt to get the figure under the
 * mouse
 * 
 * @author Ian Bull
 * 
 */
public class LayoutExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("GraphSnippet1");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		final Graph g = new Graph(shell, SWT.NONE);
		GraphNode root = new GraphNode(g, SWT.NONE, "Root");
		for (int i = 0; i < 3; i++) {
			GraphNode n = new GraphNode(g, SWT.NONE, "1 - " + i);
			for (int j = 0; j < 3; j++) {
				GraphNode n2 = new GraphNode(g, SWT.NONE, "2 - " + j);
				new GraphConnection(g, SWT.NONE, n, n2);
			}
			new GraphConnection(g, SWT.NONE, root, n);
		}

		SpringLayoutAlgorithm springLayoutAlgorithm = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);

		ConstraintAdapter constraintAdapters = new ConstraintAdapter() {

			public void populateConstraint(Object object, LayoutConstraint constraint) {
				if (constraint instanceof BasicEdgeConstraints) {
					BasicEdgeConstraints basicEdgeConstraints = (BasicEdgeConstraints) constraint;
					GraphConnection connection = (GraphConnection) object;
					if (connection.getSource().getText().equals("Root")) {
						basicEdgeConstraints.weight = 10;
					}
					else {
						basicEdgeConstraints.weight = 0;
					}
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
