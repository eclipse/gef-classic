/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper object which tracks the parent chain hierarchy.
 * since 3.0
 */
class AncestorHelper
	implements PropertyChangeListener, FigureListener 
{

/**
 * The base figure whose ancestor chain is being observed.
 */
protected final IFigure base;
/**
 * The list of ancestor listeners.
 */
protected List listeners = null;

/**
 * Constructs a new helper on the given base figure and starts listening to figure and
 * property changes on the base figure's parent chain.  When no longer needed, the helper
 * should be disposed.
 * @since 2.1
 * @param baseFigure
 */
public AncestorHelper(IFigure baseFigure) {
	this.base = baseFigure;
	addAncestors(baseFigure);
}

/**
 * Appends a new listener to the list of listeners.
 * @since 2.1
 * @param listener the listener
 */
public void addAncestorListener(AncestorListener listener) {
	if (listeners == null) 
		listeners = new ArrayList();
	listeners.add(listener);
}

/**
 * Hooks up internal listeners used for maintaining the proper figure listeners.
 * @since 2.1
 * @param rootFigure the root figure
 */
protected void addAncestors(IFigure rootFigure) {
	for (IFigure ancestor = rootFigure;
			ancestor != null;
			ancestor = ancestor.getParent()) {
		ancestor.addFigureListener(this);
		ancestor.addPropertyChangeListener("parent", this);
	}
}

/**
 * Removes all internal listeners.
 * @since 2.1
 */
public void dispose() {
	removeAncestors(base);
	listeners.clear();
}

public void figureMoved(IFigure ancestor) {
	fireAncestorMoved(ancestor);
}

/**
 * Fires notification to the listener list 
 * @since 2.1
 * @param ancestor the figure which moved
 */
protected void fireAncestorMoved(IFigure ancestor) {
	for (int i = 0; i < listeners.size(); i++)
		((AncestorListener)listeners.get(i)).ancestorMoved(ancestor);
}

/**
 * Fires notification to the listener list 
 * @since 2.1
 * @param ancestor the figure which moved
 */
protected void fireAncestorAdded(IFigure ancestor) {
	for (int i = 0; i < listeners.size(); i++)
		((AncestorListener)listeners.get(i)).ancestorAdded(ancestor);
}

/**
 * Fires notification to the listener list 
 * @since 2.1
 * @param ancestor the figure which moved
 */
protected void fireAncestorRemoved(IFigure ancestor) {
	for (int i = 0; i < listeners.size(); i++)
		((AncestorListener)listeners.get(i)).ancestorRemoved(ancestor);
}

/**
 * Returns the total number of listeners.
 * @since 2.1
 * @return the number of listeners
 */
public int getNumberOfListeners() {
	return listeners.size();
}

public void propertyChange(PropertyChangeEvent event) {
	if (event.getPropertyName().equals("parent")) { //$NON-NLS-1$
		IFigure oldParent = (IFigure)event.getOldValue();
		IFigure newParent = (IFigure)event.getNewValue();
		if (oldParent != null) {
			removeAncestors(oldParent);
			fireAncestorRemoved(oldParent);
		}
		if (newParent != null) {
			addAncestors(newParent);
			fireAncestorAdded(newParent);
		}
	}
}

/**
 * Removes the first occurence of the given listener
 * @since 2.1
 * @param listener the listener to remove
 */
public void removeAncestorListener(AncestorListener listener) {
	listeners.remove(listener);
}

/**
 * Unhooks listeners starting at the given figure
 * @since 2.1
 * @param rootFigure
 */
protected void removeAncestors(IFigure rootFigure) {
	for (IFigure ancestor = rootFigure; 
				ancestor != null; 
				ancestor = ancestor.getParent()) {
		ancestor.removeFigureListener(this);
		ancestor.removePropertyChangeListener("parent", this); //$NON-NLS-1$
	}
}

}