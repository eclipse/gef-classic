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

import org.eclipse.gef.AccessibleAnchorProvider;

import org.eclipse.gef.examples.logicdesigner.figures.AndGateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.GateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OrGateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OutputFigure;
import org.eclipse.gef.examples.logicdesigner.figures.XOrGateFigure;
import org.eclipse.gef.examples.logicdesigner.model.AndGate;
import org.eclipse.gef.examples.logicdesigner.model.OrGate;
import org.eclipse.gef.examples.logicdesigner.model.XORGate;

/**
 * EditPart for holding gates in the Logic Example.
 */
public class GateEditPart
	extends OutputEditPart
{

/**
 * Returns a newly created Figure of this.
 *
 * @return A new Figure of this.
 */
protected IFigure createFigure() {
	OutputFigure figure;
	if (getModel() == null)
		return null;
	if (getModel() instanceof OrGate)	
		figure = new OrGateFigure();
	else if (getModel() instanceof AndGate)
		figure = new AndGateFigure();
	else if (getModel() instanceof XORGate)
		figure = new XOrGateFigure();
	else
		figure = new GateFigure();
	return figure;
}

public Object getAdapter(Class key) {
	if (key == AccessibleAnchorProvider.class)
		return new DefaultAccessibleAnchorProvider() { 
			public List getSourceAnchorLocations() {
				List list = new ArrayList();
				Vector sourceAnchors = getNodeFigure().getSourceConnectionAnchors();
				for (int i=0; i<sourceAnchors.size(); i++) {
					ConnectionAnchor anchor = (ConnectionAnchor)sourceAnchors.get(i);
					list.add(anchor.getReferencePoint().getTranslated(0, -3));
				}
				return list;
			}
			public List getTargetAnchorLocations() {
				List list = new ArrayList();
				Vector targetAnchors = getNodeFigure().getTargetConnectionAnchors();
				for (int i=0; i<targetAnchors.size(); i++) {
					ConnectionAnchor anchor = (ConnectionAnchor)targetAnchors.get(i);
					list.add(anchor.getReferencePoint());
				}
				return list;
			}
		};
	return super.getAdapter(key);
}

}
