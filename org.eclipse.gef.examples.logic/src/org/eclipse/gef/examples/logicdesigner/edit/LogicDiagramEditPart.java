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
package org.eclipse.gef.examples.logicdesigner.edit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DeselectAllTracker;
import org.eclipse.gef.tools.MarqueeDragTracker;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;

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

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart(){
		public void getName(AccessibleEvent e) {
			e.result = LogicMessages.LogicDiagram_LabelText;
		}
	};
}

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
	f.setBackgroundColor(ColorConstants.listBackground);
	f.setOpaque(true);
	return f;
}

/**
 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
 */
public Object getAdapter(Class adapter) {
	if (adapter == SnapToStrategy.class)
		return new SnapToGrid(this);
	return super.getAdapter(adapter);
}

public DragTracker getDragTracker(Request req){
	if (req instanceof SelectionRequest 
		&& ((SelectionRequest)req).getLastButtonPressed() == 3)
			return new DeselectAllTracker(this);
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
