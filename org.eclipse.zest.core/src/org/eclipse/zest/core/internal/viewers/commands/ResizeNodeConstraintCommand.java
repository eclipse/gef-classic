/***********************************************************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 **********************************************************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.commands;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphNodeEditPart;

/**
 * A command to resize and/or move a node. The command can be undone or redone.
 * 
 * @author Chris Callendar
 */
public class ResizeNodeConstraintCommand extends Command {

	/** Stores the new size and location. */
	private final Rectangle newBounds;

	/** Stores the old size and location. */
	private Rectangle oldBounds;

	/** A request to move/resize an edit part. */
	private final ChangeBoundsRequest request;

	/** Node to manipulate. */
	private final IGraphModelNode node;

	/** The Edit part of the node */
	private final NestedGraphNodeEditPart editPart;

	private boolean enforceBounds;

	/**
	 * Create a command that can resize and/or move a node.
	 * 
	 * @param node
	 *            the node to mode/resize
	 * @param req
	 *            the move and resize request
	 * @param newBounds
	 *            the new size and location
	 * @throws IllegalArgumentException
	 *             if any of the parameters is null
	 */
	public ResizeNodeConstraintCommand(NestedGraphNodeEditPart editPart, IGraphModelNode node, ChangeBoundsRequest req,
			Rectangle newBounds) {
		if (node == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.editPart = editPart;
		this.node = node;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		this.enforceBounds = false;
		setLabel("Move / Resize");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		Object type = request.getType();
		if ( RequestConstants.REQ_RESIZE_CHILDREN.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type)) {
			IFigure parentFigure = editPart.getFigure().getParent();
			
			Rectangle  parentBounds = parentFigure.getBounds().getCopy();
			// @tag bug (152289) : Enforce bounds for resize in Nested Viewer
			// If the new bounds are outside the parent return false
			
			Rectangle myBounds = newBounds.getCopy();
			parentFigure.translateToParent(myBounds);
			if ( myBounds.x < parentBounds.x ||
				myBounds.y < parentBounds.y ||
				myBounds.width > ( parentBounds.width - myBounds.x) ||
				myBounds.height > ( parentBounds.height - myBounds.y )) return false;
			return true;
		}
		// make sure the Request is of a type we support:
		return (RequestConstants.REQ_MOVE.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type)
				|| RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN.equals(type));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		oldBounds = new Rectangle(node.getLocation(), node.getSize());
		redo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {

		node.setSizeInLayout(newBounds.getSize().width, newBounds.getSize().height);
		if (enforceBounds && false) {
			Rectangle rect = ((NestedGraphNodeEditPart)editPart.getParent()).getAbsoluteBounds();
			if ((newBounds.right() > rect.width)) {
				newBounds.x = rect.width - newBounds.width;
			}
			if ((newBounds.bottom() > rect.height)) {
				newBounds.y = rect.height - newBounds.height;
			}
			newBounds.x = Math.max(0, newBounds.x);
			newBounds.y = Math.max(0, newBounds.y);
		}

		// check if the new bounds overlaps any of the existing nodes

		// move the current editpart's figure to last in the list to put it on
		// top
		IFigure fig = editPart.getFigure();
		fig.getParent().getChildren().remove(fig);
		fig.getParent().getChildren().add(fig);

		node.setLocation(newBounds.getLocation().x, newBounds.getLocation().y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		node.setSizeInLayout(oldBounds.getSize().width, oldBounds.getSize().height);
		node.setLocation(oldBounds.getLocation().x, oldBounds.getLocation().y);
	}


	/**
	 * Sets if the bounds are enforced. If a node is moved outside the bounds it
	 * will be placed at the boundary.
	 * 
	 * @param enforceBounds
	 */
	public void setEnforceBounds(boolean enforceBounds) {
		this.enforceBounds = enforceBounds;
	}

}
