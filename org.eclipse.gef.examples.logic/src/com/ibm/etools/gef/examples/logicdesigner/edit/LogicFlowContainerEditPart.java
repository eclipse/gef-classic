package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.gef.EditPolicy;
import com.ibm.etools.gef.examples.logicdesigner.LogicColorConstants;

import com.ibm.etools.gef.examples.logicdesigner.figures.LogicFlowBorder;

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

protected LogicFlowBorder getLogicFlowBorder(){
	if(getFigure()!=null)
		return (LogicFlowBorder)getFigure().getBorder();
	return null;
}

public void setSelected(int i){
	super.setSelected(i);
	refreshVisuals();
}

protected void refreshVisuals(){
	super.refreshVisuals();
	int i = getSelected();
	if (getLogicFlowBorder()!=null){
		if (i == SELECTED_NONE)
			getLogicFlowBorder().setGrabBarColor(
				LogicColorConstants.logicGreen);
		if (i == SELECTED_PRIMARY)
			getLogicFlowBorder().setGrabBarColor(
				LogicColorConstants.logicPrimarySelectedColor);
		if (i == SELECTED)
			getLogicFlowBorder().setGrabBarColor(
				LogicColorConstants.logicSecondarySelectedColor);
	}
}

}