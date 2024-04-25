/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Rectangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScrollPaneTests {
	private static final int WIDTH = 300;
	private static final int HEIGHT = 500;
	private static final int FILLER = 50;

	private Shell shell;
	private FigureCanvas figureCanvas;

	@Before
	public void setUp() {
		shell = new Shell();
		// Shell should be bigger to account for decorations
		shell.setSize(WIDTH + FILLER, HEIGHT + FILLER);
		figureCanvas = new FigureCanvas(shell);
		figureCanvas.setBackground(ColorConstants.white);
		figureCanvas.setSize(WIDTH, HEIGHT);
		shell.open();
		shell.layout();
	}

	@After
	public void tearDown() {
		shell.dispose();
	}

	/**
	 * With overlay-scrolling enabled, the scrollbars don't require any additional
	 * space.
	 */
	@Test
	public void testGtkWithOverlayScrolling() {
		assumeTrue("gtk".equals(SWT.getPlatform())); //$NON-NLS-1$

		figureCanvas.setHorizontalScrollBarVisibility(FigureCanvas.ALWAYS);
		figureCanvas.setVerticalScrollBarVisibility(FigureCanvas.ALWAYS);
		Rectangle bounds = getViewportBounds(SWT.SCROLLBAR_OVERLAY);
		assertEquals("Viewport width:", WIDTH, bounds.width()); //$NON-NLS-1$
		assertEquals("Viewport height: ", HEIGHT, bounds.height()); //$NON-NLS-1$

		figureCanvas.setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
		figureCanvas.setVerticalScrollBarVisibility(FigureCanvas.NEVER);
		bounds = getViewportBounds(SWT.SCROLLBAR_OVERLAY);
		assertEquals("Viewport width:", WIDTH, bounds.width()); //$NON-NLS-1$
		assertEquals("Viewport height: ", HEIGHT, bounds.height()); //$NON-NLS-1$
	}

	/**
	 * With overlay scrolling disabled, the scrollbars require some of the available
	 * client area.
	 */
	@Test
	public void testGtkWithoutOverlayScrolling() {
		assumeTrue("gtk".equals(SWT.getPlatform())); //$NON-NLS-1$

		figureCanvas.setHorizontalScrollBarVisibility(FigureCanvas.ALWAYS);
		figureCanvas.setVerticalScrollBarVisibility(FigureCanvas.ALWAYS);
		Rectangle bounds = getViewportBounds(SWT.NONE);
		assertTrue("Expected non-empty scrollbar width", WIDTH > bounds.width()); //$NON-NLS-1$
		assertTrue("Expected non-empty scrollbar height", HEIGHT > bounds.height()); //$NON-NLS-1$

		figureCanvas.setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
		figureCanvas.setVerticalScrollBarVisibility(FigureCanvas.NEVER);
		bounds = getViewportBounds(SWT.NONE);
		assertEquals("Viewport width:", WIDTH, bounds.width()); //$NON-NLS-1$
		assertEquals("Viewport height: ", HEIGHT, bounds.height()); //$NON-NLS-1$
	}

	private Rectangle getViewportBounds(int scrollMode) {
		// force recalculation of scrollbars
		figureCanvas.setScrollbarsMode(scrollMode);
		shell.setVisible(false);
		shell.setVisible(true);

		AtomicBoolean layoutViewportCalled = new AtomicBoolean();
		UpdateListener layoutViewportListener = new UpdateListener.Stub() {
			@Override
			public void notifyValidating() {
				layoutViewportCalled.set(true);
			}
		};

		UpdateManager updateManager = figureCanvas.getLightweightSystem().getUpdateManager();
		updateManager.addUpdateListener(layoutViewportListener);
		updateManager.addInvalidFigure(new Figure());
		updateManager.performValidation();

		assertTrue("FigureCanvas.layoutViewport() has likely not been called!", layoutViewportCalled.get()); //$NON-NLS-1$
		return figureCanvas.getViewport().getBounds();
	}
}
