package com.ibm.etools.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.Rectangle;
import com.ibm.etools.draw2d.IFigure;

public class RelativeHandleLocator
	extends com.ibm.etools.draw2d.RelativeLocator
{

public RelativeHandleLocator(IFigure reference, int location){
	super(reference, location);
}

protected Rectangle getReferenceBox(){
	IFigure f = getReferenceFigure();
	if (f instanceof HandleBounds)
		return ((HandleBounds)f).getHandleBounds();
	return super.getReferenceBox();
}

}