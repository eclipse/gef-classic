/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;


/**
 * Supports moving of GraphModelNode objects (no resizing).
 * 
 * @author Chris Callendar
 */
public class NoResizeNodeConstraintCommand extends Command {

	/** Stores the old size and location. */
	//private Rectangle oldBounds;
	
	/** A request to move/resize an edit part. */
	private final ChangeBoundsRequest request;
	
	/**
	 * GraphSetConstraintCommand constructor.
	 */
	public NoResizeNodeConstraintCommand(IGraphModelNode node, ChangeBoundsRequest request, Rectangle newBounds) {
		if (node == null || request == null || newBounds == null) {
			throw new IllegalArgumentException("Couldn't create a GraphSetConstraint with those parameters");
		}
		this.request = request;
		this.setLabel("Move");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		Object type = request.getType();
		// make sure the Request is of a type we support:

		return (RequestConstants.REQ_MOVE.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
//		oldBounds = new Rectangle(node.getLocation(), node.getSize());
//		redo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
//		node.setLocationInLayout(newBounds.x, newBounds.y);
//		node.setSizeInLayout(newBounds.width, newBounds.height);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
//		node.setLocationInLayout(oldBounds.x, oldBounds.y);
//		node.setSizeInLayout(oldBounds.width, oldBounds.height);
	}
	
}
