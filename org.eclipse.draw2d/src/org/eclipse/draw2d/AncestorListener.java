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

/**
 * A listener interface for receiving changes in the ancestor hierarchy of the listening
 * IFigure.
 */
public interface AncestorListener {

/**
 * Called when an ancestor has been added into the listening figure's hierarchy.
 * @param ancestor The ancestor that was added
 */
void ancestorAdded(IFigure ancestor);

/**
 * Called when an ancestor has moved to a new location.
 * @param ancestor The ancestor that has moved
 */
void ancestorMoved(IFigure ancestor);

/**
 * Called when an ancestor has been removed from the listening figure's hierarchy.
 * @param ancestor The ancestor that has been removed
 */
void ancestorRemoved(IFigure ancestor);

/**
 * An empty implementation of AncestorListener for convenience.
 */
class Stub implements AncestorListener {
	public void ancestorMoved(IFigure ancestor) { }
	public void ancestorAdded(IFigure ancestor) { }
	public void ancestorRemoved(IFigure ancestor) { }
}

}