package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.*;

abstract public class GraphicalNodeEditPolicy
	extends GraphicalEditPolicy
{

protected FeedbackHelper feedbackHelper;
protected Connection connectionFeedback;

protected Connection createDummyConnection(Request req){
	return new PolylineConnection();
}

protected void eraseCreationFeedback(CreateConnectionRequest request){
	if (connectionFeedback != null){
		removeFeedback(connectionFeedback);
		feedbackHelper = null;
		connectionFeedback = null;
	}
}

/**
 * Erase feedback indicating that the receiver object is 
 * being dragged.  This method is called when a drag is
 * completed or cancelled on the receiver object.
 * @param dragTracker org.eclipse.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
public void eraseSourceFeedback(Request request) {
	if (REQ_CONNECTION_END.equals(request.getType()))
	{
		eraseCreationFeedback((CreateConnectionRequest)request);
		debugFeedback("Request to erase \"" + request.getType() + "\" source feedback");//$NON-NLS-2$//$NON-NLS-1$
	}
}

/**
 * Erase feedback indicating that the edit part is no longer 
 * the target of a drag.
 */
protected void eraseTargetConnectionFeedback(DropRequest request) {}

public void eraseTargetFeedback(Request request) {
	if (REQ_CONNECTION_START.equals(request.getType()) ||
		REQ_CONNECTION_END.equals(request.getType()) ||
		REQ_RECONNECT_SOURCE.equals(request.getType()) ||
		REQ_RECONNECT_TARGET.equals(request.getType()))
	{
		eraseTargetConnectionFeedback((DropRequest)request);
		debugFeedback("Request to erase \"" + request.getType() + "\" target feedback");//$NON-NLS-2$//$NON-NLS-1$
	}
}

/**
 * Get the command that performs an operation
 * of the type indicated by @commandString on the
 * receiver.  Data needed to create the command is
 * contained in @tool
 *
 * Possible values for the commandString depend on
 * the tool.  Default tools send "create" and "move".
 *
 * @return org.eclipse.gef.commands.Command  The command that performs the operation
 * @param commandString java.lang.String The type of command to create
 * @param commandData org.eclipse.gef.CommandData Data needed to create the command
 */
public Command getCommand(Request request) {
	if (REQ_CONNECTION_START.equals(request.getType()))
		return getConnectionCreateCommand((CreateConnectionRequest)request);
	if (REQ_CONNECTION_END.equals(request.getType()))
		return getConnectionCompleteCommand((CreateConnectionRequest)request);
	if (REQ_RECONNECT_TARGET.equals(request.getType()))
		return getReconnectTargetCommand((ReconnectRequest)request);
	if (REQ_RECONNECT_SOURCE.equals(request.getType()))
		return getReconnectSourceCommand((ReconnectRequest)request);

	return null;
}

abstract protected Command getConnectionCompleteCommand(CreateConnectionRequest request);

abstract protected Command getConnectionCreateCommand(CreateConnectionRequest request);

protected FeedbackHelper getFeedbackHelper(CreateConnectionRequest request) {
	if (feedbackHelper == null) {
		feedbackHelper = new FeedbackHelper();
		Point p = request.getLocation();
		connectionFeedback = createDummyConnection(request);
		ConnectionLayer layer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		connectionFeedback.setConnectionRouter(layer.getConnectionRouter());
		connectionFeedback.setSourceAnchor(getSourceConnectionAnchor(request));
		feedbackHelper.setConnection(connectionFeedback);
		addFeedback(connectionFeedback);
		feedbackHelper.update(null, p);
	}
	return feedbackHelper;
}

abstract protected Command getReconnectTargetCommand(ReconnectRequest request);
abstract protected Command getReconnectSourceCommand(ReconnectRequest request);

protected ConnectionAnchor getSourceConnectionAnchor(CreateConnectionRequest request) {
	EditPart source = request.getSourceEditPart();
	return source instanceof NodeEditPart 
		? ((NodeEditPart) source).getSourceConnectionAnchor(request)
		: null;
}

protected ConnectionAnchor getTargetConnectionAnchor(CreateConnectionRequest request) {
	EditPart target = request.getTargetEditPart();
	return target instanceof NodeEditPart 
		? ((NodeEditPart) target).getTargetConnectionAnchor(request)
		: null;
}

public EditPart getTargetEditPart(Request request){
	if (REQ_CONNECTION_START.equals(request.getType()) ||
		REQ_CONNECTION_END.equals(request.getType()) ||
		REQ_RECONNECT_SOURCE.equals(request.getType()) ||
		REQ_RECONNECT_TARGET.equals(request.getType()))
		return getHost();
	return null;
}

protected void showCreationFeedback(CreateConnectionRequest request){
	FeedbackHelper helper = getFeedbackHelper(request);
	Point p = new Point(request.getLocation());
	helper.update(getTargetConnectionAnchor(request), p);
}

public void showSourceFeedback(Request request){
	if (REQ_CONNECTION_END.equals(request.getType())){
		showCreationFeedback((CreateConnectionRequest)request);
		debugFeedback("Request to show \"" + request.getType() + "\" SOURCE feeback");//$NON-NLS-2$//$NON-NLS-1$
	}
}

protected void showTargetConnectionFeedback(DropRequest request) {}

public void showTargetFeedback(Request request) {
	if (REQ_CONNECTION_START.equals(request.getType()) ||
		REQ_CONNECTION_END.equals(request.getType()) ||
		REQ_RECONNECT_SOURCE.equals(request.getType()) ||
		REQ_RECONNECT_TARGET.equals(request.getType()))
	{
		showTargetConnectionFeedback((DropRequest)request);
		debugFeedback("Request to show \"" + request.getType() + "\" TARGET feeback");//$NON-NLS-2$//$NON-NLS-1$
	}
}

}