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
package org.eclipse.draw2d;

/**
 * Constants representing cardinal directions and relative positions.
 */
public interface PositionConstants {

/** Left */
int LEFT   =  1;
/** Center (Horizontal) */
int CENTER =  2;
/** Right */
int RIGHT  =  4;
/** Bit-wise OR of LEFT, CENTER, and RIGHT */
int LEFT_CENTER_RIGHT = LEFT | CENTER | RIGHT;
/** Top */
int TOP    =  8;
/** Middle (Vertical) */
int MIDDLE = 16;
/** Bottom */
int BOTTOM = 32;
/** Bit-wise OR of TOP, MIDDLE, and BOTTOM */
int TOP_MIDDLE_BOTTOM = TOP | MIDDLE | BOTTOM;
/** None */
int NONE  =  0;
/** North */
int NORTH =  1;
/** South */
int SOUTH =  4;
/** West */
int WEST  =  8;
/** East */
int EAST  = 16;
/** North-East: a bit-wise OR of {@link #NORTH} and {@link #EAST} */
int NORTH_EAST = NORTH | EAST;
/** North-West: a bit-wise OR of {@link #NORTH} and {@link #WEST} */
int NORTH_WEST = NORTH | WEST;
/** South-East: a bit-wise OR of {@link #SOUTH} and {@link #EAST} */
int SOUTH_EAST = SOUTH | EAST;
/** South-West: a bit-wise OR of {@link #SOUTH} and {@link #WEST} */
int SOUTH_WEST = SOUTH | WEST;
/** North-South: a bit-wise OR of {@link #NORTH} and {@link #SOUTH} */
int NORTH_SOUTH = NORTH | SOUTH;
/** East-West: a bit-wise OR of {@link #EAST} and {@link #WEST} */
int EAST_WEST = EAST | WEST;

}