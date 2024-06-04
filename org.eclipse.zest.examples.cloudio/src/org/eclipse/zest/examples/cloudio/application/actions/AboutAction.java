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
package org.eclipse.zest.examples.cloudio.application.actions;

import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.zest.examples.cloudio.application.about.AboutDialog;

/**
 *
 * @author sschwieb
 *
 */
public class AboutAction extends Action implements IWorkbenchAction {

	public AboutAction() {
		super.setId("about");
		setText("About");
	}

	@Override
	public void run() {
		AboutDialog dialog = new AboutDialog(Display.getCurrent().getActiveShell());
		dialog.open();
	}

	@Override
	public void dispose() {

	}

}
