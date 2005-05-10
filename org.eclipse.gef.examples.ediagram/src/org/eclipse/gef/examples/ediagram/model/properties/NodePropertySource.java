/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

import java.util.List;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.examples.ediagram.model.Node;

public class NodePropertySource 
	extends BasePropertySource
{
	
private static final String ID_X_LOC = "X";
private static final String ID_Y_LOC = "Y";
private static final String ID_WIDTH = "Width";

public NodePropertySource(Object model) {
	super(model);
}

protected void createPropertyDescriptors(List list) {
	list.add(new TextPropertyDescriptor(ID_X_LOC, ID_X_LOC));
	list.add(new TextPropertyDescriptor(ID_Y_LOC, ID_Y_LOC));
	list.add(new TextPropertyDescriptor(ID_WIDTH, ID_WIDTH));
}

protected Node getNode() {
	return (Node)getModel();
}

public Object getPropertyValue(Object id) {
	if (id == ID_X_LOC)
		return "" + getNode().getLocation().x; //$NON-NLS-1$
	if (id == ID_Y_LOC)
		return "" + getNode().getLocation().y; //$NON-NLS-1$
	if (id == ID_WIDTH)
		return "" + getNode().getWidth(); //$NON-NLS-1$
	return null;
}

public boolean isPropertyResettable(Object id) {
	return id == ID_WIDTH;
}

public boolean isPropertySet(Object id) {
	if (id == ID_WIDTH)
		return getNode().getWidth() != -1;
	return false;
}

public void resetPropertyValue(Object id) {
	if (id == ID_WIDTH)
		getNode().setWidth(-1);
}

public void setPropertyValue(Object id, Object value) {
	if (id == ID_X_LOC) {
		Point newLoc = getNode().getLocation().getCopy();
		newLoc.x = Integer.parseInt((String)value);
		getNode().setLocation(newLoc);
	} else if (id == ID_Y_LOC) {
		Point newLoc = getNode().getLocation().getCopy();
		newLoc.y = Integer.parseInt((String)value);
		getNode().setLocation(newLoc);
	} else if (id == ID_WIDTH)
		getNode().setWidth(Integer.parseInt((String)value));
}

}