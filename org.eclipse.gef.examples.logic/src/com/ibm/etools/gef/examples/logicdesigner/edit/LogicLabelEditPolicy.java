package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.common.command.Command;
import com.ibm.etools.gef.EditPart;
import com.ibm.etools.gef.GraphicalEditPart;
import com.ibm.etools.gef.Request;
import com.ibm.etools.gef.examples.logicdesigner.model.LogicLabel;
import com.ibm.etools.gef.examples.logicdesigner.model.LogicLabelCommand;

public class LogicLabelEditPolicy 
	extends LogicElementEditPolicy 
{

public Command getCommand(Request request) {
	if (NativeDropRequest.ID.equals(request.getType()))
		return getDropTextCommand((NativeDropRequest)request);
	return super.getCommand(request);
}

protected Command getDropTextCommand(NativeDropRequest request) {
	LogicLabelCommand command = new LogicLabelCommand((LogicLabel)getHost().getModel(), (String)request.getData());
	return command;
}

public EditPart getTargetEditPart(Request request) {
	if (NativeDropRequest.ID.equals(request.getType()))
		return getHost();
	return super.getTargetEditPart(request);
}

}
