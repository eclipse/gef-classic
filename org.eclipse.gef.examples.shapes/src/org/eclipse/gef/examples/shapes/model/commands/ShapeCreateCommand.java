/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *    IBM Corporation
 *******************************************************************************/
package org.eclipse.gef.examples.shapes.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.examples.shapes.model.Shape;
import org.eclipse.gef.examples.shapes.model.ShapesDiagram;


/**
 * A command to add a Shape to a ShapeDiagram.
 * The command can be undone or redone.
 * @author Elias Volanakis
 */
public class ShapeCreateCommand extends Command {
/** The new shape. */ 
private Shape newShape;

/** ShapeDiagram to add to. */
private final ShapesDiagram parent;
/** A request to create a new Shape. */
private final CreateRequest request;
/** True, if newShape was added to parent. */
private boolean shapeAdded;

/**
 * Create a command that will add a new Shape to a ShapesDiagram.
 * @param parent the ShapesDiagram that will hold the new element
 * @param req     a request to create a new Shape
 * @throws IllegalArgumentException if any parameter is null, or the request
 * 						  does not provide a new Shape instance
 */
public ShapeCreateCommand(ShapesDiagram parent, CreateRequest req) {
	if (parent == null || req == null || !(req.getNewObject() instanceof Shape)) {
		throw new IllegalArgumentException();
	}
	this.parent = parent;
	this.request = req;
	setLabel("shape creation");
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#canUndo()
 */
public boolean canUndo() {
	return shapeAdded;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	// Obtain the new Shape instance from the request.
	// This causes the factory stored in the request to create a new instance.
	// The factory is supplied in the palette-tool-entry, see
	// ShapesEditorPaletteFactory#createComponentsGroup()
	newShape = (Shape) request.getNewObject();
	// Get desired location and size from the request
	newShape.setSize(request.getSize()); // might be null!
	newShape.setLocation(request.getLocation());
	redo();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#redo()
 */
public void redo() {
	shapeAdded = parent.addChild(newShape);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	parent.removeChild(newShape);
}
	
}
