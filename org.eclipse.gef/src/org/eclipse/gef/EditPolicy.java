package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.Command;

/**
 * A pluggable contribution implementing a portion of an EditPart's behavior. EditPolicies
 * contribute to the overall <i>editing behavior</i> of an EditPart.  Editing behavior is
 * defined as one or more of the following:
 * <ul>
 *   <li><b>Command Creation </b>- Returning a <code>Command</code> in response to {@link
 *     #getCommand(Request)}
 *   <li><b>Feedback Management</b> - Showing/erasing source and/or target feedback in
 *     response to Requests.
 *   <li><b>Delegation/Forwarding</b> - Collecting contributions from other EditParts (and
 *     therefore their EditPolicies). In response to a given <code>Request</code>, an
 *     EditPolicy may create a derived Request and forward it to other EditParts. For
 *     example, during the deletion of a composite EditPart, that composite may consult
 *     its children for contributions to the delete command. Then, if the children have
 *     any additional work to do, they will return additional comands to be executed.
 * </ul>
 * <P>
 * EditPolicies should determine an EditPart's editing capabilities. It is possible to
 * implement an EditPart such that it handles all editing responsibility. However, it is
 * much more flexible and object-oriented to use EditPolicies. Using policies, you can
 * pick and choose the editing behavior for an EditPart without being bound to its class
 * hierarchy. Code reuse is increased, and code management is easier.
 */

public interface EditPolicy {

/**
 * The key used to install a <i>component</i> EditPolicy.  A <i>component</i> is defined
 * as anything in the model. This EditPolicy should handle the fundamental operations that
 * do not fit under any other EditPolicy role. For example, delete is a fundamental
 * operation. Generally the component EditPolicy knows only about the model, and can be
 * used in any type of EditPartViewer.
 */
String COMPONENT_ROLE  = "ComponentEditPolicy"; //$NON-NLS-1$

/**
 * The key used to install a <i>connection endpoint</i> EditPolicy.  A <i>connection
 * endpoint</i> EditPolicy is usually a {@link
 * org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy} subclass. Besides rendering
 * selection by displaying <code>Handle</code>s at then ends of the connection, the
 * EditPolicy also understands how to move the endpoints of the connection. If the
 * endpoints are moveable, the EditPolicy will show feedback and provide
 * <code>Commands</code> to perform the move.
 */
String CONNECTION_ENDPOINTS_ROLE = "Connection Endpoint Policy"; //$NON-NLS-1$

/**
 * The key used to install a <i>bendpoint</i> EditPolicy.  A <i>bendpoint</i> EditPolicy
 * is an optional EditPolicy for connections that are visibile.  As with {@link
 * #CONNECTION_ENDPOINTS_ROLE endpoints}, bendpoint EditPolicies are porbably {@link
 * org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy}.
 */
String CONNECTION_BENDPOINTS_ROLE = "Connection Bendpoint Policy"; //$NON-NLS-1$

/**
 * The key used to install a <i>connection</i> EditPolicy. The behavior of a
 * <code>ConnectionEditPart</code> may be implemented in its <i>component</i> EditPolicy, 
 */
String CONNECTION_ROLE = "ConnectionEditPolicy"; //$NON-NLS-1$

/**
 * The key used to install a <i>container</i> EditPolicy.
 */
String CONTAINER_ROLE  = "ContainerEditPolicy"; //$NON-NLS-1$

/**
 * The key used to install a <i>direct edit</i> EditPolicy.
 */
String DIRECT_EDIT_ROLE = "DirectEditPolicy"; //$NON-NLS-1$

/**
 * The key used to install a <i>graphical node</i> EditPolicy.
 */
String GRAPHICAL_NODE_ROLE = "GraphicalNodeEditPolciy"; //$NON-NLS-1$

/**
 * The key used to install a <i>layout</i> EditPolicy.
 */
String LAYOUT_ROLE = "LayoutEditPolicy"; //$NON-NLS-1$

/**
 * The key used to install a <i>node</i> EditPolicy.
 */
String NODE_ROLE = "NodeEditPolicy"; //$NON-NLS-1$

/**
 * The key used to install a <i>primary drag</i> EditPolicy.
 */
String PRIMARY_DRAG_ROLE = "PrimaryDrag Policy"; //$NON-NLS-1$

/**
 * The key used to install a <i>selection feedback</i> EditPolicy.
 */
String SELECTION_FEEDBACK_ROLE = "Selection Feedback"; //$NON-NLS-1$

/**
 * The key used to install a <i>tree container</i> EditPolicy.
 */
String TREE_CONTAINER_ROLE = "TreeContainerEditPolicy"; //$NON-NLS-1$

/**
 * Activates this EditPolicy. The EditPolicy might need to hook listeners. These
 * listeners should be unhooked in <code>deactivate()</code>. The EditPolicy might
 * also contribute feedback/visuals immediately, such as <i>selection handles</i>
 * if the EditPart was selected at the time of activation.
 * <P>
 * Activate will only be called after the host has been set, and that host has been
 * active.
 * @see EditPart#activate()
 * @see #deactivate()
 * @see EditPart#installEditPolicy(Object, EditPolicy)
 */
void activate();

/**
 * Deactivates the EditPolicy, the inverse of {@link #activate()}. Deactivate is called
 * when the <i>host</i> is deactivated, or when the EditPolicy is uninstalled from an
 * active host. Deactivate unhooks listeners, and removes all feedback.
 * @see EditPart#deactivate()
 * @see #activate()
 * @see EditPart#removeEditPolicy(Object)
 */
void deactivate();

/**
 * Erases source feedback based on the given <code>Request</code>. Does nothing if the
 * EditPolicy does not apply to the given Request.
 * @param request the Request
 */
void eraseSourceFeedback(Request request);

/**
 * Erases target feedback based on the given <code>Request</code>. Does nothing if the
 * EditPolicy does not apply to the given Request.
 * @param request the Request
 * */
void eraseTargetFeedback(Request request);

/**
 * Returns the <code>Command</code> contribution for the given <code>Request</code>, or
 * <code>null</code>. <code>null</code> is treated as a no-op by the caller, or an empty
 * contribution. The EditPolicy must return an {@link
 * org.eclipse.gef.commands.UnexecutableCommand} if it wishes to disallow the Request.
 * @param request the Request
 * @return <code>null</code> or a Command contribution
 */
Command getCommand(Request request);

/**
 * Returns <code>null</code> or the appropriate <code>EditPart<code> for the specified
 * <code>Request</code>. In general, this EditPolicy will return its <i>host</i> EditPart
 * if it understands the Request. Otherwise, it will return <code>null</code>.
 * @param request the Request
 * @return <code>null</code> or the appropriate target <code>EditPart</code>
 */
EditPart getTargetEditPart(Request request);

/**
 * Sets the host in which this EditPolicy is installed.
 * @param editpart the host EditPart
 */
void setHost(EditPart editpart);

/**
 * Shows or updates <i>source feedback<i> for the specified <code>Request</code>. This
 * method may be called repeatedly for the purpose of updating feedback based on changes
 * to the Request.
 * <P>
 * Does nothing if the EditPolicy does not recognize the given Request.
 * @param request the Request
 */
void showSourceFeedback(Request request);

/**
 * Shows or updates <i>target feedback</i> for the specified <code>Request</code>. This
 * method may be called repeatedly for the purpose of updating feedback based on changes
 * to the Request.
 * <P>
 * Does nothing if the EditPolicy does not recognize the given request.
 * @param request the Request
 */
void showTargetFeedback(Request request);

boolean understandsRequest(Request request);

}
