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

public class SliderPaletteEditPart 
	extends PaletteEditPart
{

public SliderPaletteEditPart(PaletteRoot paletteRoot) {
	super(paletteRoot);
}

public IFigure createFigure() {
	Figure figure = new Figure();
	figure.setOpaque(true);
	figure.setForegroundColor(ColorConstants.listForeground);
	figure.setBackgroundColor(ColorConstants.button);
	ToolbarLayout layout = new PaletteToolbarLayout();
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