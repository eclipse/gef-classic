/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

class EditPartTipHelper 
	extends org.eclipse.draw2d.PopUpHelper
{

private static EditPartTipHelper currentHelper;

private static void setHelper(EditPartTipHelper helper) {
	if (currentHelper != null && currentHelper != helper && currentHelper.isShowing())
		currentHelper.hide();
	currentHelper = helper;
}

public EditPartTipHelper(Control c) {
	super(c);	
}

/**
 * Sets the LightWeightSystem object's contents  
 * to the passed tooltip, and displays the tip at 
 * the coordianates specified by tipPosX and tipPosY.  The given coordinates will be
 * adjusted if the tip cannot be completely visible on the screen.
 *
 * @param tip  The tool tip to be displayed.
 * @param tipPosX X coordiante of tooltip to be displayed
 * @param tipPosY Y coordinate of tooltip to be displayed
 */
public void displayToolTipAt(IFigure tip, int tipPosX, int tipPosY) {
	if (tip != null) {
		// Adjust the position if the tip will not be completely visible on the screen
		int shiftX = 0;
		int shiftY = 0;
		Dimension tipSize = tip.getPreferredSize();
		org.eclipse.swt.graphics.Rectangle area = control.getDisplay().getClientArea();
		org.eclipse.swt.graphics.Point end = new org.eclipse.swt.graphics.Point(
					tipPosX + tipSize.width, tipPosY + tipSize.height);
		if (!area.contains(end)) {
			shiftX = end.x - (area.x + area.width);
			shiftY = end.y - (area.y + area.height);
			shiftX = shiftX < 0 ? 0 : shiftX;
			shiftY = shiftY < 0 ? 0 : shiftY;
		}
		tipPosX -= shiftX;
		tipPosY -= shiftY;
		
		// Display the tip
		EditPartTipHelper.setHelper(this);
		getLightweightSystem().setContents(tip);
		setShellBounds(tipPosX, tipPosY, tipSize.width, tipSize.height);
		show();
		getShell().setCapture(true);
	}
}

/**
 * @see org.eclipse.draw2d.PopUpHelper#hide()
 */
protected void hide() {
	super.hide();
	currentHelper = null;
}

protected void hookShellListeners() {

	/* If the cursor leaves the tip window, hide the tooltip and
	   dispose of its shell */
	getShell().addMouseTrackListener(new MouseTrackAdapter() {
		public void mouseExit(MouseEvent e) {
			getShell().setCapture(false);	
			dispose();
		}
	});
	/* If the mouseExit listener does not get called,
	   dispose of the shell if the cursor is no longer in the 
	   tooltip. This occurs in the rare case that a mouseEnter 
	   is not received on the tooltip when it appears.*/
	getShell().addMouseMoveListener(new MouseMoveListener() {
		public void mouseMove(MouseEvent e) {
			Point eventPoint = getShell().toDisplay(new Point(e.x, e.y));
			if (!getShell().getBounds().contains(eventPoint)) {
				if (isShowing())
					getShell().setCapture(false);
				dispose();
			}
		}
	});
}

}