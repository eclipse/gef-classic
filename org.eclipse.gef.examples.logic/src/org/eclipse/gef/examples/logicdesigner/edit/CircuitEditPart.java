/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;

import org.eclipse.gef.examples.logicdesigner.figures.CircuitFigure;
import org.eclipse.gef.examples.logicdesigner.figures.FigureFactory;

/**
 * Holds a circuit, which is a container capable of 
 * holding other LogicEditParts.
 */
public class CircuitEditPart
	extends LogicContainerEditPart
{

protected void createEditPolicies(){
	super.createEditPolicies();
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new LogicXYLayoutEditPolicy(
			(XYLayout)getContentPane().getLayoutManager()));
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
}

/**
 * Creates a new Circuit Figure and returns it.
 *
 * @return  Figure representing the circuit.
 */
protected IFigure createFigure() {
	return FigureFactory.createNewCircuit();
}

public Object getAdapter(Class key) {
	if (key == AutoexposeHelper.class)
		return new ViewportAutoexposeHelper(this);
	if (key == ExposeHelper.class)
		return new ViewportExposeHelper(this);
	if (key == AccessibleAnchorProvider.class)
		return new DefaultAccessibleAnchorProvider() { 
			public List getSourceAnchorLocations() {
				List list = new ArrayList();
				Vector sourceAnchors = getNodeFigure().getSourceConnectionAnchors();
				Vector targetAnchors = getNodeFigure().getTargetConnectionAnchors();
				for (int i=0; i<sourceAnchors.size(); i++) {
					ConnectionAnchor sourceAnchor = (ConnectionAnchor)sourceAnchors.get(i);
					ConnectionAnchor targetAnchor = (ConnectionAnchor)targetAnchors.get(i);
					list.add(new Rectangle(sourceAnchor.getReferencePoint(), targetAnchor.getReferencePoint()).getCenter());
				}
				return list;
			}
			public List getTargetAnchorLocations() {
				return getSourceAnchorLocations();
			}
		};
	return super.getAdapter(key);
}

/**
 * Returns the Figure of this as a CircuitFigure.
 *
 * @return CircuitFigure of this.
 */
protected CircuitFigure getCircuitBoardFigure() {
	return (CircuitFigure)getFigure();
}

public IFigure getContentPane() {
	return getCircuitBoardFigure().getContentsPane();
}

}
