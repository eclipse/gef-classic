/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.tools.MarqueeDragTracker;

/**
 * Provides support for representation of any other graphical
 * EditPart. It contains Layers which are used to represent
 * specific type of visual information. The Layers are
 * (1) Primary - Used to hold the main EditPart's Figures.
 * (2) Connection - Used to hold the connections between EditParts.
 * (3) Handle - Takes care of holding handles for EditParts.
 * (4) Feedback - Shows feedback information for the EditParts.
 * 
 */
public class FreeformGraphicalRootEditPart
	extends AbstractGraphicalEditPart
	implements RootEditPart, LayerConstants, LayerManager
{

protected EditPart contents;
protected EditPartViewer viewer;
private LayeredPane innerLayers;
private LayeredPane printableLayers;

protected void createEditPolicies(){}

protected IFigure createFigure() {
	FreeformViewport viewport = new FreeformViewport();
	innerLayers = new FreeformLayeredPane();
	createLayers(innerLayers);
	viewport.setContents(innerLayers);
	return viewport;
}

protected void createLayers(LayeredPane layeredPane) {
	layeredPane.add(getPrintableLayers(), PRINTABLE_LAYERS);
	layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
	layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
}

protected LayeredPane createPrintableLayers() {
	FreeformLayeredPane layeredPane = new FreeformLayeredPane();
	layeredPane.add(new FreeformLayer(), PRIMARY_LAYER);
	layeredPane.add(new ConnectionLayer(), CONNECTION_LAYER);
	return layeredPane;
}

/**
 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
 */
public Object getAdapter(Class adapter) {
	if (adapter == AutoexposeHelper.class)
		return new ViewportAutoexposeHelper(this);
	return super.getAdapter(adapter);
}

/** 
 * Doesnt provide any command support, returns an
 * un-executable command
 */
public Command getCommand(Request req){
	return UnexecutableCommand.INSTANCE;
}

/**
 * Returns the figure to which childrens' figures will be added.
 * An example would be a ScrollPane.  Figures of child editpart are not
 * added to the ScrollPane, but to its ViewPort's View.
 */
public IFigure getContentPane(){
	return getLayer(PRIMARY_LAYER);
}

public EditPart getContents(){
	return contents;
}

/**
 * Return a drag tracker suitable for dragging this.
 */
public DragTracker getDragTracker(Request req) {
	// The drawing cannot be dragged.
	return new MarqueeDragTracker();
}

/**
 * Returns the layer for the given key
 */
public IFigure getLayer(Object key){
	if (innerLayers == null)
		return null;
	IFigure layer = innerLayers.getLayer(key);
	if (layer != null)
		return layer;
	if (printableLayers == null)
		return null;
	return printableLayers.getLayer(key);
}

/**
 * Returns the model of this EditPart. 
 */
public Object getModel(){
	return LayerManager.ID;
}

protected LayeredPane getPrintableLayers() {
	if (printableLayers == null)
		printableLayers = createPrintableLayers();
	return printableLayers;
}

/*
 * defined on interface
 */
public RootEditPart getRoot() {return this;}

/*
 * from RootEditPart
 */
public EditPartViewer getViewer() {return viewer;}

protected void refreshChildren(){}

/**
 * Sets the contents.  The root contains a single child, it's contents.
 */
public void setContents(EditPart editpart){
	if (contents != null)
		removeChild(contents);
	contents = editpart;
	if (contents != null)
		addChild(contents,0);
}

/**
 * Sets the viewer.
 * @param viewer EditPartViewer.
 */
public void setViewer(EditPartViewer newViewer) {
	if (viewer == newViewer)
		return;
	if (viewer != null)
		unregister();
	viewer = newViewer;
	if (viewer != null)
		register();
}

class FeedbackLayer
	extends FreeformLayer
{
	FeedbackLayer() {
		setEnabled(false);
	}
}

}
