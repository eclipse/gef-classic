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

import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DeselectAllTracker;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.gef.ui.parts.RulerProvider;

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
	installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
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
	return f;
}

/**
 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
 */
public Object getAdapter(Class adapter) {
	if (adapter == SnapToStrategy.class) {
		List snapStrategies = new ArrayList();
		Boolean val = (Boolean)getViewer().getProperty(RulerProvider.RULER_VISIBILITY);
		if (val != null && val.booleanValue())
			snapStrategies.add(new SnapToGuides(this));
		val = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
		if (val != null && val.booleanValue())
			snapStrategies.add(new SnapToGeometry(this));
		val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
		if (val != null && val.booleanValue())
			snapStrategies.add(new SnapToGrid(this));
		
		if (snapStrategies.size() == 0)
			return null;
		if (snapStrategies.size() == 1)
			return (SnapToStrategy)snapStrategies.get(0);

		SnapToStrategy ss[] = new SnapToStrategy[snapStrategies.size()];
		for (int i = 0; i < snapStrategies.size(); i++)
			ss[i] = (SnapToStrategy)snapStrategies.get(i);
		return new CompoundSnapToStrategy(ss);
	}
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