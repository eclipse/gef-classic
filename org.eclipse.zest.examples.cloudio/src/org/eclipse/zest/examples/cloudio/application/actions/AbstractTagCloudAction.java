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

import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.zest.cloudio.TagCloudViewer;
import org.eclipse.zest.examples.cloudio.application.ui.TagCloudViewPart;

/**
 *
 * @author sschwieb
 *
 */
public abstract class AbstractTagCloudAction implements IWorkbenchWindowActionDelegate {

	private Shell shell;
	private TagCloudViewPart tcViewPart;

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.shell = window.getShell();
		tcViewPart = (TagCloudViewPart) window.getActivePage().getActivePart();
	}

	public Shell getShell() {
		return shell;
	}

	protected TagCloudViewer getViewer() {
		return tcViewPart.getViewer();
	}

}
