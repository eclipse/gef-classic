/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editpolicies;

import java.util.List;

import org.eclipse.draw2d.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Provides support for interacting with children <code>GraphicalEditParts</code> with the
 * host figure's current {@link org.eclipse.draw2d.LayoutManager}.
 * <P>
 * LayoutEditPolicies are responsible for moving, resizing, reparenting, and creating
 * children. The should provide <code>Commands</code> for all of these operations.
 * Feedback on the container can also be useful for some layouts like grids.
 * <P>
 * LayoutEditPolicies will decorate the host's children with "satellite" EditPolicies.
 * These policies are installed using the {@link EditPolicy#PRIMARY_DRAG_ROLE}. Simple
 * layouts will use either {@link ResizableEditPolicy} or {@link NonResizableEditPolicy},
 * depending on how the LayoutManager works, and/or attributes of the child EditPart.
 */
public abstract class LayoutEditPolicy
	extends GraphicalEditPolicy
{

private Shape sizeOnDropFeedback;

private EditPartListener listener;

/**
 * Extends activate() to 
 * @see org.eclipse.gef.EditPolicy#activate() */
public void activate() {
	setListener(createListener());
	decorateChildren();
	super.activate();
}

/**
 * Returns the "satellite" EditPolicy used to decorate the child.
 * @param child the child EditPart * @return an EditPolicy to be installed as the {@link EditPolicy#PRIMARY_DRAG_ROLE} */
protected abstract EditPolicy createChildEditPolicy(EditPart child);

/**
 * creates the EditPartListener for observing when children are added to the host.
 * @return EditPartListener */
protected EditPartListener createListener() {
	return new EditPartListener.Stub() {
		public void childAdded(EditPart child, int index) {
			decorateChild(child);
		}
	};
}

/**
 * Overrides deactivate to remove the EditPartListener.
 * @see org.eclipse.gef.EditPolicy#deactivate() */
public void deactivate() {
	setListener(null);
	super.deactivate();
}

/**
 * Decorates the child with a {@link EditPolicy#PRIMARY_DRAG_ROLE} such as {@link
 * ResizableEditPolicy}.
 * @param child the child EditPart being decorated */
protected void decorateChild(EditPart child) {
	EditPolicy policy = createChildEditPolicy(child);
	child.installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, policy);
}

/**
 * Decorates all existing children. This method is called on activation.
 */
protected void decorateChildren() {
	List children = getHost().getChildren();
	for (int i = 0; i < children.size(); i++)
		decorateChild((EditPart)children.get(i));
}

/**
 * Made <code>final</code> for compatibility. Override eraseLayoutTargetFeedback(). The
 * term "drag" is actually confusing, since when the User actually releases the mouse is
 * considered a "drop" by most conventions.
 * @throws Exception exception
 * @param request the Request */
protected final void eraseDragTargetFeedback(Request request) throws Exception { }

/**
 * Erases target layout feedback. This method is the inverse of {@link
 * #showLayoutTargetFeedback(Request)}.
 * @param request the Request */
protected void eraseLayoutTargetFeedback(Request request) {
}

/**
 * Erases size-on-drop feedback used during creation.
 * @param request the Request */
protected void eraseSizeOnDropFeedback(Request request) {
	if (sizeOnDropFeedback != null) {
		removeFeedback(sizeOnDropFeedback);
		sizeOnDropFeedback  = null;
	}
}

/**
 * Calls two more specific methods depending on the Request.
 * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(Request) */
public void eraseTargetFeedback(Request request) {
	if (REQ_ADD.equals(request.getType())
		|| REQ_MOVE.equals(request.getType())
		|| REQ_CREATE.equals(request.getType()))
		eraseLayoutTargetFeedback(request);

	if (REQ_CREATE.equals(request.getType()))
		eraseSizeOnDropFeedback(request);
}

/**
 * Override to return the <code>Command</code> to perform an {@link
 * RequestConstants#REQ_ADD ADD}. By default, <code>null</code> is returned.
 * @param request the ADD Request * @return A command to perform the ADD. */
protected Command getAddCommand(Request request) {
	return null;
}

/**
 * Factors incoming requests into various specific methods.
 * @see org.eclipse.gef.EditPolicy#getCommand(Request) */
public Command getCommand(Request request) {
	if (REQ_DELETE_DEPENDANT.equals(request.getType()))
		return getDeleteDependantCommand(request);

//	if (REQ_DELETE.equals(request.getType()))
//		return getDeleteCommand((DeleteRequest)request);

//	if (REQ_ANCESTOR_DELETED.equals(request.getType()))
//		return getAncestorDeletedCommand(request);

	if (REQ_ADD.equals(request.getType()))
		return getAddCommand(request);

	if (REQ_ORPHAN_CHILDREN.equals(request.getType()))
		return getOrphanChildrenCommand(request);

	if (REQ_MOVE_CHILDREN.equals(request.getType()))
		return getMoveChildrenCommand(request);

	if (REQ_CREATE.equals(request.getType()))
		return getCreateCommand((CreateRequest)request);

	return null;
}

/**
 * Returns the <code>Command</code> to perform a create.
 * @param request the CreateRequest * @return a Command to perform a create */
protected abstract Command getCreateCommand(CreateRequest request);

/**
 * Returns the <code>Command</code> to delete a child.
 * @param request the Request
 * @return the Command to delete the child
 */
protected abstract Command getDeleteDependantCommand(Request request);

/**
 * Returns the host's {@link GraphicalEditPart#getContentPane() contentPane}. The
 * contentPane is the Figure which parents the childrens' figures. It is also the figure
 * which has the LayoutManager that corresponds to this EditPolicy. All operations should
 * be interpreted with respect to this figure.
 * @return the Figure that owns the corresponding <code>LayoutManager</code> */
protected IFigure getLayoutContainer() {
	return ((GraphicalEditPart)getHost()).getContentPane();
}

/**
 * Returns the <code>Command</code> to move a group of children.
 * @param request the Request * @return the Command to perform the move */
protected abstract Command getMoveChildrenCommand(Request request);

/**
 * Returns the <code>Command</code> to orphan a group of children. The contribution to
 * orphan might contain two parts, both of which are option. The first part is to
 * actually remove the children from their existing parent. Some application models will
 * perform an orphan implicitly when the children are added to their new parent. The
 * second part is to perform some adjustments on the remaining children. For example, a
 * Table layout might simplify itself by collapsing any unused columns and rows.
 * @param request the Request * @return <code>null</code> or a Command to perform an orphan */
protected Command getOrphanChildrenCommand(Request request) {
	return null;
}

/**
 * Lazily creates and returns the Figure to use for size-on-drop feedback.
 * @return the size-on-drop feedback figure */
protected IFigure getSizeOnDropFeedback() {
	if (sizeOnDropFeedback == null) {
		sizeOnDropFeedback  = new RectangleFigure();
		FigureUtilities.makeGhostShape(sizeOnDropFeedback);
		sizeOnDropFeedback.setLineStyle(Graphics.LINE_DASHDOT);
		sizeOnDropFeedback.setForegroundColor(ColorConstants.white);
		addFeedback(sizeOnDropFeedback);
	}
	return sizeOnDropFeedback;

}

/**
 * Returns the <i>host</i> if the Request is an ADD, MOVE, or CREATE.
 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request) */
public EditPart getTargetEditPart(Request request) {
	if (REQ_ADD.equals(request.getType())
		|| REQ_MOVE.equals(request.getType())
		|| REQ_CREATE.equals(request.getType()))
		return getHost();
	return null;
}

/**
 * Sets the EditPartListener used to decorate new children. If the listener is currently
 * set, it will be unhooked. If the new value is not <code>null</code>, it will be hooked.
 * <P>
 * The listener must be remembered in case this EditPolicy is removed from the host and
 * replaced with another LayoutEditPolicy.
 * @param listener <code>null</code> or the listener. */
protected void setListener(EditPartListener listener) {
	if (this.listener != null)
		getHost().removeEditPartListener(this.listener);
	this.listener = listener;
	if (this.listener != null)
		getHost().addEditPartListener(this.listener);
}

/**
 * Made final for compatibility.
 * @throws Exception bogus
 * @param request the Request */
protected final void showDragTargetFeedback(Request request) throws Exception { }

/**
 * Shows target layout feedback. During <i>moves</i>, <i>reparents</i>, and
 * <i>creation</i>, this method is called to allow the LayoutEditPolicy to temporarily
 * show features of its layout that will help the User understand what will happen if the
 * operation is performed in the current location.
 * <P>
 * By default, no feedback is shown.
 * @param request the Request
 * @see #eraseLayoutTargetFeedback(Request)
 */
protected void showLayoutTargetFeedback(Request request) { }

/**
 * Shows size-on-drop feedback during creation.
 * @param request the CreateRequest */
protected void showSizeOnDropFeedback(CreateRequest request) {
}

/**
 * Factors feedback requests into two more specific methods.
 * @see org.eclipse.gef.EditPolicy#showTargetFeedback(Request) */
public void showTargetFeedback(Request request) {
	if (REQ_ADD.equals(request.getType())
		|| REQ_MOVE.equals(request.getType())
		|| REQ_RESIZE_CHILDREN.equals(request.getType())
		|| REQ_CREATE.equals(request.getType())) {
		debugFeedback("Request to show \"" + request.getType()//$NON-NLS-1$
			+ "\" target feedback"); //$NON-NLS-1$
		showLayoutTargetFeedback(request);
	}

	if (REQ_CREATE.equals(request.getType())) {
		CreateRequest createReq = (CreateRequest)request;
		if (createReq.getSize() != null)
			showSizeOnDropFeedback(createReq);
	}
}

}
