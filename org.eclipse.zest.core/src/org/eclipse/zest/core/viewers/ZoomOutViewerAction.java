/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.viewers;

import org.eclipse.gef.ui.actions.ZoomOutAction;

/**
 * An action for zooming out on Zest Viewers.
 * @author Del Myers
 *
 */
public class ZoomOutViewerAction extends ZoomOutAction {
	public static final String ID = "zest.action.zoomout";
	/**
	 * Creates a new action for the given viewer.
	 * @param viewer the viewer to zoom out on.
	 */
	public ZoomOutViewerAction(AbstractZoomableViewer viewer) {
		super(viewer.getZoomManager());
	}
	public String getId() {
		return ID;
	}
}