package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.commands.Command;



/**
 EditParts are the conceptual objects that populate an EditPartViewer.
 An EditPart ties together the application's model, a visual representation
 of some type, and all editing behavior.  It can be composed of or have
 references to other EditParts.
 <P>The creator of an EditPart should call only setModel(Object).  The
 remaining API is used mostly by Tools, EditPolicies, and other EditParts.
 CHANGES are made to the  model, not the EditPart.

 Most interaction with EditParts is acheived using {@link Request Requests}.
 A Request specifies the type of interaction, and is used in targeting,
 filtering, graphical feedback, and most importantly, obtaining commands.
 Only {@link Command Commands} should change the model.
 */
public interface EditPart 
	extends IAdaptable
{

/**
 * Used to indicate no selection
 */
int SELECTED_NONE = 0;

/**
 * Used to indicate non-primary selection
 */
int SELECTED = 1;

/**
 * Used to indicate primary selection, or "Anchor" selection.
 * Primary selection is defined as the last object selected.
 */
int SELECTED_PRIMARY = 2;

/**
 Indicates that editing has begun and the model may start changing.
 Called by the managing EditPart.  The managing edit part may call
 {@link #activate()} and then {@link #deactivate()} multiple times on 
 the receiver.
 <P>Activiation should initiate several things.  First, the EditPart 
 should begin to observe its model if appropriate, and should continue 
 the observation until {@link #deactivate()} is called.  The EditPart 
 should create and activate all of its EditPolicies.  EditPolicies
 may also observe the model, and should also stop observing when deactivated.
 Any EditParts managed by this EditPart should also be activated.
*/
void activate();

/**
 * Adds a listener to the EditPart.
 * Duplicate calls result in duplicate notification.
 */
void addEditPartListener(EditPartListener listener);

/**
 * Called by the managing EditPart(s).  The managing EditPart(s) may
 * call {@link #activate()} and then {@link #deactivate()} multiple 
 * times on the receiver. The EditPart should unhook all listeners to 
 * the model, and should deactivate its EditPolicies.  It may be 
 * reactivated later.  The EditPart should deactivate all EditParts 
 * that it manages.
 */
void deactivate();

/**
 * Called when the EditPart will no longer be used.  The EditPart should perform
 * cleanup that cannot be performed in {@link #deactivate()}, such as unregistering
 * itself from the Viewer.
 */
void dispose();

/**
 * Erases feedback for which this EditPart is the source.
 * This method may be called several times due to some lazy code in the
 * provided tools.
 */
void eraseSourceFeedback(Request request);

/**
 * Erase feedback for which this Editpart is the target.
 * This method may be called several times due to some lazy code in the
 * provided tools.
 */
void eraseTargetFeedback(Request request);

/**
 * Returns the children for this EditPart.
 * This method should rarely be called, and is only made public so that
 * helper objects of this EditPart, such as EditPolicies, can obtain the
 * children.
 */
List getChildren();

/**
 * Returns the command to perform the given Request or null.
 */
Command getCommand(Request request);

/**
 * Return a drag tracker for dragging this EditPart.
 * The {@link org.eclipse.gef.tools.SelectionTool SelectionTool}
 * makes such requests, but other Tools may also do so.
 */
DragTracker getDragTracker(Request request);

/**
 * Returns <code>null</code> or the EditPolicy installed with the given key.
 */
EditPolicy getEditPolicy(Object key);

/**
 * Returns the KeyHandler for this EditPart or <code>null</code>.
 */
KeyHandler getKeyHandler();

/**
 * Return the primary model object that this EditPart represents.
 * EditParts may correspond to more than one model object, or even no
 * mode object.
 * In practice, the Object returned is simply used to
 * identify this EditPart.  In addition, EditPolicies can call this method to
 * set parameters for Commands being returned.
 */
Object getModel();

/**
 * Returns the parent of this editpart.
 * This method should only be called internally or by helpers such as
 * edit policies.
 */
EditPart getParent();

/**
 * Returns the root EditPart.
 * This method should only be called internally or by helpers such as
 * edit policies.
 */
RootEditPart getRoot();

/**
 * Returns the selected state of this EditPart.
 * This method should only be called internally or by helpers such as
 * edit policies.
 */
int getSelected();

/**
 * Return the editpart that should be used as the target
 * for the Request.
 * Tools will generally call this method will the mouse location so that the
 * receiver can implement drop targeting.
 * Typically, if this EditPart is not the requested target 
 * (for example, this EditPart is not a composite),
 * it will forward the call to its parent.
 */
EditPart getTargetEditPart(Request request);

/**
 * Returns true if this EditPart has focus.
 */
boolean hasFocus();

/**
 * installs an EditPolicy for a certain editing role.
 * An example role might be a connection node.
 * A node editpolicy would be responsible for understanding
 * requests to manipulate and create connections.
 * <code>null</code> is a valid value for reserving 
 */
void installEditPolicy(Object role, EditPolicy ep);

/**
 * Performs the specified Request.  This method can be used to send a generic message
 * to an EditPart.  It is bad practice to use the API as a way to make changes to the model.
 * Commands should be used to modify the model.
 */
void performRequest(Request request);

/**
 * Called to force a refresh of all properties displayed by this EditPart.
 */
void refresh();

/**
 * Removes the first entry for that listener in the list of listeners.
 * Does nothing if the listener was not present.
 */
void removeEditPartListener(EditPartListener listener);

/**
 * Removes the EditPolicy for the given role, if found.
 * If the EditPolicy is not null, it is deactivated.
 * The slot for that role is maintained with <code>null</code> in
 * the place of the old policy.
 */
void removeEditPolicy(Object role);

/**
 * Tells the EditPart whether it is the focus owner. Changes in focus are notified
 * using {@link EditPartListener#selectedStateChanged(EditPart)}.
 */
void setFocus(boolean hasFocus);

/**
 * Sets the model.
 * This method is made public to facilitate the use of EditPart factories.
 * It should only be called by the creator of this EditPart.
 */
void setModel(Object model);

/**
 * <img src="doc-files/black.gif"/>
 * Sets the parent.  This should only be called by the managing EditPart.
 */
void setParent(EditPart parent);

/**
 * <img src="doc-files/black.gif"/>
 * Sets the selected state.
 * This should only be called by the EditPartViewer.
 * Fires selectedStateChanged(EditPart) to any EditPartListeners.
 */
void setSelected(int value);

/**
 * Shows or updates source feedback for the given request.
 * This method can be called multiple times so that the feedback can be updated
 * for changes in the request, such as the mouse location changing.
 */
void showSourceFeedback(Request request);

/**
 * Shows or updates target feedback for the given request.
 * This method can be called multiple times so that the feedback can be updated
 * for changes in the request, such as the mouse location changing.
 */
void showTargetFeedback(Request request);

/**
 * Returns true if editpart understands the given Request.
 * EditParts should ignore requests that they don't understand, but this can be used
 * to filter out non-participating EditParts from operations that act on a set.
 */
boolean understandsRequest(Request request);

}
