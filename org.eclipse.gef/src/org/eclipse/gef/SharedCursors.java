package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.internal.Internal;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;

/**
 * A shared collection of Cursors.
 * @since 2.0 */
public class SharedCursors
	extends Cursors
{

/**
 *  */
public static final Cursor CURSOR_PLUG;
/**
 *  */
public static final Cursor CURSOR_TREE_ADD;
/**
 *  */
public static final Cursor CURSOR_TREE_MOVE;

static {
	CURSOR_PLUG = new Cursor(null,
		new ImageData(
			Internal.class.getResourceAsStream("icons/plugmask.gif")), //$NON-NLS-1$
		new ImageData(
			Internal.class.getResourceAsStream("icons/plug.bmp")), //$NON-NLS-1$
		0, 0);

	CURSOR_TREE_ADD = new Cursor(null,
		new ImageData(Internal.class
			.getResourceAsStream("icons/Tree_Add_Mask.gif")), //$NON-NLS-1$
		new ImageData(Internal.class
			.getResourceAsStream("icons/Tree_Add.gif")), //$NON-NLS-1$
		0, 0);

	CURSOR_TREE_MOVE = new Cursor(null,
		new ImageData(Internal.class
			.getResourceAsStream("icons/Tree_Move_Mask.gif")), //$NON-NLS-1$
		new ImageData(Internal.class
			.getResourceAsStream("icons/Tree_Move.gif")), //$NON-NLS-1$
		0, 0);
}

}
