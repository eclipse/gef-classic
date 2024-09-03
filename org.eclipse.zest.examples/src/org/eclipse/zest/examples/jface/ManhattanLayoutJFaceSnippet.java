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
 * Contributors:
 *     The Chisel Group, University of Victoria
 *     Zoltan Ujhelyi - update for connectionprovider
 *******************************************************************************/
package org.eclipse.zest.examples.jface;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider2;
import org.eclipse.zest.core.viewers.IEntityConnectionStyleProvider2;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;

/**
 * This snippet shows how to use the {@link IConnectionRouterStyleProvider}
 * interface to set the connection router for some references.
 *
 * Based on {@link GraphJFaceSnippet4}.
 *
 * @author Zoltan Ujhelyi - update for connectionprovider
 * @author Ian Bull - original implementation
 *
 */
public class ManhattanLayoutJFaceSnippet {

	static class MyContentProvider implements IGraphContentProvider {

		@Override
		public Object getDestination(Object rel) {
			if ("Rock2Paper".equals(rel)) {
				return "Rock";
			}
			if ("Paper2Scissors".equals(rel)) {
				return "Paper";
			}
			if ("Scissors2Rock".equals(rel)) {
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
			}
			if ("Paper2Scissors".equals(rel)) {
				return "Scissors";
			}
			if ("Scissors2Rock".equals(rel)) {
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

	static class MyConnectionRelationLabelProvider extends LabelProvider implements IConnectionStyleProvider2 {
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

		/* Relation-based customization: IConnectionStyleProvider */

		public ConnectionRouter getRouter(Object rel) {
			if (!rel.equals("Scissors2Rock")) {
				return new ManhattanConnectionRouter();
			}
			return null;
		}

		@Override
		public int getConnectionStyle(Object rel) {
			return SWT.NONE;
		}

		@Override
		public Color getColor(Object rel) {
			return null;
		}

		@Override
		public Color getHighlightColor(Object rel) {
			return null;
		}

		@Override
		public int getLineWidth(Object rel) {
			return -1;
		}

		@Override
		public IFigure getTooltip(Object entity) {
			return null;
		}

	}

	static class MyEndpointEntityLabelProvider extends LabelProvider implements IEntityConnectionStyleProvider2 {
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

		/* Endpoint-based customization: IEntityConnectionStyleProvider */

		@Override
		public ConnectionRouter getRouter(Object src, Object dest) {
			System.out.println(src + " -> " + dest);
			if (!(src.equals("Paper") && dest.equals("Rock"))) {
				return new ManhattanConnectionRouter();
			}
			return null;
		}

		@Override
		public int getConnectionStyle(Object src, Object dest) {
			return SWT.NONE;
		}

		@Override
		public Color getColor(Object src, Object dest) {
			return null;
		}

		@Override
		public Color getHighlightColor(Object src, Object dest) {
			return null;
		}

		@Override
		public int getLineWidth(Object src, Object dest) {
			return -1;
		}

		@Override
		public IFigure getTooltip(Object src, Object dest) {
			return null;
		}

		@Override
		public IFigure getTooltip(Object entity) {
			return null;
		}

	}

	static GraphViewer viewer = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("GraphJFaceSnippet2");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyConnectionRelationLabelProvider()); // MyEndpointEntityLabelProvider
		viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm());
		viewer.setInput(new Object());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				System.out
						.println("Selection Changed: " + selectionToString((StructuredSelection) event.getSelection()));
			}

			private String selectionToString(StructuredSelection selection) {
				StringBuffer stringBuffer = new StringBuffer();
				Iterator iterator = selection.iterator();
				boolean first = true;
				while (iterator.hasNext()) {
					if (first) {
						first = false;
					} else {
						stringBuffer.append(" : ");
					}
					stringBuffer.append(iterator.next());
				}
				return stringBuffer.toString();
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
