package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Rectangle;

public interface FreeformFigure
	extends IFigure
{

void addFreeformListener(FreeformListener listener);

void fireExtentChanged();

Rectangle getFreeformExtent();

void removeFreeformListener(FreeformListener listener);

void setFreeformBounds(Rectangle bounds);

}
