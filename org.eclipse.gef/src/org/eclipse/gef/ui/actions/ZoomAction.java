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
package org.eclipse.gef.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.Disposable;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;

/**
 * @author hudsonr
 */
abstract class ZoomAction
	extends Action
	implements ZoomListener, Disposable
{

protected ZoomManager zoomManager;

/**
 * @param zoomManager
 */
public ZoomAction(String text, ImageDescriptor image, ZoomManager zoomManager) {
	super(text, image);
	this.zoomManager = zoomManager;
	zoomManager.addZoomListener(this);
}

/**
 * @see org.eclipse.gef.Disposable#dispose()
 */
public void dispose() {
	zoomManager.removeZoomListener(this);
}

}
