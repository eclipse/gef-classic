/*******************************************************************************
 * Copyright (c) 2011, 2024 Stephan Schwiebert and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Stephan Schwiebert - initial API and implementation
 ******************************************************************************/
package org.eclipse.zest.examples.cloudio.application;

import org.eclipse.swt.widgets.Display;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 *
 * @author sschwieb
 *
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return;
		}
		final Display display = workbench.getDisplay();
		display.syncExec(() -> {
			if (!display.isDisposed()) {
				workbench.close();
			}
		});
	}

}
