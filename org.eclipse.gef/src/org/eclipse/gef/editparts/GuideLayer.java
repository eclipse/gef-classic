/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editparts;

import java.util.*;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Pratik Shah
 */
public class GuideLayer
	extends FreeformLayer
{
	
private Map constraints;
	
public Object getConstraint(IFigure child) {
	return getConstraints().get(child);
}

public Map getConstraints() {
	if (constraints == null) {
		constraints = new HashMap();
	}
	return constraints;
}
	
public Rectangle getFreeformExtent() {
	/*
	 * These ints are initialized to 5 so that when the final extent is expanded by 5,
	 * the bounds do not go into the negative (unless necessary).
	 */
	int maxX = 5, minX = 5, maxY = 5, minY = 5;
	Iterator children = getChildren().iterator();
	while (children.hasNext()) {
		IFigure child = (IFigure)children.next();
		Boolean isHorizontal = (Boolean)getConstraint(child);
		if (isHorizontal != null) {
			if (isHorizontal.booleanValue()) {
				int position = child.getBounds().y;
				minY = Math.min(minY, position);
				maxY = Math.max(maxY, position);
			} else {
				int position = child.getBounds().x;
				minX = Math.min(minX, position);
				maxX = Math.max(maxX, position);
			}			
		}
	}
	Rectangle r = new Rectangle(
			minX, minY, maxX - minX + 1, maxY - minY + 1);
	if (r.width > 1) {
		r.expand(5, 0);
	}
	if (r.height > 1) {
		r.expand(0, 5);
	}
	return r;
}

public Dimension getPreferredSize(int wHint, int hHint) {
	Rectangle extents = getFreeformExtent();
	return new Dimension(extents.getRight().x, extents.getBottom().y);
}

public void remove(IFigure child) {
	getConstraints().remove(child);
	super.remove(child);
}

public void setConstraint(IFigure child, Object constraint) {
	getConstraints().put(child, constraint);
}

public void setFreeformBounds(Rectangle bounds) {
	super.setFreeformBounds(bounds);
	Iterator children = getChildren().iterator();
	while (children.hasNext()) {
		IFigure child = (IFigure)children.next();
		Boolean isHorizontal = (Boolean)getConstraint(child);
		if (isHorizontal != null) {
			if (isHorizontal.booleanValue()) {
				Rectangle.SINGLETON.setLocation(getBounds().x, child.getBounds().y);
				Rectangle.SINGLETON.setSize(getBounds().width, 1);
			} else {
				Rectangle.SINGLETON.setLocation(child.getBounds().x, getBounds().y);
				Rectangle.SINGLETON.setSize(1, getBounds().height);
			}
			child.setBounds(Rectangle.SINGLETON);				
		}
	}				
}

}