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
package org.eclipse.gef.examples.logicdesigner.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

/**
 * @author Pratik Shah
 */
public class LogicGuide 
	implements Serializable
{
	
public static final String PROPERTY_CHILDREN = "subparts changed"; //$NON-NLS-1$
public static final String PROPERTY_POSITION = "position changed"; //$NON-NLS-1$

static final long serialVersionUID = 1;
	
protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
private Map map;
private int position;
private boolean horizontal;

public LogicGuide() {
	// empty constructor
}

public LogicGuide(boolean isHorizontal) {
	setHorizontal(isHorizontal);
}

/*
 * @TODO:Pratik   use PositionConstants here
 */
// -1 is left; 0, center; 1, right (same thing for top, middle, bottom, respectively)
public void attachPart(LogicSubpart part, int alignment) {
	if (getMap().containsKey(part)) {
		int key = ((Integer)getMap().get(part)).intValue();
		if (key == alignment) {
			return;
		}
	}
	getMap().put(part, new Integer(alignment));
	LogicGuide parent = isHorizontal() ? part.getHorizontalGuide() : part.getVerticalGuide();
	if (parent != null && parent != this) {
		parent.detachPart(part);
	}
	if (isHorizontal()) {
		part.setHorizontalGuide(this);
	} else {
		part.setVerticalGuide(this);
	}
	listeners.firePropertyChange(PROPERTY_CHILDREN, null, part);
}

public void addPropertyChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
}

public int getAlignment(LogicSubpart part) {
	if (getMap().get(part) != null)
		return ((Integer)getMap().get(part)).intValue();
	return 0;
}
	
public Map getMap() {
	if (map == null) {
		map = new Hashtable();
	}
	return map;
}

public Set getParts() {
	return getMap().keySet();
}

public int getPosition() {
	return position;
}

public boolean isHorizontal() {
	return horizontal;
}

public void detachPart(LogicSubpart part) {
	if (getMap().containsKey(part)) {
		getMap().remove(part);
		if (isHorizontal()) {
			part.setHorizontalGuide(null);
		} else {
			part.setVerticalGuide(null);
		}
		listeners.firePropertyChange(PROPERTY_CHILDREN, null, part);
	}
}

public void removePropertyChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

public void setHorizontal(boolean isHorizontal) {
	horizontal = isHorizontal;
}

public void setPosition(int offset) {
	if (position != offset) {
		int oldValue = position;
		position = offset;
		listeners.firePropertyChange(PROPERTY_POSITION, new Integer(oldValue), 
				new Integer(position));
	}
}

}