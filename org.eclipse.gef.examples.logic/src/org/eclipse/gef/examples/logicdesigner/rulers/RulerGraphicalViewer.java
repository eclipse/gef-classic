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
package org.eclipse.gef.examples.logicdesigner.rulers;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.draw2d.*;

import org.eclipse.gef.ui.parts.GraphicalViewerImpl;

/**
 * @author Pratik Shah
 */
public class RulerGraphicalViewer 
	extends GraphicalViewerImpl 
{

private boolean horizontal;

public RulerGraphicalViewer(boolean isHorizontal) {
	horizontal = isHorizontal;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#createControl(org.eclipse.swt.widgets.Composite)
 */
public Control createControl(Composite composite) {
	FigureCanvas canvas = new FigureCanvas(composite, getLightweightSystem());
//	canvas.setViewport(new RulerViewport(horizontal, 
//			canvas.getLightweightSystem().getUpdateManager()));
	canvas.setContents(new Figure());
	canvas.getContents().setLayoutManager(new ToolbarLayout(!horizontal));
	canvas.setScrollBarVisibility(FigureCanvas.NEVER);
//	if (editor != null) {
//		if (isHorizontal) {
//			canvas.getViewport().setHorizontalRangeModel(
//					editor.getViewport().getHorizontalRangeModel());
//		} else {
//			canvas.getViewport().setVerticalRangeModel(
//					editor.getViewport().getVerticalRangeModel());
//		}
//	}
	return canvas;

}

}
