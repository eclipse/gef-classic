/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package swt.bugs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/*
 * Shows that expansion comes after key events, but before mouse events.
 */
public class GTKTreeItemAccess {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setLayout(new GridLayout());

		Text text = new Text(shell, SWT.MULTI);
		text.setText("blah"); //$NON-NLS-1$
		text.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("widget selected"); //$NON-NLS-1$
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("widget default selected"); //$NON-NLS-1$
			}
		});

		Tree tree = new Tree(shell, 0);
		new TreeItem(tree, 0).setText("item 1"); //$NON-NLS-1$
		new TreeItem(tree, 0).setText("item 2"); //$NON-NLS-1$
		new TreeItem(new TreeItem(tree, 0), 0).setText("blah"); //$NON-NLS-1$

		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("widget selected"); //$NON-NLS-1$
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("widget default selected"); //$NON-NLS-1$
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println(e.time + " mouse down"); //$NON-NLS-1$
			}
		});

		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// e.doit = false;
				System.out.println("key pressed"); //$NON-NLS-1$
			}
		});

		tree.addTreeListener(new TreeListener() {
			@Override
			public void treeCollapsed(TreeEvent e) {
				System.out.println("collapse"); //$NON-NLS-1$
			}

			@Override
			public void treeExpanded(TreeEvent e) {
				System.out.println("expand"); //$NON-NLS-1$
			}
		});

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}