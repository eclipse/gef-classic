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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An UpdateManager that asynchronously updates the affected figures.  
 */
public class DeferredUpdateManager
	extends UpdateManager
{

/**
 * Calls {@link DeferredUpdateManager#performUpdate()}.
 */
protected class UpdateRequest
	implements Runnable
{
	/**
	 * Calls {@link DeferredUpdateManager#performUpdate()}.
	 */
	public void run() {
		performUpdate();
	}
}
private Rectangle damage;
private Map dirtyRegions = new HashMap();

private GraphicsSource graphicsSource;
private List invalidFigures = new ArrayList();
private IFigure root;
private boolean updateQueued = false;

private boolean updating;
private RunnableChain afterUpdate;

private static class RunnableChain {
	RunnableChain next;
	Runnable run;

	RunnableChain(Runnable run, RunnableChain next) {
		this.run = run;
		this.next = next;
	}
	
	void run() {
		if (next != null)
			next.run();
		run.run();
	}
}

/**
 * Empty constructor.
 */
public DeferredUpdateManager() { }

/**
 * Constructs a new DererredUpdateManager with the given GraphicsSource.
 * @param gs the graphics source
 */
public DeferredUpdateManager(GraphicsSource gs) {
	setGraphicsSource(gs);
}

/**
 * Adds a dirty region (defined by the rectangle <i>x, y, w, h</i>) to the update queue.
 * If the figure isn't visible or either the width or height are 0, the method returns
 * without queueing the dirty region.
 * 
 * @param figure the figure that contains the dirty region
 * @param x the x coordinate of the dirty region
 * @param y the y coordinate of the dirty region
 * @param w the width of the dirty region
 * @param h the height of the dirty region
 */
public synchronized void addDirtyRegion(IFigure figure, int x, int y, int w, int h) {
	if (w == 0 || h == 0 || !figure.isShowing())
		return;

	Rectangle rect = (Rectangle)dirtyRegions.get(figure);
	if (rect == null) {
		rect = new Rectangle(x, y, w, h);
		dirtyRegions.put(figure, rect);
	} else
		rect.union(x, y, w, h);
	
	queueWork();
}

/**
 * Adds the given figure to the update queue.  Invalid figures will be validated before 
 * the damaged regions are repainted.
 * 
 * @param f the invalid figure
 */
public synchronized void addInvalidFigure(IFigure f) {
	if (invalidFigures.contains(f))
		return;
	queueWork();
	invalidFigures.add(f);
}

/**
 * Returns a Graphics object for the given region.
 * @param region the region to be repainted
 * @return the Graphics object
 */
protected Graphics getGraphics(Rectangle region) {
	if (graphicsSource == null)
		return null;
	return graphicsSource.getGraphics(region);
}

/**
 * Performs the update.  Validates the invalid figures and then repaints the dirty
 * regions.
 * @see #validateFigures()
 * @see #repairDamage()
 */
public synchronized void performUpdate() {
	if (isDisposed() || updating)
		return;
	updating = true;
	try {
		validateFigures();
		updateQueued = false;
		repairDamage();
		if (afterUpdate != null) {
			RunnableChain chain = afterUpdate;
			afterUpdate = null;
			chain.run(); //chain may queue additional Runnable.
			if (afterUpdate != null)
				queueWork();
		}
	} finally {
		updating = false;
	}
}

/**
 * Adds the given exposed region to the update queue and then performs the update.
 * 
 * @param exposed the exposed region
 */
public synchronized void performUpdate(Rectangle exposed) {
	addDirtyRegion(root, exposed);
	performUpdate();
}

/**
 * Posts an {@link UpdateRequest} using {@link Display#asyncExec(Runnable)}.  If work has
 * already been queued, a new request is not needed.
 */
protected void queueWork() {
	if (!updateQueued) {
		Display.getCurrent().asyncExec(new UpdateRequest());
		updateQueued = true;
	}
}

/**
 * Releases the graphics object, which causes the GraphicsSource to flush.
 * @param graphics the graphics object
 */
protected void releaseGraphics(Graphics graphics) {
	graphicsSource.flushGraphics(damage);
}

/**
 * Repaints the dirty regions on the update queue and calls 
 * {@link UpdateManager#firePainting(Rectangle, Map)}, unless there are no dirty regions.
 */
protected void repairDamage() {
	Iterator keys = dirtyRegions.keySet().iterator();
	Rectangle contribution;
	IFigure figure;
	IFigure walker;

	while (keys.hasNext()) {
		figure = (IFigure)keys.next();
		walker = figure.getParent();
		contribution = (Rectangle)dirtyRegions.get(figure);
		//A figure can't paint beyond its own bounds
		contribution.intersect(figure.getBounds());
		while (!contribution.isEmpty() && walker != null) {
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
		dirtyRegions = new HashMap();
	}
	
	if (damage != null && !damage.isEmpty()) {
		//ystem.out.println(damage);
		Graphics graphics = getGraphics(damage);
		if (graphics != null) {
			root.paint(graphics);
			releaseGraphics(graphics);
		}
	}
	damage = null;
}

/**
 * Adds the given runnable and queues an update if an update is not under progress.
 * @param runnable the runnable
 */
public synchronized void runWithUpdate(Runnable runnable) {
	afterUpdate = new RunnableChain(runnable, afterUpdate);
	if (!updating)
		queueWork();
}

/**
 * Sets the graphics source.
 * @param gs the graphics source
 */
public void setGraphicsSource(GraphicsSource gs) {
	graphicsSource = gs;
}

/**
 * Sets the root figure.
 * @param figure the root figure
 */
public void setRoot(IFigure figure) {
	root = figure;
}

/**
 * Validates the invalid figures on the update queue and calls 
 * {@link UpdateManager#fireValidating()} unless there are no invalid figures.
 */
protected void validateFigures() {
	if (invalidFigures.isEmpty())
		return;
	try {
		IFigure fig;
		fireValidating();
		for (int i = 0; i < invalidFigures.size(); i++) {
			fig = (IFigure) invalidFigures.get(i);
			invalidFigures.set(i, null);
			fig.validate();
		}
	} finally {
		invalidFigures.clear();
	}
}

}