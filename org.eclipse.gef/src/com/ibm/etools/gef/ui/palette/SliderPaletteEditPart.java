package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;
import com.ibm.etools.gef.palette.*;
import com.ibm.etools.gef.*;
import java.util.List;
import org.eclipse.swt.graphics.Color;

public class SliderPaletteEditPart 
	extends PaletteEditPart {

public SliderPaletteEditPart(PaletteRoot paletteRoot){
	setModel(paletteRoot);
}

public IFigure createFigure(){
	Figure figure = new Figure();
	figure.setBorder(new MarginBorder(2,0,2,0));
	figure.setForegroundColor(ColorConstants.buttonDarker);
	figure.setBackgroundColor(ColorConstants.white);
	ToolbarLayout layout = new ToolbarLayout();
	layout.setSpacing(3);
	figure.setLayoutManager(layout);
	return figure;
}

public List getModelChildren(){
	return ((PaletteRoot)getModel()).getChildren();
}

protected void addChildVisual(EditPart childEditPart, int index){
	((GraphicalEditPart)childEditPart).getFigure().setBorder(new PaletteDrawerBorder());
	super.addChildVisual(childEditPart, index);
}

}