package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * Repositions a Figure attached to a Connection
 * when the Connection is moved.
 */
public abstract class AbstractLocator
	implements Locator
{

private int relativePosition = PositionConstants.CENTER;
private int gap;
private static double sqrt2 = Math.sqrt(2.0);

public AbstractLocator() {}

/**
 * Returns the number of pixels the figure's bounding rectangle
 * is from the connection.  Only used if getRelativePosition()
 * returns something other than CENTER.
 * 
 * @since 2.0
 */
public int getGap() {
	return gap;
}

/**
 * Returns a reference point used to calculate the location.
 * 
 * @since 2.0
 */
protected abstract Point getReferencePoint();

/**
 * Recalculate the location of the figure according
 * to its desired position relative to the center point.
 * 
 * @since 2.0
 */
protected Rectangle getNewBounds(Dimension size, Point center) {
	Rectangle bounds = new Rectangle(center, size);

	bounds.x -= bounds.width/2;
	bounds.y -= bounds.height/2;

	int xFactor=0, yFactor=0;
	int position = getRelativePosition();

	if ((position & PositionConstants.NORTH) != 0) yFactor = -1;
	else if ((position & PositionConstants.SOUTH) != 0) yFactor = 1;

	if ((position & PositionConstants.WEST) != 0) xFactor = -1;
	else if ((position & PositionConstants.EAST) != 0) xFactor = 1;

	bounds.x += xFactor * (bounds.width/2 + getGap());
	bounds.y += yFactor * (bounds.height/2+ getGap());

	return bounds;
}

/**
 * Returns the position of the figure with respect to the center point.
 * May be CENTER, NORTH, SOUTH, EAST, WEST, NORTH_EAST,
 * NORTH_WEST, SOUTH_EAST, or SOUTH_WEST.
 * 
 * @since 2.0
 */
public int getRelativePosition() {
	return relativePosition;
}

/*
 * Recalculates the position of the figure and 
 * returns the updated bounds.
 */
public void relocate(IFigure target) {
	Dimension prefSize = target.getPreferredSize();
	Point center = getReferencePoint();
	target.setBounds(getNewBounds(prefSize, center));
}

/**
 * Sets the number of pixels the figure's bounding rectangle
 * is from the connection.  Only used if getRelativePosition()
 * returns something other than CENTER.
 * 
 * @since 2.0
 */
public void setGap(int i) {
	gap = i;
}

/**
 * Sets the position of the figure with respect to the center point.
 * May be CENTER, NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, 
 * SOUTH_EAST, or SOUTH_WEST.
 * 
 * @since 2.0
 */
public void setRelativePosition(int pos) {
	relativePosition = pos;
}

}