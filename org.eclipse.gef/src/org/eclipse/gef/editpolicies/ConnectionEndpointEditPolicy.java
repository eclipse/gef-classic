package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.ConnectionEndHandle;
import org.eclipse.gef.handles.ConnectionStartHandle;
import org.eclipse.gef.requests.ReconnectRequest;


public class ConnectionEndpointEditPolicy
	extends SelectionHandlesEditPolicy
{

private ConnectionAnchor originalAnchor;
private FeedbackHelper feedbackHelper;
private ConnectionFocus focus;

static Color xorFocusColor = new Color(null, 255, 255, 255);

class ConnectionFocus
	extends Polygon
	implements PropertyChangeListener
{
	PointList second;
	ConnectionFocus(){
		setForegroundColor(xorFocusColor);
		setBackgroundColor(ColorConstants.black);
		setXOR(true);
		setLineStyle(Graphics.LINE_DOT);
		setFill(false);
		setOutline(true);
	}
	
	public void addNotify(){
		super.addNotify();
		getConnection().addPropertyChangeListener(Connection.PROPERTY_POINTS, this);
	}
	protected void outlineShape(Graphics g){
		super.outlineShape(g);
		g.drawPolygon(second);
	}
	public void propertyChange(PropertyChangeEvent evt){
		revalidate();
	}

	public void removeNotify(){
		getConnection().removePropertyChangeListener(Connection.PROPERTY_POINTS, this);
		super.removeNotify();
	}
	
	public void validate(){
		if (isValid())
			return;
		PointList points = getConnection().getPoints();
		points = StrokePointList.strokeList(points, 5);
		setPoints(points);
		second = new PointList(points.size()+2);
		int minY = Integer.MAX_VALUE;
		int index = -1;
		for (int i=0; i <points.size(); i++){
			Point p = points.getPoint(i);
			if (p.y < minY){
				minY = p.y;
				index = i;
			}
			second.addPoint(p);
		}
		if (index != -1){
			second.insertPoint(points.getPoint(index), index);
			second.insertPoint(points.getPoint(index).translate(0, -1), index+1);
			second.insertPoint(points.getPoint(index).translate(-1, -1), index+1);
			second.insertPoint(points.getPoint(index).translate(0, -2), index+1);
		}
	}
}

protected List createSelectionHandles() {
	List list = new ArrayList();
	list.add(new ConnectionEndHandle((ConnectionEditPart)getHost()));
	list.add(new ConnectionStartHandle((ConnectionEditPart)getHost()));
 	return list;
}

protected void eraseConnectionMoveFeedback(ReconnectRequest request){
	if (originalAnchor == null)
		return;
	if (request.isMovingStartAnchor())
		getConnection().setSourceAnchor(originalAnchor);
	else
		getConnection().setTargetAnchor(originalAnchor);
	originalAnchor = null;
	feedbackHelper = null;
}

/**
 * Erase feedback indicating that the receiver object is 
 * being dragged.  This method is called when a drag is
 * completed or cancelled on the receiver object.
 * @param dragTracker org.eclipse.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
public void eraseSourceFeedback(Request request) {
	if (REQ_RECONNECT_TARGET.equals(request.getType()) ||
		REQ_RECONNECT_SOURCE.equals(request.getType()))
	{
		eraseConnectionMoveFeedback((ReconnectRequest)request);
		debugFeedback("Request to erase \"" + request.getType() + "\" source feedback");//$NON-NLS-2$//$NON-NLS-1$
	}
}

public Command getCommand(Request request){
	return null;
}

protected Connection getConnection() {
	return (Connection)((GraphicalEditPart)getHost()).getFigure();
}

protected FeedbackHelper getFeedbackHelper(ReconnectRequest request) {
	if (feedbackHelper == null) {
		feedbackHelper = new FeedbackHelper();
		feedbackHelper.setConnection(getConnection());
		feedbackHelper.setMovingStartAnchor(request.isMovingStartAnchor());
	}
	return feedbackHelper;
}

protected void hideFocus(){
	if (focus != null){
		removeFeedback(focus);
		focus = null;
	}
}

protected void showConnectionMoveFeedback(ReconnectRequest request){
	NodeEditPart node = null;
	if (request.getTarget() instanceof NodeEditPart)
		node = (NodeEditPart)request.getTarget();
	if (originalAnchor == null) {
		if (request.isMovingStartAnchor())
			originalAnchor = getConnection().getSourceAnchor();
		else
			originalAnchor = getConnection().getTargetAnchor();
	}
	ConnectionAnchor anchor = null;
	if (node != null) {
		if (request.isMovingStartAnchor())
			anchor = node.getSourceConnectionAnchor(request);
		else
			anchor = node.getTargetConnectionAnchor(request);
	}
	FeedbackHelper helper = getFeedbackHelper(request);
	helper.update(anchor, request.getLocation());
}

protected void showFocus(){
	if (focus == null){
		focus = new ConnectionFocus();
		addFeedback(focus);
	}
}

public void showSourceFeedback(Request request){
	if (REQ_RECONNECT_SOURCE.equals(request.getType()) ||
		REQ_RECONNECT_TARGET.equals(request.getType()))
		showConnectionMoveFeedback((ReconnectRequest)request);
}

}