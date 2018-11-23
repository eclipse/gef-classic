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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

public class ShellTestor {

static int x,y;

public static void main(String[] args) {
	Display d = Display.getDefault();
	final Shell shell = new Shell(d, SWT.H_SCROLL |SWT.V_SCROLL| SWT.DIALOG_TRIM);
	
	Text text = new Text(shell, SWT.BORDER);
	text.setBounds(100,10,80,23);
	text.setText("foobar");
	
	shell.getVerticalBar().addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			int dy = shell.getVerticalBar().getSelection() - y;
			shell.scroll(0, 0, x, dy, 200, 200, true);
			y = shell.getVerticalBar().getSelection();
		}
	});
	
	shell.setSize(600,500);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();

}

}
