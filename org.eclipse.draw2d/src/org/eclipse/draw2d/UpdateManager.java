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
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Update managers handle the job of repainting and laying out figures.  A desirable 
 * implementation is one that batches work to be done and collapses any redundant work.
 * Update managers may contain 0 or more nested update managers. Some optimizations can 
 * only be performed after all requests have been batched.  For this reason, an 
 * UpdateManager should call performUpdate() on its nested UpdateManagers prior to doing 
 * its own update.  During the nested updates, new requests may be added.
 */
public abstract class UpdateManager {

private List listeners = new ArrayList();
private boolean disposed;

/**
 * Adds the dirty region defined by the coordinates on the IFigure <b>figure</b>.  The 
 * update manager should repaint the dirty region in a timely fashion.
 * 
 * @param figure the dirty figure
 * @param x the x coordinate of the dirty region
 * @param y the y coordinate of the dirty region
 * @param w the width of the dirty region
 * @param h the height of the dirty region
 */
public abstract void addDirtyRegion(IFigure figure, int x, int y, int w, int h);

/**
 * @see #addDirtyRegion(IFigure, int, int, int, int)
 */
public void addDirtyRegion(IFigure figure, Rectangle rect) {
	addDirtyRegion(figure, rect.x, rect.y, rect.width, rect.height);
}

/**
 * Causes an update to occur at some itme, and the given runnable to be executed upon completion.
 * @since 3.1
 * @param run the runnable
 */
public void runWithUpdate(Runnable run) { }

/**
 * The receiver should call validate() on the IFigure <i>figure</i> in a timely fashion.
 * 
 * @param figure the invalid figure
 */
public abstract void addInvalidFigure(IFigure figure);

/**
 * Adds the given listener to the list of listeners to be notified of painting and 
 * validation.
 * @param listener the listener
 */
public void addUpdateListener(UpdateListener listener) {
	listeners.add(listener);
}

/**
 * Called when the EditPartViewer is being disposed.
 */
public void dispose() {
	disposed = true;
}

/**
 * Notifies listeners that painting is about to occur, passing them the damaged rectangle
 * and the map of dirty regions.
 * @param damage the damaged rectangle
 * @param dirtyRegions map of dirty regions to figures
 */
protected void firePainting(Rectangle damage, Map dirtyRegions) {
	for (int i = 0; i < listeners.size(); i++) {
		UpdateListener listener = (UpdateListener)listeners.get(i);
		listener.notifyPainting(damage, dirtyRegions);
	}
}

/**
 * Notifies listeners that validation is about to occur.
 */
protected void fireValidating() {
	for (int i = 0; i < listeners.size(); i++) {
		UpdateListener listener = (UpdateListener)listeners.get(i);
		listener.notifyValidating();
	}
}

/**
 * @return whether this update manager has been disposed.
 */
protected boolean isDisposed() {
	return disposed;
}

/**
 * Performs the update.
 */
public abstract void performUpdate();

/**
 * Performs an update on the given exposed rectangle.
 * @param exposed the exposed rectangle
 */
public abstract void performUpdate(Rectangle exposed);

/**
 * Removes the given UpdateListener.
 * @param listener the listener
 */
public void removeUpdateListener(UpdateListener listener) {
	listeners.remove(listener);
}

/**
 * Sets the GraphicsSource for this update manager.
 * @param gs the new GraphicsSource
 */
public abstract void setGraphicsSource(GraphicsSource gs);

/**
 * Sets the root figure.
 * @param figure the new root figure
 */
public abstract void setRoot(IFigure figure);
}