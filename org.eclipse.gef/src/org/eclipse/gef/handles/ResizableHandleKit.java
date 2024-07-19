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
package org.eclipse.gef.handles;

import java.util.List;

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.CursorProvider;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.internal.WrappedCursorProvider;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;

/**
 * A set of utility methods to create Handles for the common locations on a
 * figure's bounds.
 *
 * @see Handle
 */
public class ResizableHandleKit {

	/**
	 * Adds a single handle in the given direction to the given List.
	 *
	 * @param part      the owner GraphicalEditPart of the handle
	 * @param handles   the List to add the handle to
	 * @param direction the integer constant from PositionConstants that refers to
	 *                  the handle direction
	 */
	public static void addHandle(GraphicalEditPart part, List handles, int direction) {
		handles.add(createHandle(part, direction));
	}

	/**
	 * Adds a single handle in the given direction to the given List with the given
	 * DragTracker
	 *
	 * @param tracker   the DragTracker to assign to this handle
	 * @param part      the owner GraphicalEditPart of the handle
	 * @param handles   the List to add the handle to
	 * @param direction the integer constant from PositionConstants that refers to
	 *                  the handle direction
	 * @param cursor    the Cursor to use when hovering over this handle
	 * @deprecated Use
	 *             {@link #addHandle(GraphicalEditPart, List, int, DragTracker, CursorProvider)}
	 *             instead.
	 */
	@Deprecated(since = "3.19", forRemoval = true)
	public static void addHandle(GraphicalEditPart part, List handles, int direction, DragTracker tracker,
			Cursor cursor) {
		addHandle(part, handles, direction, tracker, new WrappedCursorProvider(cursor));
	}

	/**
	 * Adds a single handle in the given direction to the given List with the given
	 * DragTracker
	 *
	 * @param tracker   the DragTracker to assign to this handle
	 * @param part      the owner GraphicalEditPart of the handle
	 * @param handles   the List to add the handle to
	 * @param direction the integer constant from PositionConstants that refers to
	 *                  the handle direction
	 * @param cursor    the Cursor to use when hovering over this handle
	 * @since 3.19
	 */
	public static void addHandle(GraphicalEditPart part, List handles, int direction, DragTracker tracker,
			CursorProvider cursor) {
		handles.add(createHandle(part, direction, tracker, cursor));
	}

	/**
	 * Fills the given List with handles at each corner and the north, south, east,
	 * and west of the GraphicalEditPart.
	 *
	 * @param part    the owner GraphicalEditPart of the handles
	 * @param handles the List to add the handles to
	 * @deprecated
	 */
	@Deprecated
	public static void addHandles(GraphicalEditPart part, List handles) {
		addMoveHandle(part, handles);
		addCornerAndSideHandles(part, handles);
	}

	/**
	 * Fills the given List with handles at each corner and side of a figure.
	 *
	 * @param part    the handles' GraphicalEditPart
	 * @param handles the List to add the four corner handles to
	 * @since 3.7
	 */
	public static void addCornerAndSideHandles(GraphicalEditPart part, List handles) {
		handles.add(createHandle(part, PositionConstants.EAST));
		handles.add(createHandle(part, PositionConstants.SOUTH_EAST));
		handles.add(createHandle(part, PositionConstants.SOUTH));
		handles.add(createHandle(part, PositionConstants.SOUTH_WEST));
		handles.add(createHandle(part, PositionConstants.WEST));
		handles.add(createHandle(part, PositionConstants.NORTH_WEST));
		handles.add(createHandle(part, PositionConstants.NORTH));
		handles.add(createHandle(part, PositionConstants.NORTH_EAST));
	}

	/**
	 * Fills the given List with handles at each corner and side of a figure.
	 *
	 * @param part    the handles' GraphicalEditPart
	 * @param handles the List to add the four corner handles to
	 * @param tracker the handles' DragTracker
	 * @param cursor  the handles' Cursor
	 * @since 3.7
	 * @deprecated Use
	 *             {@link #addCornerAndSideHandles(GraphicalEditPart, List, DragTracker, CursorProvider)}
	 *             instead.
	 */
	@Deprecated(since = "3.19", forRemoval = true)
	public static void addCornerAndSideHandles(GraphicalEditPart part, List handles, DragTracker tracker,
			Cursor cursor) {
		addCornerAndSideHandles(part, handles, tracker, new WrappedCursorProvider(cursor));
	}

