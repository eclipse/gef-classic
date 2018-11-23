/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * FlowCreationWizard
 * 
 * @author Daniel Lee
 */
public class FlowCreationWizard extends Wizard implements INewWizard {

	private FlowWizardPage1 flowWizardPage;
	private IStructuredSelection selection;
	private IWorkbench workbench;

	/**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		flowWizardPage = new FlowWizardPage1(workbench, selection);
		addPage(flowWizardPage);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench aWorkbench,
			IStructuredSelection currentSelection) {
		workbench = aWorkbench;
		selection = currentSelection;
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		return flowWizardPage.finish();
	}

}
