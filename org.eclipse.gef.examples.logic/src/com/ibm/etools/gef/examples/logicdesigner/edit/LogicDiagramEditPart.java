package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.beans.PropertyChangeEvent;

import org.eclipse.swt.widgets.Display;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.commands.*;
import com.ibm.etools.gef.editpolicies.RootComponentEditPolicy;
import com.ibm.etools.gef.tools.*;
import com.ibm.etools.gef.tools.DeselectAllTracker;

import com.ibm.etools.gef.examples.logicdesigner.model.*;

/**
 * Holds all other LogicEditParts under this. It is 
 * activated by LogicEditorPart, to hold the entire
 * model. It is sort of a blank board where all 
 * other EditParts get added.
 */
public class LogicDiagramEditPart
	extends LogicContainerEditPart
	implements LayerConstants
{

final private List fEmptyHandles = new ArrayList();

/**
 * Installs EditPolicies specific to this. 
 */
protected void createEditPolicies(){
	super.createEditPolicies();
	installEditPolicy(EditPolicy.NODE_ROLE, null);
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
}

/**
 * Returns a Figure to represent this. 
 *
 * @return  Figure.
 */
protected IFigure createFigure() {
	Figure f = new FreeformLayer();
//	f.setBorder(new GroupBoxBorder("Diagram"));
	f.setLayoutManager(new FreeformLayout());
	f.setBorder(new MarginBorder(5));
//	f.setBackgroundColor(ColorConstants.yellow);
	f.setOpaque(true);
	return f;
}

public DragTracker getDragTracker(Request req){
	return new MarqueeDragTracker();
}

/**
 * Returns <code>NULL</code> as it does not hold any connections.
 *
 * @return  ConnectionAnchor
 */
public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart editPart) {
	return null;
}

/**
 * Returns <code>NULL</code> as it does not hold any connections.
 *
 * @return  ConnectionAnchor
 */
public ConnectionAnchor getSourceConnectionAnchor(int x, int y) {
	return null;
}

/**
 * Returns <code>NULL</code> as it does not hold any connections.
 *
 * @return  ConnectionAnchor
 */
public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart editPart) {
	return null;
}

/**
 * Returns <code>NULL</code> as it does not hold any connections.
 *
 * @return  ConnectionAnchor
 */
public ConnectionAnchor getTargetConnectionAnchor(int x, int y) {
	return null;
}

public void propertyChange(PropertyChangeEvent evt){
	if (LogicDiagram.ID_ROUTER.equals(evt.getPropertyName()))
		refreshVisuals();
	else
		super.propertyChange(evt);
}

protected void refreshVisuals(){
	ConnectionLayer cLayer = (ConnectionLayer) getLayer(CONNECTION_LAYER);
	if (getLogicDiagram().getConnectionRouter().equals(LogicDiagram.ROUTER_MANUAL)){
		AutomaticRouter router = new FanRouter();
		router.setNextRouter(new BendpointConnectionRouter());
		cLayer.setConnectionRouter(router);
	}
	else
		cLayer.setConnectionRouter(new ManhattanConnectionRouter());
}

}
