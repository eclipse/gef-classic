package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

/**
 * A collection of cursors.
 */
public class Cursors {

/**
 * Returns the cursor corresponding to the given direction, defined in 
 * {@link PositionConstants}.
 * @param direction The relative direction of the desired cursor
 * @return The appropriate directional cursor
 */
public static Cursor getDirectionalCursor(int direction) {
	switch (direction) {
		case PositionConstants.NORTH :
			return SIZEN;
		case PositionConstants.SOUTH:
			return SIZES;
		case PositionConstants.EAST :
			return SIZEE;
		case PositionConstants.WEST:
			return SIZEW;
		case PositionConstants.SOUTH_EAST:
			return SIZESE;
		case PositionConstants.SOUTH_WEST:
			return SIZESW;
		case PositionConstants.NORTH_EAST:
			return SIZENE;
		case PositionConstants.NORTH_WEST:
			return SIZENW;
		default:
		 	break;
	}
	return null;
}

/**
 * @see {@link SWT#CURSOR_ARROW}
 */
public static final Cursor ARROW;
/**
 * @see {@link SWT#CURSOR_SIZEN}
 */
public static final Cursor SIZEN;
/**
 * @see {@link SWT#CURSOR_SIZENE}
 */
public static final Cursor SIZENE;
/**
 * @see {@link SWT#CURSOR_SIZEE}
 */
public static final Cursor SIZEE;
/**
 * @see {@link SWT#CURSOR_SIZESE}
 */
public static final Cursor SIZESE;
/**
 * @see {@link SWT#CURSOR_SIZES}
 */
public static final Cursor SIZES;
/**
 * @see {@link SWT#CURSOR_SIZESW}
 */
public static final Cursor SIZESW;
/**
 * @see {@link SWT#CURSOR_SIZEW}
 */
public static final Cursor SIZEW;
/**
 * @see {@link SWT#CURSOR_SIZENW}
 */
public static final Cursor SIZENW;
/**
 * @see {@link SWT#CURSOR_APPSTARTING}
 */
public static final Cursor APPSTARTING;
/**
 * @see {@link SWT#CURSOR_CROSS}
 */
public static final Cursor CROSS;
/**
 * @see {@link SWT#CURSOR_HAND}
 */
public static final Cursor HAND;
/**
 * @see {@link SWT#CURSOR_HELP}
 */
public static final Cursor HELP;
/**
 * @see {@link SWT#CURSOR_IBEAM}
 */
public static final Cursor IBEAM;
/**
 * @see {@link SWT#CURSOR_NO}
 */
public static final Cursor NO;
/**
 * @see {@link SWT#CURSOR_SIZEALL}
 */
public static final Cursor SIZEALL;
/**
 * @see {@link SWT#CURSOR_SIZENESW}
 */
public static final Cursor SIZENESW;
/**
 * @see {@link SWT#CURSOR_SIZENWSE}
 */
public static final Cursor SIZENWSE;
/**
 * @see {@link SWT#CURSOR_UPARROW}
 */
public static final Cursor UPARROW;
/**
 * @see {@link SWT#CURSOR_WAIT}
 */
public static final Cursor WAIT;

static {
	ARROW   = new Cursor(null, SWT.CURSOR_ARROW);
	SIZEN   = new Cursor(null, SWT.CURSOR_SIZEN);
	SIZENE  = new Cursor(null, SWT.CURSOR_SIZENE);
	SIZEE   = new Cursor(null, SWT.CURSOR_SIZEE);
	SIZESE  = new Cursor(null, SWT.CURSOR_SIZESE);
	SIZES   = new Cursor(null, SWT.CURSOR_SIZES);
	SIZESW  = new Cursor(null, SWT.CURSOR_SIZESW);
	SIZEW   = new Cursor(null, SWT.CURSOR_SIZEW);
	SIZENW  = new Cursor(null, SWT.CURSOR_SIZENW);
	APPSTARTING	= new Cursor(null, SWT.CURSOR_APPSTARTING);
	CROSS   = new Cursor(null, SWT.CURSOR_CROSS);
	HAND    = new Cursor(null, SWT.CURSOR_HAND);
	HELP    = new Cursor(null, SWT.CURSOR_HELP);
	IBEAM   = new Cursor(null, SWT.CURSOR_IBEAM);
	NO      = new Cursor(null, SWT.CURSOR_NO);
	SIZEALL = new Cursor(null, SWT.CURSOR_SIZEALL);
	SIZENESW = new Cursor(null, SWT.CURSOR_SIZENESW);
	SIZENWSE = new Cursor(null, SWT.CURSOR_SIZENWSE);
	UPARROW  = new Cursor(null, SWT.CURSOR_UPARROW);
	WAIT     = new Cursor(null, SWT.CURSOR_WAIT);
} 

}