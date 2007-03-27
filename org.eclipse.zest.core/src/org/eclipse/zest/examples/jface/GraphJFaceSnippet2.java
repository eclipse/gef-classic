/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.examples.jface;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylar.zest.core.viewers.GraphViewer;
import org.eclipse.mylar.zest.core.viewers.IGraphContentProvider;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Ian Bull
 * 
 */
public class GraphJFaceSnippet2 {

	static class MyContentProvider implements IGraphContentProvider {

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

		public Object[] getElements(Object input) {
			return new Object[] { "Rock2Paper", "Paper2Scissors", "Scissors2Rock" };
		}

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
			// TODO Auto-generated method stub
			return 0;
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	static class MyLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return null;
		}

		public String getText(Object element) {
			return element.toString();
		}

		public void addListener(ILabelProviderListener listener) {

		}

		public void dispose() {

		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}

	}

	static GraphViewer viewer = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Reload");
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				viewer.setInput(new Object());
			}

		});
		viewer = new GraphViewer(shell, SWT.NONE);

		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyLabelProvider());
		viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

		viewer.setInput(new Object());

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}

	}
}
