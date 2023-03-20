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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * A scroll policy which ensures that the center of the canvas is maintained
 * during zooming.
 * 
 * This behavior was the default behavior of GEF Classic and Zest pre version
 * 3.15.
 * 
 * @since 3.13
 */
public class DefaultScrollPolicy implements IZoomScrollPolicy {

	@Override
	public Point calcNewViewLocation(Viewport vp, double oldZoom, double newZoom) {
		Point center = vp.getClientArea().getCenter();
		Point zoomedCenter = center.getScaled(newZoom / oldZoom);
		Dimension dif = zoomedCenter.getDifference(center);
		return vp.getViewLocation().getTranslated(dif);
	}

}
