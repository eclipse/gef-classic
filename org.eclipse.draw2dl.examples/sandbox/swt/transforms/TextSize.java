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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TextSize {

static float angle = 4;

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	shell.setFont(new Font(display, "Arial", 16, 0));
	shell.setBackground(new Color(null, 60, 100, 255));

	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			Point p = gc.stringExtent("Something");
			System.out.println("Un-rotated size = " + p.x + ", " + p.y);
			Transform t = new Transform(null);
			t.translate(150, 30);
			t.rotate(180);
			gc.setTransform(t);
			p = gc.stringExtent("Something");
			System.out.println("Rotated size = " + p.x + ", " + p.y);
			t.dispose();
			gc.drawString("Something", 0, 0, false);
			gc.drawRectangle(0, 0, p.x, p.y);
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