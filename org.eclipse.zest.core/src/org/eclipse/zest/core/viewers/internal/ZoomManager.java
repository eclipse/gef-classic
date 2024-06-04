/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.zest.core.viewers.internal;

import java.util.ArrayList;

import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.Viewport;

/**
 * Manage the primary zoom function in a graphical viewer. This class is used by
 * the zoom contribution items, including:
 * <UL>
 * <LI>{@link org.eclipse.gef.ui.actions.ZoomInAction}
 * <LI>{@link org.eclipse.gef.ui.actions.ZoomOutAction}
 * <LI>and {@link org.eclipse.gef.ui.actions.ZoomComboContributionItem}
 * </UL>
 * <P>
 * A ZoomManager controls how zoom in and zoom out are performed. It also
 * determines the list of choices the user sees in the drop-down Combo on the
 * toolbar. The zoom manager controls a <code>ScalableFigure</code>, which
 * performs the actual zoom, and also a <code>Viewport</code>. The viewport is
 * needed so that the scrolled location is preserved as the zoom level changes.
 * <p>
 * <b>NOTE:</b> For the settings of {@link #FIT_ALL Page}, {@link #FIT_WIDTH
 * Width} and {@link #FIT_HEIGHT Height} to work properly, the given
 * <code>Viewport</code> should have its scrollbars always visible or never
 * visible. Otherwise, these settings may cause undesired effects.
 *
 * @author Dan Lee
 * @author Eric Bordeau
 */
@SuppressWarnings("javadoc") // GEF is not in classpath
//TODO zest 2.x: Move to org.eclipse.zest.core.widgets.internal
public class ZoomManager extends org.eclipse.draw2d.zoom.AbstractZoomManager {

	/**
	 * String constant for the "Height" zoom level. At this zoom level, the zoom
	 * manager will adopt a zoom setting such that the entire height of the diagram
	 * will be visible on the screen.
	 */
	public static final String FIT_HEIGHT = SharedMessages.FitHeightAction_Label;
	/**
	 * String constant for the "Width" zoom level. At this zoom level, the zoom
	 * manager will adopt a zoom setting such that the entire width of the diagram
	 * will be visible on the screen.
	 */
	public static final String FIT_WIDTH = SharedMessages.FitWidthAction_Label;
	/**
	 * String constant for the "Page" zoom level. At this zoom level, the zoom
	 * manager will adopt a zoom setting such that the entire diagram will be
	 * visible on the screen.
	 */
	public static final String FIT_ALL = SharedMessages.FitAllAction_Label;

	/**
	 * Creates a new ZoomManager.
	 *
	 * @param pane     The ScalableFigure associated with this ZoomManager
	 * @param viewport The Viewport associated with this ZoomManager
	 */
	public ZoomManager(ScalableFigure pane, Viewport viewport) {
		super(pane, viewport);
		ArrayList<String> zoomLevelContributions = new ArrayList<>();
		zoomLevelContributions.add(FIT_ALL);
		setZoomLevelContributions(zoomLevelContributions);
	}

	@Override
	protected boolean isFitWidth(String zoomString) {
		return zoomString.equalsIgnoreCase(FIT_WIDTH);
	}

	@Override
	protected boolean isFitAll(String zoomString) {
		return zoomString.equalsIgnoreCase(FIT_ALL);
	}

	@Override
	protected boolean isFitHeight(String zoomString) {
		return zoomString.equalsIgnoreCase(FIT_HEIGHT);
	}

}
