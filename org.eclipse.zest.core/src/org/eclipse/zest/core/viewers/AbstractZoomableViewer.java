/*******************************************************************************
 * Copyright 2005-2010, 2024 CHISEL Group, University of Victoria, Victoria,
 *                           BC, Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.zest.core.viewers.internal.ZoomManager;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A simple interface that provides zooming capabilites. Not intended to be
 * subclassed by clients.
 *
 * @author Del Myers
 *
 * @noextend This class is not intended to be subclassed by clients.
 */
//@tag bug.156286-Zooming.fix
public abstract class AbstractZoomableViewer extends StructuredViewer {
	/**
	 * Returns a ZoomManager that zooming can be done on. May return null if none is
	 * available.
	 *
	 * @return a ZoomManager that zooming can be done on.
	 */
	// TODO zest 2.x: Mark with @noreference
	protected abstract ZoomManager getZoomManager();

	public void zoomTo(int x, int y, int width, int height) {
		Rectangle r = new Rectangle(x, y, width, height);
		if (r.isEmpty()) {
			getZoomManager().setZoomAsText("100%"); //$NON-NLS-1$
		} else {
			getZoomManager().zoomTo(r);
		}
	}
}
