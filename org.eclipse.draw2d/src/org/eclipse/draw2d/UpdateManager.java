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

import java.util.Map;

import org.eclipse.swt.graphics.GC;

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

private UpdateListener listeners[] = new UpdateListener[0];
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
 * Causes an update to occur at some time, and the given runnable to be executed
 * following the update.
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
 * @param listener the listener to add
 */
public void addUpdateListener(UpdateListener listener) {
	if (listener == null)
		throw new IllegalArgumentException();
    if (listeners == null) {
        listeners = new UpdateListener[1];
        listeners[0] = listener;
    } else {
    	int oldSize = listeners.length;
    	UpdateListener newListeners[] = new UpdateListener[oldSize + 1];
    	System.arraycopy(listeners, 0, newListeners, 0, oldSize);
    	newListeners[oldSize] = listener;
    	listeners = newListeners;
    }
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
	UpdateListener localListeners[] = listeners;
	for (int i = 0; i < localListeners.length; i++)
		localListeners[i].notifyPainting(damage, dirtyRegions);
}

/**
 * Notifies listeners that validation is about to occur.
 */
protected void fireValidating() {
	UpdateListener localListeners[] = listeners;
	for (int i = 0; i < localListeners.length; i++)
		localListeners[i].notifyValidating();
}

/**
 * @return whether this update manager has been disposed.
 */
protected boolean isDisposed() {
	return disposed;
}

/**
 * Forces an update to occur. Update managers will perform updates automatically, but may
 * do so asynchronously. Calling this method forces a synchronous update.
 */
public abstract void performUpdate();

void paint(GC gc) {
	performUpdate(new Rectangle(gc.getClipping()));
}

/**
 * Performs an update on the given exposed rectangle.
 * @param exposed the exposed rectangle
 */
public abstract void performUpdate(Rectangle exposed);

/**
 * Removes one occurence of the given UpdateListener by identity.
 * @param listener the listener to remove
 */
public void removeUpdateListener(UpdateListener listener) {
	if (listeners == null)
		throw new IllegalArgumentException();
	for (int index = 0; index < listeners.length; index++)
		if (listeners[index] == listener) {
			int newSize = listeners.length - 1;
			UpdateListener newListeners[] = null;
			if (newSize != 0) {
				newListeners = new UpdateListener[newSize];
				System.arraycopy(listeners, 0, newListeners, 0, index);
				System.arraycopy(listeners, index + 1, newListeners, index, newSize - index);
			}
			listeners = newListeners;
			return;
		}
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

/**
 * Validates all invalid figures without repainting. Some update managers may not support
 * independant validation of figures. In those cases, this method is equivalent to calling
 * {@link #performUpdate()}.
 * 
 * @since 3.2
 */
public void performValidation() {
	performUpdate();
}

}
