package org.eclipse.gef.examples.flow.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import org.eclipse.gef.examples.flow.ui.*;

/**
 * FlowCreationWizard
 * @author Daniel Lee
 */
public class FlowCreationWizard extends Wizard implements INewWizard {

private FlowWizardPage1 flowWizardPage;
private IStructuredSelection selection;
private IWorkbench workbench;

/**
 * @see org.eclipse.jface.wizard.IWizard#addPages()
 */
public void addPages(){
	flowWizardPage = new FlowWizardPage1(workbench,selection);
	addPage(flowWizardPage);
}

/**
 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
 */
public void init(IWorkbench aWorkbench,IStructuredSelection currentSelection) {
	workbench = aWorkbench;
	selection = currentSelection;
}

/**
 * @see org.eclipse.jface.wizard.IWizard#performFinish()
 */
public boolean performFinish(){
	return flowWizardPage.finish();
}

}
