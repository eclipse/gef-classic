package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.*;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.editparts.*;
import com.ibm.etools.gef.tools.*;
import com.ibm.etools.gef.commands.*;

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

protected void createEditPolicies(){}

protected IFigure createFigure() {
	FreeformViewport viewport = new FreeformViewport();
	innerLayers = new FreeformLayeredPane();

	innerLayers.add(new FreeformLayer(), PRIMARY_LAYER);
	innerLayers.add(new ConnectionLayer(), CONNECTION_LAYER);
	innerLayers.add(new FreeformLayer(), HANDLE_LAYER);
	innerLayers.add(new FeedbackLayer(), FEEDBACK_LAYER);

	viewport.setContents(innerLayers);
	return viewport;
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
	return innerLayers.getLayer(key);
}

/**
 * Returns the model of this EditPart. 
 */
public Object getModel(){
	return LayerManager.ID;
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
//	{setBorder(new GroupBoxBorder("Feedback"));}
	public IFigure findFigureAtExcluding( int x, int y, Collection c ){
		return null;
	}
	public void updateFreeformBounds(Rectangle union){
		super.updateFreeformBounds(union);
	}
}

}
