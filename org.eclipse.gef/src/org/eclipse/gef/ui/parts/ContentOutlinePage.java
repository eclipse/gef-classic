/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.parts;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

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
