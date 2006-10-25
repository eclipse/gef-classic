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

import org.eclipse.gef.ui.actions.ZoomInAction;

/**
 * An action that can be contributed for zoomable viewers.
 * @author Del Myers
 *
 */
//@tag zest.bug.156286-Zooming.fix
public class ZoomInViewerAction extends ZoomInAction {
	public static final String ID = "zest.actions.zoomin";
	public ZoomInViewerAction(AbstractZoomableViewer viewer) {
		super(viewer.getZoomManager());
	}
	public String getId() {
		return ID;
	}
}
