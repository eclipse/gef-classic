/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.ui.actions;

import org.eclipse.gef3.editparts.ZoomListener;
import org.eclipse.gef3.editparts.ZoomManager;
import org.eclipse.gef3.internal.GEFMessages;
import org.eclipse.gef3.internal.InternalImages;

/**
 * @author danlee
 */
public class ZoomInAction extends ZoomAction {

	/**
	 * Constructor for ZoomInAction.
	 * 
	 * @param zoomManager
	 *            the zoom manager
	 */
	public ZoomInAction(ZoomManager zoomManager) {
		super(GEFMessages.ZoomIn_Label, InternalImages.DESC_ZOOM_IN,
				zoomManager);
		setToolTipText(GEFMessages.ZoomIn_Tooltip);
		setId(GEFActionConstants.ZOOM_IN);
		setActionDefinitionId(GEFActionConstants.ZOOM_IN);
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		zoomManager.zoomIn();
	}

	/**
	 * @see ZoomListener#zoomChanged(double)
	 */
	public void zoomChanged(double zoom) {
		setEnabled(zoomManager.canZoomIn());
	}

}
