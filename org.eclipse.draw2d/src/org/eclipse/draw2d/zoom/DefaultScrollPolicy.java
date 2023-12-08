/*******************************************************************************
 * Copyright (c) 2022 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
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
