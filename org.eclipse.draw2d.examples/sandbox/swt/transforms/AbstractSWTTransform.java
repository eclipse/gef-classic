/*******************************************************************************
 * Copyright (c) 2023 Johannes Kepler University Linz.
 *
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Alois Zoitl - initial API and implementation
 *******************************************************************************/

package swt.transforms;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

abstract class AbstractSWTTransform {

	protected void runTransformTest() {
		final Display display = new Display();
		final Shell shell = createShell(display);

		shell.addPaintListener(this::performPaint);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	protected abstract void performPaint(PaintEvent e);

	@SuppressWarnings("static-method") // allow sub classes to overwrite and add own configurations
	protected Shell createShell(final Display display) {
		final Shell shell = new Shell(display);
		shell.setText("Shell"); //$NON-NLS-1$
		shell.setFont(new Font(display, "Arial", 12, 0)); //$NON-NLS-1$
		shell.setSize(200, 200);
		return shell;
	}

}
