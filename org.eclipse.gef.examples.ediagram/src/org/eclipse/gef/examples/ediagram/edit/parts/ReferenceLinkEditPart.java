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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;

import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ReferenceLinkEditPart
	extends LinkEditPart
{

private Label srcCount, srcName, targetCount, targetName;
private ConnectionEndpointLocator srcCountLocator, srcNameLocator, 
		targetCountLocator, targetNameLocator;
private RotatableDecoration srcDecor, targetDecor;
private EReference opposite;

public ReferenceLinkEditPart(ReferenceView model) {
	super(model);
}

/**
 * Upon activation, attach to the model element as a property change listener.
 */
public void activate() {
	super.activate();
	getEReference().eAdapters().add(modelListener);
	updateEOpposite(getEReference().getEOpposite());
}

private String createCountString(EReference ref) {
	int lower = ref.getLowerBound();
	int upper = ref.getUpperBound();
	if (lower == upper)
		return "" + lower; //$NON-NLS-1$
	return lower + ".." + (upper == -1 ? "n" : "" + upper); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

protected IFigure createFigure() {
	PolylineConnection conn = new PolylineConnection();

	// Create the endpoint locators
	srcCountLocator = new ConnectionEndpointLocator(conn, false);
	srcNameLocator = new ConnectionEndpointLocator(conn, false);
	srcNameLocator.setVDistance(-4);
	targetCountLocator = new ConnectionEndpointLocator(conn, true);
	targetNameLocator = new ConnectionEndpointLocator(conn, true);
	targetCountLocator.setVDistance(-4);
	
	return conn;
}

private PolygonDecoration createPolygonDecoration() {
	PolygonDecoration decoration = new PolygonDecoration();
	PointList decorationPointList = new PointList();
	decorationPointList.addPoint(0,0);
	decorationPointList.addPoint(-1,1);
	decorationPointList.addPoint(-2,0);
	decorationPointList.addPoint(-1,-1);
	decoration.setTemplate(decorationPointList);
	return decoration;
}

private PolylineDecoration createPolylineDecoration () {
	PolylineDecoration decoration = new PolylineDecoration();
	decoration.setScale(10, 5);
	return decoration;
}

/**
 * Upon deactivation, detach from the model element as a property change listener.
 */
public void deactivate() {
	updateEOpposite(null);
	getEReference().eAdapters().remove(modelListener);
	super.deactivate();
}

private PolylineConnection getConnection() {
	return (PolylineConnection)getFigure();
}

protected EReference getEReference() {
	return getRefView().getEReference();
}

protected ReferenceView getRefView() {
	return (ReferenceView)getModel();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(ReferenceView.class)) {
		case ModelPackage.REFERENCE_VIEW__OPPOSITE_SHOWN:
			refreshVisuals();
			return;
	}
	switch (msg.getFeatureID(EReference.class)) {
		case EcorePackage.EREFERENCE__NAME:
			updateSourceName();
			updateTargetName();
			return;
		case EcorePackage.EREFERENCE__LOWER_BOUND:
		case EcorePackage.EREFERENCE__UPPER_BOUND:
		case EcorePackage.EREFERENCE__MANY:
			updateSourceCount();
			updateTargetCount();
			return;
		case EcorePackage.EREFERENCE__EOPPOSITE:
			updateEOpposite(getEReference().getEOpposite());
		case EcorePackage.EREFERENCE__CONTAINER:
		case EcorePackage.EREFERENCE__CONTAINMENT:
			refreshVisuals();
			return;
	}
	super.handlePropertyChanged(msg);
}

protected void refreshVisuals() {
	super.refreshVisuals();
	updateSourceDecoration();
	updateTargetDecoration();
	updateSourceCount();
	updateSourceName();
	updateTargetCount();
	updateTargetName();
}

private void updateEOpposite(EReference opp) {
	if (opposite == opp)
		return;
	if (opposite != null)
		opposite.eAdapters().remove(modelListener);
	opposite = opp;
	if (opposite != null)
		opposite.eAdapters().add(modelListener);
}

private void updateSourceCount() {
	if (getRefView().isOppositeShown() && getEReference().getEOpposite() != null) {
		if (getEReference().isContainment()) {
			if (srcCount != null) {
				getConnection().remove(srcCount);
				srcCount = null;
			}
		} else {
			if (srcCount == null) {
				srcCount = new Label();
				srcCount.setOpaque(true);
				getConnection().add(srcCount, srcCountLocator);
			}
			srcCount.setText(createCountString(getEReference().getEOpposite()));
		}
	} else if (srcCount != null) {
		getConnection().remove(srcCount);
		srcCount = null;
	}
}

private void updateSourceDecoration() {
	if (getEReference().isContainment()) {
		if (srcDecor == null) {
			srcDecor = createPolygonDecoration();
			getConnection().setSourceDecoration(srcDecor);
		}
	} else if (srcDecor != null) {
		srcDecor = null;
		getConnection().setSourceDecoration(srcDecor);
	}
}

private void updateSourceName() {
	if (getRefView().isOppositeShown() && getEReference().getEOpposite() != null) {
		if (srcName == null) {
			srcName = new Label();
			srcName.setOpaque(true);
			getConnection().add(srcName, srcNameLocator);
		}
		srcName.setText("+" + getEReference().getEOpposite().getName());
	} else if (srcName != null) {
		getConnection().remove(srcName);
		srcName = null;
	}
}

private void updateTargetCount() {
	if (getEReference().isContainer() && getRefView().isOppositeShown() 
			&& getEReference().getEOpposite() != null) {
		if (targetCount != null) {
			getConnection().remove(targetCount);
			targetCount = null;
		}
	} else {
		if (targetCount == null) {
			targetCount = new Label();
			targetCount.setOpaque(true);
			getConnection().add(targetCount, targetCountLocator);
		}
		targetCount.setText(createCountString(getEReference()));
	}
}

private void updateTargetDecoration() {
	if (getRefView().isOppositeShown() && getEReference().getEOpposite() != null) {
		if (getEReference().isContainer()) {
			if (targetDecor != null && !(targetDecor instanceof PolygonDecoration)) {
				getConnection().setTargetDecoration(null);
				targetDecor = null;
			}
			if (targetDecor == null) {
				targetDecor = createPolygonDecoration();
				getConnection().setTargetDecoration(targetDecor);
			}
		} else if (targetDecor != null) {
			targetDecor = null;
			getConnection().setTargetDecoration(targetDecor);
		}
	} else if (targetDecor == null) {
		targetDecor = createPolylineDecoration();
		getConnection().setTargetDecoration(targetDecor);
	}
}

private void updateTargetName() {
	if (targetName == null) {
		targetName = new Label();
		targetName.setOpaque(true);
		getConnection().add(targetName, targetNameLocator);
	}
	targetName.setText("+" + getEReference().getName());
}

}