/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @since 3.1
 */
public abstract class ModelElement {

private Container container;

transient protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

public void addPropertyChangeListener(PropertyChangeListener l) {
	listeners.addPropertyChangeListener(l);
}

protected void firePropertyChange(String prop, Object old, Object newValue) {
	listeners.firePropertyChange(prop, old, newValue);
}

/**
 * @return Returns the container.
 */
public Container getContainer() {
	return container;
}

public void removePropertyChangeListener(PropertyChangeListener l) {
	listeners.removePropertyChangeListener(l);
}

/**
 * @since 3.1
 * @param container
 */
public void setParent(Container container) {
	this.container = container;
}

abstract int size();

}