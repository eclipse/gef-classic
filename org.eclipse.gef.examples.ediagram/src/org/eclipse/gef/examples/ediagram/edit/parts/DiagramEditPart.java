/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.parts;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import org.eclipse.gef.examples.ediagram.edit.policies.DiagramLayoutEditPolicy;
import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class DiagramEditPart 
	extends AbstractGraphicalEditPart
	implements LayerConstants
{
	
private ShortestPathConnectionRouter spRouter;
protected Adapter modelListener = new AdapterImpl() {
	public void notifyChanged(Notification msg) {
		handlePropertyChanged(msg);
	}
};

public DiagramEditPart(Diagram model) {
	super();
	setModel(model);
}

public void activate() {
	super.activate();
	((Diagram)getModel()).eAdapters().add(modelListener);
}

protected IFigure createFigure() {
	Figure f = new FreeformLayer();
	f.setBorder(new MarginBorder(5));
	f.setLayoutManager(new FreeformLayout());
	return f;
}

protected void createEditPolicies() {
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
	installEditPolicy(EditPolicy.CONTAINER_ROLE, new DiagramLayoutEditPolicy());
}

public void deactivate() {
	getContentPane().removeLayoutListener(spRouter.getLayoutListener());
	((Diagram)getModel()).eAdapters().remove(modelListener);
	super.deactivate();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(Diagram.class)) {
		case ModelPackage.DIAGRAM__CONTENTS:
			refreshChildren();
	}
}

protected void refreshVisuals() {
	// refreshVisuals is called before activate(), so we add the router here (only once)
	if (spRouter == null) {
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(CONNECTION_LAYER);
		FanRouter router = new FanRouter();
		router.setSeparation(30);
		spRouter = new ShortestPathConnectionRouter(getFigure()); 
		router.setNextRouter(spRouter);
		cLayer.setConnectionRouter(router);
		getContentPane().addLayoutListener(spRouter.getLayoutListener());
	}
}

protected List getModelChildren() {
	return ((Diagram)getModel()).getContents();
}

}