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
public static final int LEFT   =  1;
/** Center (Horizontal) */
public static final int CENTER =  2;
/** Right */
public static final int RIGHT  =  4;
/** Bit-wise OR of LEFT, CENTER, and RIGHT */
public static final int LEFT_CENTER_RIGHT = LEFT | CENTER | RIGHT;
/** Top */
public static final int TOP    =  8;
/** Middle (Vertical) */
public static final int MIDDLE = 16;
/** Bottom */
public static final int BOTTOM = 32;
/** Bit-wise OR of TOP, MIDDLE, and BOTTOM */
public static final int TOP_MIDDLE_BOTTOM = TOP | MIDDLE | BOTTOM;
/** None */
public static final int NONE  =  0;
/** North */
public static final int NORTH =  1;
/** South */
public static final int SOUTH =  4;
/** East */
public static final int WEST  =  8;
/** West */
public static final int EAST  = 16;
/** North-East: a bit-wise OR of {@link #NORTH} and {@link #EAST} */
public static final int NORTH_EAST = NORTH | EAST;
/** North-West: a bit-wise OR of {@link #NORTH} and {@link #WEST} */
public static final int NORTH_WEST = NORTH | WEST;
/** South-East: a bit-wise OR of {@link #SOUTH} and {@link #EAST} */
public static final int SOUTH_EAST = SOUTH | EAST;
/** South-West: a bit-wise OR of {@link #SOUTH} and {@link #WEST} */
public static final int SOUTH_WEST = SOUTH | WEST;
/** North-South: a bit-wise OR of {@link #NORTH} and {@link #SOUTH} */
public static final int NORTH_SOUTH = NORTH | SOUTH;
/** East-West: a bit-wise OR of {@link #EAST} and {@link #WEST} */
public static final int EAST_WEST = EAST | WEST;

}