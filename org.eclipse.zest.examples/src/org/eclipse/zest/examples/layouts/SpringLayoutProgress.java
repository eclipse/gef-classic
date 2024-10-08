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
package org.eclipse.zest.examples.layouts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

/**
 *
 */
public class SpringLayoutProgress {
	static Runnable r = null;
	static boolean MouseDown = false;

	static boolean first = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("GraphSnippet1");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		shell.setLayout(gridLayout);
		shell.setSize(500, 500);

		final Graph g = new Graph(shell, SWT.NONE);
		g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 5));
		g.setSize(500, 500);

		GraphNode aa = new GraphNode(g, SWT.NONE, "A");
		GraphNode bb = new GraphNode(g, SWT.NONE, "B");
		GraphNode cc = new GraphNode(g, SWT.NONE, "C");

		GraphNode dd = new GraphNode(g, SWT.NONE, "D");
		GraphNode ee = new GraphNode(g, SWT.NONE, "E");
		GraphNode ff = new GraphNode(g, SWT.NONE, "F");

		GraphNode root = new GraphNode(g, SWT.NONE, "Root");

		new GraphConnection(g, SWT.NONE, root, aa);
		new GraphConnection(g, SWT.NONE, root, bb);
		new GraphConnection(g, SWT.NONE, root, cc);

		new GraphConnection(g, SWT.NONE, aa, bb);
		new GraphConnection(g, SWT.NONE, bb, cc);
		new GraphConnection(g, SWT.NONE, cc, aa);
		new GraphConnection(g, SWT.NONE, aa, dd);
		new GraphConnection(g, SWT.NONE, bb, ee);
		new GraphConnection(g, SWT.NONE, cc, ff);
		new GraphConnection(g, SWT.NONE, cc, dd);
		new GraphConnection(g, SWT.NONE, dd, ee);
		new GraphConnection(g, SWT.NONE, ee, ff);

		GraphNode nodes[] = new GraphNode[3];
		nodes[0] = aa;
		nodes[1] = bb;
		nodes[2] = cc;

		for (int k = 0; k < 1; k++) {
			for (int i = 0; i < 8; i++) {
				GraphNode n = new GraphNode(g, SWT.NONE, "1 - " + i);
				for (int j = 0; j < 5; j++) {
					GraphNode n2 = new GraphNode(g, SWT.NONE, "2 - " + j);
					new GraphConnection(g, SWT.NONE, n, n2).setWeight(-1);
					new GraphConnection(g, SWT.NONE, nodes[j % 3], n2);

				}
				new GraphConnection(g, SWT.NONE, root, n);
			}
		}

		List nodes2 = g.getNodes();
		for (Object element : nodes2) {
			GraphNode node = (GraphNode) element;
			node.setLocation(200, 200);
		}
		g.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				MouseDown = false;

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				MouseDown = true;

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		g.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				List selection = g.getSelection();
				List graphNodes = g.getNodes();
				for (Object graphNode : graphNodes) {
					GraphNode node = (GraphNode) graphNode;
					if (!g.getSelection().contains(node)) {
						node.unhighlight();
					}
				}

				List connctions = g.getConnections();
				for (Object connction : connctions) {
					GraphConnection connection = (GraphConnection) connction;
					connection.unhighlight();
					connection.setWeight(-1);
				}

				for (Object object : selection) {
					if (object instanceof GraphNode node) {
						List sourceConnections = node.getSourceConnections();
						for (Object sourceConnection : sourceConnections) {
							GraphConnection connection = (GraphConnection) sourceConnection;
							connection.getDestination().highlight();
							connection.highlight();
							connection.setWeight(10);

						}

						List target = node.getTargetConnections();
						for (Object element : target) {
							GraphConnection connection = (GraphConnection) element;
							connection.getSource().highlight();
							connection.highlight();
							connection.setWeight(10);

						}

					}

				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		final SpringLayoutAlgorithm springLayoutAlgorithm = new SpringLayoutAlgorithm();
		g.addLayoutFilter(item -> {
			if (item instanceof GraphNode) {
				return item.getGraphModel().getSelection().contains(item) && MouseDown; // MouseDown;
			}
			return false;
		});

		g.setLayoutAlgorithm(springLayoutAlgorithm, false);
		// springLayoutAlgorithm.setIterations(1000);
		// springLayoutAlgorithm.fitWithinBounds = true;
		g.applyLayoutNow();
		Button b = new Button(shell, SWT.FLAT);
		b.setText("step");

		final Label label = new Label(shell, SWT.LEFT);
		label.setText("<--click");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		b.addSelectionListener(new SelectionAdapter() {
			int steps = 0;

			@Override
			public void widgetSelected(SelectionEvent e) {

				r = () -> {
					springLayoutAlgorithm.performNIteration(1);

					g.redraw();
					label.setText("steps: " + ++steps);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Display.getCurrent().asyncExec(r);
				};
				Display.getCurrent().asyncExec(r);

			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}

}
