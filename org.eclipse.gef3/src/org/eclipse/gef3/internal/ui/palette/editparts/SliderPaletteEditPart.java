/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.internal.ui.palette.editparts;

import org.eclipse.draw2dl.ColorConstants;
import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.ToolbarLayout;

import org.eclipse.gef3.palette.PaletteRoot;
import org.eclipse.gef3.ui.palette.PaletteViewer;
import org.eclipse.gef3.ui.palette.editparts.PaletteAnimator;
import org.eclipse.gef3.ui.palette.editparts.PaletteEditPart;
import org.eclipse.gef3.ui.palette.editparts.PaletteToolbarLayout;
import org.eclipse.gef3.editparts.AbstractGraphicalEditPart;

public class SliderPaletteEditPart extends PaletteEditPart {

	private PaletteAnimator controller;

	public SliderPaletteEditPart(PaletteRoot paletteRoot) {
		super(paletteRoot);
	}

	public IFigure createFigure() {
		Figure figure = new Figure();
		figure.setOpaque(true);
		figure.setForegroundColor(ColorConstants.listForeground);
		figure.setBackgroundColor(ColorConstants.listBackground);
		return figure;
	}

	/**
	 * This method overrides super's functionality to do nothing.
	 * 
	 * @see PaletteEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
	}

	/**
	 * @see AbstractGraphicalEditPart#registerVisuals()
	 */
	protected void registerVisuals() {
		super.registerVisuals();
		controller = new PaletteAnimator(
				((PaletteViewer) getViewer()).getPaletteViewerPreferences());
		getViewer().getEditPartRegistry()
				.put(PaletteAnimator.class, controller);
		ToolbarLayout layout = new PaletteToolbarLayout();
		getFigure().setLayoutManager(layout);
		getFigure().addLayoutListener(controller);
	}

}
