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
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.GraphicalEditPart;

/**
 * An implementation of {@link org.eclipse.gef.AutoexposeHelper} that performs
 * autoscrolling of a <code>Viewport</code> figure. This helper is for use with
 * graphical editparts that contain a viewport figure.  This helper will search the
 * editpart and find the viewport. Autoscroll will occur when the detect location is
 * inside the viewport's bounds, but near its edge.  It will continue for as long as the
 * location continues to meet these criteria.  The autoscroll direction is approximated to
 * the nearest orthogonal or diagonal direction (north, northeast, east, etc.).
 * @author hudsonr
 */
public class ViewportAutoexposeHelper
	extends ViewportHelper
	implements AutoexposeHelper
{

private static final Insets EXPOSE_THRESHOLD = new Insets(18);

/**
 * Constructs a new helper on the given GraphicalEditPart. The editpart must have a
 * <code>Viewport</code> somewhere between its <i>contentsPane</i> and its <i>figure</i>
 * inclusively.
 * @param owner the GraphicalEditPart that owns the Viewport
 */
public ViewportAutoexposeHelper(GraphicalEditPart owner) {
	super(owner);
}

/**
 * Returns <code>true</code> if the given point is inside the viewport, but near its edge.
 * @see org.eclipse.gef.AutoexposeHelper#detect(org.eclipse.draw2d.geometry.Point)
 */
public boolean detect(Point where) {
	Viewport port = findViewport(owner);
	Rectangle rect = Rectangle.SINGLETON;
	port.getClientArea(rect);
	port.translateToParent(rect);
	port.translateToAbsolute(rect);
	return rect.contains(where)
	  && !rect.crop(EXPOSE_THRESHOLD).contains(where);
}

/**
 * Returns <code>true</code> if the given point is inside the viewport, but near its edge.
 * Scrolls the viewport by a small amount in the current direction.
 * @see org.eclipse.gef.AutoexposeHelper#step(org.eclipse.draw2d.geometry.Point)
 */
public boolean step(Point where) {
	Viewport port = findViewport(owner);
	Rectangle rect = Rectangle.SINGLETON;
	port.getClientArea(rect);
	port.translateToParent(rect);
	port.translateToAbsolute(rect);
	if (!rect.contains(where)
	  || rect.crop(EXPOSE_THRESHOLD).contains(where))
		return false;
	rect.crop(EXPOSE_THRESHOLD);
	
	int region = rect.getPosition(where);
	Point loc = port.getViewLocation();

	if ((region & PositionConstants.SOUTH) != 0)
		loc.y += 4;
	else if ((region & PositionConstants.NORTH) != 0)
		loc.y -= 4;

	if ((region & PositionConstants.EAST) != 0)
		loc.x += 4;
	else if ((region & PositionConstants.WEST) != 0)
		loc.x -= 4;
	port.setViewLocation(loc);
	return true;
}

}
