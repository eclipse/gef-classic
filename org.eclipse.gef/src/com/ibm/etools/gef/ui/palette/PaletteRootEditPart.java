package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.palette.*;
import com.ibm.etools.gef.ui.parts.*;
import java.util.List;

public class PaletteRootEditPart 
	extends GraphicalRootEditPart
	implements RootEditPart
{
	
public IFigure createFigure(){
	Figure figure = new Figure();
	figure.setLayoutManager(new StackLayout());
	return figure;
}

public IFigure getContentPane(){
	return getFigure();
}

}