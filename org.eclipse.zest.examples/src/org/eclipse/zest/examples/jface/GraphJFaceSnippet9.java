/*******************************************************************************
 * Copyright (c) 2011, 2024 Zoltan Ujhelyi and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Zoltan Ujhelyi - initial implementation
 *******************************************************************************/
package org.eclipse.zest.examples.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

/**
 * This snippet shows how to use the ITreeContentProvider to build a graph.
 */
public class GraphJFaceSnippet9 {

	static class MyContentProvider implements ITreeContentProvider {

		private static final String n1 = "First", n2 = "Second", n3 = "Third", n4 = "Fourth";

		@Override
		public Object[] getElements(Object inputElement) {
			return new String[] { n1 };
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement.equals(n1)) {
				return new Object[] { n2, n3 };
			}
			if (parentElement.equals(n2)) {
				return new Object[] { n4 };
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			if (element.equals(n2)) {
				return n1;
			}
			if (element.equals(n3)) {
				return n1;
			}
			if (element.equals(n4)) {
				return new Object[] { n2 };
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element.equals("First") || element.equals("Second");
		}
	}

	static class MyLabelProvider extends LabelProvider {
		final Image image = Display.getDefault().getSystemImage(SWT.ICON_WARNING);

		@Override
		public Image getImage(Object element) {
			if (element instanceof String) {
				return image;
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof String) {
				return element.toString();
			}
			return null;
		}

	}

	static GraphViewer viewer = null;

	public static void main(String[] args) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Reload");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.setInput(new Object());
			}
		});
		viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyLabelProvider());
		viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm());
		viewer.addSelectionChangedListener(event -> System.out.println("Selection changed: " + (event.getSelection())));
		viewer.setInput(new Object());
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}
