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
import java.lang.reflect.Method;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug54989 {

static Display display;

static Shell shell;

static Combo combo, readOnlyCombo;

static CCombo ccombo, readOnlyCcombo;

public static void main(String[] args) {
	display = new Display();
	shell = new Shell(display);
	shell.setLayout(new GridLayout(2, true));
	shell.setText("CCombo Accessibility Test");

	final Button b = new Button(shell, 0);
	b.setText("some button");
	
	final Canvas canvas = new Canvas(shell, 0) {
		/* This is just a hack to make canvas size == button size */
		public Point computeSize(int wHint, int hHint, boolean flush) {
			return b.computeSize(wHint, hHint, flush);
		}
	};
	
	canvas.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			Image image = new Image(null, canvas.getBounds().width, canvas.getBounds(). height);
			GC gc = new GC(b);
			gc.copyArea(image,0,0);
			e.gc.drawImage(image, 0, 0);
			gc.dispose();
			image.dispose();
		}
	});

	long time = System.currentTimeMillis();
	try {
		Class clazz = Class.forName("java.text.BreakIterator");
		Method m = clazz.getMethod("getLineInstance", null);
		m.invoke(null, null);
	} catch (Exception e) {
		e.printStackTrace();
	}
	//BreakIterator.getLineInstance().setText("bogus");
	System.out.println(System.currentTimeMillis() - time);
	
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
}
}