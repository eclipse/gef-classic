package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
	ToolbarLayout layout = new ToolbarLayout();
	figure.setLayoutManager(layout);
	return figure;
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
}

}