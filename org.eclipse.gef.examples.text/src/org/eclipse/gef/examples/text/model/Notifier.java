/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * @since 3.1
 */
public class Notifier implements Serializable {

	protected transient PropertyChangeSupport listeners;
	private static final long serialVersionUID = 1;

	public void addPropertyChangeListener(PropertyChangeListener l) {
		if (listeners == null)
			listeners = new PropertyChangeSupport(this);
		listeners.addPropertyChangeListener(l);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue) {
		if (listeners != null)
			listeners.firePropertyChange(prop, old, newValue);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		if (listeners != null)
			listeners.removePropertyChangeListener(l);
	}

}