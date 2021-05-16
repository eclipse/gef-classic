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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PR_85876 {
public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setFont(new Font(display, "Arial", 18, SWT.BOLD));
	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			Path path = new Path(display);
			path.addRectangle(10, 20, 100, 200);
			e.gc.setClipping(path);
			Region r = new Region(display);
			e.gc.getClipping(r);
			System.out.println(r.getBounds());
//			gc.setBackground(new Color(display, 0, 100, 255));
//			gc.fillRectangle(0, 0, 200, 200);
//			gc.setBackground(new Color(display, 255, 255, 0));
//			gc.setAlpha(200);
//			gc.drawString("Test String", 10, 10, false);
		}
	});
	shell.open();
	
    while (!shell.isDisposed())
        if (!display.readAndDispatch())
        	display.sleep();
}
}
