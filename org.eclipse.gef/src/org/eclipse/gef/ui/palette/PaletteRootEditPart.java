package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.ui.parts.GraphicalRootEditPart;

public class PaletteRootEditPart extends GraphicalRootEditPart {
	
public IFigure createFigure(){
	Figure figure = new Figure();
	figure.setLayoutManager(new StackLayout());
	return figure;
}

public IFigure getContentPane(){
	return getFigure();
}

}