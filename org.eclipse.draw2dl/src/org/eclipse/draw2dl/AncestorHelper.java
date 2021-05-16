/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A helper object which tracks the parent chain hierarchy.
 * 
 * @since 2.1
 */
class AncestorHelper implements PropertyChangeListener, FigureListener {

	/**
	 * The base figure whose ancestor chain is being observed.
	 */
	protected final org.eclipse.draw2dl.IFigure base;
	/**
	 * The array of ancestor listeners.
	 */
	protected org.eclipse.draw2dl.AncestorListener[] listeners;

	/**
	 * Constructs a new helper on the given base figure and starts listening to
	 * figure and property changes on the base figure's parent chain. When no
	 * longer needed, the helper should be disposed.
	 * 
	 * @since 2.1
	 * @param baseFigure
	 */
	public AncestorHelper(org.eclipse.draw2dl.IFigure baseFigure) {
		this.base = baseFigure;
		addAncestors(baseFigure);
	}

	/**
	 * Appends a new listener to the list of listeners.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addAncestorListener(org.eclipse.draw2dl.AncestorListener listener) {
		if (listeners == null) {
			listeners = new org.eclipse.draw2dl.AncestorListener[1];
			listeners[0] = listener;
		} else {
			int oldSize = listeners.length;
			org.eclipse.draw2dl.AncestorListener newListeners[] = new org.eclipse.draw2dl.AncestorListener[oldSize + 1];
			System.arraycopy(listeners, 0, newListeners, 0, oldSize);
			newListeners[oldSize] = listener;
			listeners = newListeners;
		}
	}

	/**
	 * Hooks up internal listeners used for maintaining the proper figure
	 * listeners.
	 * 
	 * @param rootFigure
	 *            the root figure
	 */
	protected void addAncestors(org.eclipse.draw2dl.IFigure rootFigure) {
		for (org.eclipse.draw2dl.IFigure ancestor = rootFigure; ancestor != null; ancestor = ancestor
				.getParent()) {
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
	 * @see org.eclipse.draw2dl.FigureListener#figureMoved(org.eclipse.draw2dl.IFigure)
	 */
	public void figureMoved(org.eclipse.draw2dl.IFigure ancestor) {
		fireAncestorMoved(ancestor);
	}

	/**
	 * Fires notification to the listener list
	 * 
	 * @param ancestor
	 *            the figure which moved
	 */
	protected void fireAncestorMoved(org.eclipse.draw2dl.IFigure ancestor) {
		if (listeners == null)
			return;
		for (int i = 0; i < listeners.length; i++)
			listeners[i].ancestorMoved(ancestor);
	}

	/**
	 * Fires notification to the listener list
	 * 
	 * @param ancestor
	 *            the figure which moved
	 */
	protected void fireAncestorAdded(org.eclipse.draw2dl.IFigure ancestor) {
		if (listeners == null)
			return;
		for (int i = 0; i < listeners.length; i++)
			listeners[i].ancestorAdded(ancestor);
	}

	/**
	 * Fires notification to the listener list
	 * 
	 * @param ancestor
	 *            the figure which moved
	 */
	protected void fireAncestorRemoved(org.eclipse.draw2dl.IFigure ancestor) {
		if (listeners == null)
			return;
		for (int i = 0; i < listeners.length; i++)
			listeners[i].ancestorRemoved(ancestor);
	}

	/**
	 * Returns the total number of listeners.
	 * 
	 * @return the number of listeners
	 */
	public boolean isEmpty() {
		return listeners == null;
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("parent")) { //$NON-NLS-1$
			org.eclipse.draw2dl.IFigure oldParent = (org.eclipse.draw2dl.IFigure) event.getOldValue();
			org.eclipse.draw2dl.IFigure newParent = (org.eclipse.draw2dl.IFigure) event.getNewValue();
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
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeAncestorListener(org.eclipse.draw2dl.AncestorListener listener) {
		if (listeners == null)
			return;
		for (int index = 0; index < listeners.length; index++)
			if (listeners[index] == listener) {
				int newSize = listeners.length - 1;
				org.eclipse.draw2dl.AncestorListener newListeners[] = null;
				if (newSize != 0) {
					newListeners = new AncestorListener[newSize];
					System.arraycopy(listeners, 0, newListeners, 0, index);
					System.arraycopy(listeners, index + 1, newListeners, index,
							newSize - index);
				}
				listeners = newListeners;
				return;
			}
	}

	/**
	 * Unhooks listeners starting at the given figure
	 * 
	 * @param rootFigure
	 */
	protected void removeAncestors(org.eclipse.draw2dl.IFigure rootFigure) {
		for (IFigure ancestor = rootFigure; ancestor != null; ancestor = ancestor
				.getParent()) {
			ancestor.removeFigureListener(this);
			ancestor.removePropertyChangeListener("parent", this); //$NON-NLS-1$
		}
	}

}
