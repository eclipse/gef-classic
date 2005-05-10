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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.examples.ediagram.model.Link;

public class LinkPropertySource 
	extends BasePropertySource
{
	
private static final String ID_BENDPOINTS = "bendpoints";

public LinkPropertySource(Object model) {
	super(model);
}

protected void createPropertyDescriptors(List list) {
	list.add(new TextPropertyDescriptor(ID_BENDPOINTS, ID_BENDPOINTS));
}

public boolean isPropertyResettable(Object id) {
	return id == ID_BENDPOINTS;
}

protected Link getLink() {
	return (Link)getModel();
}

public Object getPropertyValue(Object id) {
	if (id == ID_BENDPOINTS) {
		StringBuffer buffer = new StringBuffer();
		for (Iterator iter = getLink().getBendpoints().iterator(); iter.hasNext();) {
			Point point = ((Bendpoint)iter.next()).getLocation();
			buffer.append(point.x);
			buffer.append(',');
			buffer.append(point.y);
			buffer.append(' ');
		}
		return buffer.toString();
	}
	return null;
}

public boolean isPropertySet(Object id) {
	return id == ID_BENDPOINTS && !getLink().getBendpoints().isEmpty();
}

public void resetPropertyValue(Object id) {
	if (id == ID_BENDPOINTS)
		getLink().getBendpoints().clear();
}

public void setPropertyValue(Object id, Object value) {
	if (id == ID_BENDPOINTS) {
		List points = new ArrayList();
		try {
			String[] result = ((String)value).split(" ");
			for (int i = 0; i < result.length; i++) {
				String[] coordinates = result[i].split(",");
				points.add(new AbsoluteBendpoint(Integer.parseInt(coordinates[0]), 
						Integer.parseInt(coordinates[1])));
			}
			getLink().getBendpoints().clear();
			getLink().getBendpoints().addAll(points);
		} catch (Exception e) {}
	}
}

}