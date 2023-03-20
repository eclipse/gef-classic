/*******************************************************************************
 * Copyright (c) 2022 Johannes Kepler University Linz
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alois Zoitl - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.zoom;

import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;

/**
 * With this policy the scrolling behavior during zooming can be controlled.
 * 
 * It is used by the {@link AbstractZoomManager} to calculate the new viewport
 * location after zooming.
 * 
 * @since 3.13
 */
public interface IZoomScrollPolicy {

	/**
	 * Calculate the viewport location for the given viewport that should be set by
	 * {@link AbstractZoomManager} after zooming is completed.
	 * 
	 * @param vp      the viewport that the zooming will be applied to
	 * @param oldZoom the current zoom scaling factor
	 * @param newZoom the upcoming new zoom scaling factor
	 * @return the new viewport location to be applied after zooming is completed.
	 * 
	 * @since 3.2
	 */
	Point calcNewViewLocation(Viewport vp, double oldZoom, double newZoom);

}
