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
package bidi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MixedOrientations {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(SWT.SHELL_TRIM | SWT.RIGHT_TO_LEFT);
	shell.setLayout(new GridLayout());

	final String message = "\u202b\u0634\u0634\u0634 \u202c\u202c\u0636\u0636\u0636";
	final String rawMessage = message.substring(1);

	StyledText text = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.LEFT_TO_RIGHT);
	text.setLayoutData(new GridData(GridData.FILL_BOTH));
	text.setText(message);

	final Canvas canvas = new Canvas(shell, SWT.BORDER | SWT.LEFT_TO_RIGHT);
	canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
	canvas.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			TextLayout layout = new TextLayout(shell.getDisplay());
			layout.setWidth(shell.getClientArea().width);
			layout.setOrientation(SWT.RIGHT_TO_LEFT);
			//layout.setAlignment(SWT.RIGHT);
			layout.setText(rawMessage);
			for (int i = 0; i < rawMessage.length(); i++)
				System.out.print(layout.getLevel(i));
			System.out.println();
			layout.setText(message);
			for (int i = 0; i < message.length(); i++)
				System.out.print(layout.getLevel(i));
			System.out.println();
			layout.draw(e.gc, 0, 0);
			layout.dispose();
		}
	});

	shell.setSize(400, 300);
	shell.open();

	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}


}