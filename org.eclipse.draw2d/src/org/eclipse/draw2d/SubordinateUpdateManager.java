/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.*;

public abstract class SubordinateUpdateManager
	extends UpdateManager
{

protected IFigure root;
protected GraphicsSource graphicsSource;

public void addDirtyRegion(IFigure f, int x, int y, int w, int h){
	if (getSuperior() == null)
		return;
	getSuperior().addDirtyRegion(f,x,y,w,h);
}

public void addInvalidFigure(IFigure f){
	UpdateManager um = getSuperior();
	if (um==null) return;
	um.addInvalidFigure(f);
}

abstract protected IFigure getHost();

protected UpdateManager getSuperior(){
	if (getHost().getParent() == null) return null;
	return getHost().getParent().getUpdateManager();
}

public void performUpdate(){
	UpdateManager um = getSuperior();
	if (um==null) return;
	um.performUpdate();
}

public void performUpdate(Rectangle rect){
	UpdateManager um = getSuperior();
	if (um==null) return;
	um.performUpdate(rect);
}

public void setRoot(IFigure f){root=f;}
public void setGraphicsSource(GraphicsSource gs){
	graphicsSource = gs;
}
}