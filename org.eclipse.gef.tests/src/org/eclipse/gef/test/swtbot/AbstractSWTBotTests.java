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

package org.eclipse.gef.test.swtbot;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;

import org.eclipse.draw2d.FigureCanvas;

import org.eclipse.gef.EditPart;

import org.junit.After;
import org.junit.Before;

/**
 * To be able to execute these tests in the Eclipse IDE, the tests must
 * <b>NOT</b> be run in the UI thread.
 */
@SuppressWarnings("nls")
public abstract class AbstractSWTBotTests {
	private ILogListener problemListener;
	private List<Throwable> problems;
	protected SWTGefBot bot;

	@Before
	public void setUp() throws Exception {
		if (Display.getCurrent() != null) {
			fail("""
					SWTBot test needs to run in a non-UI thread.
					Make sure that "Run in UI thread" is unchecked in your launch configuration or that useUIThread is set to false in the pom.xml
					""");
		}
		// Keep track of all unchecked exceptions
		problems = new ArrayList<>();
		problemListener = (status, plugin) -> {
			Throwable exception = status.getException();
			if (isRelevant(exception)) {
				problems.add(exception);
			}
		};
		Platform.addLogListener(problemListener);
		// Create SWTBot instance
		bot = new SWTGefBot();
	}

	/**
	 * Convenience method to filter all exceptions that are not thrown within
	 * GEF/Draw2D. This is to make sure that tests won't randomly fail due to
	 * (unrelated) exceptions from e.g. the Eclipse IDE.
	 */
	private static final boolean isRelevant(Throwable throwable) {
		if (throwable == null) {
			return false;
		}

		for (StackTraceElement element : throwable.getStackTrace()) {
			String className = element.getClassName();
			if (className.matches("org.eclipse.(draw2d|gef).*")) {
				return true;
			}
		}
		return false;
	}

	@After
	public void tearDown() throws Exception {
		// Cleanup problem listener and check for exceptions
		Platform.removeLogListener(problemListener);
		problems.forEach(Throwable::printStackTrace);
		assertTrue("Test threw an unchecked exception. Check logs for details", problems.isEmpty());
	}

	/**
	 * Convenience method that forces an update of the GEF viewer. This is necessary
	 * when e.g. moving a figure, as the new position of the edit part is calculated
	 * asynchronously by the update manager (which is not handled by SWTBot). This
	 * method must be run from within the UI thread.
	 */
	protected static void forceUpdate(SWTBotGefViewer viewer) {
		SWTBotGefEditPart editPart = viewer.rootEditPart();
		EditPart gefEditPart = editPart.part();
		FigureCanvas figureCanvas = (FigureCanvas) gefEditPart.getViewer().getControl();
		figureCanvas.getLightweightSystem().getUpdateManager().performUpdate();
	}
}
