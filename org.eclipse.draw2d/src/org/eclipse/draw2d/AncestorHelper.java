/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A helper object which tracks the parent chain hierarchy.
 * @since 2.1
 */
class AncestorHelper
	implements PropertyChangeListener, FigureListener 
{

/**
 * The base figure whose ancestor chain is being observed.
 */
protected final IFigure base;
/**
 * The array of ancestor listeners.
 */
protected AncestorListener[] listeners = null;
/**
 * The current number of listeners.
 * Maintains invariant: 0 <= size <= listeners.length.
 */
private int size;

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
 * @param listener the listener
 */
public void addAncestorListener(AncestorListener listener) {
    if (size == 0)
        listeners = new AncestorListener[2];
    else {
        // check for duplicates using identity
        for (int i = 0; i < size; ++i)
            if (listeners[i] == listener)
                return;
        
        // grow array if necessary
        if (size == listeners.length)
            System.arraycopy(listeners, 0,
                    listeners = new AncestorListener[size * 2 + 1], 0, size);
    }

    listeners[size] = listener;
    size++;
}

/**
 * Hooks up internal listeners used for maintaining the proper figure listeners.
 * @param rootFigure the root figure
 */
protected void addAncestors(IFigure rootFigure) {
	for (IFigure ancestor = rootFigure;
			ancestor != null;
			ancestor = ancestor.getParent()) {
		ancestor.addFigureListener(this);
		ancestor.addPropertyChangeListener("parent", this); //$NON-NLS-1$
	}
}

/**
 * Removes all internal listeners.
 */
public void dispose() {
	removeAncestors(base);
	listeners = null;
}

/**
 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
 */
public void figureMoved(IFigure ancestor) {
	fireAncestorMoved(ancestor);
}

/**
 * Fires notification to the listener list 
 * @param ancestor the figure which moved
 */
protected void fireAncestorMoved(IFigure ancestor) {
	for (int i = 0; i < size; i++)
		listeners[i].ancestorMoved(ancestor);
}

/**
 * Fires notification to the listener list 
 * @param ancestor the figure which moved
 */
protected void fireAncestorAdded(IFigure ancestor) {
	for (int i = 0; i < size; i++)
		listeners[i].ancestorAdded(ancestor);
}

/**
 * Fires notification to the listener list 
 * @param ancestor the figure which moved
 */
protected void fireAncestorRemoved(IFigure ancestor) {
	for (int i = 0; i < size; i++)
		listeners[i].ancestorRemoved(ancestor);
}

/**
 * Returns the total number of listeners.
 * @return the number of listeners
 */
public int getNumberOfListeners() {
	return size;
}

/**
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
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
 * @param listener the listener to remove
 */
public void removeAncestorListener(AncestorListener listener) {
    for (int i = 0; i < size; i++) {
        if (listeners[i] == listener) {
            if (size == 1) {
                listeners = null;
                size = 0;
            } else {
                System.arraycopy(listeners, i + 1, listeners, i, --size - i);
                listeners[size] = null;
            }
            return;
        }
    }
}

/**
 * Unhooks listeners starting at the given figure
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
