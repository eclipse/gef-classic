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

import org.eclipse.draw2dl.geometry.Point;

/**
 * Provides support for anchors which depend on a figure for thier location.
 * 
 * @author hudsonr
 */
public abstract class AbstractConnectionAnchor extends ConnectionAnchorBase
		implements AncestorListener {

	private org.eclipse.draw2dl.IFigure owner;

	/**
	 * Constructs an AbstractConnectionAnchor with no owner.
	 * 
	 * @since 2.0
	 */
	public AbstractConnectionAnchor() {
	}

	/**
	 * Constructs an AbstractConnectionAnchor with the owner supplied as input.
	 * 
	 * @since 2.0
	 * @param owner
	 *            Owner of this anchor
	 */
	public AbstractConnectionAnchor(org.eclipse.draw2dl.IFigure owner) {
		setOwner(owner);
	}

	/**
	 * Adds the given listener to the listeners to be notified of anchor
	 * location changes.
	 * 
	 * @since 2.0
	 * @param listener
	 *            Listener to be added
	 * @see #removeAnchorListener(org.eclipse.draw2dl.AnchorListener)
	 */
	public void addAnchorListener(org.eclipse.draw2dl.AnchorListener listener) {
		if (listener == null)
			return;
		if (listeners.size() == 0)
			getOwner().addAncestorListener(this);
		super.addAnchorListener(listener);
	}

	/**
	 * Notifies all the listeners of this anchor's location change.
	 * 
	 * @since 2.0
	 * @param figure
	 *            Anchor-owning Figure which has moved
	 * @see org.eclipse.draw2dl.AncestorListener#ancestorMoved(org.eclipse.draw2dl.IFigure)
	 */
	public void ancestorMoved(org.eclipse.draw2dl.IFigure figure) {
		fireAnchorMoved();
	}

	/**
	 * @see org.eclipse.draw2dl.AncestorListener#ancestorAdded(org.eclipse.draw2dl.IFigure)
	 */
	public void ancestorAdded(org.eclipse.draw2dl.IFigure ancestor) {
	}

	/**
	 * @see org.eclipse.draw2dl.AncestorListener#ancestorRemoved(org.eclipse.draw2dl.IFigure)
	 */
	public void ancestorRemoved(org.eclipse.draw2dl.IFigure ancestor) {
	}

	/**
	 * Returns the owner Figure on which this anchor's location is dependent.
	 * 
	 * @since 2.0
	 * @return Owner of this anchor
	 * @see #setOwner(org.eclipse.draw2dl.IFigure)
	 */
	public org.eclipse.draw2dl.IFigure getOwner() {
		return owner;
	}

	/**
	 * Returns the point which is used as the reference by this
	 * AbstractConnectionAnchor. It is generally dependent on the Figure which
	 * is the owner of this AbstractConnectionAnchor.
	 * 
	 * @since 2.0
	 * @return The reference point of this anchor
	 * @see ConnectionAnchor#getReferencePoint()
	 */
	public Point getReferencePoint() {
		if (getOwner() == null)
			return null;
		else {
			Point ref = getOwner().getBounds().getCenter();
			getOwner().translateToAbsolute(ref);
			return ref;
		}
	}

	/**
	 * Removes the given listener from this anchor. If all the listeners are
	 * removed, then this anchor removes itself from its owner.
	 * 
	 * @since 2.0
	 * @param listener
	 *            Listener to be removed from this anchors listeners list
	 * @see #addAnchorListener(org.eclipse.draw2dl.AnchorListener)
	 */
	public void removeAnchorListener(AnchorListener listener) {
		super.removeAnchorListener(listener);
		if (listeners.size() == 0)
			getOwner().removeAncestorListener(this);
	}

	/**
	 * Sets the owner of this anchor, on whom this anchors location is
	 * dependent.
	 * 
	 * @since 2.0
	 * @param owner
	 *            Owner of this anchor
	 */
	public void setOwner(IFigure owner) {
		this.owner = owner;
	}

}
