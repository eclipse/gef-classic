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

import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * @deprecated this class is not used
 */
public abstract class SubordinateUpdateManager extends org.eclipse.draw2dl.UpdateManager {

	/**
	 * A root figure.
	 */
	protected org.eclipse.draw2dl.IFigure root;

	/**
	 * A graphics source
	 */
	protected org.eclipse.draw2dl.GraphicsSource graphicsSource;

	/**
	 * @see org.eclipse.draw2dl.UpdateManager#addDirtyRegion(org.eclipse.draw2dl.IFigure, int, int, int, int)
	 */
	public void addDirtyRegion(org.eclipse.draw2dl.IFigure f, int x, int y, int w, int h) {
		if (getSuperior() == null)
			return;
		getSuperior().addDirtyRegion(f, x, y, w, h);
	}

	/**
	 * @see org.eclipse.draw2dl.UpdateManager#addInvalidFigure(org.eclipse.draw2dl.IFigure)
	 */
	public void addInvalidFigure(org.eclipse.draw2dl.IFigure f) {
		org.eclipse.draw2dl.UpdateManager um = getSuperior();
		if (um == null)
			return;
		um.addInvalidFigure(f);
	}

	/**
	 * Returns the host figure.
	 * 
	 * @return the host figure
	 */
	protected abstract org.eclipse.draw2dl.IFigure getHost();

	/**
	 * Returns the superior update manager.
	 * 
	 * @return the superior
	 */
	protected org.eclipse.draw2dl.UpdateManager getSuperior() {
		if (getHost().getParent() == null)
			return null;
		return getHost().getParent().getUpdateManager();
	}

	/**
	 * @see org.eclipse.draw2dl.UpdateManager#performUpdate()
	 */
	public void performUpdate() {
		org.eclipse.draw2dl.UpdateManager um = getSuperior();
		if (um == null)
			return;
		um.performUpdate();
	}

	/**
	 * @see org.eclipse.draw2dl.UpdateManager#performUpdate(Rectangle)
	 */
	public void performUpdate(Rectangle rect) {
		org.eclipse.draw2dl.UpdateManager um = getSuperior();
		if (um == null)
			return;
		um.performUpdate(rect);
	}

	/**
	 * @see org.eclipse.draw2dl.UpdateManager#setRoot(org.eclipse.draw2dl.IFigure)
	 */
	public void setRoot(IFigure f) {
		root = f;
	}

	/**
	 * @see UpdateManager#setGraphicsSource(org.eclipse.draw2dl.GraphicsSource)
	 */
	public void setGraphicsSource(GraphicsSource gs) {
		graphicsSource = gs;
	}
}
