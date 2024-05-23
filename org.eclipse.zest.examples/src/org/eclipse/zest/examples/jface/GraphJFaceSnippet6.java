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
package org.eclipse.zest.examples.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.INestedContentProvider;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

/**
 * This snippet shows how to use the INestedGraphContentProvider to create a
 * graph with Zest. In this example, getElements returns 3 edges: * Rock2Paper *
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
public class GraphJFaceSnippet6 {
	private static final String SCISSORS = "scissors"; //$NON-NLS-1$
	private static final String PAPER = "paper"; //$NON-NLS-1$
	private static final String ROCK = "rock"; //$NON-NLS-1$
	private static final String THIRD = "Third"; //$NON-NLS-1$
	private static final String SECOND = "Second"; //$NON-NLS-1$
	private static final String FIRST = "First"; //$NON-NLS-1$

	static class MyContentProvider implements IGraphEntityContentProvider, INestedContentProvider {

		@Override
		public Object[] getConnectedTo(Object entity) {
			if (entity.equals(FIRST)) {
				return new Object[] { SECOND };
			}
			if (entity.equals(SECOND)) {
				return new Object[] { THIRD, ROCK };
			}
			if (entity.equals(THIRD)) {
				return new Object[] { FIRST };
			}
			if (entity.equals(ROCK)) {
				return new Object[] { PAPER };
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return new String[] { FIRST, SECOND, THIRD };
		}

		public double getWeight(Object entity1, Object entity2) {
			return 0;
		}

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getChildren(Object element) {
			return new Object[] { ROCK, PAPER, SCISSORS };
		}

		@Override
		public boolean hasChildren(Object element) {
			return element.equals(FIRST);
		}

	}

	static class MyLabelProvider extends LabelProvider {
		final Image image = Display.getDefault().getSystemImage(SWT.ICON_WARNING);

		@Override
		public Image getImage(Object element) {
			if (ROCK.equals(element) || PAPER.equals(element) || SCISSORS.equals(element)) {
				return image;
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof EntityConnectionData) {
				return ""; //$NON-NLS-1$
			}
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
		shell.setText("GraphJFaceSnippet2"); //$NON-NLS-1$
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyLabelProvider());
		viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		viewer.setInput(new Object());

		Button button = new Button(shell, SWT.PUSH);
		button.setText("push"); //$NON-NLS-1$
		button.addListener(SWT.Selection, e -> viewer.setInput(new Object()));

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}

	}

}
