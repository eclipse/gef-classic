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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.examples.ediagram.model.Link;

public class LinkPropertySource 
	extends ModelPropertySource
{
	
private final PropertyId idBendpoints;

public LinkPropertySource(String categoryName, Object model) {
	super(model);
	idBendpoints = new PropertyId(categoryName, "Bendpoints");
}

protected void createPropertyDescriptors(List list) {
	list.add(new StringPropertyDescriptor(idBendpoints));
}

public boolean isPropertyResettable(Object id) {
	return id == idBendpoints;
}

protected Link getLink() {
	return (Link)getModel();
}

public Object getPropertyValue(Object id) {
	if (id == idBendpoints) {
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
	return id == idBendpoints && !getLink().getBendpoints().isEmpty();
}

public void resetPropertyValue(Object id) {
	if (id == idBendpoints)
		getLink().getBendpoints().clear();
}

public void setPropertyValue(Object id, Object value) {
	if (id == idBendpoints) {
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