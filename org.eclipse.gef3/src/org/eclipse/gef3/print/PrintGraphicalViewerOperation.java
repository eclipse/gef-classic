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
package org.eclipse.gef3.print;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2dl.Layer;
import org.eclipse.draw2dl.PrintOperation;
import org.eclipse.swt.printing.Printer;

import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.PrintFigureOperation;

import org.eclipse.gef3.GraphicalViewer;
import org.eclipse.gef3.LayerConstants;
import org.eclipse.gef3.editparts.LayerManager;

/**
 * @author danlee
 */
public class PrintGraphicalViewerOperation extends PrintFigureOperation {

	private GraphicalViewer viewer;
	private List selectedEditParts;

	/**
	 * Constructor for PrintGraphicalViewerOperation
	 * 
	 * @param p
	 *            The Printer to print to
	 * @param g
	 *            The viewer containing what is to be printed NOTE: The
	 *            GraphicalViewer to be printed must have a
	 *            {@link Layer Layer} with the
	 *            {@link org.eclipse.gef3.LayerConstants PRINTABLE_LAYERS} key.
	 */
	public PrintGraphicalViewerOperation(Printer p, GraphicalViewer g) {
		super(p);
		viewer = g;
		LayerManager lm = (LayerManager) viewer.getEditPartRegistry().get(
				LayerManager.ID);
		IFigure f = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
		setPrintSource(f);
	}

	/**
	 * Returns the viewer.
	 * 
	 * @return GraphicalViewer
	 */
	public GraphicalViewer getViewer() {
		return viewer;
	}

	/**
	 * @see PrintOperation#preparePrintSource()
	 */
	protected void preparePrintSource() {
		super.preparePrintSource();
		selectedEditParts = new ArrayList(viewer.getSelectedEditParts());
		viewer.deselectAll();
	}

	/**
	 * @see PrintOperation#restorePrintSource()
	 */
	protected void restorePrintSource() {
		super.restorePrintSource();
		viewer.setSelection(new StructuredSelection(selectedEditParts));
	}

	/**
	 * Sets the viewer.
	 * 
	 * @param viewer
	 *            The viewer to set
	 */
	public void setViewer(GraphicalViewer viewer) {
		this.viewer = viewer;
	}

}
