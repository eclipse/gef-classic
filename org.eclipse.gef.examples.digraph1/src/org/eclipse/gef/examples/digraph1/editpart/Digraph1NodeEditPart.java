/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef.examples.digraph1.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.examples.digraph1.figure.Digraph1NodeFigure;
import org.eclipse.gef.examples.digraph1.model.Digraph1Node;

/**
 * The edit part which describes a node in the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph1NodeEditPart extends AbstractGraphicalEditPart {

	/*
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		return false;
	}

	/*
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		/* not implemented */
	}

	/*
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		int number = ((Digraph1Node) getModel()).getNumber();
		return new Digraph1NodeFigure(number);
	}

	/*
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#registerVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		Digraph1NodeFigure nodeFigure = (Digraph1NodeFigure) getFigure();
		Point location = nodeFigure.getRectangleFigure().getLocation();
		Dimension size = nodeFigure.getRectangleFigure().getSize();
		Digraph1GraphEditPart graph = (Digraph1GraphEditPart) getParent();
		Rectangle constraint = new Rectangle(location, size);
		graph.setLayoutConstraint(this, nodeFigure, constraint);
	}
}
