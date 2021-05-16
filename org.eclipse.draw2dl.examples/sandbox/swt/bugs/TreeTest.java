/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeTest {

public static void main(String[] args) {
	Shell shell = new Shell();

	Tree tree = new Tree(shell, SWT.NONE);
	final TreeItem item1 = new TreeItem(tree, SWT.NONE);
	item1.setText("Item 1");
	item1.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			boolean expanded = item1.getExpanded();
		}
	});
	shell.open();
	tree.removeAll();
	Display display = Display.getDefault();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
	display.dispose();
}

}
