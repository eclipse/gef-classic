package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.beans.*;
import java.io.*;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

abstract public class LogicElement
	implements IPropertySource, Cloneable, Serializable
{

public static final String
	CHILDREN = "Children", 	//$NON-NLS-1$
	INPUTS = "inputs",	//$NON-NLS-1$
	OUTPUTS = "outputs";	//$NON-NLS-1$

transient protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
static final long serialVersionUID = 1;

public void addPropertyChangeListener(PropertyChangeListener l){
	listeners.addPropertyChangeListener(l);
}

protected void firePropertyChange(String prop, Object old, Object newValue){
	listeners.firePropertyChange(prop, old, newValue);
}

protected void fireStructureChange(String prop, Object child){
	listeners.firePropertyChange(prop, null, child);
}

public Object getEditableValue(){
	return this;
}

public IPropertyDescriptor[] getPropertyDescriptors(){
	return new IPropertyDescriptor[0];
}

public Object getPropertyValue(Object propName){
	return null;
}

final Object getPropertyValue(String propName){
	return null;
}

public boolean isPropertySet(Object propName){
	return isPropertySet((String)propName);
}

final boolean isPropertySet(String propName){
	return true;
}

private void readObject(ObjectInputStream in)throws IOException, ClassNotFoundException {
	in.defaultReadObject();
	listeners = new PropertyChangeSupport(this);
}

public void removePropertyChangeListener(PropertyChangeListener l){
	listeners.removePropertyChangeListener(l);
}

public void resetPropertyValue(Object propName){
}

final void resetPropertyValue(String propName){}

public void setPropertyValue(Object propName, Object val){}
final void setPropertyValue(String propName, Object val){}

public void update(){
}

}
