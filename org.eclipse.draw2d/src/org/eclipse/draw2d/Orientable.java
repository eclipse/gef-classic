package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * An interface for objects that can be either horizontally or vertically oriented.
 */
public interface Orientable
	extends PositionConstants, IFigure
{

/**
 * A constant representing a horizontal orientation.
 */
static int HORIZONTAL = 0;
/**
 * A constant representing a vertical orientation.
 */
static int VERTICAL = 1;

/**
 * Sets the orientation. Can be either {@link #HORIZONTAL} or {@link #VERTICAL}.
 * @param orientation The orientation
 */
void setOrientation(int orientation);

/**
 * Sets the direction the orientable figure will face.  Can be one of many directional
 * constants defined in {@link PositionConstants}.
 * @param direction The direction
 */
void setDirection(int direction);

}