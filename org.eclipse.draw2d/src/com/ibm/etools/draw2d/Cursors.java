package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * A collection of cursors.
 */
public class Cursors {

/**
 * Returns the cursor corresponding to the given direction, defined
 * in {@link PositionConstants}.
 *
public static Cursor checkoutDirectionalCursor(int direction, Object requestor){
	return getSharedDirectionalCursor(direction).checkout(requestor);
}*/

public static Cursor getDirectionalCursor(int direction){
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

public static final Cursor
	ARROW, SIZEN, SIZENE, SIZEE, SIZESE, SIZES,
	SIZESW, SIZEW, SIZENW, APPSTARTING, CROSS,
	HAND, HELP, IBEAM, NO, SIZEALL, SIZENESW,
	SIZENWSE, UPARROW, WAIT;

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