package com.ibm.etools.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;

import com.ibm.etools.draw2d.EventListenerList;
import com.ibm.etools.common.command.Command;

import com.ibm.etools.gef.*;

/**
 The baseline implementation for the {@link EditPart} interface.
 <P>Since this is the default implementation of an interface, this document
 deals with proper sub-classing of this implementation.  This class is not
 the API.  For documentation on proper usage of the public API, see the
 * documentation for the interface itself: {@link EditPart}.
 <P><Table>
 	<tr><TD><img src="../doc-files/green.gif"/>
 		<TD>Indicates methods that are commonly overridden or even abstract
 	</tr><tr><TD><img src="../doc-files/blue.gif"/>
 		<TD>These methods might be overridden.  Especially if you were
 			extending this class directly.
 	</tr><tr><TD><img src="../doc-files/black.gif"/>
 		<TD>Should rarely be overridden.
 	</tr><tr><TD><img src="../doc-files/dblack.gif"/>
 		<TD>Essentially "internal" and should never be overridden.
</tr></table>
 <P>This class assumes no visual representation.
 Subclasses {@link AbstractGraphicalEditPart} and {@link AbstractTreeEditPart}
 add support for {@link com.ibm.etools.draw2d.IFigure Figures} and
 {@link org.eclipse.swt.widgets.TreeItem TreeItems} respectively.
 <P>AbstractEditPart provides support for, but does not require the usage of:
 <UL>
   <LI> A Children relationship
   <LI> A Source connections relationship. i.e., connections which originate from this part.
   <LI> A Target connection relationship. i.e., connections which terminate at this part
 </UL>
 */
