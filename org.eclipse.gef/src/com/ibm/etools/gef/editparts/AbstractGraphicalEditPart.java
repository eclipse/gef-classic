package com.ibm.etools.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.commands.*;
import com.ibm.etools.gef.handles.*;

/**
 * Abstract class which uses IFigures for visuals.
 */
abstract public class AbstractGraphicalEditPart
	extends AbstractEditPart
	implements GraphicalEditPart
{

protected IFigure figure;
protected List handles = new ArrayList(8);
protected List sourceConnections;
protected List targetConnections;

protected class DefaultAccessibleAnchorProvider
	implements AccessibleAnchorProvider
{
	private List getDefaultLocations(){
		List list = new ArrayList();
		Rectangle r = getFigure().getBounds();
		Point p = r.getTopRight().translate(-1, r.height/3);
		getFigure().translateToAbsolute(p);
		list.add(p);
		return list;
	}

	public List getSourceAnchorLocations(){
		return getDefaultLocations();
	}
	public List getTargetAnchorLocations(){
		return getDefaultLocations();
	}
}

static class MergedAccessibleHandles
	implements AccessibleHandleProvider
{
	List locations = new ArrayList();
	MergedAccessibleHandles(EditPolicyIterator iter){
		while (iter.hasNext()){
			EditPolicy policy = iter.next();
			if (!(policy instanceof IAdaptable))
				continue;
			IAdaptable adaptable = (IAdaptable) policy;
			AccessibleHandleProvider adapter =
				(AccessibleHandleProvider)adaptable.getAdapter(AccessibleHandleProvider.class);
			if (adapter != null)
				locations.addAll(adapter.getAccessibleHandleLocations());
		}
	}
	public List getAccessibleHandleLocations(){
		return locations;
	}
}

public void activate(){
	super.activate();
	List l = getSourceConnections();
	for (int i = 0; i < l.size(); i++)
		((EditPart)l.get(i)).activate();
}

/**
 * Before performing the addition of the child EditPart to
 * the model structure in super, its Figure is added to the 
 * graphical model.
 *
 * @param childEditPart  EditPart being added to the structure
 * @param index   Psotion the EditPart is being added into.
 */
protected void addChildVisual(EditPart childEditPart, int index){
	IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
	getContentPane().add(child, index);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Adds a connection to this EditPart, the source.
 * The connection will have its source set to this EditPart,
 *
 * The connection will be conditionally activated.
 * primAddSourceConnection(ConnectionEditPart, int) is called
 * to separate the act of adding the connection from the activation
 * and firing of notification.  Subclasses should generally override
 * primAddSourceConnection(ConnectionEditPart, int);
 *
 * @param connection  Connection being added
 * @param index   Index where it is being added
 * @see #primAddSourceConnection(ConnectionEditPart, int)
 * @see #removeSourceConnection
 */
protected void addSourceConnection(ConnectionEditPart connection, int index) {
	primAddSourceConnection(connection, index);
	connection.setSource(this);
	if (isActive())
		connection.activate();
	fireSourceConnectionAdded(connection, index);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Adds a connection to this EditPart, the target.
 * @param connection  Connection being added
 * @param index   Index where it is being added
 * @see  #removeTargetConnection(ConnectionEditPart)
 * @see  #addSourceConnection(ConnectionEditPart,int)
 */
protected void addTargetConnection(ConnectionEditPart connection, int index) {
	primAddTargetConnection(connection, index);
	connection.setTarget(this);
	fireTargetConnectionAdded(connection, index);
}

/**
 * Returns the figure to be used as this part's visuals.
 */
abstract protected IFigure createFigure();

public void deactivate(){
	List l = getSourceConnections();
	for (int i = 0; i < l.size(); i++)
		((EditPart)l.get(i)).deactivate();

	super.deactivate();
}

public void dispose(){
	List conns;
	conns = getSourceConnections();
	for (int i=0; i < conns.size(); i++)
		((ConnectionEditPart)conns.get(i)).setSource(null);
	conns = getTargetConnections();
	for (int i=0; i < conns.size(); i++)
		((ConnectionEditPart)conns.get(i)).setTarget(null);
	super.dispose();
}

/**
 * Notifies listeners that a source connection has been added.
 *
 * @param connection  <code>ConnectionEditPart</code> being added as child.
 * @param index  Position child is being added into.
 */
protected void fireRemovingSourceConnection(ConnectionEditPart connection, int index){
	if (eventListeners == null)
		return;
	Iterator listeners = eventListeners.getListeners(NodeListener.class);
	NodeListener listener = null;
	while(listeners.hasNext()){
		listener = (NodeListener)listeners.next();
		listener.removingSourceConnection(connection, index);
	}
}

/**
 * Notifies listeners that a source connection has been added.
 *
 * @param connection  <code>ConnectionEditPart</code> being added as child.
 * @param index  Position child is being added into.
 */
protected void fireRemovingTargetConnection(ConnectionEditPart connection, int index){
	if (eventListeners == null)
		return;
	Iterator listeners = eventListeners.getListeners(NodeListener.class);
	NodeListener listener = null;
	while(listeners.hasNext()){
		listener = (NodeListener)listeners.next();
		listener.removingTargetConnection(connection, index);
	}
}

/**
 * Notifies listeners that a source connection has been added.
 *
 * @param connection  <code>ConnectionEditPart</code> being added as child.
 * @param index  Position child is being added into.
 */
protected void fireSourceConnectionAdded(ConnectionEditPart connection, int index){
	if (eventListeners == null)
		return;
	Iterator listeners = eventListeners.getListeners(NodeListener.class);
	NodeListener listener = null;
	while(listeners.hasNext()){
		listener = (NodeListener)listeners.next();
		listener.sourceConnectionAdded(connection, index);
	}
}

/**
 * Notifies listeners that a source connection has been added.
 *
 * @param connection  <code>ConnectionEditPart</code> being added as child.
 * @param index  Position child is being added into.
 */
protected void fireTargetConnectionAdded(ConnectionEditPart connection, int index){
	if (eventListeners == null)
		return;
	Iterator listeners = eventListeners.getListeners(NodeListener.class);
	NodeListener listener = null;
	while(listeners.hasNext()){
		listener = (NodeListener)listeners.next();
		listener.targetConnectionAdded(connection, index);
	}
}

public Object getAdapter(Class key){
	if (key == AccessibleHandleProvider.class)
		return new MergedAccessibleHandles(getEditPolicyIterator());

	if (key == AccessibleAnchorProvider.class)
		return new DefaultAccessibleAnchorProvider();

	return super.getAdapter(key);
}

/*
 * defined in interface
 */
public IFigure getContentPane(){
	return getFigure();
}

/**
 * Returns a DragTrack for the given Request.
 * The Request object will indicate the context of the drag.
 *
 * @param Request A Request describing the type of drag.
 */
public DragTracker getDragTracker(Request request){
	return new com.ibm.etools.gef.tools.DragEditPartsTracker(this);
}

/**
 * Returns the graphical object of this.
 *
 * @return  The graphical part as a <code>IFigure</code>
 */
final public IFigure getFigure(){
	if (figure == null)
		setFigure(createFigure());
	return figure;
}

/**
 * Returns the layer with the input name from the 
 * <code>LayerManager</code> from
 *
 * @param layer  Name of the Layer to be returned
 * @return  The layer with the given name.
 */
protected IFigure getLayer(Object layer){
	LayerManager manager = (LayerManager)getViewer().getEditPartRegistry().get(LayerManager.ID);
	return manager.getLayer(layer);
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Returns a <code>List</code> of the connection model objects
 * for which this EditPart's model is the <b>source</b>.
 * {@link #refreshSourceConnections()} requires that this List be
 * non-<code>null</code>.  For each connection model object,
 * {@link #createConnection(Object)} will be called to obtain a corresponding
 * {@link ConnectionEditPart}.
 */
protected List getModelSourceConnections() {return Collections.EMPTY_LIST;}

/**
 * <img src="../doc-files/blue.gif"/>
 * Returns a <code>List</code> of the connection model objects
 * for which this EditPart's model is the <b>target</b>.
 * {@link #refreshTargetConnections()} requires that this List be
 * non-<code>null</code>.  For each connection model object,
 * {@link #createConnection(Object)} will be called to obtain a corresponding
 * {@link ConnectionEditPart}.
 */
protected List getModelTargetConnections() {return Collections.EMPTY_LIST;}

final protected IFigure getParentingFigure()throws Exception{
	return null;
}

/*
 * defined by interface
 */
public List getSourceConnections(){
	if (sourceConnections == null)
		return Collections.EMPTY_LIST;
	return sourceConnections;
}

/*
 * defined by interface
 */
public List getTargetConnections(){
	if (targetConnections == null)
		return Collections.EMPTY_LIST;
	return targetConnections;
}

/**
 * <img src="../doc-files/black.gif"/>
 * Performs the actual addition of a source connection.
 * @param connection  The connection to add.
 * @param index  Position connection is being added.
 * @see  #addSourceConnection(ConnectionEditPart,int)
 */
protected void primAddSourceConnection(ConnectionEditPart connection, int index) {
	if (sourceConnections == null)
		sourceConnections = new ArrayList();
	sourceConnections.add(index, connection);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Performs the actual addition of a target connection.
 * A target connection is a connection whose target is this EditPart.
 *
 * @param connection  The connection being added.
 * @param index  Position connection is being added.
 * @see  #addTargetConnection(ConnectionEditPart,int)
 */
protected void primAddTargetConnection(ConnectionEditPart connection, int index) {
	if (targetConnections == null)
		targetConnections = new ArrayList();
	targetConnections.add(index, connection);
}

/**
 * Performs the actual removal of a source connection.
 * @param connection  Connection to remove.
 * @see  #removeSourceConnection(ConnectionEditPart)
 */
protected void primRemoveSourceConnection(ConnectionEditPart connection) {
	sourceConnections.remove(connection);
}

/**
 * Performs the actual removal of a target connection.
 * @param connection  Connection to remove.
 * @see  #removeTargetConnection(ConnectionEditPart)
 */
protected void primRemoveTargetConnection(ConnectionEditPart editPart) {
	targetConnections.remove(editPart);
}

public void refresh(){
	super.refresh();
	refreshSourceConnections();
	refreshTargetConnections();
}

/**
 * <img src="../doc-files/black.gif"/>
 * Refreshes the set of Source Connections.
 * This method should not be overridden. {@link #createConnection(Object)}
 * and {@link #getModelSourceConnections()} should be overridden together.
 */
protected void refreshSourceConnections(){
	int i;
	ConnectionEditPart editPart;
	Object model;

	Hashtable modelToEditPart = new Hashtable ();
	List editParts = getSourceConnections();

	for (i=0; i < editParts.size(); i++){
		editPart = (ConnectionEditPart)editParts.get(i);
		modelToEditPart.put(editPart.getModel(), editPart);
	}

	List modelObjects = getModelSourceConnections();
	if (modelObjects == null) modelObjects = new ArrayList();

	for (i = 0; i < modelObjects.size(); i++) {
		model = modelObjects.get(i);

//		editPart = i < fSourceConnections.size() ? (ConnectionEditPart) fSourceConnections.get(i) : null;
//		if (editPart != null && editPart.getModel() == model) continue;
		
		editPart = (ConnectionEditPart) modelToEditPart.get(model);
		if (editPart != null)
			reorderSourceConnection(editPart, i);
		else {
			editPart = createOrFindConnection(model);
			addSourceConnection(editPart, i);
		}
	}

	//Remove the remaining EditParts
	List trash = new ArrayList ();
	for (; i<editParts.size(); i++)
		trash.add(editParts.get(i));
	for (i=0; i<trash.size(); i++)
		removeSourceConnection((ConnectionEditPart)trash.get(i));
}

/**
 * <img src="../doc-files/black.gif"/>
 * Refreshes the set of Target Connections.
 * This method should not be overridden. {@link #createConnection(Object)}
 * and {@link #getModelTargetConnections()} should be overridden together.
 */
protected void refreshTargetConnections(){
	int i;
	ConnectionEditPart editPart;
	Object model;

	Hashtable mapModelToEditPart = new Hashtable ();
	List connections = getTargetConnections();

	for (i=0; i < connections.size(); i++){
		editPart = (ConnectionEditPart)connections.get(i);
		mapModelToEditPart.put(editPart.getModel(), editPart);
	}

	List modelObjects = getModelTargetConnections();
	if (modelObjects == null) modelObjects = new ArrayList();

	for (i = 0; i < modelObjects.size(); i++) {
		model = modelObjects.get(i);
		editPart = i < connections.size() ? (ConnectionEditPart) connections.get(i) : null;
		if (editPart != null && editPart.getModel() == model) continue;

		editPart = (ConnectionEditPart)mapModelToEditPart.get(model);
		if (editPart != null)
			reorderTargetConnection(editPart, i);
		else {
			editPart = createOrFindConnection(model);
			addTargetConnection(editPart, i);
		}
	}

	//Remove the remaining Connection EditParts
	List trash = new ArrayList ();
	for (; i<connections.size(); i++)
		trash.add(connections.get(i));
	for (i=0; i<trash.size(); i++)
		removeTargetConnection((ConnectionEditPart)trash.get(i));
}

protected void registerVisuals(){
	getViewer().getVisualPartMap().put(getFigure(), this);
}

/**
 * Before removing the child EditPart from the model structure 
 * in super, its Figure is removed from the graphical model.
 *
 * @param childEditPart  EditPart being removed from the structure
 */
protected void removeChildVisual(EditPart childEditPart){
	IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
	getContentPane().remove(child);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Removes the given connection for which this EditPart is the <B>source</b>.
 * <BR>Fires notification.
 * <BR>Inverse of {@link #addSourceConnection(ConnectionEditPart, int)}
 * @param connection Connection being removed
 */
protected void removeSourceConnection(ConnectionEditPart connection) {
	fireRemovingSourceConnection(connection, getSourceConnections().indexOf(connection));
	connection.deactivate();
	connection.setSource(null);
	primRemoveSourceConnection(connection);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Removes the given connection for which this EditPart is the <B>target</b>.
 * <BR>Fires notification.
 * <BR>Inverse of {@link #addTargetConnection(ConnectionEditPart, int)}
 * @param connection Connection being removed
 */
protected void removeTargetConnection(ConnectionEditPart connection) {
	fireRemovingTargetConnection(connection, getTargetConnections().indexOf(connection));
	connection.setTarget(null);
	primRemoveTargetConnection(connection);
}

/**
 * Reorders the child to be at the specified new position.
 * The child's contraints are saved, it is removed from the
 * structure, and added back at the index given. Its constraints
 * are then set back to it.
 *
 * @param child  EditPart being reordered.
 * @param index  PosPosition being reordered to.
 */
protected void reorderChild(EditPart child, int index) {
	// Save the constraint of the child so that it does not
	// get lost during the remove and re-add.
	IFigure childFigure = ((GraphicalEditPart) child).getFigure();
	LayoutManager layout = getContentPane().getLayoutManager();
	Object constraint = null;
	if (layout != null)
		constraint = layout.getConstraint(childFigure);

	super.reorderChild(child, index);
	getContentPane().setConstraint(childFigure, constraint);
}

/**
 * <img src="../doc-files/black.gif"/>
 * Bubbles the given source ConnectionEditPart into a lower index than it
 * previously occupied.
 *
 * @param connection  Connection being reordered
 * @param index the new Position into which the connection is being placed.
 */
protected void reorderSourceConnection(ConnectionEditPart editPart, int index) {
	primRemoveSourceConnection(editPart);
	primAddSourceConnection(editPart, index);
}

/**
 * <img src="../doc-files/blue.gif"/>
 * Bubbles the given target ConnectionEditPart into a lower index than it
 * previously occupied.
 *
 * @param connection  Connection being reordered
 * @param index the new Position into which the connection is being placed.

 */
protected void reorderTargetConnection(ConnectionEditPart editPart, int index) {
	primRemoveTargetConnection(editPart);
	primAddTargetConnection(editPart, index);
}

/**
 * Sets the Figure representing the graphical content of this.
 *
 * @param figure  Figure being set.
 */
protected void setFigure(IFigure figure){
	this.figure = figure;
}

public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint){
	getContentPane().setConstraint(childFigure, constraint);
}

/**
 * Updates this EditPart. Update is done only in the
 * presence of a set Figure. 
 *
 */
protected boolean shouldInitialize(){
	return super.shouldInitialize() &&
		getFigure() != null;
}

protected void unregister(){
	super.unregister();
	getViewer().getVisualPartMap().remove(getFigure());
}

}
