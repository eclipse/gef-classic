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
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug68003 {

static final int INITIAL = 0;
static final int DRAGGING = 1;
static final int ABORTED = 2;

private static int state = INITIAL;
	
public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell();
	shell.setLayout(new GridLayout());
	final Canvas canvas = new Canvas(shell, SWT.BORDER);
	canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
	
	//Make the canvas focusable
	canvas.addKeyListener(new KeyAdapter(){});

	final Label status = new Label(shell, SWT.BORDER);
	status.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	
	canvas.addMouseListener(new MouseAdapter() {
		public void mouseDown(MouseEvent e) {
			state = DRAGGING;
			status.setText("drag in progress");
			shell.setEnabled(false);
			shell.setEnabled(true);
		}

		public void mouseUp(MouseEvent e) {
			if (state == DRAGGING) {
				status.setText("drag completed");
			}
			state = INITIAL;
		}
	});
	
	canvas.addFocusListener(new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			if (state == DRAGGING) {
				state = ABORTED;
				status.setText("Drag Aborted due to FocusLost");
			}
		}
	});
	
	shell.setSize(400,300);
	shell.open();
	
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}