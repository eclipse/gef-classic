/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.draw2d.Cursors;

import org.eclipse.gef.internal.Internal;

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
 * 
 */
public static final Cursor CURSOR_PLUG_NOT;
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
	CURSOR_PLUG_NOT = new Cursor(null,
		new ImageData(
			Internal.class.getResourceAsStream("icons/plugmasknot.gif")), //$NON-NLS-1$
		new ImageData(
			Internal.class.getResourceAsStream("icons/plugnot.bmp")), //$NON-NLS-1$
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
