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

package org.eclipse.draw2dl.examples.hittest;
import org.eclipse.draw2dl.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2dl.geometry.Dimension;

public class EllipseHitting {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell();
	
	org.eclipse.draw2dl.LightweightSystem lws = new LightweightSystem(shell);
	
	org.eclipse.draw2dl.Figure panel = new Figure();
	panel.setLayoutManager(new FlowLayout());
	panel.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.white);

	org.eclipse.draw2dl.MouseMotionListener listener = new MouseMotionListener.Stub() {
		public void mouseEntered(org.eclipse.draw2dl.MouseEvent me) {
			((org.eclipse.draw2dl.Shape)me.getSource()).setBackgroundColor(org.eclipse.draw2dl.ColorConstants.yellow);
		}
		public void mouseExited(MouseEvent me) {
			((Shape)me.getSource()).setBackgroundColor(ColorConstants.white);
		}
	};

	for (int i=1; i <= 4; i++){
		org.eclipse.draw2dl.Ellipse e = new Ellipse();
		e.setFill(true);
		e.setPreferredSize(new Dimension (20 + 10*i + i%2, 60 - 10*i + i/2));
		e.addMouseMotionListener(listener);
		panel.add(e);
	}
	
	lws.setContents(panel);
	shell.setSize(400,300);
	shell.open();
	
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();

}

}
