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
package org.eclipse.gef.requests;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.*;

/**
 * A Request to change the bounds of the EditPart(s).
 */
public class ChangeBoundsRequest
	extends GroupRequest
	implements DropRequest
{

public static final String VERTICAL_GUIDE = "$vertical guide";
public static final String VERTICAL_ANCHOR = "$vertical anchor";
public static final String HORIZONTAL_GUIDE = "$horz guide";
public static final String HORIZONTAL_ANCHOR = "$horz anchor";	
	
private Point moveDelta = new Point();
private Dimension resizeDelta = new Dimension();
private int resizeDirection;
private Point mouseLocation;
private Map extendedData = new HashMap();

/**
 * Default constructor.
 */
public ChangeBoundsRequest() { }

/**
 * Creates a ChangeBoundsRequest with the given type.
 *
 * @param type The type of Request.
 */
public ChangeBoundsRequest(Object type) {
	setType(type);
}

public Map getExtendedData() {
	return extendedData;
}

/**
 * Returns the location of the mouse pointer.
 *
 * @return The location of the mouse pointer.
 */
public Point getLocation() {
	return mouseLocation;
}

/** @deprecated Use {@link #getLocation() } 
 *  @return The location of the mouse pointer.
 */
public Point getMouseLocation() {
	return getLocation();
}

/**
 * Returns a Point representing the distance the EditPart has moved.
 *
 * @return A Point representing the distance the EditPart has moved.
 */
public Point getMoveDelta() {
	return moveDelta;
}

/**
 * Returns the direction the figure is being resized.  Possible values are
 * <ul>
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#EAST}
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#WEST}
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#NORTH}
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#SOUTH} 
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#NORTH_EAST}
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#NORTH_WEST}
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#SOUTH_EAST}
 * 		<li>{@link org.eclipse.draw2d.PositionConstants#SOUTH_WEST} 
 * </ul>
 * 
 * @return the resize direction
 */
public int getResizeDirection() {
	return resizeDirection;
}

/**
 * Returns a Dimension representing how much the EditPart has been resized.
 *
 * @return A Dimension representing how much the EditPart has been resized.
 */
public Dimension getSizeDelta() {
	return resizeDelta;
}

/**
 * Transforms a copy of the passed in rectangle to account for the move and/or resize 
 * deltas and returns this copy.
 * 
 * @param rect the rectangle to transform
 * @return a copy of the passed in rectangle representing the new bounds
 */
public Rectangle getTransformedRectangle(Rectangle rect) {
	return rect.getCopy()
		.translate(moveDelta)
		.resize(resizeDelta);
}

/**
 * Sets the move delta.
 *
 * @param p The Point representing the move delta
 */
public void setMoveDelta(Point p) {
	moveDelta = p;
}

/**
 * Sets the direction the figure is being resized.
 * 
 * @see #getResizeDirection()
 */
public void setResizeDirection(int dir) {
	resizeDirection = dir;
}

/** @deprecated Use {@link #setLocation(Point)} 
 *  @param p The location of the mouse pointer.
 */
public void setMouseLocation(Point p) {
	setLocation(p);
}

/**
 * Sets the location of the mouse pointer.
 *
 * @param p The location of the mouse pointer.
 */
public void setLocation(Point p) {
	mouseLocation = p;
}

/**
 * Sets the size delta.
 *
 * @param d The Dimension representing the size delta.
 */
public void setSizeDelta(Dimension d) {
	resizeDelta = d;
}

}