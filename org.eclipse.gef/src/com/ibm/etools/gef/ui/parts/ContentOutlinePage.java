package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.viewers.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ibm.etools.gef.EditPartViewer;

public class ContentOutlinePage 
	extends org.eclipse.ui.part.Page 
	implements org.eclipse.ui.views.contentoutline.IContentOutlinePage
{

private EditPartViewer viewer;
private Control control;

/**
 * The constructor for the Outline Page.  It takes an EditPartViewer.
 */
public ContentOutlinePage(EditPartViewer viewer){
	this.viewer = viewer;
}

/* (non-Javadoc)
 * Method declared on ISelectionProvider.
 */
public void addSelectionChangedListener(ISelectionChangedListener listener){
	getViewer().addSelectionChangedListener(listener);
}

/**
 * Creates the control for the viewer.  
 */
public void createControl(Composite parent){
	control = getViewer().createControl(parent);
}

public void dispose(){
	getViewer().dispose();
	super.dispose();
}

/**
 * Returns the control of the GEFgetViewer()
 */
public Control getControl(){
	return control;
}

/* (non-Javadoc)
 * Method declared on ISelectionProvider.
 */
public ISelection getSelection(){
	if (getViewer() == null)
		return StructuredSelection.EMPTY;
	return getViewer().getSelection();
}

protected EditPartViewer getViewer(){
	return viewer;
}

/* (non-Javadoc)
 * Method declared on ISelectionProvider.
 */
public void removeSelectionChangedListener(ISelectionChangedListener listener){
	getViewer().removeSelectionChangedListener(listener);
}

/**
 * Sets focus to a part in the page.
 */
public void setFocus(){
	if (getControl() != null)
		getControl().setFocus();
}

/* (non-Javadoc)
 * Method declared on ISelectionProvider.
 */
public void setSelection(ISelection selection){
	if (getViewer() != null)
		getViewer().setSelection(selection);
}

}
