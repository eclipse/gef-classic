/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.internal.SystemCursorProvider;

/**
 * A collection of DPI-aware cursors.
 *
 * @since 3.17
 */
public class CursorProviders {

	/**
	 * Returns the cursor corresponding to the given direction, defined in
	 * {@link PositionConstants}. Note that
	 * {@link #getDirectionalCursor(int, boolean)} should be used for applications
	 * which want to run properly when running in a mirrored environment. The
	 * behavior is the same as calling {@link #getDirectionalCursor(int, boolean)
	 * getDirectionalCursor(direction, false)}.
	 *
	 * @param direction the relative direction of the desired cursor
	 * @return The appropriate directional cursor
	 */
	public static CursorProvider getDirectionalCursor(int direction) {
		return getDirectionalCursor(direction, false);
	}

	/**
	 * Returns the cursor corresponding to the given direction and mirroring. The
	 * direction must be one of:
	 * <UL>
	 * <LI>{@link PositionConstants#NORTH}
	 * <LI>{@link PositionConstants#SOUTH}
	 * <LI>{@link PositionConstants#EAST}
	 * <LI>{@link PositionConstants#WEST}
	 * <LI>{@link PositionConstants#NORTH_EAST}
	 * <LI>{@link PositionConstants#NORTH_WEST}
	 * <LI>{@link PositionConstants#SOUTH_EAST}
	 * <LI>{@link PositionConstants#SOUTH_WEST}
	 * </UL>
	 * <P>
	 * The behavior is undefined for other values. If <code>isMirrored</code> is set
	 * to <code>true</code>, EAST and WEST will be inverted.
	 *
	 * @param direction  the relative direction of the desired cursor
	 * @param isMirrored <code>true</code> if EAST and WEST should be inverted
	 * @return The appropriate directional cursor
	 */
	public static CursorProvider getDirectionalCursor(int direction, boolean isMirrored) {
		if (isMirrored && (direction & PositionConstants.EAST_WEST) != 0) {
			direction = direction ^ PositionConstants.EAST_WEST;
		}
		switch (direction) {
		case PositionConstants.NORTH:
			return SIZEN;
		case PositionConstants.SOUTH:
			return SIZES;
		case PositionConstants.EAST:
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
	 * @see SWT#CURSOR_ARROW
	 */
	public static final CursorProvider ARROW;
	/**
	 * @see SWT#CURSOR_SIZEN
	 */
	public static final CursorProvider SIZEN;
	/**
	 * @see SWT#CURSOR_SIZENE
	 */
	public static final CursorProvider SIZENE;
	/**
	 * @see SWT#CURSOR_SIZEE
	 */
	public static final CursorProvider SIZEE;
	/**
	 * @see SWT#CURSOR_SIZESE
	 */
	public static final CursorProvider SIZESE;
	/**
	 * @see SWT#CURSOR_SIZES
	 */
	public static final CursorProvider SIZES;
	/**
	 * @see SWT#CURSOR_SIZESW
	 */
	public static final CursorProvider SIZESW;
	/**
	 * @see SWT#CURSOR_SIZEW
	 */
	public static final CursorProvider SIZEW;
	/**
	 * @see SWT#CURSOR_SIZENW
	 */
	public static final CursorProvider SIZENW;
	/**
	 * @see SWT#CURSOR_APPSTARTING
	 */
	public static final CursorProvider APPSTARTING;
	/**
	 * @see SWT#CURSOR_CROSS
	 */
	public static final CursorProvider CROSS;
	/**
	 * @see SWT#CURSOR_HAND
	 */
	public static final CursorProvider HAND;
	/**
	 * @see SWT#CURSOR_HELP
	 */
	public static final CursorProvider HELP;
	/**
	 * @see SWT#CURSOR_IBEAM
	 */
	public static final CursorProvider IBEAM;
	/**
	 * @see SWT#CURSOR_NO
	 */
	public static final CursorProvider NO;
	/**
	 * @see SWT#CURSOR_SIZEALL
	 */
	public static final CursorProvider SIZEALL;
	/**
	 * @see SWT#CURSOR_SIZENESW
	 */
	public static final CursorProvider SIZENESW;
	/**
	 * @see SWT#CURSOR_SIZENWSE
	 */
	public static final CursorProvider SIZENWSE;
	/**
	 * @see SWT#CURSOR_SIZEWE
	 */
	public static final CursorProvider SIZEWE;
	/**
	 * @see SWT#CURSOR_SIZENS
	 */
	public static final CursorProvider SIZENS;
	/**
	 * @see SWT#CURSOR_UPARROW
	 */
	public static final CursorProvider UPARROW;
	/**
	 * @see SWT#CURSOR_WAIT
	 */
	public static final CursorProvider WAIT;

	static {
		ARROW = new SystemCursorProvider(SWT.CURSOR_ARROW);
		SIZEN = new SystemCursorProvider(SWT.CURSOR_SIZEN);
		SIZENE = new SystemCursorProvider(SWT.CURSOR_SIZENE);
		SIZEE = new SystemCursorProvider(SWT.CURSOR_SIZEE);
		SIZESE = new SystemCursorProvider(SWT.CURSOR_SIZESE);
		SIZES = new SystemCursorProvider(SWT.CURSOR_SIZES);
		SIZESW = new SystemCursorProvider(SWT.CURSOR_SIZESW);
		SIZEW = new SystemCursorProvider(SWT.CURSOR_SIZEW);
		SIZENW = new SystemCursorProvider(SWT.CURSOR_SIZENW);
		SIZENS = new SystemCursorProvider(SWT.CURSOR_SIZENS);
		SIZEWE = new SystemCursorProvider(SWT.CURSOR_SIZEWE);
		APPSTARTING = new SystemCursorProvider(SWT.CURSOR_APPSTARTING);
		CROSS = new SystemCursorProvider(SWT.CURSOR_CROSS);
		HAND = new SystemCursorProvider(SWT.CURSOR_HAND);
		HELP = new SystemCursorProvider(SWT.CURSOR_HELP);
		IBEAM = new SystemCursorProvider(SWT.CURSOR_IBEAM);
		NO = new SystemCursorProvider(SWT.CURSOR_NO);
		SIZEALL = new SystemCursorProvider(SWT.CURSOR_SIZEALL);
		SIZENESW = new SystemCursorProvider(SWT.CURSOR_SIZENESW);
		SIZENWSE = new SystemCursorProvider(SWT.CURSOR_SIZENWSE);
		UPARROW = new SystemCursorProvider(SWT.CURSOR_UPARROW);
		WAIT = new SystemCursorProvider(SWT.CURSOR_WAIT);
	}

}
