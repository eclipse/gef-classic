/*******************************************************************************
 * Copyright 2012, 2024 Zoltan Ujhelyi and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Zoltan Ujhelyi
 ******************************************************************************/
package org.eclipse.zest.core.widgets.gestures;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.GestureListener;

import org.eclipse.zest.core.viewers.internal.ZoomManager;
import org.eclipse.zest.core.widgets.Graph;

/**
 * A simple magnify gesture listener class that calls an associated
 * {@link ZoomManager} class to perform zooming.
 *
 * @author Zoltan Ujhelyi
 * @since 1.14
 */
public class ZoomGestureListener implements GestureListener {
	ZoomManager manager;

	double zoom = 1.0;

	@Override
	public void gesture(GestureEvent e) {
		if (!(e.widget instanceof Graph)) {
			return;
		}
		switch (e.detail) {
		case SWT.GESTURE_BEGIN:
			manager = ((Graph) e.widget).getZoomManager();
			zoom = manager.getZoom();
			break;
		case SWT.GESTURE_END:
			break;
		case SWT.GESTURE_MAGNIFY:
			double newValue = zoom * e.magnification;
			manager.setZoom(newValue);
			break;
		default:
			// Do nothing
		}
	}
}