package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.Command;

/**
Responsible for providing <u>one</u> type of editing behavior
to an EditPart. An editing behavior consists of one or more of the following:
<ul>
  <li><b>Command creation </b>- Returning a command in response to {@link
    #getCommand(Request)}
  <li><b>Feedback management</b> -&nbsp; Showing source and/or target feedback
    in response to Requests.</li>
  <li><b>Delegation</b> - Collecting contributions from other EditParts (and
    therefore their EditPolicies).&nbsp; In response to a given Request, an
    EditPolicy may create a derived request and forward it to other EditParts.&nbsp;
    For example, during the deletion of a composite part, that composite may
    consult its children for contributions to the delete command.&nbsp; Then, if
    the children have any clean-up to do, they will return their contribution to
    the delete.</li>
</ul>
EditPolicies should determine an EditPart's editing capabilities. It is possible
to implement an EditPart such that it handles all editing responsibility.&nbsp;
However, it is much more flexible and object-oriented to use EditPolicies.&nbsp;
Using policies, you can pick and choose the editing behavior for an EditPart
without being bound to its class hierarchy.&nbsp; Code reuse is increased, and
code management is easier.
 */

public interface EditPolicy {

/**
 * This policy will initialize and establish any necessary listeners.
 * Activate is called after the host has been set, and that host is active.
 * EditPolicy activation is contingent upon EditPart activation.
 * @see EditPart#activate()
 * @see #deactivate()
 * @see EditPart#installEditPolicy(Object, EditPolicy)
 */
void activate();

/**
 * This policy should shutdown and remove all listeners established in 
 <code>activate()</code>.
 deactivate() will be called either when the host is deactivated, or when
 this policy is uninstalled from its host.
 * @see EditPart#deactivate()
 * @see #activate()
 * @see EditPart#removeEditPolicy(Object)
 */
void deactivate();

/**
 * Erases source feedback based on the given request.
 * Does nothing if the EditPolicy does not apply to the given request.
 */
void eraseSourceFeedback(Request request);

/**
 * Erases target feedback based on the given request.
 * Does nothing if the EditPolicy does not apply to the given request.
 */
void eraseTargetFeedback(Request request);

/**
 * Returns the command for a given request, or null if the EditPolicy does
 * not work with that request type.
 * <code>null</code> is treated as a no-op by the caller, or a zero contribution.
 * The EditPolicy must return an <code>UnexectuableCommand</code>
 * if it wishes to prevent the operation from being performed.
 */
Command getCommand(Request request);

/**
 Returns the host EditPart if the EditPolicy works with the given request type,
 otherwise return <code>null</code>. For all practical uses, this method has
 "boolean" behavior.  It is possible, although rarely useful, to
 return an EditPart other than the host.
 */
EditPart getTargetEditPart(Request request);

/**
 * Sets the host in which this EditPolicy is installed.
 */
void setHost(EditPart editpart);

/**
 * Shows or updates source feedback if the receiver works with the request type.
 * This method may be called repeatedly for the purpose of updating feedback
 * based on changes to the request object.
 * Does nothing if the EditPolicy does not recognize the given request.
 */
void showSourceFeedback(Request request);

/**
 * Shows or updates target feedback if the receiver works with the request type.
 * This method may be called repeatedly for the purpose of updating feedback
 * based on changes to the request object.
 * Does nothing if the EditPolicy does not recognize the given request.
 */
void showTargetFeedback(Request request);

boolean understandsRequest(Request request);

static final String COMPONENT_ROLE  = "ComponentEditPolicy"; //$NON-NLS-1$
static final String CONNECTION_ENDPOINTS_ROLE = "Connection Endpoint Policy"; //$NON-NLS-1$
static final String CONNECTION_BENDPOINTS_ROLE = "Connection Bendpoint Policy"; //$NON-NLS-1$
static final String CONNECTION_ROLE = "ConnectionEditPolicy"; //$NON-NLS-1$
static final String CONTAINER_ROLE  = "ContainerEditPolicy"; //$NON-NLS-1$
static final String DIRECT_EDIT_ROLE = "DirectEditPolicy"; //$NON-NLS-1$
static final String GRAPHICAL_NODE_ROLE = "GraphicalNodeEditPolciy"; //$NON-NLS-1$
static final String LAYOUT_ROLE = "LayoutEditPolicy"; //$NON-NLS-1$
static final String NODE_ROLE = "NodeEditPolicy"; //$NON-NLS-1$
static final String PRIMARY_DRAG_ROLE = "PrimaryDrag Policy"; //$NON-NLS-1$
static final String SELECTION_FEEDBACK_ROLE = "Selection Feedback"; //$NON-NLS-1$
static final String TREE_CONTAINER_ROLE = "TreeContainerEditPolicy"; //$NON-NLS-1$

}
