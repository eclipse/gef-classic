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
package org.eclipse.gef.editparts;

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.draw2d.EventListenerList;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;

/**
 * The baseline implementation for the {@link EditPart} interface.
 * <P>
 * Since this is the default implementation of an interface, this document deals with
 * proper sub-classing of this implementation.  This class is not the API.  For
 * documentation on proper usage of the public API, see the documentation for the
 * interface itself: {@link EditPart}.
 * <P>
 * <Table>
 * 	 <tr>
 * 	   <TD><img src="../doc-files/green.gif"/>
 * 	   <TD>Indicates methods that are commonly overridden or even abstract
 *   </tr>
 *   <tr>
 *     <TD><img src="../doc-files/blue.gif"/>
 *     <TD>These methods might be overridden.  Especially if you were extending this class
 *     directly.
 * 	 </tr>
 * 	 <tr>
 * 	   <TD><img src="../doc-files/black.gif"/>
 * 	   <TD>Should rarely be overridden.
 * 	 </tr>
 *   <tr>
 * 	   <TD><img src="../doc-files/dblack.gif"/>
 * 	   <TD>Essentially "internal" and should never be overridden.
 *   </tr>
 * </table>
 * <P>
 * This class assumes no visual representation. Subclasses {@link
 * AbstractGraphicalEditPart} and {@link AbstractTreeEditPart} add support for {@link
 * org.eclipse.draw2d.IFigure Figures} and {@link org.eclipse.swt.widgets.TreeItem
 * TreeItems} respectively.
 * <P>
 * AbstractEditPart provides support for children. All AbstractEditPart's can potentially
 * be containers for other EditParts.
 */
