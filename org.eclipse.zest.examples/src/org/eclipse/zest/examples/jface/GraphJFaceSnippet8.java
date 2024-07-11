/*******************************************************************************
 * Copyright (c) 2011, 2024 Fabian Steeg and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Fabian Steeg - initial implementation
 *******************************************************************************/
package org.eclipse.zest.examples.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

/**
 * Shows curved edges.
 *
 * @author Fabian Steeg
 */
public class GraphJFaceSnippet8 {

	static class MyContentProvider implements IGraphEntityContentProvider {

		@Override
		public Object[] getConnectedTo(Object entity) {
			if (entity.equals("First")) {
				return new Object[] { "First", "Second" };
			}
			if (entity.equals("Second")) {
				return new Object[] { "Third" };
			}
			if (entity.equals("Third")) {
				return new Object[] { "Second" };
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return new String[] { "First", "Second", "Third" };
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
	}

	static class MyLabelProvider extends LabelProvider implements ISelfStyleProvider {

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof EntityConnectionData) {
				return ""; //$NON-NLS-1$
			}
			return element.toString();
		}

		@Override
		public void selfStyleConnection(Object element, GraphConnection connection) {
			connection.setLineStyle(SWT.LINE_CUSTOM);
			PolylineConnection pc = (PolylineConnection) connection.getConnectionFigure();
			pc.setLineDash(new float[] { 4 });
			pc.setTargetDecoration(createDecoration(ColorConstants.white));
			pc.setSourceDecoration(createDecoration(ColorConstants.green));
		}

		private PolygonDecoration createDecoration(final Color color) {
			PolygonDecoration decoration = new PolygonDecoration() {
				@Override
				protected void fillShape(Graphics g) {
					g.setBackgroundColor(color);
					super.fillShape(g);
				}
			};
			decoration.setScale(10, 5);
			decoration.setBackgroundColor(color);
			return decoration;
		}

		@Override
		public void selfStyleNode(Object element, GraphNode node) {
		}

	}

	static GraphViewer viewer = null;

	public static void main(String[] args) {
		Shell shell = new Shell();
		Display d = shell.getDisplay();
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyLabelProvider());
		viewer.setLayoutAlgorithm(new RadialLayoutAlgorithm.Zest1());
		viewer.setInput(new Object());

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}

	}
}
