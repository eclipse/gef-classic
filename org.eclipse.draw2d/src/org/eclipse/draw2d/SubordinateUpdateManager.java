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

import org.eclipse.draw2d.geometry.Rectangle;

public abstract class SubordinateUpdateManager
	extends UpdateManager
{

protected IFigure root;
protected GraphicsSource graphicsSource;

/**
 * @see UpdateManager#addDirtyRegion(IFigure, int, int, int, int)
 */
public void addDirtyRegion(IFigure f, int x, int y, int w, int h) {
	if (getSuperior() == null)
		return;
	getSuperior().addDirtyRegion(f, x, y, w, h);
}

/**
 * @see UpdateManager#addInvalidFigure(IFigure)
 */
public void addInvalidFigure(IFigure f) {
	UpdateManager um = getSuperior();
	if (um == null) 
		return;
	um.addInvalidFigure(f);
}

protected abstract IFigure getHost();

protected UpdateManager getSuperior() {
	if (getHost().getParent() == null) 
		return null;
	return getHost().getParent().getUpdateManager();
}

/**
 * @see UpdateManager#performUpdate()
 */
public void performUpdate() {
	UpdateManager um = getSuperior();
	if (um == null) 
		return;
	um.performUpdate();
}

/**
 * @see UpdateManager#performUpdate(Rectangle)
 */
public void performUpdate(Rectangle rect) {
	UpdateManager um = getSuperior();
	if (um == null) 
		return;
	um.performUpdate(rect);
}

/**
 * @see UpdateManager#setRoot(IFigure)
 */
public void setRoot(IFigure f) {
	root = f;
}

/**
 * @see UpdateManager#setGraphicsSource(GraphicsSource)
 */
public void setGraphicsSource(GraphicsSource gs) {
	graphicsSource = gs;
}
}
