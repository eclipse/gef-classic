package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class LogicCreationWizard extends Wizard implements INewWizard {
	private CreateLogicPage1 logicPage = null;
	private IStructuredSelection selection;
	private IWorkbench workbench;

public void addPages(){
	logicPage = new CreateLogicPage1(workbench,selection);
	addPage(logicPage);
}

public void init(IWorkbench aWorkbench,IStructuredSelection currentSelection) {
	workbench = aWorkbench;
	selection = currentSelection;
}

public boolean performFinish(){
	return logicPage.finish();
}

}