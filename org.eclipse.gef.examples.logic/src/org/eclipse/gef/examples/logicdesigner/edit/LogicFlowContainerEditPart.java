package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.EditPolicy;

import org.eclipse.gef.examples.logicdesigner.figures.LogicFlowBorder;

public class LogicFlowContainerEditPart 
	extends LogicContainerEditPart 
{

protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(EditPolicy.NODE_ROLE, null);
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new LogicFlowEditPolicy());
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
}

protected IFigure createFigure() {
	Figure figure = new Figure();
	figure.setLayoutManager(new FlowLayout());
	figure.setBorder(new LogicFlowBorder());
	figure.setOpaque(true);
	return figure;
}  

}