/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of cursors.
 */
public class Cursors {

	/**
	 * This utility class is used to create a system {@link Cursor} using any of the
	 * supported {@link SWT} constants. The {@link Cursor} is created using the
	 * {@link Display} bound to the current thread.
	 *
	 * @since 3.14
	 */
	private static class SystemCursorFactory {
		/**
		 * Returns the matching standard platform {@link Cursor} for the given constant,
		 * which should be one of the {@link Cursor} constants specified in class
		 * {@link SWT}. This {@link Cursor} should not be free'd because it was
		 * allocated by the system, not the application. A value of {@code null} will be
		 * returned if the supplied constant is not an {@link SWT} cursor constant.
		 * Note: This method should be invoked from within the UI thread if possible, as
		 * it will attempt to create a new Display instance, if not!
		 *
		 * @since 3.14
		 * @param which the {@link SWT} {@link Cursor} constant
		 * @return the corresponding {@link Cursor} or {@code null}
		 */
		public static Cursor getCursor(final int which) {
			Display display = Display.getCurrent();
			if (display != null) {
				return display.getSystemCursor(which);
			}
			display = Display.getDefault();
			return display.syncCall(() -> Display.getCurrent().getSystemCursor(which));
		}
	}

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
	public static Cursor getDirectionalCursor(int direction) {
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
	public static Cursor getDirectionalCursor(int direction, boolean isMirrored) {
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
	public static final Cursor ARROW;
	/**
	 * @see SWT#CURSOR_SIZEN
	 */
	public static final Cursor SIZEN;
	/**
	 * @see SWT#CURSOR_SIZENE
	 */
	public static final Cursor SIZENE;
	/**
	 * @see SWT#CURSOR_SIZEE
	 */
	public static final Cursor SIZEE;
	/**
	 * @see SWT#CURSOR_SIZESE
	 */
	public static final Cursor SIZESE;
	/**
	 * @see SWT#CURSOR_SIZES
	 */
	public static final Cursor SIZES;
	/**
	 * @see SWT#CURSOR_SIZESW
	 */
	public static final Cursor SIZESW;
	/**
	 * @see SWT#CURSOR_SIZEW
	 */
	public static final Cursor SIZEW;
	/**
	 * @see SWT#CURSOR_SIZENW
	 */
	public static final Cursor SIZENW;
	/**
	 * @see SWT#CURSOR_APPSTARTING
	 */
	public static final Cursor APPSTARTING;
	/**
	 * @see SWT#CURSOR_CROSS
	 */
	public static final Cursor CROSS;
	/**
	 * @see SWT#CURSOR_HAND
	 */
	public static final Cursor HAND;
	/**
	 * @see SWT#CURSOR_HELP
	 */
	public static final Cursor HELP;
	/**
	 * @see SWT#CURSOR_IBEAM
	 */
	public static final Cursor IBEAM;
	/**
	 * @see SWT#CURSOR_NO
	 */
	public static final Cursor NO;
	/**
	 * @see SWT#CURSOR_SIZEALL
	 */
	public static final Cursor SIZEALL;
	/**
	 * @see SWT#CURSOR_SIZENESW
	 */
	public static final Cursor SIZENESW;
	/**
	 * @see SWT#CURSOR_SIZENWSE
	 */
	public static final Cursor SIZENWSE;
	/**
	 * @see SWT#CURSOR_SIZEWE
	 */
	public static final Cursor SIZEWE;
	/**
	 * @see SWT#CURSOR_SIZENS
	 */
	public static final Cursor SIZENS;
	/**
	 * @see SWT#CURSOR_UPARROW
	 */
	public static final Cursor UPARROW;
	/**
	 * @see SWT#CURSOR_WAIT
	 */
	public static final Cursor WAIT;

	static {
		ARROW = SystemCursorFactory.getCursor(SWT.CURSOR_ARROW);
		SIZEN = SystemCursorFactory.getCursor(SWT.CURSOR_SIZEN);
		SIZENE = SystemCursorFactory.getCursor(SWT.CURSOR_SIZENE);
		SIZEE = SystemCursorFactory.getCursor(SWT.CURSOR_SIZEE);
		SIZESE = SystemCursorFactory.getCursor(SWT.CURSOR_SIZESE);
		SIZES = SystemCursorFactory.getCursor(SWT.CURSOR_SIZES);
		SIZESW = SystemCursorFactory.getCursor(SWT.CURSOR_SIZESW);
		SIZEW = SystemCursorFactory.getCursor(SWT.CURSOR_SIZEW);
		SIZENW = SystemCursorFactory.getCursor(SWT.CURSOR_SIZENW);
		SIZENS = SystemCursorFactory.getCursor(SWT.CURSOR_SIZENS);
		SIZEWE = SystemCursorFactory.getCursor(SWT.CURSOR_SIZEWE);
		APPSTARTING = SystemCursorFactory.getCursor(SWT.CURSOR_APPSTARTING);
		CROSS = SystemCursorFactory.getCursor(SWT.CURSOR_CROSS);
		HAND = SystemCursorFactory.getCursor(SWT.CURSOR_HAND);
		HELP = SystemCursorFactory.getCursor(SWT.CURSOR_HELP);
		IBEAM = SystemCursorFactory.getCursor(SWT.CURSOR_IBEAM);
		NO = SystemCursorFactory.getCursor(SWT.CURSOR_NO);
		SIZEALL = SystemCursorFactory.getCursor(SWT.CURSOR_SIZEALL);
		SIZENESW = SystemCursorFactory.getCursor(SWT.CURSOR_SIZENESW);
		SIZENWSE = SystemCursorFactory.getCursor(SWT.CURSOR_SIZENWSE);
		UPARROW = SystemCursorFactory.getCursor(SWT.CURSOR_UPARROW);
		WAIT = SystemCursorFactory.getCursor(SWT.CURSOR_WAIT);
	}

}
