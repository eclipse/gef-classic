package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.palette.*;

abstract class PaletteEditPart 
	extends com.ibm.etools.gef.editparts.AbstractGraphicalEditPart
{

public void createEditPolicies(){}

protected static int triangleSide = 10;

static Triangle getArrowRight(){
	Triangle arrowRight = new Triangle();
	arrowRight.setDirection(Triangle.EAST);
	arrowRight.setOrientation(Triangle.HORIZONTAL);
	arrowRight.setBackgroundColor(ColorConstants.black);
	arrowRight.setPreferredSize(triangleSide,triangleSide);
	return arrowRight;
}

public List getModelChildren(){
	if (getModel() instanceof PaletteContainer)
		return ((PaletteContainer)getModel()).getChildren();
	return super.getModelChildren();
}

}