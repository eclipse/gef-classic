/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * Provides support for selecting, positioning, and resizing an edit part.
 * Selection is indicated via eight square handles along the editpart's figure,
 * and a rectangular handle that outlines the edit part with a 1-pixel black
 * line. The eight square handles will resize the current selection in the eight
 * primary directions. The rectangular handle will drag the current selection
 * using a {@link org.eclipse.gef.tools.DragEditPartsTracker}.
 * <p>
 * During feedback, a rectangle filled using XOR and outlined with dashes is
 * drawn. Subclasses may tailor the feedback.
 * 
 * @author hudsonr
 * @author msorens
 * @author anyssen
 */
public class ResizableEditPolicy extends NonResizableEditPolicy {

	private int resizeDirections = PositionConstants.NSEW;
	private Dimension defaultMaximumSize = IFigure.MAX_DIMENSION;
	private Dimension defaultMinimumSize = IFigure.MIN_DIMENSION;

	/**
	 * Constructs a new {@link ResizableEditPolicy}.
	 * 
	 * @since 3.7
	 */
	public ResizableEditPolicy() {
	}

	/**
	 * Constructs a new {@link ResizableEditPolicy} with the given default
	 * resizing constraints.
	 * 
	 * @param defaultMinimumSize
	 *            default minimum size, as used by
	 *            {@link #getMinimumSizeFor(ChangeBoundsRequest)}
	 * @param defaultMaximumSize
	 *            default maximum size, as used by
	 *            {@link #getMaximumSizeFor(ChangeBoundsRequest)}.
	 * 
	 * @since 3.7
	 */
	public ResizableEditPolicy(Dimension defaultMinimumSize,
			Dimension defaultMaximumSize) {
		super();
		this.defaultMinimumSize = defaultMinimumSize;
		this.defaultMaximumSize = defaultMaximumSize;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		if (resizeDirections == PositionConstants.NONE) {
			// non resizable, so delegate to super implementation
			return super.createSelectionHandles();
		}

		// resizable in at least one direction
		List list = new ArrayList();
		createMoveHandle(list);
		createResizeHandle(list, PositionConstants.NORTH);
		createResizeHandle(list, PositionConstants.EAST);
		createResizeHandle(list, PositionConstants.SOUTH);
		createResizeHandle(list, PositionConstants.WEST);
		createResizeHandle(list, PositionConstants.SOUTH_EAST);
		createResizeHandle(list, PositionConstants.SOUTH_WEST);
		createResizeHandle(list, PositionConstants.NORTH_WEST);
		createResizeHandle(list, PositionConstants.NORTH_EAST);
		return list;
	}

