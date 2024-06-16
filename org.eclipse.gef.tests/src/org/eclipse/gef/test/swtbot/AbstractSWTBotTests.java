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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

import org.eclipse.draw2d.FigureCanvas;

import org.eclipse.gef.EditPart;

import org.junit.After;
import org.junit.Before;
import org.osgi.framework.Bundle;

/**
 * To be able to execute these tests in the Eclipse IDE, the tests must
 * <b>NOT</b> be run in the UI thread.
 */
@SuppressWarnings("nls")
public abstract class AbstractSWTBotTests {
	private static final String PROJECT_NAME = "TestProject";
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
		// This bundle disables the examples via an activityPatternBinding
		Bundle bundle = Platform.getBundle("org.eclipse.gef.examples.ui.capabilities");
		if (bundle != null) {
			bundle.uninstall();
		}
		IWorkbench wb = PlatformUI.getWorkbench();
		Display display = wb.getDisplay();
		// Close "Welcome" page
		IIntroManager im = wb.getIntroManager();
		IIntroPart intro = wb.getIntroManager().getIntro();
		if (intro != null) {
			display.syncExec(() -> im.closeIntro(intro));
		}
		// Switch to the "Resource" perspective
		display.syncCall(() -> {
			IWorkbenchWindow ww = wb.getActiveWorkbenchWindow();
			return wb.showPerspective("org.eclipse.ui.resourcePerspective", ww);
		});
		// Create & open new test project
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		display.syncCall(() -> {
			if (!project.exists()) {
				project.create(null);
			}
			project.open(null);
			return null;
		});
		// Create new example file
		IWizardRegistry wr = wb.getNewWizardRegistry();
		IWizardDescriptor wd = wr.findWizard(getWizardId());
		display.syncCall(() -> {
			IWorkbenchWizard wizard = wd.createWizard();
			wizard.init(wb, new StructuredSelection(project));
			WizardDialog wizardDialog = new WizardDialog(display.getActiveShell(), wizard);
			wizardDialog.setBlockOnOpen(false);
			wizardDialog.open();
			WizardNewFileCreationPage wizardPage = (WizardNewFileCreationPage) wizardDialog.getCurrentPage();
			wizardPage.setFileName(getFileName());
			wizard.performFinish();
			wizardDialog.close();
			return null;
		});
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
		IWorkbench wb = PlatformUI.getWorkbench();
		Display display = wb.getDisplay();
		// Close all open editors
		display.syncCall(() -> {
			IWorkbenchWindow ww = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = ww.getActivePage();
			return page.closeAllEditors(false);
		});
		// Delete test project
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		display.syncCall(() -> {
			project.delete(true, null);
			return null;
		});
		// Cleanup problem listener and check for exceptions
		Platform.removeLogListener(problemListener);
		problems.forEach(Throwable::printStackTrace);
		assertTrue("Test threw an unchecked exception. Check logs for details", problems.isEmpty());
	}

	/**
	 * @return The id of the wizard with which the test resources is created.
	 */
	protected abstract String getWizardId();

	/**
	 * @return The file name (with extension) of the test resource.
	 */
	protected abstract String getFileName();

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
