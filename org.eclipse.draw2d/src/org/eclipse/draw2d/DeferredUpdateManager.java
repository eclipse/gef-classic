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

import java.util.*;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;

public class DeferredUpdateManager
	extends UpdateManager
{

private boolean updating;

private GraphicsSource graphicsSource;
private IFigure root;
private boolean updateQueued = false;
private List invalidFigures = new ArrayList();
private Map dirtyRegions = new HashMap();
private Rectangle damage;

public DeferredUpdateManager(GraphicsSource gs){
	setGraphicsSource(gs);
}

public DeferredUpdateManager(){}

protected class UpdateRequest
	implements Runnable
{
	public void run(){
		performUpdate();
	}
}

synchronized public void addDirtyRegion(IFigure figure, int x, int y, int w, int h){
	if (!figure.isShowing())
		return;
	if (w == 0 || h == 0)
		return;
	Rectangle rect;
	rect = (Rectangle)dirtyRegions.get(figure);
	if (rect == null){
		rect = new Rectangle(x, y, w, h);
		dirtyRegions.put(figure, rect);
	}
	else
		rect.union(x, y, w, h);
	queueWork();
}

synchronized public void addInvalidFigure(IFigure f){
	if (invalidFigures.contains(f))
		return;
	queueWork();
	invalidFigures.add(f);
}

protected Graphics getGraphics(Rectangle region){
	if (graphicsSource == null)
		return null;
	return graphicsSource.getGraphics(region);
}

synchronized public void performUpdate(Rectangle exposed){
	addDirtyRegion(root, exposed);
	performUpdate();
}

synchronized public void performUpdate(){
	if (isDisposed() || updating)
		return;
	updating = true;
	try {
		validateFigures();
		updateQueued = false;
		repairDamage();
	} finally{
		updating = false;
	}
}

protected void queueWork(){
	if (!updateQueued){
		Display.getCurrent().asyncExec(new UpdateRequest());
		updateQueued = true;
	}
}

protected void releaseGraphics(Graphics graphics){
	graphicsSource.flushGraphics(damage);
}

protected void repairDamage() {
	Iterator keys = dirtyRegions.keySet().iterator();
	Rectangle contribution;
	IFigure figure;
	IFigure walker;

	while (keys.hasNext()) {
		figure = (IFigure)keys.next();
		walker = figure.getParent();
		contribution = (Rectangle)dirtyRegions.get(figure);
		while (!contribution.isEmpty() && walker != null){
			walker.translateToParent(contribution);
			contribution.intersect(walker.getBounds());
			walker = walker.getParent();
		}
		if (damage == null)
			damage = new Rectangle(contribution);
		else
			damage.union(contribution);
//		keys.remove();
	}

	if (!dirtyRegions.isEmpty()) {
		firePainting(damage, dirtyRegions);
		dirtyRegions.clear();
	}
	
	if (damage != null && !damage.isEmpty()) {
		//System.out.println(damage);
		Graphics graphics = getGraphics(damage);
		if (graphics != null) {
			root.paint(graphics);
			releaseGraphics(graphics);
		}
	}
	damage = null;
}

public void setGraphicsSource(GraphicsSource gs){
	graphicsSource = gs;
}

public void setRoot(IFigure figure){
	root = figure;
}

protected void validateFigures(){
	if (invalidFigures.isEmpty())
		return;
	try {
		IFigure fig;
		fireValidating();
		for (int i=0; i < invalidFigures.size(); i++){
			fig = (IFigure) invalidFigures.get(i);
			invalidFigures.set(i,null);
			fig.validate();
		}
	} finally {
		invalidFigures.clear();
	}
}

}