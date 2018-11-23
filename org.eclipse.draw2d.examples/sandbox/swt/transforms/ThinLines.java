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
package swt.transforms;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;

public class ThinLines {

static float angle = 4;

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	shell.setFont(new Font(display, "Arial", 12, 0));

	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			Transform t = new Transform(display);
			t.translate(60, 60);
			t.rotate(angle);
			t. scale(4f, 4f);
			gc.setTransform(t);
			
			gc.setBackground(ColorConstants.lightBlue);
			gc.fillOval(0, 0, 2, 2);
			gc.setLineWidth(1);
			gc.setBackground(ColorConstants.yellow);
			gc.fillOval(-3, 0, 2, 2);
			gc.drawString("Dana", 6, 0, true);
			gc.drawOval(0, 3, 3, 3);
			gc.drawOval(-4, 3, 3, 3);
			angle += 0.3;
		}
	});
	
	shell.setSize(200, 200);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

}