package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;

public class LogicWizardPage1 
	extends WizardNewFileCreationPage 
	implements MouseListener
{

private IWorkbench	workbench;
private static int exampleCount = 1;
private Button model1 = null;
private Button model2 = null;
private int modelSelected = 1;

public LogicWizardPage1(IWorkbench aWorkbench, IStructuredSelection selection) {
	super("sampleLogicPage1", selection);  //$NON-NLS-1$
	this.setTitle(LogicMessages.CreateLogicPage1_Title);
	this.setDescription(LogicMessages.CreateLogicPage1_Description);
	this.setImageDescriptor(ImageDescriptor.createFromFile(getClass(),"icons/logic.gif"));  //$NON-NLS-1$
	this.workbench = aWorkbench;
}

public void createControl(Composite parent) {
	super.createControl(parent);
	this.setFileName("emptyModel" + exampleCount + ".logic");  //$NON-NLS-2$//$NON-NLS-1$
	
	Composite composite = (Composite)getControl();
	
	new Label(composite,SWT.NONE);
	
	// sample section generation group
	Group group = new Group(composite,SWT.NONE);
	group.setLayout(new GridLayout());
	group.setText(LogicMessages.CreateLogicPage1_ModelNames_GroupName); 
	group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
	
	// sample section generation checkboxes
	model1 = new Button(group,SWT.RADIO);
	model1.setText(LogicMessages.CreateLogicPage1_ModelNames_EmptyModelName);
	model1.addMouseListener(this);
	model1.setSelection(true);

	model2 = new Button(group,SWT.RADIO);
	model2.setText(LogicMessages.CreateLogicPage1_ModelNames_FourBitAdderModelName);
	model2.addMouseListener(this);
	
	new Label(composite,SWT.NONE);
	
	setPageComplete(validatePage());
}

protected InputStream getInitialContents() {
	LogicDiagram ld = new LogicDiagram();
	if (modelSelected == 2)
			ld = (LogicDiagram)LogicDiagramFactory.createLargeModel();
	ByteArrayInputStream bais = null;
	try {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(ld);
		oos.flush();
		oos.close();
		baos.close();
		bais = new ByteArrayInputStream(baos.toByteArray());
		bais.close();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	return bais;
}

public boolean finish() {
	IFile newFile = createNewFile();
	if (newFile == null) 
		return false;  // ie.- creation was unsuccessful

	// Since the file resource was created fine, open it for editing
	// iff requested by the user
	try {
		IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = dwindow.getActivePage();
		if (page != null) 
			page.openEditor(newFile);
	} 
	catch (org.eclipse.ui.PartInitException e) {
		e.printStackTrace();
		return false;
	}
	exampleCount++;
	return true;
}

public void mouseDoubleClick(MouseEvent e) {}

public void mouseDown(MouseEvent e) {}

public void mouseUp(MouseEvent e) {
	Widget w = e.widget;
	if (w.equals(model1)) {
		modelSelected = 1;
		setFileName("emptyModel" + exampleCount + ".logic");  //$NON-NLS-2$//$NON-NLS-1$
	}
	if (w.equals(model2)) {
		modelSelected = 2;
		setFileName("fourBitAdder" + exampleCount + ".logic");  //$NON-NLS-2$//$NON-NLS-1$
	}
}

}