package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.AccessibleEditPart;

import org.eclipse.gef.examples.logicdesigner.figures.OutputFigure;
import org.eclipse.gef.examples.logicdesigner.model.SimpleOutput;

/**
 * EditPart for Output types in Logic Example
 */
public class OutputEditPart
	extends LogicEditPart
{

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart(){
		public void getName(AccessibleEvent e) {
			e.result = getSimpleOutput().toString();
		}
	};
}

/**
 * Returns a newly created Figure.
 */
protected IFigure createFigure() {
	if (getModel() == null)
		return null;
	OutputFigure figure = new OutputFigure();
	figure.setImage(getSimpleOutput().getIcon());
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

		};
	return super.getAdapter(key);
}

/**
 * Returns the Figure for this as an OutputFigure.
 * @return  Figure of this as a OutputFigure.
 */
protected OutputFigure getOutputFigure() {
	return (OutputFigure)getFigure();
}

/**
 * Returns the model of this as a SimpleOutput.
 * @return  Model of this as a SimpleOutput.
 */
protected SimpleOutput getSimpleOutput() {
	return (SimpleOutput)getModel();
}

}
