package org.eclipse.gef.editparts;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.GraphicalEditPart;

/**
 * @author hudsonr
 */
public class ViewportAutoexposeHelper
	extends ViewportHelper
	implements AutoexposeHelper
{

private static final Insets EXPOSE_THRESHOLD = new Insets(18);

public ViewportAutoexposeHelper(GraphicalEditPart owner) {
	super(owner);
}

/**
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
		loc.y+=4;
	else if ((region & PositionConstants.NORTH) != 0)
		loc.y-=4;

	if ((region & PositionConstants.EAST) != 0)
		loc.x+=4;
	else if ((region & PositionConstants.WEST) != 0)
		loc.x-=4;
	port.setViewLocation(loc);
	return true;
}

}
