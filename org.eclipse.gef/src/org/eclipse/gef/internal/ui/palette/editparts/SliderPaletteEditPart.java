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

import org.eclipse.draw2d.*;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;

public class SliderPaletteEditPart 
	extends PaletteEditPart
{

private DrawerAnimationController controller;

public SliderPaletteEditPart(PaletteRoot paletteRoot) {
	super(paletteRoot);
}

public IFigure createFigure() {
	Figure figure = new Figure();
	figure.setOpaque(true);
	figure.setForegroundColor(ColorConstants.listForeground);
	figure.setBackgroundColor(ColorConstants.button);

	// The controller is being created here because it cannot be created in the 
	// constructor as its parent is not set at that point (and hence the viewer cannot
	// be accessed).
	controller = new DrawerAnimationController(
			((PaletteViewer)getViewer()).getPaletteViewerPreferences());
	getViewer().getEditPartRegistry().put(DrawerAnimationController.class, controller);
	ToolbarLayout layout = new PaletteToolbarLayout(controller);
	figure.setLayoutManager(layout);
	
	return figure;
}

/**
 * This method overrides super's functionality to do nothing.
 * 
 * @see org.eclipse.gef.ui.palette.PaletteEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
}

}