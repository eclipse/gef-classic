package com.ibm.etools.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.DirectEditRequest;

abstract public class DirectEditPolicy
	extends GraphicalEditPolicy
{

private boolean showing;

public void eraseSourceFeedback(Request request) {
	if (RequestConstants.REQ_DIRECT_EDIT == request.getType())
		eraseDirectEditFeedback((DirectEditRequest)request);
}

protected void eraseDirectEditFeedback(DirectEditRequest request){
	if (showing){
		revertOldEditValue(request);
		showing = false;
	}
}

public Command getCommand(Request request) {
	if (RequestConstants.REQ_DIRECT_EDIT == request.getType())
		return getDirectEditCommand((DirectEditRequest)request);
	return null;
}

abstract protected Command getDirectEditCommand(DirectEditRequest edit);

protected void revertOldEditValue(DirectEditRequest request){
	getHost().refresh();
}

public void showSourceFeedback(Request request) {
	if (RequestConstants.REQ_DIRECT_EDIT == request.getType())
		showDirectEditFeedback((DirectEditRequest)request);
}

protected void showDirectEditFeedback(DirectEditRequest request){
	if (!showing){
		storeOldEditValue(request);
		showing = true;
	}
	showCurrentEditValue(request);
}

abstract protected void showCurrentEditValue(DirectEditRequest request);

protected void storeOldEditValue(DirectEditRequest request){
}

}
