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
 * A listener interface for receiving notification that an anchor has moved.
 */
public interface AnchorListener {

/**
 * Called when an anchor has moved to a new location.
 * @param anchor The anchor that has moved.
 */
void anchorMoved(ConnectionAnchor anchor);

}