public abstract class AbstractEditPart
	implements EditPart, RequestConstants, IAdaptable
{

/**
 * This flag is set during {@link #activate()}, and reset on {@link #deactivate()}
 */
protected static final int FLAG_ACTIVE   = 1;
/**
 * This flag indicates that the EditPart has focus.
 */
protected static final int FLAG_FOCUS = 2;

/**
 * The left-most bit that is reserved by this class for setting flags. Subclasses may
 * define additional flags starting at <code>(MAX_FLAG << 1)</code>.
 */
protected static final int MAX_FLAG = FLAG_FOCUS;

private   	Object model;
private	int flags;
private   	EditPart parent;
private	int selected;

private 	List editPolicies = new ArrayList(2);
private   	List editPolicyKeys = new ArrayList(2);
/**
 * The List of children EditParts
 */
protected 	List children;

/**
 * call getEventListeners(Class) instead.
 */
EventListenerList eventListeners = new EventListenerList();

/**
 * Iterates over a <code>List</code> of EditPolcies, skipping any <code>null</code> values
 * encountered.
 */
protected static class EditPolicyIterator {
	private Iterator iter;
	private Object next;

	/**
	 * Constructs an Iterator for the given <code>List</code>.
	 */
	public EditPolicyIterator(List list) {
		iter = list.iterator();
	}

	/**
	 * 
	 */
	public EditPolicy next() {
		Object temp = next;
		next = null;
		return (EditPolicy)temp;
	}
	
	/**
	 * Availabilty of further <code>EditPolicies</code>
	 * is returned
	 *
	 * @return  <code>boolean</code> representing availability
	 *          of the next <code>EditPolicy</code>
	 */
	public boolean hasNext() {
		if (next != null)
			return true;
		if (iter.hasNext())
			do {
				next = iter.next();
			} while ((next == null) && iter.hasNext());
		return next != null;
	}
}

/**
 * <img src="../doc-files/green.gif"/> Activates this EditPart, which in turn activates
 * its children and EditPolicies. Subclasses should <em>extend</em> this method to add
 * listeners to the model. Activation indicates that the EditPart is realized in an
 * EditPartViewer. <code>deactivate()</code> is the inverse, and is eventually called on
 * all EditParts.
 * @see EditPart#activate()
 * @see #deactivate()
 */
public void activate() {
	debugPush("Activating");//$NON-NLS-1$
	setFlag(FLAG_ACTIVE, true);

	activateEditPolicies();

	List c = getChildren();
	for (int i = 0; i < c.size(); i++)
		((EditPart)c.get(i)).activate();

	fireActivated();
	debugPop();
}

/**
 * <img src="../doc-files/dblack.gif"/> Activates all EditPolicies installed on this part.
 * There is no reason to override this method.
 * @see #activate()
 */
protected void activateEditPolicies() {
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next().activate();
}

/**
 * <img src="../doc-files/black.gif"/> Adds a child <code>EditPart</code> to this
 * EditPart. This method is called from {@link #refreshChildren()}. The following events
 * occur in the order listed:
 * <OL>
 *   <LI>The child is added to the {@link #children} List, and its parent is set to
 *   <code>this</code>
 *   <LI>{@link #addChildVisual(EditPart)} is called to add the child's visual
 *   <LI>{@link EditPart#addNotify()} is called on the child.
 *   <LI><code>activate()</code> is called if this part is active
 *   <LI><code>EditPartListeners</code> are notified that the child has been added.
 * </OL>
 * <P>
 * Subclasses should implement {@link #addChildVisual(EditPart)}.
 * @param child The <code>EditPart</code> to add
 * @param index The index
 * @see  #addChildVisual(EditPart, int)
 * @see  #removeChild(EditPart)
 * @see  #reorderChild(EditPart,int)
 */
protected void addChild(EditPart child, int index) {
	Assert.isNotNull(child);
	if (index == -1)
		index = getChildren().size();
	if (children == null)
		children = new ArrayList(2);

	children.add(index, child);
	child.setParent(this);
	addChildVisual(child, index);
	child.addNotify();

	if (isActive())
		child.activate();
	fireChildAdded(child, index);
}

/**
 * <img src="../doc-files/blue.gif"/> Performs the addition of the child's <i>visual</i>
 * to this EditPart's Visual. The provided subclasses {@link AbstractGraphicalEditPart}
 * and {@link AbstractTreeEditPart} already implement this method correctly, so it is
 * unlikely that this method should be overridden.
 * @param child  The EditPart being added.
 * @param index  The child's position.
 * @see #addChild(EditPart, int)
 * @see AbstractGraphicalEditPart#removeChildVisual(EditPart)
 */
protected abstract void addChildVisual(EditPart child, int index);

/**
 * <img src="../doc-files/dblack.gif"/> Adds an EditPartListener.
 * @param listener the listener
 */
public void addEditPartListener(EditPartListener listener) {
	eventListeners.addListener(EditPartListener.class, listener);
}

/**
 * @see EditPart#addNotify()
 */
public void addNotify() {
	register();
	createEditPolicies();
	refresh();
}

/**
 * <img src="../doc-files/black.gif"/> Create the child <code>EditPart</code>
 * for the given model object. This method is called from {@link #refreshChildren()}.
 * <P>
 * By default, the implementation will delegate to the <code>EditPartViewer</code>'s
 * {@link EditPartFactory}. Subclasses may override this method instead of using a
 * Factory.
 * @param model the Child model object
 * @return The child EditPart
 */
protected EditPart createChild(Object model) {
	return getViewer().getEditPartFactory().createEditPart(this, model);
}

/**
 * <img src="../doc-files/green.gif"/>Creates the initial EditPolicies and/or reserves
 * slots for dynamic ones. Should be implemented to install the inital EditPolicies based
 * on the model's initial state. <code>null</code> can be used to reserve a "slot", should
 * there be some desire to guarantee the ordering of EditPolcies.
 * @see #doInitialize()
 * @see EditPart#installEditPolicy(Object, EditPolicy)
 */
protected abstract void createEditPolicies();

/**
 * <img src="../doc-files/green.gif"/> Deactivates this EditPart, and in turn deactivates
 * its children and EditPolicies. Subclasses should <em>extend</em> this method to remove
 * any listeners established in {@link #activate()}
 * @see  EditPart#deactivate()
 * @see  #activate()
 */
public void deactivate() {
	debugPush("Deactivating");//$NON-NLS-1$
	List c = getChildren();
	for (int i = 0; i < c.size(); i++)
		((EditPart)c.get(i)).deactivate();
	
	deactivateEditPolicies();
	
	setFlag(FLAG_ACTIVE, false);
	fireDeactivated();
	debugPop();
}

/**
 * <img src="../doc-files/dblack.gif"/> Deactivates all installed EditPolicies.
 */
protected void deactivateEditPolicies() {
	debug("Deactivating EditPolicies");//$NON-NLS-1$
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next().deactivate();
}

/**
 * This method will log a message to GEF's trace/debug system if the corresponding flag
 * for EditParts is set to true.
 * @param message a debug message
 */
protected final void debug(String message) {
	if (!GEF.DebugEditParts)
		return;
	GEF.debug("EDITPART:\t" + toString() + ":\t" + message);//$NON-NLS-2$//$NON-NLS-1$
}

void debugPop() {
	if (!GEF.DebugEditParts)
		return;
	GEF.debugPop();
}

void debugPush(String heading) {
	if (GEF.DebugEditParts)
		GEF.debugPush("EDITPART:\t" + toString()//$NON-NLS-1$
			+ ": " + heading);//$NON-NLS-1$
}

/**
 * This method will log the message to GEF's trace/debug system if the corrseponding flag
 * for FEEDBACK is set to true.
 * @param message  Message to be passed
 */
protected final void debugFeedback(String message) {
	if (GEF.DebugFeedback)
		GEF.debug("FEEDBACK:\t" + toString() + ":\t" + message);//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * <img src="../doc-files/black.gif"/> Erases source feedback for the given
 * <code>Request</code>. By default, this responsibility is delegated to this part's
 * <code>EditPolicies</code>. Subclasses should rarely extend this method.
 * <P>
 * <table>
 *   <tr>
 *     <td><img src="../doc-files/important.gif"/>
 *     <td>It is recommended that feedback be handled by EditPolicies, and not directly by
 *     the EditPart.
 *   </tr>
 * </table>
 * @param request identifies the type of feedback to erase.
 * @see #showSourceFeedback(Request)
 */
public void eraseSourceFeedback(Request request) {
	if (isActive()) {
		EditPolicyIterator iter = getEditPolicyIterator();
		while (iter.hasNext())
			iter.next().
				eraseSourceFeedback(request);
	}
	debugFeedback("Request to erase \"" + request.getType()//$NON-NLS-1$
		+ "\" SOURCE feedback");//$NON-NLS-1$
}

/**
 * <img src="../doc-files/black.gif"/> Erases target feedback for the given
 * <code>Request</code>. By default, this responsibility is delegated to this part's
 * EditPolicies.  Subclasses should rarely extend this method.
 * <P>
 * <table>
 *   <tr>
 *     <td><img src="../doc-files/important.gif"/>
 *     <td>It is recommended that feedback be handled by EditPolicies, and not directly by
 *     the EditPart.
 *   </tr>
 * </table>
 * @param request  Command requesting the erase.
 * @see  #showTargetFeedback(Request)
 */
public void eraseTargetFeedback(Request request) {
	if (isActive()) {
		EditPolicyIterator iter = getEditPolicyIterator();
		while (iter.hasNext())
			iter.next().
				eraseTargetFeedback(request);
	}
	debugFeedback("Request to erase \"" + request.getType()//$NON-NLS-1$
		+ "\" TARGET feedback");//$NON-NLS-1$
}

/**
 * <img src="../doc-files/dblack.gif"/> Notifies <code>EditPartListeners</code> that this
 * EditPart has been activated.
 */
protected void fireActivated() {
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			partActivated(this);
}

/**
 * <img src="../doc-files/dblack.gif"/> Notifies <code>EditPartListeners</code> that a
 * child has been added.
 * @param child  <code>EditPart</code> being added as child.
 * @param index  Position child is being added into.
 */
protected void fireChildAdded(EditPart child, int index) {
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			childAdded(child, index);
}

/**
 * <img src="../doc-files/dblack.gif"/> Notifies <code>EditPartListeners </code> that this
 * EditPart has been deactivated.
 */
protected void fireDeactivated() {
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			partDeactivated(this);
}

/**
 * <img src="../doc-files/dblack.gif"/> Notifies <code>EditPartListeners</code> that a
 * child is being removed.
 * @param child  <code>EditPart</code> being removed.
 * @param index  Position of the child in children list.
 */
protected void fireRemovingChild(EditPart child, int index) {
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			removingChild(child, index);
}

/**
 * <img src="../doc-files/dblack.gif"/> Notifies <code>EditPartListeners</code> that the
 * selection has changed.
 */
protected void fireSelectionChanged() {
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			selectedStateChanged(this);
}

/**
 * <img src="../doc-files/green.gif"/> Returns the <code>AccessibleEditPart</code> adapter
 * for this EditPart. Returns <code>null</code> if this EditPart is not accessible.
 * @return <code>null</code> or an AccessibleEditPart adapter
 */
protected AccessibleEditPart getAccessibleEditPart() {
	return null;
}

/**
 * <img src="../doc-files/blue.gif"/> Returns the specified adapter if recognized, for
 * example: {@link IPropertySource}. Otherwise returns <code>null</code>.
 * <P>
 * By default, the following adapter types are handled:
 * <UL>
 *   <LI>{@link IPropertySource} - If getModel() is an <code>IPropertySource</code> it
 *   will be returned. If getModel() is IAdaptable, it will be asked for its
 *   <code>IPropertySource</code> adapter, and the result is returned.
 *   <LI>{@link AccessibleEditPart} - If the adapter key is
 *   <code>AccessibleEditPart</code>, then {@link #getAccessibleEditPart()} is returned.
 * </UL>
 * <P>
 * Additional adapter types may be added in the future. Subclasses should extend this
 * method.
 * @see IAdaptable
 */
public Object getAdapter(Class key) {
	if (IPropertySource.class == key) {
		if (getModel() instanceof IPropertySource)
			return getModel();
		if (getModel() instanceof IAdaptable)
			return ((IAdaptable)getModel()).getAdapter(key);
	}
	if (AccessibleEditPart.class == key)
		return getAccessibleEditPart();
	return null;
}

/** * @see org.eclipse.gef.EditPart#getChildren() */
public List getChildren() {
	if (children == null)
		return Collections.EMPTY_LIST;
	return children;
}

/**
 * <img src="../doc-files/black.gif"/> Subclasses should rarely extend this method.
 * The default implementation combines the contributions from each installed
 * <code>EditPolicy</code>. This method is implemented indirectly using EditPolicies.
 * <P>
 * <table>
 *   <tr>
 *     <td><img src="../doc-files/important.gif"/>
 *     <td>It is recommended that Command creation be handled by EditPolicies, and not
 *     directly by the EditPart.
 *   </tr>
 * </table>
 * @see EditPart#getCommand(Request)
 * @see EditPolicy#getCommand(Request)
 * @param request the Request
 * @return a Command
 */
public Command getCommand(Request request) {
	Command command = null;
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext()) {
		if (command != null)
			command = command.chain(i.next().
				getCommand(request));
		else
			command = i.next().
				getCommand(request);
	}
	return command;
}

private List getEditPolicies() {
	return editPolicies;
}

private List getEditPolicyKeys() {
	return editPolicyKeys;
}

/**
 * Returns an iterator for the specified type of listener
 * @param clazz the Listener type over which to iterate * @return Iterator */
protected final Iterator getEventListeners(Class clazz) {
	if (eventListeners == null)
		return Collections.EMPTY_LIST.iterator();
	return eventListeners.getListeners(clazz);
}

/** * @see org.eclipse.gef.EditPart#getEditPolicy(Object) */
public EditPolicy getEditPolicy(Object key) {
	int index = getEditPolicyKeys().indexOf(key);
	if (index == -1)
		return null;
	return (EditPolicy)getEditPolicies().get(index);
}

/**
 * Used internally to iterate over the installed EditPolicies. While EditPolicy slots may
 * be reserved with <code>null</code>, the iterator only returns the non-null ones.
 * @return an EditPolicyIterator
 */
protected final EditPolicyIterator getEditPolicyIterator() {
	return new EditPolicyIterator(editPolicies);
}

/**
 * Returns the boolean value of the given flag. Specifically, returns <code>true</code> if
 * the bitwise AND of the specified flag and the internal flags field is non-zero.
 * 
 * @param flag Bitmask indicating which flag to return
 * @return the requested flag's value
 * @see  #setFlag(int,boolean)
 */
protected final boolean getFlag(int flag) {
	return (flags & flag) != 0;
}

/** * @see org.eclipse.gef.EditPart#getModel() */
public Object getModel() {
	return model;
}

/**
 * <img src="../doc-files/green.gif"/> Returns a <code>List</code> containing the children
 * model objects. If this EditPart's model is a container, this method should be
 * overridden to returns its children. This is what causes children EditParts to be
 * created.
 * <P>
 * Called by {@link #refreshChildren()}. Must not return <code>null</code>.
 * @return the List of children
 */
protected List getModelChildren() {
	return Collections.EMPTY_LIST;
}

/** * @see org.eclipse.gef.EditPart#getParent() */
public EditPart getParent() {
	return parent;
}

/** * @see org.eclipse.gef.EditPart#getRoot() */
public RootEditPart getRoot() {
	return getParent().getRoot();
}

/** * @see org.eclipse.gef.EditPart#getSelected() */
public int getSelected() {
	return selected;
}

/**
 * <img src="../doc-files/black.gif"/> Returns the <code>EditPart</code> which is the
 * target of the <code>Request</code>.  The default implementation delegates this method
 * to the installed EditPolicies. The first non-<code>null</code> result returned by an
 * EditPolicy is returned. Subclasses should rarely extend this method.
 * <P>
 * <table>
 *   <tr>
 *     <td><img src="../doc-files/important.gif"/>
 *     <td>It is recommended that targeting be handled by EditPolicies, and not directly
 *     by the EditPart.
 *   </tr>
 * </table>
 * @param request Describes the type of target desired.
 * @return <code>null</code> or the <i>target</i> <code>EditPart</code>
 * @see EditPart#getTargetEditPart(Request)
 * @see EditPolicy#getTargetEditPart(Request)
 */
public EditPart getTargetEditPart(Request request) {
	EditPolicyIterator i = getEditPolicyIterator();
	EditPart editPart;
	while (i.hasNext()) {
		editPart = i.next()
			.getTargetEditPart(request);
		if (editPart != null)
			return editPart;
	}

	if (RequestConstants.REQ_SELECTION == request.getType()) {
		if (isSelectable())
			return this;
	}

	return null;
}

/**
 * A convenience method which uses the Root to obtain the EditPartViewer.
 * @throws NullPointerException if the root is not found
 * @return the EditPartViewer
 */
public EditPartViewer getViewer() {
	return getRoot().getViewer();
}
/** * @see org.eclipse.gef.EditPart#hasFocus() */
public boolean hasFocus() {
	return getFlag(FLAG_FOCUS);
}

/** * @see org.eclipse.gef.EditPart#installEditPolicy(Object, EditPolicy) */
public void installEditPolicy(Object key, EditPolicy editPolicy) {
	Assert.isNotNull(key, "Edit Policies must be installed with keys");//$NON-NLS-1$
	int index = editPolicyKeys.indexOf(key);
	if (index > -1) {
		EditPolicy old = (EditPolicy)editPolicies.get(index);
		if (old != null && isActive())
			old.deactivate();
		editPolicies.set(index, editPolicy);
	} else {
		editPolicyKeys.add(key);
		editPolicies.add(editPolicy);
	}
	if (editPolicy != null) {
		editPolicy.setHost(this);
		if (isActive())
			editPolicy.activate();
	}
}

/**
 * @return <code>true</code> if this EditPart is active.
 */
public boolean isActive() {
	return getFlag(FLAG_ACTIVE);
}

/**
 * Reserved for future use
 * @return boolean */
public boolean isSelectable() {
	return true;
}

/**
 * Subclasses should extend this method to handle Requests. For now, the default
 * implementation does not handle any requests.
 * @see org.eclipse.gef.EditPart#performRequest(Request) */
public void performRequest(Request req) {
}

/**
 * <img src="../doc-files/blue.gif"/> Refreshes all properties visually displayed by this
 * EditPart.  The default implementation will call {@link #refreshChildren()} to update
 * its structural features. It also calls {@link #refreshVisuals()} to update its own
 * displayed properties. Subclasses should extend this method to handle additional types
 * of structural refreshing.
 */
public void refresh() {
	debug("Refresh");//$NON-NLS-1$
	refreshVisuals();
	refreshChildren();
}

/**
 * <img src="../doc-files/black.gif"/> Updates the set of children EditParts so that it
 * is in sync with the model children. This method is called from {@link #refresh()}, and
 * may also be called in response to notification from the model.
 * <P>
 * The update is performed by comparing the exising EditParts with the set of
 * model children returned from {@link #getModelChildren()}. EditParts whose
 * model no longer exists are {@link #removeChild(EditPart) removed}. New models have
 * their EditParts {@link #createChild(Object) created}. Subclasses should override
 * <code>getModelChildren()</code>.
 * <P>
 * This method should <em>not</em> be overridden.
 * @see #getModelChildren()
 */
protected void refreshChildren() {
	int i;
	EditPart editPart;
	Object model;

	Map modelToEditPart = new HashMap();
	List children = getChildren();

	for (i = 0; i < children.size(); i++) {
		editPart = (EditPart)children.get(i);
		modelToEditPart.put(editPart.getModel(), editPart);
	}

	List modelObjects = getModelChildren();

	for (i = 0; i < modelObjects.size(); i++) {
		model = modelObjects.get(i);

		//Do a quick check to see if editPart[i] == model[i]
		if (i < children.size()
			&& ((EditPart) children.get(i)).getModel() == model)
				continue;

		//Look to see if the EditPart is already around but in the wrong location
		editPart = (EditPart)modelToEditPart.get(model);

		if (editPart != null)
			reorderChild (editPart, i);
		else {
			//An editpart for this model doesn't exist yet.  Create and insert one.
			editPart = createChild(model);
			addChild(editPart, i);
		}
	}
	List trash = new ArrayList();
	for (; i < children.size(); i++)
		trash.add(children.get(i));
	for (i = 0; i < trash.size(); i++) {
		EditPart ep = (EditPart)trash.get(i);
		removeChild(ep);
	}
}

/**
 * <img src="../doc-files/green.gif"/>
 * Refreshes this EditPart's <i>visuals</i>. This method is called by {@link #refresh()},
 * and may also be called in response to notifications from the model.
 */
protected void refreshVisuals() {
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Registers itself in the viewer's various registries. If your EditPart has a 1-to-1
 * relationship with a visual object and a 1-to-1 relationship with a model object, the
 * default implementation should be sufficent.
 *
 * @see #unregister()
 * @see EditPartViewer#getVisualPartMap()
 * @see EditPartViewer#getEditPartRegistry()
 */
protected void register() {
	registerModel();
	registerVisuals();
	registerAccessibility();
}

/**
 * Registers the <code>AccessibleEditPart</code> adapter.
 * @see #getAccessibleEditPart()
 */
protected final void registerAccessibility() {
	if (getAccessibleEditPart() != null)
		getViewer().registerAccessibleEditPart(getAccessibleEditPart());
}

/**
 * <img src="../doc-files/black.gif"/>
 * Registers the <i>model</i> in the {@link EditPartViewer#getEditPartRegistry()}.
 * Subclasses should only extend this method if they need to register this EditPart in
 * additional ways.
 */
protected void registerModel() {
	getViewer().getEditPartRegistry().put(getModel(), this);	
}

/**
 * <img src="../doc-files/black.gif"/>
 * Registers the <i>visuals</i> in the {@link EditPartViewer#getVisualPartMap()}.
 * Subclasses should override this method for the visual part they support. {@link
 * AbstractGraphicalEditPart} and {@link AbstractTreeEditPart} already do this.
 */
protected void registerVisuals() { }

/**
 * <img src="../doc-files/black.gif"/>
 * Removes a child <code>EditPart</code>. This method is called from {@link
 * #refreshChildren()}. The following events occur in the order listed:
 * <OL>
 *   <LI><code>EditPartListeners</code> are notified that the child is being removed
 *   <LI><code>deactivate()</code> is called if the child is active
 *   <LI>{@link EditPart#removeNotify()} is called on the child.
 *   <LI>{@link #removeChildVisual(EditPart)} is called to remove the child's visual
 *   object.
 *   <LI>The child's parent is set to <code>null</code>
 * </OL>
 * <P>
 * Subclasses should implement {@link #removeChildVisual(EditPart)}.
 * @param child EditPart being removed
 * @see  #addChild(EditPart,int)
 */
protected void removeChild(EditPart child) {
	Assert.isNotNull(child);
	int index = getChildren().indexOf(child);
	if (index < 0)
		return;
	fireRemovingChild(child, index);
	if (isActive())
		child.deactivate();
	child.removeNotify();
	removeChildVisual(child);
	child.setParent(null);
	getChildren().remove(child);
}

/**
 * Removes the childs visual from this EditPart's visual. Subclasses should implement this
 * method to support the visual type they introduce, such as Figures or TreeItems.
 * @param child the child EditPart
 */
protected abstract void removeChildVisual(EditPart child);

/** * <img src="../doc-files/dblack.gif"/> No reason to override
 * @see org.eclipse.gef.EditPart#removeEditPartListener(EditPartListener) */
public void removeEditPartListener(EditPartListener listener) {
	eventListeners.removeListener(EditPartListener.class, listener);
}

/**
 * <img src="../doc-files/dblack.gif"/> No reason to override
 * @see EditPart#removeEditPolicy(Object)
 */
public void removeEditPolicy(Object key) {
	int i = editPolicyKeys.indexOf(key);
	if (i == -1)
		return;
	EditPolicy policy = (EditPolicy)editPolicies.get(i);
	if (isActive() && policy != null)
		policy.deactivate();
	editPolicies.set(i, null);
}

/**
 * <img src="../doc-files/black.gif"/> Removes all references from the
 * <code>EditPartViewer</code> to this EditPart. This includes:
 * <UL>
 *   <LI>deselecting this EditPart if selected
 *   <LI>setting the Viewer's focus to <code>null</code> if this EditPart has <i>focus</i>
 *   <LI>{@link #unregister()} this EditPart
 * </UL>
 * <P>
 * In addition, <code>removeNotify()</code> is called recusively on all children
 * EditParts. Subclasses should <em>extend</em> this method to perform any additional
 * cleanup.
 * @see org.eclipse.gef.EditPart#removeNotify()
 */
public void removeNotify() {
	debugPush("removeNotify"); //$NON-NLS-1$
	if (getSelected() != SELECTED_NONE)
		getViewer().deselect(this);
	if (hasFocus())
		getViewer().setFocus(null);

	List children = getChildren();
	for (int i = 0; i < children.size(); i++)
		((EditPart)children.get(i))
			.removeNotify();
	unregister();
	GEF.debugPop();
}

/**
 * <img src="../doc-files/dblack.gif"/>
 * Moves a child <code>EditPart</code> into a lower index than it currently occupies. This
 * method is called from {@link #refreshChildren()}.
 * @param editpart the child being reordered
 * @param index new index for the child
 */
protected void reorderChild(EditPart editpart, int index) {
	removeChildVisual(editpart);
	List children = getChildren();
	children.remove(editpart);
	children.add(index, editpart);
	addChildVisual(editpart, index);
}

/**
 * Sets the value of the specified flag. Flag values are decalared as static constants.
 * Subclasses may define additional constants above {@link #MAX_FLAG}.
 * @param flag Flag being set
 * @param value Value of the flag to be set
 * @see  #getFlag(int)
 */
protected final void setFlag(int flag, boolean value) {
	if (value) flags |= flag;
	else flags &= ~flag;
}

/** * @see org.eclipse.gef.EditPart#setFocus(boolean) */
public void setFocus(boolean value) {
	if (hasFocus() == value)
		return;
	setFlag(FLAG_FOCUS, value);
	fireSelectionChanged();
}

/**
 * <img src="../doc-files/black.gif"/> Set the primary model object that this EditPart
 * represents. This method is used by an <code>EditPartFactory</code> when creating an
 * EditPart.
 * @see EditPart#setModel(Object)
 */
public void setModel(Object model) {
	if (getModel() == model)
		return;
	this.model = model;
}

/**
 * <img src="../doc-files/dblack.gif"/> Sets the parent EditPart. There is no reason to
 * override this method.
 * @see EditPart#setParent(EditPart)
 */
public void setParent(EditPart parent) {
	if (this.parent == parent)
		return;
	this.parent = parent;
}

/**
 * <img src="../doc-files/blue.gif"/> sets the selected state for this EditPart. This
 * method should rarely be overridden. Instead, EditPolicies that are selection-aware will
 * listen for notification of this property changing.
 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy
 * @see EditPartListener#selectedStateChanged(EditPart)
 * @see EditPart#setSelected(int)
 * @param value the selected value
 */
public void setSelected(int value) {
	if (selected == value)
		return;
	selected = value;
	fireSelectionChanged();
}

/**
 * <img src="../doc-files/black.gif"/> Shows or updates source feedback for the given
 * <code>Request</code>. By default, this responsibility is delegated to this part's
 * EditPolicies.  Subclasses should rarely extend this method.
 * <P>
 * <table>
 *   <tr>
 *     <td><img src="../doc-files/important.gif"/>
 *     <td>It is recommended that feedback be handled by EditPolicies, and not directly by
 *     the EditPart.
 *   </tr>
 * </table>
 * @see EditPolicy#showSourceFeedback(Request)
 * @see EditPart#showSourceFeedback(Request)
 * @param request the Request
 */
public void showSourceFeedback(Request request) {
	debugFeedback("Request to show \"" + request.getType()//$NON-NLS-1$
		+ "\" SOURCE feedback");//$NON-NLS-1$
	if (!isActive())
		return;
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next()
			.showSourceFeedback(request);
}

/**
 * <img src="../doc-files/black.gif"/> Shows or updates target feedback for the given
 * <code>Request</code>. By default, this responsibility is delegated to this part's
 * EditPolicies.  Subclasses should rarely extend this method.
 * <P>
 * <table>
 *   <tr>
 *     <td><img src="../doc-files/important.gif"/>
 *     <td>It is recommended that feedback be handled by EditPolicies, and not directly by
 *     the EditPart.
 *   </tr>
 * </table>
 * @see EditPolicy#showTargetFeedback(Request)
 * @see EditPart#showTargetFeedback(Request)
 * @param request the Request
 */
public void showTargetFeedback(Request request) {
	debugFeedback("Request to show \"" + request.getType()//$NON-NLS-1$
		+ "\" TARGET feedback");//$NON-NLS-1$
	if (!isActive())
		return;
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next()
			.showTargetFeedback(request);
}

/**
 * <img src="../doc-files/blue.gif"/> Describes this EditPart for developmental debugging
 * purposes.
 * @return a description
 */
public String toString() {
	String c = getClass().getName();
	c = c.substring(c.lastIndexOf('.') + 1);
	return c + "( " + getModel() + " )";//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * <img src="../doc-files/black.gif"/> Returns <code>true</code> if this
 * <code>EditPart</code> understand the given <code>Request</code>. By default, this
 * responsibility is delegated to this part's installed EditPolicies.
 * <P>
 * <table>
 *   <tr>
 *     <td><img src="../doc-files/important.gif"/>
 *     <td>It is recommended that EditPolicies implement <code>understandsRequest()</code>
 *   </tr>
 * </table>
 * @see EditPart#understandsRequest(Request)
 */
public boolean understandsRequest(Request req) {
	EditPolicyIterator iter = getEditPolicyIterator();
	while (iter.hasNext()) {
		if (iter.next().understandsRequest(req))
			return true;
	}
	return false;
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Undoes any registration performed by {@link #register()}.
 * The provided base classes will correctly unregister their visuals.
 */
protected void unregister() {
	unregisterAccessibility();
	unregisterVisuals();
	unregisterModel();
	debug("Unregister");//$NON-NLS-1$
}

/**
 * Unregisters the {@link #getAccessibleEditPart() AccessibleEditPart} adapter.
 */
protected final void unregisterAccessibility() {
	if (getAccessibleEditPart() != null)
		getViewer().unregisterAccessibleEditPart(getAccessibleEditPart());
}

/**
 * <img src="../doc-files/black.gif"/>
 * Unregisters the <i>model</i> in the {@link EditPartViewer#getEditPartRegistry()}.
 * Subclasses should only extend this method if they need to unregister this EditPart in
 * additional ways.
 */
protected void unregisterModel() {
	Map registry = getViewer().getEditPartRegistry();
	if (registry.get(getModel()) == this)
		registry.remove(getModel());
}

/**
 * <img src="../doc-files/black.gif"/>
 * Unregisters the <i>visuals</i> in the {@link EditPartViewer#getVisualPartMap()}.
 * Subclasses should override this method for the visual part they support. {@link
 * AbstractGraphicalEditPart} and {@link AbstractTreeEditPart} already do this.
 */
protected void unregisterVisuals() { }

}
