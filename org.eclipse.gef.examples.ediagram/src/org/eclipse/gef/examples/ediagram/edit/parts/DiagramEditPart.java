/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.parts;

import java.util.List;

import org.eclipse.swt.SWT;

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
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

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
	
	ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
	FanRouter router = new FanRouter();
	router.setSeparation(20);
	router.setNextRouter(new ShortestPathConnectionRouter(f));
	connLayer.setConnectionRouter(router);
	connLayer.setAntialias(SWT.ON);
	
	return f;
}

protected void createEditPolicies() {
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutEditPolicy());
	installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
}

public void deactivate() {
	((Diagram)getModel()).eAdapters().remove(modelListener);
	super.deactivate();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(Diagram.class)) {
		case ModelPackage.DIAGRAM__CONTENTS:
			refreshChildren();
	}
}

public Object getAdapter(Class key) {
	if (key == SnapToHelper.class)
		return new SnapToGeometry(this);
	return super.getAdapter(key);
}

protected List getModelChildren() {
	return ((Diagram)getModel()).getContents();
}

}
