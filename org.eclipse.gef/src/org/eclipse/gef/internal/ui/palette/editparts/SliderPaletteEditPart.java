/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;

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
	return figure;
}

/**
 * This method overrides super's functionality to do nothing.
 * 
 * @see org.eclipse.gef.ui.palette.PaletteEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#registerVisuals()
 */
protected void registerVisuals() {
	super.registerVisuals();
	controller = new DrawerAnimationController(
		((PaletteViewer)getViewer()).getPaletteViewerPreferences());
	getViewer().getEditPartRegistry().put(DrawerAnimationController.class, controller);
	ToolbarLayout layout = new PaletteToolbarLayout(controller);
	getFigure().setLayoutManager(layout);
}

}