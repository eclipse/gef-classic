package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.*;
import java.util.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.draw2d.geometry.Insets;

public class GroupEditPart 
	extends PaletteEditPart {

private Figure innerFigure;

public GroupEditPart(PaletteContainer group){
	setModel(group);
}

public IFigure createFigure(){
	innerFigure = new Figure();
	innerFigure.setBorder(new MarginBorder(new Insets(2)));
	innerFigure.setOpaque(true);
	innerFigure.setLayoutManager(new ToolbarLayout());
	Figure outerFigure = new Figure();
	outerFigure.setLayoutManager(new StackLayout());
	outerFigure.add(innerFigure);
	return outerFigure;
}

public IFigure getContentPane(){
	return innerFigure;
}

}