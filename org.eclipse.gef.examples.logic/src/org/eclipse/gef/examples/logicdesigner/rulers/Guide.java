/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

/**
 * @author Pratik Shah
 */
public class Guide 
	implements Serializable
{

public static final String PROPERTY_POSITION = "position"; //$NON-NLS-1$
// could mean that a part was added, removed, or its alignment was changed
public static final String PROPERTY_CHILDREN = "children"; //$NON-NLS-1$

protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
private Map map;
private int position;
private boolean horizontal;

public Guide() {
	// empty constructor
}

public Guide(boolean isHorizontal) {
	setHorizontal(isHorizontal);
}

public void addPart(LogicSubpart part, int alignment) {
	if (getMap().containsKey(part)) {
		int key = ((Integer)getMap().get(part)).intValue();
		if (key == alignment) {
			return;
		}
	}
	getMap().put(part, new Integer(alignment));
	listeners.firePropertyChange(PROPERTY_CHILDREN, null, part);
}

public void addPropertyChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
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

public void removePart(LogicSubpart part) {
	if (getMap().containsKey(part)) {
		getMap().remove(part);
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