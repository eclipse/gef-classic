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

import java.util.Iterator;

import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * A Layer that can extend in all 4 directions.
 */
public class FreeformLayer extends Layer implements org.eclipse.draw2dl.FreeformFigure {

	private org.eclipse.draw2dl.FreeformHelper helper = new org.eclipse.draw2dl.FreeformHelper(this);

	/**
	 * @see org.eclipse.draw2dl.IFigure#add(org.eclipse.draw2dl.IFigure, Object, int)
	 */
	public void add(org.eclipse.draw2dl.IFigure child, Object constraint, int index) {
		super.add(child, constraint, index);
		helper.hookChild(child);
	}

	/**
	 * @see org.eclipse.draw2dl.FreeformFigure#addFreeformListener(org.eclipse.draw2dl.FreeformListener)
	 */
	public void addFreeformListener(org.eclipse.draw2dl.FreeformListener listener) {
		addListener(org.eclipse.draw2dl.FreeformListener.class, listener);
	}

	/**
	 * @see org.eclipse.draw2dl.FreeformFigure#fireExtentChanged()
	 */
	public void fireExtentChanged() {
		Iterator iter = getListeners(org.eclipse.draw2dl.FreeformListener.class);
		while (iter.hasNext())
			((org.eclipse.draw2dl.FreeformListener) iter.next()).notifyFreeformExtentChanged();
	}

	/**
	 * Overrides to do nothing.
	 * 
	 * @see org.eclipse.draw2dl.Figure#fireMoved()
	 */
	protected void fireMoved() {
	}

	/**
	 * @see org.eclipse.draw2dl.FreeformFigure#getFreeformExtent()
	 */
	public Rectangle getFreeformExtent() {
		return helper.getFreeformExtent();
	}

	/**
	 * @see Figure#primTranslate(int, int)
	 */
	public void primTranslate(int dx, int dy) {
		bounds.x += dx;
		bounds.y += dy;
	}

	/**
	 * @see org.eclipse.draw2dl.IFigure#remove(org.eclipse.draw2dl.IFigure)
	 */
	public void remove(IFigure child) {
		helper.unhookChild(child);
		super.remove(child);
	}

	/**
	 * @see org.eclipse.draw2dl.FreeformFigure#removeFreeformListener(org.eclipse.draw2dl.FreeformListener)
	 */
	public void removeFreeformListener(org.eclipse.draw2dl.FreeformListener listener) {
		removeListener(FreeformListener.class, listener);
	}

	/**
	 * @see FreeformFigure#setFreeformBounds(Rectangle)
	 */
	public void setFreeformBounds(Rectangle bounds) {
		helper.setFreeformBounds(bounds);
	}

}