	/**
	 * Fills the given List with handles at each corner and side of a figure.
	 *
	 * @param part    the handles' GraphicalEditPart
	 * @param handles the List to add the four corner handles to
	 * @param tracker the handles' DragTracker
	 * @param cursor  the handles' Cursor
	 * @since 3.19
	 */
	public static void addCornerAndSideHandles(GraphicalEditPart part, List handles, DragTracker tracker,
			CursorProvider cursor) {
		handles.add(createHandle(part, PositionConstants.EAST, tracker, cursor));
		handles.add(createHandle(part, PositionConstants.SOUTH_EAST, tracker, cursor));
		handles.add(createHandle(part, PositionConstants.SOUTH, tracker, cursor));
		handles.add(createHandle(part, PositionConstants.SOUTH_WEST, tracker, cursor));
		handles.add(createHandle(part, PositionConstants.WEST, tracker, cursor));
		handles.add(createHandle(part, PositionConstants.NORTH_WEST, tracker, cursor));
		handles.add(createHandle(part, PositionConstants.NORTH, tracker, cursor));
		handles.add(createHandle(part, PositionConstants.NORTH_EAST, tracker, cursor));
	}

	/**
	 * Fills the given List with move borders at each side of a figure.
	 *
	 * @param f       the GraphicalEditPart that is the owner of the handles
	 * @param handles the List to add the handles to
	 */
	public static void addMoveHandle(GraphicalEditPart f, List handles) {
		handles.add(moveHandle(f));
	}

	/**
	 * Fills the given List with move borders with the given DragTracker at each
	 * side of a figure.
	 *
	 * @param tracker the DragTracker to assign to this handle
	 * @param f       the GraphicalEditPart thatis the owner of the handles
	 * @param handles the List to add the handles to
	 * @param cursor  the Cursor to use when hovering over this handle
	 * @deprecated Use
	 *             {@link #addMoveHandle(GraphicalEditPart, List, DragTracker, CursorProvider)}
	 *             instead.
	 */
	@Deprecated(since = "3.19", forRemoval = true)
	public static void addMoveHandle(GraphicalEditPart f, List handles, DragTracker tracker, Cursor cursor) {
		addMoveHandle(f, handles, tracker, new WrappedCursorProvider(cursor));
	}

	/**
	 * Fills the given List with move borders with the given DragTracker at each
	 * side of a figure.
	 *
	 * @param tracker the DragTracker to assign to this handle
	 * @param f       the GraphicalEditPart thatis the owner of the handles
	 * @param handles the List to add the handles to
	 * @param cursor  the Cursor to use when hovering over this handle
	 * @since 3.19
	 */
	public static void addMoveHandle(GraphicalEditPart f, List handles, DragTracker tracker, CursorProvider cursor) {
		handles.add(moveHandle(f, tracker, cursor));
	}

	static Handle createHandle(GraphicalEditPart owner, int direction) {
		return new ResizeHandle(owner, direction);
	}

	static Handle createHandle(GraphicalEditPart owner, int direction, DragTracker tracker, CursorProvider cursor) {
		ResizeHandle handle = new ResizeHandle(owner, direction);
		handle.setDragTracker(tracker);
		handle.setCursorProvider(cursor);
		return handle;
	}

	/**
	 * Returns a new {@link MoveHandle} with the given owner.
	 *
	 * @param owner the GraphicalEditPart that is the owner of the new MoveHandle
	 * @return the new MoveHandle
	 */
	public static Handle moveHandle(GraphicalEditPart owner) {
		return new MoveHandle(owner);
	}

	/**
	 * Returns a new {@link MoveHandle} with the given owner and DragTracker.
	 *
	 * @param tracker the DragTracker to assign to this handle
	 * @param owner   the GraphicalEditPart that is the owner of the new MoveHandle
	 * @return the new MoveHandle
	 * @param cursor the Cursor to use when hovering over this handle
	 * @deprecated Use
	 *             {@link #moveHandle(GraphicalEditPart, DragTracker, CursorProvider)}
	 *             instead.
	 */
	@Deprecated(since = "3.17", forRemoval = true)
	public static Handle moveHandle(GraphicalEditPart owner, DragTracker tracker, Cursor cursor) {
		return moveHandle(owner, tracker, new WrappedCursorProvider(cursor));
	}

	/**
	 * Returns a new {@link MoveHandle} with the given owner and DragTracker.
	 *
	 * @param tracker the DragTracker to assign to this handle
	 * @param owner   the GraphicalEditPart that is the owner of the new MoveHandle
	 * @return the new MoveHandle
	 * @param cursor the Cursor to use when hovering over this handle
	 * @since 3.19
	 */
	public static Handle moveHandle(GraphicalEditPart owner, DragTracker tracker, CursorProvider cursor) {
		MoveHandle moveHandle = new MoveHandle(owner);
		moveHandle.setDragTracker(tracker);
		moveHandle.setCursorProvider(cursor);
		return moveHandle;
	}

}
