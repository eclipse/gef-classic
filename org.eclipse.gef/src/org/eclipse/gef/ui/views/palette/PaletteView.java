/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.views.palette;

import org.eclipse.ui.*;
import org.eclipse.ui.part.*;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Pratik Shah
 */
public class PaletteView 
	extends PageBookView
{

protected IPage createDefaultPage(PageBook book) {
	MessagePage page = new MessagePage();
	initPage(page);
	page.createControl(book);
	page.setMessage(GEFMessages.Palette_Not_Available);
	return page;
}
	
protected PageRec doCreatePage(IWorkbenchPart part) {
	// Try to get a custom palette page
	Object obj = part.getAdapter(IPalettePage.class);

	if (obj != null && obj instanceof IPage) {
		IPage page = (IPage) obj;
		page.createControl(getPageBook());
		initPage((IPageBookViewPage) page);
		return new PageRec(part, page);
	}
	// Use the default page by returning null
	return null;
}

protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
	rec.page.dispose();
}

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