/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Pratik Shah
 */
public class Ruler 
{

// could mean that a guide was added or removed
public static final String PROPERTY_CHILDREN = "children"; //$NON-NLS-1$

protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);	
private boolean horizontal;
private List guides = new ArrayList();

public Ruler(boolean isHorizontal) {
	horizontal = isHorizontal;
}

public void addGuide(Guide guide){
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

}