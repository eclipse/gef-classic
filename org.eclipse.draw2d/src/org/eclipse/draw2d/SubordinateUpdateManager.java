package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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