package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

public interface PositionConstants {

public static final int
	LEFT  = 1,
	CENTER= 2,
	RIGHT = 4,
	TOP   = 8,
	MIDDLE= 16,
	BOTTOM= 32,

	NONE  = 0,
	NORTH = 1,
	EAST  = 16,
	SOUTH = 4,
	WEST  = 8,
	NORTH_EAST = NORTH | EAST,
	NORTH_WEST = NORTH | WEST,
	SOUTH_EAST = SOUTH | EAST,
	SOUTH_WEST = SOUTH | WEST,
	NORTH_SOUTH= NORTH | SOUTH,
	EAST_WEST  = EAST  | WEST;

}