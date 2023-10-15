/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class LogicCreationWizard extends Wizard implements INewWizard {
	private LogicWizardPage1 logicPage = null;
	private IStructuredSelection selection;
	private IWorkbench workbench;

	@Override
	public void addPages() {
		logicPage = new LogicWizardPage1(workbench, selection);
		addPage(logicPage);
	}

	@Override
	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		workbench = aWorkbench;
		selection = currentSelection;
	}

	@Override
	public boolean performFinish() {
		return logicPage.finish();
	}

}
