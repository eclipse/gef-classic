package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.common.command.Command;
import com.ibm.etools.draw2d.Label;
import com.ibm.etools.gef.editpolicies.DirectEditPolicy;
import com.ibm.etools.gef.examples.logicdesigner.model.LogicLabel;
import com.ibm.etools.gef.examples.logicdesigner.model.LogicLabelCommand;
import com.ibm.etools.gef.requests.DirectEditRequest;

public class LabelDirectEditPolicy 
	extends DirectEditPolicy {

/**
 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
 */
protected Command getDirectEditCommand(DirectEditRequest edit) {
	String labelText = (String)edit.getCellEditor().getValue();
	LogicLabelEditPart label = (LogicLabelEditPart)getHost();
	LogicLabelCommand command = new LogicLabelCommand((LogicLabel)label.getModel(),labelText);
	return command;
}

/**
 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
 */
protected void showCurrentEditValue(DirectEditRequest request) {
	String value = (String)request.getCellEditor().getValue();
	((Label)getHostFigure()).setText(value);
	//hack to prevent async layout from placing the cell editor twice.
	getHostFigure().getUpdateManager().performUpdate();
}

}