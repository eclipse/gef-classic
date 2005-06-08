/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     E.D.Willink
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.examples.ediagram.model.Node;

public class NodePropertySource 
	extends ModelPropertySource
{
	
private final PropertyId idX;
private final PropertyId idY;
private final PropertyId idWidth;

public NodePropertySource(String categoryName, Object model) {
	super(model);
	idX = new PropertyId(categoryName, "X");
	idY = new PropertyId(categoryName, "Y");
	idWidth = new PropertyId(categoryName, "Width");
}

protected void createPropertyDescriptors(List list) {
	list.add(new IntegerPropertyDescriptor(idX));
	list.add(new IntegerPropertyDescriptor(idY));
	list.add(new IntegerPropertyDescriptor(idWidth));
}

protected Node getNode() {
	return (Node)getModel();
}

public Object getPropertyValue(Object id) {
	if (id == idX)
		return IntegerPropertyDescriptor.fromModel(getNode().getLocation().x);
	if (id == idY)
		return IntegerPropertyDescriptor.fromModel(getNode().getLocation().y);
	if (id == idWidth)
		return IntegerPropertyDescriptor.fromModel(getNode().getWidth());
	return null;
}

public boolean isPropertyResettable(Object id) {
	return id == idWidth;
}

public boolean isPropertySet(Object id) {
	if (id == idWidth)
		return getNode().getWidth() != -1;
	return false;
}

public void resetPropertyValue(Object id) {
	if (id == idWidth)
		getNode().setWidth(-1);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idX) {
		Point newLoc = getNode().getLocation().getCopy();
		newLoc.x = IntegerPropertyDescriptor.toModel(value);
		getNode().setLocation(newLoc);
	} else if (id == idY) {
		Point newLoc = getNode().getLocation().getCopy();
		newLoc.y = IntegerPropertyDescriptor.toModel(value);
		getNode().setLocation(newLoc);
	} else if (id == idWidth)
		getNode().setWidth(IntegerPropertyDescriptor.toModel(value));
}

}