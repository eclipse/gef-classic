package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.beans.*;


import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.handles.*;
import org.eclipse.gef.requests.*;
import org.eclipse.gef.commands.Command;

/**
 * This EditPolicy defines the behavior of Bendpoints on a Connection.
 */
public abstract class BendpointEditPolicy
	extends SelectionHandlesEditPolicy
	implements PropertyChangeListener
{

private static List NULL_CONSTRAINT = new ArrayList();
	
private List originalConstraint;

/**
 * Adds a PropertyChangeListener to the Connection so we can react
 * to point changes in the connection.  
 *
 * @see SelectionHandlesEditPolicy#activate()
 */
public void activate() {
	super.activate();
	getConnection().addPropertyChangeListener(Connection.PROPERTY_POINTS, this);
}

private List createAutomaticHandles() {
	List list = new ArrayList();
	ConnectionEditPart connEP = (ConnectionEditPart)getHost();
	PointList points = getConnection().getPoints();
	for (int i = 0; i < points.size() - 2; i++) {
		BendpointHandle handle = new BendpointCreationHandle(connEP, 0, 
					new BendpointLocator(getConnection(), i + 1));
		list.add(handle);
	}
	return list;
}

private List createManualHandles() {
	List list = new ArrayList();
	ConnectionEditPart connEP = (ConnectionEditPart)getHost();
	PointList points = getConnection().getPoints();
	for (int i = 0; i < points.size() - 2; i++) {
		list.add(new BendpointCreationHandle(connEP, i));
		list.add(new BendpointMoveHandle(connEP, i));
	}
	list.add(new BendpointCreationHandle(connEP, points.size() - 2));
	return list;
}

/**
 * Creates selection handles for the bendpoints.  Explicit (user-defined)
 * bendpoints will have {@link BendpointMoveHandle}s on them with a single 
 * {@link BendpointCreationHandle} between 2 consecutive explicit bendpoints.
 * If implicit bendpoints (such as those created by the {@link AutomaticRouter})
 * are used, one {@link BendpointCreationHandle} is placed in the middle
 * of the Connection.
 */
protected List createSelectionHandles() {
	List list = new ArrayList();
	if (isAutomaticallyBending())
		list = createAutomaticHandles();
	else 
		list = createManualHandles();
 	return list;
}

/**
 * Removes this from the Connection's list of PropertyChangeListeners.
 *
 * @see SelectionHandlesEditPolicy#deactivate()
 */
public void deactivate() {
	getConnection().removePropertyChangeListener(Connection.PROPERTY_POINTS, this);
	super.deactivate();
}

/**
 * Erases bendpoint feedback.  Since the original figure is used
 * for feedback, we just restore the original constraint that
 * was saved before feedback started to show.
 */
protected void eraseConnectionFeedback(BendpointRequest request) {
	restoreOriginalConstraint();
	originalConstraint = null;
}

/**
 * Erases feedback, when appropriate.
 *
 * @see #eraseConnectionFeedback(BendpointRequest)
 */
public void eraseSourceFeedback(Request request) {
	if (REQ_MOVE_BENDPOINT.equals(request.getType())
		|| REQ_CREATE_BENDPOINT.equals(request.getType())
		|| REQ_DELETE_BENDPOINT.equals(request.getType()))
		eraseConnectionFeedback((BendpointRequest)request);
}

/**
 * Returns the appropriate Command for the request type given.  Handles
 * creating, moving and deleting bendpoints.  The actual creation of the
 * command is taken care of by subclasses implementing the appropriate
 * methods.
 *
 * @see #getCreateBendpointCommand(BendpointRequest)
 * @see #getMoveBendpointCommand(BendpointRequest)
 * @see #getDeleteBendpointCommand(BendpointRequest)
 */
public Command getCommand(Request request) {
	if (REQ_MOVE_BENDPOINT.equals(request.getType()))
		return getMoveBendpointCommand((BendpointRequest)request);
	if (REQ_DELETE_BENDPOINT.equals(request.getType()))
		return getDeleteBendpointCommand((BendpointRequest)request);
	if (REQ_CREATE_BENDPOINT.equals(request.getType()))
		return getCreateBendpointCommand((BendpointRequest)request);
	return null;
}

/**
 * Returns the Connection associated with this EditPolicy.
 */
protected Connection getConnection() {
	return (Connection)((ConnectionEditPart)getHost()).getFigure();
}

/**
 * Implement this method to return a Command that will create a bendpoint.
 */
protected abstract Command getCreateBendpointCommand(BendpointRequest request);

/**
 * Implement this method to return a Command that will delete a bendpoint.
 */
protected abstract Command getDeleteBendpointCommand(BendpointRequest request);

/**
 * Implement this method to return a Command that will move a bendpoint.
 */
protected abstract Command getMoveBendpointCommand(BendpointRequest request);

private boolean isAutomaticallyBending() {
	List constraint = (List)getConnection().getRoutingConstraint();
	PointList points = getConnection().getPoints();
	return ((points.size() > 2) && (constraint == null || constraint.isEmpty()));
}

/**
 * Adds selection handles to the Connection, if it is selected, when the points 
 * property changes.  Since we only listen for changes in the points property, 
 * this method is only called when the points of the Connection have changed.
 */
public void propertyChange(PropertyChangeEvent evt) {
	if (getHost().getSelected() != EditPart.SELECTED_NONE) {
		int count = handles.size();
		int points = getConnection().getPoints().size();
		if (count != points * 2 - 3)
			addSelectionHandles();
	}
}

/**
 * Restores the original constraint that was saved before feedback
 * began to show.
 */
protected void restoreOriginalConstraint() {
	if (originalConstraint != null) 
		getConnection().setRoutingConstraint(originalConstraint);
}

/**
 * Since the original figure is used for feedback, this method saves the 
 * original constraint, so that is can be restored when the feedback is
 * erased.
 */
protected void saveOriginalConstraint() {
	originalConstraint = (List)getConnection().getRoutingConstraint();
	if (originalConstraint == null)
		originalConstraint = NULL_CONSTRAINT;
	getConnection().setRoutingConstraint(new ArrayList(originalConstraint));
}

/**
 * Shows feedback when a bendpoint is being created.  The original figure
 * is used for feedback and the original constraint is saved, so that it
 * can be restored when feedback is erased.
 */
protected void showCreateBendpointFeedback(BendpointRequest request) {
	Point p = new Point(request.getLocation());
	List constraint;
	getConnection().translateToRelative(p);
	Bendpoint bp = new AbsoluteBendpoint(p);
	if (originalConstraint == null) {
		saveOriginalConstraint();
		constraint = (List)getConnection().getRoutingConstraint();
		constraint.add(request.getIndex(), bp);
	} 
	else {
		constraint = (List)getConnection().getRoutingConstraint();
	}
	constraint.set(request.getIndex(), bp);
	getConnection().setRoutingConstraint(constraint);
}

/**
 * Shows feedback when a bendpoint is being deleted.  This method is
 * only called once when the bendpoint is first deleted, not every
 * mouse move.  The original figure is used for feedback and the original 
 * constraint is saved, so that it can be restored when feedback is erased.
 */
protected void showDeleteBendpointFeedback(BendpointRequest request) {
	if (originalConstraint == null) {
		saveOriginalConstraint();
		List constraint = (List)getConnection().getRoutingConstraint();
		constraint.remove(request.getIndex());
		getConnection().setRoutingConstraint(constraint);
	}
}

/**
 * Shows feedback when a bendpoint is being moved.  Also checks to see if the bendpoint 
 * should be deleted and then calls {@link #showDeleteBendpointFeedback(BendpointRequest)}
 * if needed.  The original figure is used for feedback and the original constraint is 
 * saved, so that it can be restored when feedback is erased.
 */
protected void showMoveBendpointFeedback(BendpointRequest request) {
	Point p = new Point(request.getLocation());
	if (originalConstraint == null)
		saveOriginalConstraint();
	List constraint = (List)getConnection().getRoutingConstraint();
	getConnection().translateToRelative(p);
	Bendpoint bp = new AbsoluteBendpoint(p);
	constraint.set(request.getIndex(), bp);
	getConnection().setRoutingConstraint(constraint);
}

/**
 * Shows feedback, when appropriate.  Calls a different method
 * depending on the request type.
 *
 * @see #showCreateBendpointFeedback(BendpointRequest)
 * @see #showMoveBendpointFeedback(BendpointRequest)
 */
public void showSourceFeedback(Request request) {
	if (REQ_MOVE_BENDPOINT.equals(request.getType()))
		showMoveBendpointFeedback((BendpointRequest)request);
	else if (REQ_CREATE_BENDPOINT.equals(request.getType()))
		showCreateBendpointFeedback((BendpointRequest)request);
	else if (REQ_DELETE_BENDPOINT.equals(request.getType()))
		showDeleteBendpointFeedback((BendpointRequest)request);
}

}


