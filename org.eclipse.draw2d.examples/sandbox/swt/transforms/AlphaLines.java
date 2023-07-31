/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AlphaLines extends AbstractSWTTransform {

	static float angle = 4;

	public static void main(String[] args) {
		new AlphaLines().runTransformTest();
	}

	@Override
	protected void performPaint(PaintEvent e) {
		GC gc = e.gc;
		gc.setAlpha(35);
		for (int i = 0; i < 800; i++) {
			int color = java.awt.Color.HSBtoRGB((float) (i % 400) / 400, 1, 1);
			gc.setForeground(new Color((color >> 16) & 255, (color >> 8) & 255, color & 255));
			gc.drawLine(0, 800 - i, i, 0);
		}
		gc.setBackground(new Color(255, 255, 255));
		gc.fillRectangle(10, 10, 600, 600);
	}

	@Override
	protected Shell createShell(Display display) {
		Shell shell = super.createShell(display);
		shell.setSize(900, 900);
		return shell;
	}

}