package com.ibm.etools.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.SWT;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.examples.logicdesigner.model.*;
import java.io.*;

public class CreateLogicPage1 
	extends WizardNewFileCreationPage 
	implements MouseListener{

private IWorkbench	workbench;
private static int exampleCount = 1;
private Button model1 = null;
private Button model2 = null;
private Button model3 = null;
private Button model4 = null;
private int modelSelected = 1;

public CreateLogicPage1(IWorkbench aWorkbench, IStructuredSelection selection) {
	super("sampleLogicPage1", selection);  //$NON-NLS-1$
	this.setTitle(LogicResources.getString("CreateLogicPage1.Title")); //$NON-NLS-1$
	this.setDescription(LogicResources.getString("CreateLogicPage1.Description"));  //$NON-NLS-1$
	this.setImageDescriptor(ImageDescriptor.createFromFile(getClass(),"icons/logic.gif"));  //$NON-NLS-1$
	this.workbench = aWorkbench;
}

public void createControl(Composite parent){
	super.createControl(parent);
	this.setFileName("sampleLogic"+exampleCount+".logic");//$NON-NLS-2$//$NON-NLS-1$
	
	Composite composite = (Composite)getControl();
	
	new Label(composite,SWT.NONE);
	
	// sample section generation group
	Group group = new Group(composite,SWT.NONE);
	group.setLayout(new GridLayout());
	group.setText(LogicResources.getString("CreateLogicPage1.ModelNames.GroupName"));  //$NON-NLS-1$
	group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
	
	// sample section generation checkboxes
	model1 = new Button(group,SWT.RADIO);
	model1.setText(LogicResources.getString("CreateLogicPage1.ModelNames.EmptyModelName"));//$NON-NLS-1$
	model1.addMouseListener(this);
	
	model2 = new Button(group,SWT.RADIO);
	model2.setText(LogicResources.getString("CreateLogicPage1.ModelNames.HalfAdderModelName"));//$NON-NLS-1$
	model2.addMouseListener(this);
	
	model3 = new Button(group,SWT.RADIO);
	model3.setText(LogicResources.getString("CreateLogicPage1.ModelNames.FullAdderModelName"));//$NON-NLS-1$
	model3.addMouseListener(this);
	
	model4 = new Button(group,SWT.RADIO);
	model4.setText(LogicResources.getString("CreateLogicPage1.ModelNames.FourBitAdderModelName"));//$NON-NLS-1$
	model4.addMouseListener(this);
	
	new Label(composite,SWT.NONE);
	
	setPageComplete(validatePage());
}

protected java.io.InputStream getInitialContents() {
	LogicDiagram ld = new LogicDiagram();
	switch(modelSelected){
		case 1:
			break;
		case 2:
			ld.addChild(LogicDiagramFactory.createHalfAdder());
			break;
		case 3:
			ld.addChild(LogicDiagramFactory.createFullAdder());
			break;
		case 4:
			ld = (LogicDiagram)LogicDiagramFactory.createLargeModel();
			break;
	}
	ByteArrayInputStream bais = null;
	try{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(ld);
		oos.flush();
		oos.close();
		baos.close();
		bais = new ByteArrayInputStream(baos.toByteArray());
		bais.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	return bais;
}

public boolean finish(){
	org.eclipse.core.resources.IFile newFile = createNewFile();
	if (newFile == null)return false;// ie.- creation was unsuccessful

	// Since the file resource was created fine, open it for editing
	// iff requested by the user
	try {
		org.eclipse.ui.IWorkbenchWindow dwindow = 
			workbench.getActiveWorkbenchWindow();
		org.eclipse.ui.IWorkbenchPage page = 
			dwindow.getActivePage();
		if (page != null) page.openEditor(newFile);
	} catch (org.eclipse.ui.PartInitException e) {
		e.printStackTrace();
		return false;
	}
	exampleCount++;
	return true;
}

public void mouseDoubleClick(MouseEvent e){;}
public void mouseDown(MouseEvent e){;}
public void mouseUp(MouseEvent e){
	Widget w = e.widget;
	if( w.equals(model1) ){
		modelSelected=1;
		this.setFileName(new String(
			"emptyModel"+exampleCount+".logic"));//$NON-NLS-2$//$NON-NLS-1$
	}
	if( w.equals(model2) ){
		modelSelected=2;
		this.setFileName(new String(
			"halfAdder"+exampleCount+".logic"));//$NON-NLS-2$//$NON-NLS-1$
	}
	if( w.equals(model3) ){
		modelSelected=3;
		this.setFileName(new String(
			"fullAdder"+exampleCount+".logic"));//$NON-NLS-2$//$NON-NLS-1$
	}
	if( w.equals(model4) ){
		modelSelected=4;
		this.setFileName(new String(
			"fourBitAdder"+exampleCount+".logic"));//$NON-NLS-2$//$NON-NLS-1$
	}
}

}