	/**
	 * Creates a 'resize' handle, which uses a {@link ResizeTracker} in case
	 * resizing is allowed in the respective direction, otherwise returns a drag
	 * handle by delegating to
	 * {@link NonResizableEditPolicy#createDragHandle(List, int)}.
	 * 
	 * @param handles
	 *            The list of handles to add the resize handle to
	 * @param direction
	 *            A position constant indicating the direction to create the
	 *            handle for
	 * @since 3.7
	 */
	protected void createResizeHandle(List handles, int direction) {
		if ((resizeDirections & direction) == direction) {
			// display 'resize' handle to allow resizing (resize tracker)
			ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
					handles, direction);
		} else {
			// display 'resize' handle to allow dragging or indicate selection
			// only
			createDragHandle(handles, direction);
		}
	}

	/**
	 * Dispatches erase requests to more specific methods.
	 * 
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	public void eraseSourceFeedback(Request request) {
		if (REQ_RESIZE.equals(request.getType()))
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
		else
			super.eraseSourceFeedback(request);
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (REQ_RESIZE.equals(request.getType())) {
			adjustRequest((ChangeBoundsRequest) request);
			return getResizeCommand((ChangeBoundsRequest) request);
		}

		return super.getCommand(request);
	}

	/**
	 * Ensure size constraints (by default minimum and maximum) are respected by
	 * the given request. May be overwritten by clients to enforce additional
	 * constraints.
	 * 
	 * @param request
	 *            The request to validate
	 * @since 3.7
	 */
	protected void adjustRequest(ChangeBoundsRequest request) {
		// adjust request, so that minimum and maximum size constraints are
		// respected
		PrecisionRectangle originalConstraint = new PrecisionRectangle(
				((GraphicalEditPart) getHost()).getFigure().getBounds());
		getHostFigure().translateToAbsolute(originalConstraint);
		PrecisionRectangle manipulatedConstraint = new PrecisionRectangle(
				request.getTransformedRectangle(originalConstraint));
		getHostFigure().translateToRelative(manipulatedConstraint);
		// validate constraint (maximum and minimum size are regarded to be
		// 'normalized', i.e. relative to this figure's bounds coordinates).
		manipulatedConstraint.setSize(Dimension.max(
				manipulatedConstraint.getSize(), getMinimumSizeFor(request)));
		manipulatedConstraint.setSize(Dimension.min(
				manipulatedConstraint.getSize(), getMaximumSizeFor(request)));
		// translate back to absolute
		getHostFigure().translateToAbsolute(manipulatedConstraint);
		Dimension newSizeDelta = manipulatedConstraint.getSize().getShrinked(
				originalConstraint.getSize());
		request.setSizeDelta(newSizeDelta);
	}

	/**
	 * Returns the command contribution for the given resize request. By
	 * default, the request is re-dispatched to the host's parent as a
	 * {@link org.eclipse.gef.RequestConstants#REQ_RESIZE_CHILDREN}. The
	 * parent's edit policies determine how to perform the resize based on the
	 * layout manager in use.
	 * 
	 * @param request
	 *            the resize request
	 * @return the command contribution obtained from the parent
	 */
	protected Command getResizeCommand(ChangeBoundsRequest request) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
		req.setEditParts(getHost());

		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setExtendedData(request.getExtendedData());
		req.setResizeDirection(request.getResizeDirection());
		return getHost().getParent().getCommand(req);
	}

	/**
	 * Sets the directions in which handles should allow resizing. Valid values
	 * are bit-wise combinations of:
	 * <UL>
	 * <LI>{@link PositionConstants#NORTH}
	 * <LI>{@link PositionConstants#SOUTH}
	 * <LI>{@link PositionConstants#EAST}
	 * <LI>{@link PositionConstants#WEST}
	 * </UL>
	 * 
	 * @param newDirections
	 *            the direction in which resizing is allowed
	 */
	public void setResizeDirections(int newDirections) {
		resizeDirections = newDirections;
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
	 */
	public void showSourceFeedback(Request request) {
		if (REQ_RESIZE.equals(request.getType())) {
			adjustRequest((ChangeBoundsRequest) request);
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
		} else {
			super.showSourceFeedback(request);
		}
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
	 */
	public boolean understandsRequest(Request request) {
		if (REQ_RESIZE.equals(request.getType()))
			return true;
		return super.understandsRequest(request);
	}

	/**
	 * Returns the directions in which resizing should be allowed
	 * 
	 * Valid values are bit-wise combinations of:
	 * <UL>
	 * <LI>{@link PositionConstants#NORTH}
	 * <LI>{@link PositionConstants#SOUTH}
	 * <LI>{@link PositionConstants#EAST}
	 * <LI>{@link PositionConstants#WEST}
	 * </UL>
	 * or {@link PositionConstants#NONE}.
	 * 
	 */
	public int getResizeDirections() {
		return resizeDirections;
	}

	/**
	 * Determines the <em>maximum</em> size that the host can be resized to for
	 * a given request. It is called from
	 * {@link #adjustRequest(ChangeBoundsRequest)} during resizing. By default,
	 * a default value is returned. The value is interpreted to be a dimension
	 * in the host figure's coordinate system (i.e. relative to its bounds), so
	 * it is not affected by zooming affects.
	 * 
	 * @param request
	 *            the ChangeBoundsRequest
	 * @return the minimum size
	 * @since 3.7
	 */
	protected Dimension getMaximumSizeFor(ChangeBoundsRequest request) {
		return defaultMaximumSize;
	}

	/**
	 * Determines the <em>minimum</em> size that the specified child can be
	 * resized to. It is called by {@link #adjustRequest(ChangeBoundsRequest)}
	 * during resizing. By default, a default value is returned. The value is
	 * interpreted to be a dimension in the host figure's coordinate system
	 * (i.e. relative to its bounds), so it is not affected by zooming effects.
	 * 
	 * @param request
	 *            the ChangeBoundsRequest
	 * @return the minimum size
	 * @since 3.7
	 */
	protected Dimension getMinimumSizeFor(ChangeBoundsRequest request) {
		return defaultMinimumSize;
	}
}
