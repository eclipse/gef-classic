/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package swt.bugs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug23332 {

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Shell"); //$NON-NLS-1$
		final StyledText text = new StyledText(shell, SWT.MULTI | SWT.WRAP);
		text.setText(System.getProperties().toString() + System.getProperties() + System.getProperties()
				+ System.getProperties());
		shell.addListener(SWT.Resize, event -> handleShellResize(shell, text));
		shell.setSize(200, 200);
		shell.open();
		shell.setMinimized(true);
		System.out.println("BOUNDS+CLIENT: " + shell.getBounds() + ", " + shell.getClientArea()); //$NON-NLS-1$ //$NON-NLS-2$
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void handleShellResize(final Shell shell, final StyledText text) {
		System.out.println("RESIZE: " + shell.getBounds() + ", " + shell.getClientArea()); //$NON-NLS-1$ //$NON-NLS-2$
		if (shell.getClientArea().isEmpty()) {
			long start = System.currentTimeMillis();
			text.setBounds(shell.getClientArea());
			long end = System.currentTimeMillis();
			System.out.println("Wasted time:" + (end - start)); //$NON-NLS-1$
		} else {
			text.setBounds(shell.getClientArea());
		}
		System.out.println(shell.getClientArea());
	}

}