package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gef.examples.logicdesigner.LogicColorConstants;
import org.eclipse.gef.examples.logicdesigner.figures.CircuitFigure;

/**
 * Holds a circuit, which is a container capable of 
 * holding other LogicEditParts.
 */
public class CircuitEditPart
	extends LogicContainerEditPart
{

protected void createEditPolicies(){
	super.createEditPolicies();
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


public void setSelected(int i){
	super.setSelected(i);
	refreshVisuals();
}

}
