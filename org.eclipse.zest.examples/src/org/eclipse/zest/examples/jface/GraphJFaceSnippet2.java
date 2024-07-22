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
package org.eclipse.zest.examples.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

/**
 * This snippet shows how to use the IGraphContentProvider to create a graph
 * with Zest. In this example, getElements returns 3 edges: * Rock2Paper *
 * Paper2Scissors * Scissors2Rock
 *
 * And for each of these, the source and destination are returned in getSource
 * and getDestination.
 *
 * A label provider is also used to create the text and icons for the graph.
 *
 * @author Ian Bull
 *
 */
public class GraphJFaceSnippet2 {

	static class MyContentProvider implements IGraphContentProvider {

		@Override
		public Object getDestination(Object rel) {
			if ("Rock2Paper".equals(rel)) {
				return "Rock";
			} else if ("Paper2Scissors".equals(rel)) {
				return "Paper";
			} else if ("Scissors2Rock".equals(rel)) {
				return "Scissors";
			}
			return null;
		}

		@Override
		public Object[] getElements(Object input) {
			return new Object[] { "Rock2Paper", "Paper2Scissors", "Scissors2Rock" };
		}

		@Override
		public Object getSource(Object rel) {
			if ("Rock2Paper".equals(rel)) {
				return "Paper";
			} else if ("Paper2Scissors".equals(rel)) {
				return "Scissors";
			} else if ("Scissors2Rock".equals(rel)) {
				return "Rock";
			}
			return null;
		}

		public double getWeight(Object connection) {
			return 0;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	static class MyLabelProvider extends LabelProvider {
		final Image image = Display.getDefault().getSystemImage(SWT.ICON_WARNING);

		@Override
		public Image getImage(Object element) {
			if (element.equals("Rock") || element.equals("Paper") || element.equals("Scissors")) {
				return image;
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			return element.toString();
		}

	}

	static GraphViewer viewer = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Shell shell = new Shell();
		Display d = shell.getDisplay();
		shell.setText("GraphJFaceSnippet2");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyLabelProvider());
		viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm.Zest1(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		viewer.setInput(new Object());
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}

	}
}
