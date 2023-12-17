/*******************************************************************************
 * Copyright 2005-2007, 2023 CHISEL Group, University of Victoria, Victoria, BC,
 * Canada.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;
import org.eclipse.zest.layouts.interfaces.EntityLayout;
import org.eclipse.zest.layouts.interfaces.LayoutContext;

/**
 * This snippet shows how to create a custom layout. This layout simply lays the
 * nodes out vertically on the same Y-Axis as they currently have. All the work
 * is done in the applyLayoutInternal Method.
 *
 * @author irbull
 *
 */
public class CustomLayout {

	public static void main(String[] args) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("Custom Layout Example");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		Graph g = new Graph(shell, SWT.NONE);

		GraphNode n = new GraphNode(g, SWT.NONE, "Paper");
		GraphNode n2 = new GraphNode(g, SWT.NONE, "Rock");
		GraphNode n3 = new GraphNode(g, SWT.NONE, "Scissors");
		new GraphConnection(g, SWT.NONE, n, n2);
		new GraphConnection(g, SWT.NONE, n2, n3);
		new GraphConnection(g, SWT.NONE, n3, n);

		LayoutAlgorithm layoutAlgorithm = new AbstractLayoutAlgorithm(0) {
			private LayoutContext context;

			@Override
			public void setLayoutContext(LayoutContext context) {
				this.context = context;
			}

			@Override
			public void applyLayout(boolean clean) {
				EntityLayout[] entitiesToLayout = context.getEntities();
				int totalSteps = entitiesToLayout.length;
				double distance = context.getBounds().width / totalSteps;
				int xLocation = 0;

				for (EntityLayout layoutEntity : entitiesToLayout) {
					layoutEntity.setLocation(xLocation, layoutEntity.getLocation().y);
					xLocation += distance;
				}
			}

			@Override
			public void setLayoutArea(double x, double y, double width, double height) {
			}

			@Override
			protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
				return false;
			}

			@Override
			protected void applyLayoutInternal(InternalNode[] entitiesToLayout,
					InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth,
					double boundsHeight) {
			}

			@Override
			protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout,
					InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
			}

			@Override
			protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
					InternalRelationship[] relationshipsToConsider) {
			}

			@Override
			protected int getTotalNumberOfLayoutSteps() {
				return 0;
			}

			@Override
			protected int getCurrentLayoutStep() {
				return 0;
			}
		};
		g.setLayoutAlgorithm(layoutAlgorithm, true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}
