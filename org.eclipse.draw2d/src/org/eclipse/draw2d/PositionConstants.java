package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
/** Top */
public static final int TOP    =  8;
/** Middle (Vertical) */
public static final int MIDDLE = 16;
/** Bottom */
public static final int BOTTOM = 32;

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