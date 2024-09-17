/*******************************************************************************
 * Copyright (c) 2011, 2024 Simon Templer and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Simon Templer - example for using custom figures based on
 *        ILabeledFigure/IStyleableFigure, associated to bug 335136
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
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.widgets.ILabeledFigure;
import org.eclipse.zest.core.widgets.IStyleableFigure;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;

/**
 * Example demonstrating the use of a custom figure that implements
 * {@link ILabeledFigure} and {@link IStyleableFigure}.<br>
 * <br>
 * The custom figure will be configured according to the label provider and
 * highlighted when it is selected.
 */
public class CustomFigureJFaceSnippet {

	/**
	 * Custom figure shaped like a rectangle.
	 */
	public static class RectLabelFigure extends RectangleFigure implements ILabeledFigure, IStyleableFigure {

		private final Label label;

		private Color borderColor;

		public RectLabelFigure() {
			setLayoutManager(new GridLayout(1, true));

			label = new Label();
			add(label);
			GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
			setConstraint(label, gd);
		}

		@Override
		protected void outlineShape(Graphics graphics) {
			graphics.setForegroundColor(borderColor);

			super.outlineShape(graphics);
		}

		@Override
		public void setBorderColor(Color borderColor) {
			this.borderColor = borderColor;
		}

		@Override
		public void setBorderWidth(int borderWidth) {
			setLineWidth(borderWidth);
		}

		@Override
		public void setText(String text) {
			label.setText(text);
			adjustSize();
		}

		@Override
		public String getText() {
			return label.getText();
		}

		@Override
		public void setIcon(Image icon) {
			label.setIcon(icon);
			adjustSize();
		}

		@Override
		public Image getIcon() {
			return label.getIcon();
		}

		protected void adjustSize() {
			setSize(getPreferredSize());
		}

	}

	static class MyContentProvider implements IGraphEntityContentProvider {

		@Override
		public Object[] getConnectedTo(Object entity) {
			if (entity.equals("One")) {
				return new Object[] { "Two" };
			}
			if (entity.equals("Two")) {
				return new Object[] { "Three" };
			}
			if (entity.equals("Three")) {
				return new Object[] { "One" };
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return new String[] { "One", "Two", "Three" };
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	static class MyLabelProvider extends LabelProvider implements IFigureProvider {
		final Image image = Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);

		@Override
		public Image getImage(Object element) {
			if (element instanceof EntityConnectionData) {
				return null;
			}

			return image;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof EntityConnectionData) {
				return null;
			}

			return element.toString();
		}

		@Override
		public IFigure getFigure(Object element) {
			// use a custom figure
			return new RectLabelFigure();
		}

	}

	static GraphViewer viewer = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("CustomFigureJFaceSnippet");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyLabelProvider());
		viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm());
		viewer.setInput(new Object());
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}