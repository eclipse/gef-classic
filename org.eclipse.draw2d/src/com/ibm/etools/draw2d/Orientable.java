package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * An interface for objects that can be either
 * horizontally or vertically oriented.
 */
public interface Orientable
	extends PositionConstants
{

static int
	HORIZONTAL = 0,
	VERTICAL = 1;

/**
 * Sets the orientation.  Can be <code>Orientable.HORIZONTAL</code>
 * or <code>Orientable.VERTICAL</code>.
 */
void setOrientation(int orientation);

/**
 * Sets the direction.  Can be one of many directional
 * constants defined in {@link PositionConstants}.
 */
void setDirection(int direction);

}