package org.eclipse.gef.examples.logicdesigner.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pratik Shah
 */
public class Ruler
	implements Serializable
{
	
static final long serialVersionUID = 1;

// means that a guide was added or removed
public static final String PROPERTY_CHILDREN = "children"; //$NON-NLS-1$
public static final String PROPERTY_UNIT = "unit"; //$NON-NLS-1$

public static final int UNIT_INCHES = 0;
public static final int UNIT_CENTIMETERS = 1;
public static final int UNIT_PIXELS = 2;

protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
private int unit;	
private boolean horizontal;
private List guides = new ArrayList();

public Ruler(boolean isHorizontal) {
	this(isHorizontal, UNIT_INCHES);
}

public Ruler(boolean isHorizontal, int unit) {
	horizontal = isHorizontal;
	setUnit(unit);
}

public void addGuide(Guide guide) {
	if (!guides.contains(guide)) {
		guide.setHorizontal(!isHorizontal());
		guides.add(guide);
		listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
	}
}

public void addPropertyChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
}

// the returned list should not be modified
public List getGuides() {
	return guides;
}

public int getUnit() {
	return unit;
}

public boolean isHorizontal() {
	return horizontal;
}

public void removeGuide(Guide guide) {
	if (guides.contains(guide)) {
		guides.remove(guide);
		listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
	}
}

public void removePropertyChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

public void setUnit(int newUnit) {
	if (unit != newUnit) {
		int oldUnit = unit;
		unit = newUnit;
		listeners.firePropertyChange(PROPERTY_UNIT, oldUnit, newUnit);
	}
}

}