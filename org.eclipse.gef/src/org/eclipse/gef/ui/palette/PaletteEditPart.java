package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.palette.*;

abstract class PaletteEditPart 
	extends org.eclipse.gef.editparts.AbstractGraphicalEditPart
{

private AccessibleEditPart acc;

protected static int triangleSide = 10;

/**
 * returns the AccessibleEditPart for this EditPart.   This method is called lazily from
 * {@link #getAccessibleEditPart()}.
 */
protected AccessibleEditPart createAccessible() {
	return null;
}

public void createEditPolicies(){}

static Triangle getArrowRight(){
	Triangle arrowRight = new Triangle();
	arrowRight.setDirection(Triangle.EAST);
	arrowRight.setBackgroundColor(ColorConstants.black);
	arrowRight.setPreferredSize(triangleSide,triangleSide);
	arrowRight.setMaximumSize(new Dimension(triangleSide, triangleSide));
	return arrowRight;
}

protected AccessibleEditPart getAccessibleEditPart() {
	if (acc == null)
		acc = createAccessible();
	return acc;
}

public List getModelChildren(){
	if (getModel() instanceof PaletteContainer)
		return ((PaletteContainer)getModel()).getChildren();
	return super.getModelChildren();
}

}