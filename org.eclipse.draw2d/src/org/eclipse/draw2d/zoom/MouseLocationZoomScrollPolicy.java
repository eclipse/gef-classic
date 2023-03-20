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

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;

/**
 * A scroll policy which ensures that the content under the mouse cursor stays
 * where it is after scrolling. If the mouse is not inside of the viewerControl
 * the {@link DefaultScrollPolicy} is used as fallback.
 * 
 * In order to keep the target under the mouse stable we have to calculate the
 * new view location such that the following equation holds:
 * 
 * (mousepos + oldViewLocation) / oldZoom = (mousepos + newViewLocation)/newZoom
 * 
 * @since 3.13
 */
public class MouseLocationZoomScrollPolicy extends DefaultScrollPolicy {

	final Control viewerControl;

	public MouseLocationZoomScrollPolicy(Control viewerControl) {
		this.viewerControl = viewerControl;
	}

	@Override
	public Point calcNewViewLocation(Viewport vp, double oldZoom, double newZoom) {
		final Rectangle controlBounds = viewerControl.getBounds();
		org.eclipse.swt.graphics.Point mouseLocation = viewerControl.getDisplay().getCursorLocation();
		mouseLocation = viewerControl.toControl(mouseLocation);
		if (controlBounds.contains(mouseLocation)) {
			return calcMouseBasedViewLocation(vp, oldZoom, newZoom, new Point(mouseLocation));
		}

		return super.calcNewViewLocation(vp, oldZoom, newZoom);
	}

	private static Point calcMouseBasedViewLocation(Viewport vp, double oldZoom, double newZoom, Point mouseLocation) {
		final Point oldViewLocation = vp.getViewLocation();
		final Point newviewLocation = mouseLocation.getCopy();
		newviewLocation.performTranslate(oldViewLocation.x, oldViewLocation.y);
		newviewLocation.scale(newZoom / oldZoom);
		newviewLocation.performTranslate(-mouseLocation.x, -mouseLocation.y);
		return newviewLocation;
	}

}
