/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.views.palette;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

import org.eclipse.gef.internal.GEFMessages;

/**
 * The GEF palette view
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public class PaletteView
	extends PageBookView
{

/**
 * The ID for this view.  This is the same as the String used to register this view
 * with the platform's extension point. 
 */
public static final String ID = "org.eclipse.gef.ui.palette_view"; //$NON-NLS-1$

/**
 * Creates a default page saying that a palette is not available.
 * 
 * @see org.eclipse.ui.part.PageBookView#createDefaultPage(org.eclipse.ui.part.PageBook)
 */
protected IPage createDefaultPage(PageBook book) {
	MessagePage page = new MessagePage();
	initPage(page);
	page.createControl(book);
	page.setMessage(GEFMessages.Palette_Not_Available);
	return page;
}

/**
 * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
 */
protected PageRec doCreatePage(IWorkbenchPart part) {
	// Try to get a custom palette page
	Object obj = part.getAdapter(PalettePage.class);

	if (obj != null && obj instanceof IPage) {
		IPage page = (IPage) obj;
		page.createControl(getPageBook());
		initPage((IPageBookViewPage) page);
		return new PageRec(part, page);
	}
	// Use the default page by returning null
	return null;
}

/**
 * @see	PageBookView#doDestroyPage(org.eclipse.ui.IWorkbenchPart, org.eclipse.ui.part.PageBookView.PageRec)
 */
protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
	rec.page.dispose();
}

/**
 * The view shows the palette associated with the active editor.
 * 
 * @see	PageBookView#getBootstrapPart()
 */
protected IWorkbenchPart getBootstrapPart() {
	IWorkbenchPage page = getSite().getPage();
	if (page != null)
		return page.getActiveEditor();
	return null;
}

/**
 * Only editors are important.
 * 
 * @see	PageBookView#isImportant(org.eclipse.ui.IWorkbenchPart)
 */
protected boolean isImportant(IWorkbenchPart part) {
	// Workaround for Bug# 69098 -- This should be removed when/if Bug# 70510 is fixed
	// We only want a palette page to be created when this view is visible in the current
	// perspective, i.e., when both this view and the given editor are on the same page.
	return part.getSite().getPage().findViewReference(ID) != null 
			&& part instanceof IEditorPart;
}

/**
 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
 */
public void partActivated(IWorkbenchPart part) {
	if (isImportant(part))
		super.partActivated(part);
	/*
	 * Workaround for Bug# 69098 -- This should be removed when/if Bug# 70510 is fixed
	 * As a result of the fix in isImportant(), a palette page is not created for an 
	 * editor if a view is the active part when the perspective changes.  If that is the 
	 * case, we fake a partActivated for the bootstrap part. 
	 */
	else if (getBootstrapPart() != null)
		super.partActivated(getBootstrapPart());
}

}