public abstract class AbstractEditPart
	implements EditPart, RequestConstants, IAdaptable
{

static class Assert
	extends com.ibm.etools.gef.internal.Assert{}

protected static final int
	FLAG_INITIALIZED = 1,
	FLAG_FOCUS = 4,
	FLAG_ACTIVE   = 2;

/**
 The highest bit used as a bitmask in this class.
 A single integer field is used to store up to 32 boolean values.
 Subclasses may call {@link #setFlag(int, boolean)} and
 {@link #getFlag(int)} methods
 with additional bitmasks iff those masks are greater than MAX_FLAG.
 Example: <P><code> static final int MY_FLAG = super.MAX_FLAG << 1</code>;
 */
protected static final int
		MAX_FLAG = FLAG_FOCUS;

private   	Object model;
private	int flags;
private   	EditPart parent;
private	int selected;

private 	List editPolicies = new ArrayList(2);
private   	List editPolicyKeys = new ArrayList(2);
protected 	List children;

/**
 * @deprecated do not reference, call getEventListeners(Class) instead.
 */
protected	EventListenerList eventListeners = new EventListenerList();

/**
 * Iterates over a <code>List</code> of EditPolcies, skipping
 * any <code>null</code> values encountered.
 */
protected static class EditPolicyIterator {
	protected Iterator iter;
	protected Object next;

	/**
	 * Constructs an Iterator for the given <code>List</code>.
	 */
	public EditPolicyIterator(List list){
		iter = list.iterator();
	}

	/**
	 * Returns the next <code>EditPolicy</code>
	 * 
	 * @return  Next <code>EditPolicy</code> if available,
	 *          else returns <code>null</code>
	 */
	public EditPolicy next(){
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
	public boolean hasNext(){
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
 * <img src="../doc-files/green.gif"/> Initializes or reactivates
 * this part and hooks any needed listeners.
 * This method is commonly overridden to add listeners to the model.
 * Subclasses should always call <code>super.activate()</code>.
 * Activation indicates that the EditPart is realized in an EditPartViewer.
 * <code>deactivate()</code> is guaranteed to be called.  Both may be called
 * multiple times.
 * <P>During activation, and EditPart will generally:
 * <OL>
 *   <LI> Register itself as an observer to its model
 *   <LI> Activate its editpolicies (implemented here)
 *   <LI> Activate all associated EditParts managed by it (implemented here)
 * </OL>
 * An EditPart is generally activated by the part that manages it,
 * usually a parent.
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
 * <img src="../doc-files/dblack.gif"/>
 * Activates all EditPolicies.  There is little reason to override this method.
 * @see #activate()
 */
protected void activateEditPolicies(){
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next().activate();
}

/**
 * <img src="../doc-files/black.gif"/> Adds a child <code>EditPart</code>
 * to this EditPart. If activated, this editpart will activate the child.
 * <P>Notifies listeners that a child has been added.
 * <code>primAddChild(EditPart, int)</code> is called to separate
 * the act of performing the add from the act of activation and
 * the firing of notification to listeners.
 * Subclasses should generally override primAddChild(EditPart, int) instead
 * of this method.
 *
 * @param child The <code>EditPart</code> to add
 * @param index The index
 * @see  #addChildVisual(EditPart, int)
 * @see  #removeChild(EditPart)
 * @see  #reorderChild(EditPart,int)
 */
protected void addChild(EditPart child, int index) {
	if (index == -1)
		index = getChildren().size();
	if (children == null)
		children = new ArrayList(2);

	Assert.isNotNull(child);
	children.add(index, child);
	addChildVisual(child, index);
	child.setParent(this);

	if (isActive())
		child.activate();
	fireChildAdded(child, index);
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Performs the actual addition of the child to this EditPart.
 * Subclasses must supply additional code to update the visuals being used.
 * The provided subclasses {@link AbstractGraphicalEditPart} and
 * {@link AbstractTreeEditPart} already implement this method correctly,
 * so it is unlikely that this method should be overridden.
 * @param child  The EditPart being added.
 * @param index  The child's position.
 * @see #addChild(EditPart, int)
 * @see AbstractGraphicalEditPart#removeChildVisual(EditPart)
 */
protected abstract void addChildVisual(EditPart child, int index);

/**
 * <img src="../doc-files/dblack.gif"/>
 * Adds an editpart listener.
 */
public void addEditPartListener(EditPartListener listener){
	eventListeners.addListener(EditPartListener.class, listener);
}

/**
 * <img src="../doc-files/green.gif"/> Override to create the child
 * <code>EditPart</code> for the given model object.
 * This method is called from {@link #refreshChildren()}
 * Subclasses may implement this method in place, or have it call
 * out to some type of EditPart factory associated with their application.
 * This method should be overriden together with {@link #getModelChildren()}.
 *
 * @param model the Child model object
 * @return The child EditPart
 * @see #getModelChildren()
 */
protected EditPart createChild(Object model) {
	return getViewer().getEditPartFactory().createEditPart(this, model);
}

/**
 * <img src="../doc-files/green.gif"/> Creates a
 * <code>ConnectionEditPart</code> for the given model.
 * Similar to {@link #createChild(Object)}</code>.
 * called from <code>refreshSourceConnections()</code>,
 * and <code>refreshTargetConnections()</code>.
 * 
 * @param model the connection model object
 */
protected ConnectionEditPart createConnection(Object model){
	return null;
}

protected ConnectionEditPart createOrFindConnection(Object model){
	try {
		ConnectionEditPart connx;
		connx = (ConnectionEditPart)getViewer().getEditPartRegistry().get(model);
		if (connx != null)
			return connx;
	} catch (ClassCastException exception){
	}
	return createConnection(model);
}

/**
 * <img src="../doc-files/green.gif"/>Creates
 * the initial EditPolicies and reserves slots for dynamic ones.
 * Should be overridden to install the inital edit policies based on the
 * model's initial state.
 * <code>null</code> can be used to reserve a "slot", should there be
 * some desire to guarantee the ordering of EditPolcies.
 *
 * @see #doInitialize()
 * @see #installEditPolicy(Object, EditPolicy)
 */
abstract protected void createEditPolicies();

/**
 * <img src="../doc-files/green.gif"/> Override this
 * method to remove any listeners established in {@link #activate()},
 * and <EM>ALWAYS</EM> call <code>super.deactivate()</code> to ensure
 * that children, etc., are also deactivated.  activate() may be called
 * again afterwards.
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
 * <img src="../doc-files/dblack.gif"/>
 * Deactivates all installed EditPolicies.
 */
protected void deactivateEditPolicies(){
	debug("Deactivating EditPolicies");//$NON-NLS-1$
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next().deactivate();
}

/**
 * This method will log the message to GEF's debug system if the
 * corresponding flag for EditParts is set to true.
 *
 * @param message  Message to be passed
 */
protected void debug(String message){
	if (!GEF.DebugEditParts)
		return;
	GEF.debug("EDITPART:\t" + toString() + ":\t" + message);//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * package private method.
 */
void debugPop(){
	if (!GEF.DebugEditParts)
		return;
	GEF.debugPop();
}

/** 
 * package private method.
 */
void debugPush(String heading){
	if (GEF.DebugEditParts)
		GEF.debugPush("EDITPART:\t" + toString() + ": " + heading);//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * This method will log the message to GEF's debug system if the
 * corrseponding flag for FEEDBACK is set to true.
 *
 * @param message  Message to be passed
 */
protected void debugFeedback(String message){
	if (GEF.DebugFeedback)
		GEF.debug("FEEDBACK:\t" + toString() + ":\t" + message);//$NON-NLS-2$//$NON-NLS-1$
}

public void dispose(){
	debugPush("disposing"); //$NON-NLS-1$
	if (getSelected() != SELECTED_NONE)
		getViewer().deselect(this);
	if (hasFocus())
		getViewer().setFocus(null);

	List children = getChildren();
	for (int i=0; i<children.size(); i++)
		((EditPart)children.get(i)).dispose();
	unregister();
	GEF.debugPop();
}

/**
 * Initializes the entire state of the EditPart.
 */
protected void doInitialize(){
	createEditPolicies();
	refresh();
}

/**
 * <img src="../doc-files/black.gif"/>
 * Erases source feedback for the given Request.
 * By default, this responsibility is delegated to this
 * part's EditPolicies.  This method should not be overridden.
 *
 * @param request identifies the type of feedback to erase.
 * @see  #showSourceFeedback(Request)
 */
public void eraseSourceFeedback(Request request) {
	if (isActive()){
		EditPolicyIterator iter = getEditPolicyIterator();
		while (iter.hasNext())
			iter.next().
				eraseSourceFeedback(request);
	}
	debugFeedback("Request to erase \"" + request.getType() + "\" SOURCE feedback");//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * <img src="../doc-files/black.gif"/>
 * Erases target feedback for the given Request.
 * By default, this responsibility is delegated to this
 * part's EditPolicies.  This method should not be overridden.
 *
 * @param request  Command requesting the erase.
 * @see  #showTargetFeedback(Request)
 */
public void eraseTargetFeedback(Request request) {
	if (isActive()){
		EditPolicyIterator iter = getEditPolicyIterator();
		while (iter.hasNext())
			iter.next().
				eraseTargetFeedback(request);
	}
	debugFeedback("Request to erase \"" + request.getType() + "\" TARGET feedback");//$NON-NLS-2$//$NON-NLS-1$
}

protected void fireActivated(){
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			partActivated(this);
}

/**
 * Notifies listeners that a child has been added.
 *
 * @param child  <code>EditPart</code> being added as child.
 * @param index  Position child is being added into.
 */
protected void fireChildAdded(EditPart child, int index){
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			childAdded(child, index);
}

protected void fireDeactivated(){
	Iterator listeners = getEventListeners(EditPartListener.class);
	while (listeners.hasNext())
		((EditPartListener)listeners.next()).
			partDeactivated(this);
}

/**
 * Notifies listeners prior to removing a child.
 *
 * @param child  <code>EditPart</code> being removed.
 * @param index  Position of the child in children list.
 */
protected void fireRemovingChild(EditPart child, int index){
	Iterator listeners = getEventListeners(EditPartListener.class);
	while(listeners.hasNext())
		((EditPartListener)listeners.next()).
			removingChild(child, index);
}

/**
 * Called when the selected state for this EditPart changes.
 * Notifies to all listeners
 */
protected void fireSelectionChanged(){
	Iterator listeners = getEventListeners(EditPartListener.class);
	while(listeners.hasNext())
		((EditPartListener)listeners.next()).
			selectedStateChanged(this);
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Returns the adapter of the given type, for example: {@link IPropertySource}.
 * If your model implements IPropertySource, or if it is IAdaptable then there
 * is no reason to override this method.  Or, if you do not need propertysheet
 * support.
 * EditParts are the objects sent out as selection to other viewers.  If you have
 * viewers that require additional adapter types, return them here.
 * @see IAdaptable
 */
public Object getAdapter(Class key) {
	if (key.equals(IPropertySource.class)) {
		if (getModel() instanceof IPropertySource)
			return getModel();
		if (getModel() instanceof IAdaptable)
			return ((IAdaptable)getModel()).getAdapter(key);
	}
	return null;
}

/**
 * <img src="../doc-files/black.gif"/>
 * Returns a non-<code>null</code> List containing the children EditParts.
 *
 * @return children <code>EditParts</code>
 */
public List getChildren() {
	if (children == null)
		return Collections.EMPTY_LIST;
	return children;
}

/**
 * <img src="../doc-files/black.gif"/>
 * Returns <code>null</code> or a command that performs the operation
 * specified by the Request.  This method should not be overridden.
 * By default, each installed EditPolicy will be given the opportunity
 * to return its contribution, and all contributions will be chained together
 * and returned. 
 *
 * @param Request A request describing the command to be created.
 * @see EditPolicy#getCommand(Request)
 */
public Command getCommand(Request request) {
	Command command = null;
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext()){
		if (command != null)
			command = command.chain(i.next().
				getCommand(request));
		else
			command = i.next().
				getCommand(request);
	}
	return command;
}

private List getEditPolicies(){
	return editPolicies;
}

private List getEditPolicyKeys(){
	return editPolicyKeys;
}

protected Iterator getEventListeners(Class clazz){
	if (eventListeners == null)
		return Collections.EMPTY_LIST.iterator();
	return eventListeners.getListeners(clazz);
}

public EditPolicy getEditPolicy(Object key){
	int index = getEditPolicyKeys().indexOf(key);
	if (index == -1)
		return null;
	return (EditPolicy)getEditPolicies().get(index);
}

/**
 * Used internally to iterate over the installed EditPolicies.
 * While EditPolicy slots may be reserved with <code>null</code>, the iterator
 * only returns the non-null ones.
 */
protected EditPolicyIterator getEditPolicyIterator() {
	return new EditPolicyIterator(editPolicies);
}

/**
 * Returns the boolean value of the given flag.
 * Specifically, returns <code>true</code> if the bitwise AND of the bitmask
 * and the internal flags field is non-zero.
 * 
 * @param flag Bitmask indicating which flag to return
 * @see  #setFlag(int,boolean)
 */
protected boolean getFlag(int flag){
	return (flags & flag) != 0;
}

public KeyHandler getKeyHandler(){
	return null;
}

/**
 * <img src="../doc-files/black.gif"/> Returns the primary model object for
 * this EditPart.
 */
public Object getModel(){return model;}

/**
 * <img src="../doc-files/green.gif"/>
 * Returns a <code>List</code> containing the children model objects.
 * Iff this EditPart's model is a composite, this method should be overridden
 * to returns its children.
 * {@link #refreshChildren()} requires that this List be non-<code>null</code>.
 */
 protected List getModelChildren() {return Collections.EMPTY_LIST;}

/*
 * defined by interface
 */
public EditPart getParent() {
	return parent;
}

/*
 * defined by interface
 */
public RootEditPart getRoot() {
	return getParent().getRoot();
}

/*
 * defined by interface
 */
public int getSelected(){
	return selected;
}

/**
 * <img src="../doc-files/black.gif"/>
 * Returns the <code>EditPart</code> which is the
 * target of the Request.  This implementation determines
 * the target by delegating to its EditPolicies.  An EditPart should not
 * understand any editing requests, instead, editing behavior is added by
 * installing EditPolicies.
 * The first non-<code>null</code> result returned by an
 * EditPolicy is returned.  If no EditPolicy understand the request
 * (all EditPolicies return null),
 * this EditPart is not the target, and the request is forwarded
 * to the parent EditPart.
 *
 * @param request Describes the type of target desired.
 */
public EditPart getTargetEditPart(Request request){
	EditPolicyIterator i = getEditPolicyIterator();
	EditPart editPart;
	while (i.hasNext()){
		editPart = i.next().
			getTargetEditPart(request);
		if (editPart != null)
			return editPart;
	}

	if (RequestConstants.REQ_SELECTION == request.getType()){
		if (isSelectable())
			return this;
	}

	/*
	 *	No input policy understood this request.
	 *	Promote the request to whatever is underneath.
	 *	Calling getParent() is wrong since the parent may not
	 *	be immediately underneath this EditPart, as in connections.
	 */
	GEF.hack();
	if (getParent() != null)
		return getParent().getTargetEditPart(request);

	return null;
}

/**
 * A convenience method which uses the Root to obtain the Viewer.
 */
protected EditPartViewer getViewer() {
	if (getRoot() == null)
		return null;
	return getRoot().getViewer();
}

public boolean hasFocus(){
	return getFlag(FLAG_FOCUS);
}

/**
 * Calls {@link #doInitialize()} if {@link #shouldInitialize()} returns true.
 * Sets a flag indicating that initialization has occurred.
 */
protected void initialize(){
	if (!shouldInitialize())
		return;
	doInitialize();
	setFlag(FLAG_INITIALIZED, true);
}

/**
 * Installs the given EditPolicy using the given key or ID.
 * Previously installed polciies for the same key are deactivated
 * and overwritten.
 * @param key Identifier for the type of EditPolicy.
 * @param editPolicy the EditPolicy being added.
 * @see #removeEditPolicy(Object)
 */
public void installEditPolicy(Object key, EditPolicy editPolicy){
	Assert.isNotNull(key, "Edit Policies must be installed with keys");//$NON-NLS-1$
	int index = editPolicyKeys.indexOf(key);
	if (index > -1){
		EditPolicy old = (EditPolicy)editPolicies.get(index);
		if (old != null && isActive())
			old.deactivate();
		editPolicies.set(index, editPolicy);
	}
	else {
		editPolicyKeys.add(key);
		editPolicies.add(editPolicy);
	}
	if (editPolicy != null){
		editPolicy.setHost(this);
		if (isActive())
			editPolicy.activate();
	}
}

/**
 * Returns true if this EditPart is active.
 */
protected boolean isActive() {
	return getFlag(FLAG_ACTIVE);
}

protected boolean isSelectable() {
	return true;
}

public void performRequest(Request req){
}

/**
 * <img src="../doc-files/blue.gif"/> Refreshes all properties visually
 * displayed by this EditPart.  The default implementation will call
 * {@link #refreshChildren()} to update its structural features.
 * It also calls {@link #refreshVisuals()} to update its own displayed properties.
 * This method should only be overridden if the previous four methods are not
 * sufficient.
 */
public void refresh(){
	debug("Refresh");//$NON-NLS-1$
	refreshVisuals();
	refreshChildren();
}

/**
 * <img src="../doc-files/black.gif"/> Refreshes the set of Children.
 * This method should not be overridden. {@link #createChild(Object)}
 * and {@link #getModelChildren()} should be overridden together.
 */
protected void refreshChildren(){
	int i;
	EditPart editPart;
	Object model;

	Map modelToEditPart = new HashMap();
	List children = getChildren();

	for (i=0; i < children.size(); i++){
		editPart = (EditPart)children.get(i);
		modelToEditPart.put(editPart.getModel(), editPart);
	}

	List modelObjects = getModelChildren();

	for (i = 0; i < modelObjects.size(); i++) {
		model = modelObjects.get(i);

		//Do a quick check to see if editPart[i] == model[i]
		editPart = (i < children.size()) ? (EditPart)children.get(i) : null;
		if (editPart != null && editPart.getModel() == model)
			continue;

		//Look to see if the EditPart is already around but in the wrong location
		editPart = (EditPart)modelToEditPart.get(model);

		if (editPart != null)
			reorderChild (editPart, i);
		else {
			//And editpart for this model doesn't exist yet.  Create and insert one.
			editPart = createChild(model);
			addChild(editPart, i);
		}
	}
	List trash = new ArrayList();
	for (; i<children.size(); i++)
		trash.add(children.get(i));
	for (i=0; i<trash.size(); i++) {
		EditPart ep = (EditPart)trash.get(i);
		ep.dispose();
		removeChild(ep);
	}
}

/**
 * <img src="../doc-files/green.gif"/>
 * Refreshes this part's visuals.  After creating the visuals, they should be
 * initialized and the updated in response to model changes by implementing
 * this method.
 */
protected void refreshVisuals(){
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Registers itself in the viewer's various registries.
 * If your EditPart has a 1-to-1 relationship with a visual object and a
 * 1-to-1 relationship with a model object, the default implementation should
 * be sufficent.
 * @see #createConnection(Object)
 * @see #unregister()
 * @see EditPartViewer#getVisualPartMap()
 * @see EditPartViewer#getEditPartRegistry()
 */
protected void register() {
	registerModel();
	registerVisuals();
}

protected void registerModel(){
	getViewer().getEditPartRegistry().put(getModel(), this);	
}

protected void registerVisuals(){}

/**
 * <img src="../doc-files/black.gif"/>
 * Removes a child EditPart.
 * Calls {@link #removeChildVisual(EditPart)} to separate
 * the act of removal from the process of deactivation and
 * notification.
 * Subclasses would generally override removeChildVisual(EditPart)
 * instead of this method.
 * <BR>Deactivates child.
 * <BR>Fires notification.
 * <BR>Inverse of addChild(EditPart, int)
 * @param child  EditPart being removed
 * @see  #addChild(EditPart,int)
 */
protected void removeChild(EditPart child) {
	Assert.isNotNull(child);
	int index = getChildren().indexOf(child);
	if (index < 0)
		return;
	fireRemovingChild(child, index);
	child.deactivate();
	removeChildVisual(child);
	child.setParent(null);
	getChildren().remove(child);
}

protected abstract void removeChildVisual(EditPart child);

/**
 * <img src="../doc-files/dblack.gif"/>
 * Removes a listener.
 */
public void removeEditPartListener(EditPartListener listener){
	eventListeners.removeListener(EditPartListener.class, listener);
}

/**
 * <img src="../doc-files/dblack.gif"/>
 * Uninstalls the edit policy if present.
 */
public void removeEditPolicy(Object key){
	int i = editPolicyKeys.indexOf(key);
	if (i == -1) return;
	EditPolicy policy = (EditPolicy)editPolicies.get(i);
	if (isActive() && policy != null)
		policy.deactivate();
	editPolicies.set(i,null);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Bubbles and EditPart forward into a lower index than it
 * previously occupied.
 * This method is correctly implemented in the provided base classes.
 * This method is called from {@link #refreshChildren()}.
 * @param child  EditPart being reordered
 * @param index  Position into which it is being shifted
 */
protected void reorderChild(EditPart editpart, int index){
	removeChildVisual(editpart);
	List children = getChildren();
	children.remove(editpart);
	children.add(index, editpart);
	addChildVisual(editpart, index);
}

/**
 * Sets the value of the given flag to the appropriate
 * value.
 *
 * @param flag  Flag being set
 * @param value  Value of the flag to be set
 * @see  #getFlag(int)
 */
final protected void setFlag(int flag, boolean value){
	if (value) flags |= flag;
	else flags &= ~flag;
}

public void setFocus(boolean value){
	if (hasFocus() == value)
		return;
	setFlag(FLAG_FOCUS, value);
	fireSelectionChanged();
}

/**
 * <img src="../doc-files/black.gif"/>
 * Set the primary model object that this EditPart represents.
 * Listeners to the model should be added in {#activate()}, not here.
 * activate() and deactivate() may be called multiple times on an EditPart.
 */
public void setModel(Object model){
	if (getModel() == model)
		return;
	this.model = model;
	initialize();
}

/**
 * <img src="../doc-files/dblack.gif"/> Sets the parent EditPart.
 */
public void setParent(EditPart parent) {
	if (this.parent == parent)
		return;
	this.parent = parent;
	if (parent != null)
		register();
	initialize();
}

/**
 * <img src="../doc-files/blue.gif"/>
 * sets the selected state of this EditPart.
 * This method should rarely be overridden.
 * Instead, EditPolicies that are selection-aware will listen for notification
 * of this property changing.
 * @see com.ibm.etools.gef.editpolicies.SelectionHandlesEditPolicy
 */
public void setSelected(int value) {
	if (selected == value)
		return;
	selected = value;
	fireSelectionChanged();
}

/**
 * <img src="../doc-files/blue.gif"/>
 * determines whether {@link #doInitialize()} should be called.
 * The provided abstract base classes correctly override this method to ensure
 * that the visuals exists prior to doInitialize().
 */
protected boolean shouldInitialize(){
	if (getModel() == null)
		return false;
	if (getParent() == null)
		return false;
	if (getFlag(FLAG_INITIALIZED))
		return false;
	return true;
}

/**
 * <img src="../doc-files/black.gif"/>
 * Shows source feedback for the given Request.
 * By default, this responsibility is delegated to this
 * part's EditPolicies.  This method should not be overridden.
 *
 * @param request identifies the type of feedback to erase.
 * @see  #eraseSourceFeedback(Request)
 */
public void showSourceFeedback(Request request) {
	debugFeedback("Request to show \"" + request.getType() + "\" SOURCE feedback");//$NON-NLS-2$//$NON-NLS-1$
	if (!isActive())
		return;
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next()
			.showSourceFeedback(request);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Shows target feedback for the given Request.
 * By default, this responsibility is delegated to this
 * part's EditPolicies.  This method should not be overridden.
 *
 * @param request identifies the type of feedback to erase.
 * @see  #eraseTargetFeedback(Request)
 */
public void showTargetFeedback(Request request) {
	debugFeedback("Request to show \"" + request.getType() + "\" TARGET feedback");//$NON-NLS-2$//$NON-NLS-1$
	if (!isActive())
		return;
	EditPolicyIterator i = getEditPolicyIterator();
	while (i.hasNext())
		i.next()
			.showTargetFeedback(request);
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Describes this EditPart for developmental debugging purposes.
 */
public String toString(){
	String c = getClass().getName();
	c = c.substring(c.lastIndexOf('.')+1);
	return c + "( " + getModel() + " )";//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * <img src="../doc-files/black.gif"/>
 * Returns true if this EditPart understand the given Request.
 * By default, this is determined by the installed EditPolicies.
 * <table><tr><td><img src="../doc-files/important.gif"/><td>
 *    EditParts should not understand any requests directly.
 *    ALL editing behavior is to be added by installing one or more
 *    EditPolicies into the EditPart.</tr></table>
 * @param request identifies the type of feedback to erase.
 * @see  #eraseTargetFeedback(Request)
 */
public boolean understandsRequest(Request req){
	EditPolicyIterator iter = getEditPolicyIterator();
	while (iter.hasNext()){
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
protected void unregister(){
	Map registry = getViewer().getEditPartRegistry();
	if (registry.get(getModel()) == this)
		registry.remove(getModel());
	debug("Unregister");//$NON-NLS-1$
}

}
