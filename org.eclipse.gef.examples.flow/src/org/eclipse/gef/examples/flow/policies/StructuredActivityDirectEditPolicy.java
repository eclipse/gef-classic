package org.eclipse.gef.examples.flow.policies;

import org.eclipse.draw2d.Label;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gef.examples.flow.figures.SubgraphFigure;

/**
 * StructuredActivityDirectEditPolicy
 * @author Daniel Lee
 */
public class StructuredActivityDirectEditPolicy
	extends ActivityDirectEditPolicy {

/**
 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
 */
public Command getCommand(Request request) {
	if (RequestConstants.REQ_DIRECT_EDIT == request.getType()) {
		((DirectEditRequest)request).getLocation();
		return getDirectEditCommand((DirectEditRequest)request);
	}
	return null;
}

/**
 * @see DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
 */
protected void showCurrentEditValue(DirectEditRequest request) {
	String value = (String)request.getCellEditor().getValue();
	((Label)((SubgraphFigure)getHostFigure()).getHeader()).setText(value);
	((Label)((SubgraphFigure)getHostFigure()).getFooter()).setText("/"+value);//$NON-NLS-1$
	
	//hack to prevent async layout from placing the cell editor twice.
	getHostFigure().getUpdateManager().performUpdate();
}

}
