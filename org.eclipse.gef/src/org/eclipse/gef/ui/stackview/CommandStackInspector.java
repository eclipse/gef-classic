package org.eclipse.gef.ui.stackview;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;

import org.eclipse.ui.*;
import org.eclipse.ui.part.*;

//import org.eclipse.jface.*;
//import org.eclipse.jface.action.*;
//import org.eclipse.jface.viewers.*;

public class CommandStackInspector
	extends PageBookView
{

/**
 * Returns the default page.
 *
 * @return the default property sheet page.
 */
protected IPage createDefaultPage(PageBook book) {
	Page page = new Page(){
		Control control;
		public void createControl(Composite parent){
			control = new Canvas(parent, SWT.NONE);
		}
		public Control getControl(){
			return control;
		}
		public void setFocus(){}
	};

	page.createControl(book);
	return page;
}

/**
 * Answer a page for the part.
 */
protected PageRec doCreatePage(IWorkbenchPart part) {
	// Try to get a custom command stack page.
	Object obj= part.getAdapter(CommandStackInspectorPage.class);
	if (obj instanceof IPage) {
		IPage page= (IPage) obj;
		page.createControl(getPageBook());
		return new PageRec(part, page);
	}

	// Use the default page
	return null;
}

/**
 * Destroys a page in the pagebook.
 * <p>
 * Subclasses of PageBookView must implement the creation and
 * destruction of pages in the view.  This method should be implemented
 * by the subclass to destroy a page for the given part.
 * </p>
 * @param part the input part
 * @rec a page record for the part
 */
protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
	IPage page= (IPage) rec.page;
	page.dispose();
}

/**
 * Answer the active workbench part.
 */
protected IWorkbenchPart getBootstrapPart() {
	IWorkbenchPage persp = getSite().getWorkbenchWindow().getActivePage();
	if (persp != null)
		return persp.getActiveEditor();
	else
		return null;
}

protected boolean isImportant(IWorkbenchPart part) {
	return part instanceof IEditorPart;
}

}
