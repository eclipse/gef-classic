package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ibm.etools.draw2d.ConnectionAnchor;
import com.ibm.etools.draw2d.IFigure;
import com.ibm.etools.gef.AccessibleAnchorProvider;
import com.ibm.etools.gef.examples.logicdesigner.figures.GateFigure;
import com.ibm.etools.gef.examples.logicdesigner.figures.OutputFigure;

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
	if (getModel() == null)
		return null;
	OutputFigure figure = new GateFigure();
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
