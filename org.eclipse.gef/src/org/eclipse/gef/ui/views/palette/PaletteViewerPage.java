/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.views.palette;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.part.Page;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;

/**
 * @author Pratik Shah
 */
public class PaletteViewerPage 
	extends Page
	implements PalettePage, IAdaptable {
	
protected PaletteViewerProvider provider;
protected PaletteViewer viewer;
	
public PaletteViewerPage(PaletteViewerProvider pvProvider) {
	Assert.isNotNull(pvProvider);
	provider = pvProvider;
}

public void createControl(Composite parent) {
	viewer = provider.createPaletteViewer(parent);
}

public void dispose() {
	if (provider.getEditDomain().getPaletteViewer() == viewer)
		provider.getEditDomain().setPaletteViewer(null);
	super.dispose();
	viewer = null;
}

public Object getAdapter(Class adapter) {
	if (adapter == EditPart.class && viewer != null)
		return viewer.getEditPartRegistry().get(viewer.getPaletteRoot());
	if (adapter == IFigure.class && viewer != null) {
		Object obj = viewer.getEditPartRegistry().get(viewer.getPaletteRoot());
		if (obj instanceof GraphicalEditPart)
			return ((GraphicalEditPart)obj).getFigure();
	}
	return null;
}

public Control getControl() {
	return viewer.getControl();
}

public void setFocus() {
	getControl().setFocus();
}

}