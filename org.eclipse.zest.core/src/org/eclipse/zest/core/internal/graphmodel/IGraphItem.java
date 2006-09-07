/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphmodel;

import java.beans.PropertyChangeListener;

/**
 * Simple base interface for graph items to allow property change support.
 * The properties that change are dependant on the particular graph item.
 * @author Del Myers
 *
 */
//@tag bug(154259-Abstraction(fix))
public interface IGraphItem {
	/** 
	 * Attach a non-null PropertyChangeListener to this object.
	 * @param l a non-null PropertyChangeListener instance
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public void addPropertyChangeListener(PropertyChangeListener l);
	/** 
	 * Remove a PropertyChangeListener from this component.
	 * @param l a PropertyChangeListener instance
	 */
	public void removePropertyChangeListener(PropertyChangeListener l);
	
	/**
	 * Fires the given property change to all listeners.
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChange(String property, Object oldValue, Object newValue);
	
	/**
	 * The data for this item. Normally the user's model object.
	 * @return
	 */
	public Object getData();
}
