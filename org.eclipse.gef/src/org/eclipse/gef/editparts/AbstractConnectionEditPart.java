package org.eclipse.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.*;
import org.eclipse.gef.tools.SelectEditPartTracker;

/**
 * Provides support for connections based 
 * EditParts.
 *
 */
abstract public class AbstractConnectionEditPart
	extends AbstractGraphicalEditPart
	implements ConnectionEditPart, LayerConstants
{

private static final ConnectionAnchor DEFAULT_SOURCE_ANCHOR = new XYAnchor(new Point(10,10));
private static final ConnectionAnchor DEFAULT_TARGET_ANCHOR = new XYAnchor(new Point(100,100));

class DefaultAccessibleAnchorProvider
	implements AccessibleAnchorProvider
{
	public List getSourceAnchorLocations(){
		List list = new ArrayList();
		if (getFigure() instanceof Connection){
			Point p = ((Connection)getFigure())
					.getPoints()
					.getMidpoint();
			getFigure().translateToAbsolute(p);
			list.add(p);
		}
		return list;
	}
	public List getTargetAnchorLocations(){
		return getSourceAnchorLocations();
	}
}

private EditPart
		sourceEditPart,
		targetEditPart;

/**
 * Activates the Figure representing this, by setting up
 * the start and end connections, and adding the figure
 * to the Connection Layer.
 * 
 * @see #deactivate()
 */
protected void activateFigure(){
	refreshTargetAnchor();
	refreshSourceAnchor();
	getLayer(CONNECTION_LAYER).add(getFigure());
}

/*
 * Override this method to add EditPolicies. The connection 
 * needs additional Edit policies.
 */
protected void createEditPolicies() { }

/**
 * Returns a newly created Figure to represent these type of
 * EditParts.
 *
 * @return  The created Figure.
 */
protected IFigure createFigure(){
	return new PolylineConnection();
}

/**
 * Deactivates the Figure representing this, by removing
 * it from the connection layer, and resetting the 
 * source and target connections to <code>null</code>.
 */
protected void deactivateFigure() {
	getLayer(CONNECTION_LAYER).remove(getFigure());
	getConnectionFigure().setSourceAnchor(null);
	getConnectionFigure().setTargetAnchor(null);
}

public void dispose(){
	deactivateFigure();
	super.dispose();
}

protected void doInitialize(){
	activateFigure();
	super.doInitialize();
}

public Object getAdapter(Class adapter){
	if (adapter == AccessibleAnchorProvider.class)
		return new DefaultAccessibleAnchorProvider();
	return super.getAdapter(adapter);
}

/**
 * Returns the Figure representing the connection.
 *
 * @return  Figure as an IConnection.
 */
public Connection getConnectionFigure(){
	return (Connection)getFigure();
}

public DragTracker getDragTracker(Request req){
	return new SelectEditPartTracker(this);
}

/**
 * Returns the source EditPart
 *
 * @return  EditPart representing the source of this.
 */
public EditPart getSource(){return sourceEditPart;}

/**
 * Returns the target EditPart
 *
 * @return  EditPart representing the target of this.
 */
public EditPart getTarget(){return targetEditPart;}


/**
 * If the source is an instance of GraphicalNodeEditPart, it
 * returns the anchor associated with it, else it returns 
 * <code>null</code>
 *
 * @return  Connection anchor of the source.
 */
protected ConnectionAnchor getSourceConnectionAnchor() {
	if (getSource() != null && getSource() instanceof NodeEditPart) {
		NodeEditPart editPart = (NodeEditPart) getSource();
		return editPart.getSourceConnectionAnchor(this);
	}
	return DEFAULT_TARGET_ANCHOR;
}

/**
 * If the target is an instance of NodeEditPart, it
 * returns the anchor associated with it, else it returns 
 * <code>null</code>
 *
 * @return  Connection anchor of the target.
 */
protected ConnectionAnchor getTargetConnectionAnchor() {
	if (getTarget() != null && getTarget() instanceof NodeEditPart) {
		NodeEditPart editPart = (NodeEditPart) getTarget();
		return editPart.getTargetConnectionAnchor(this);
	}
	return DEFAULT_TARGET_ANCHOR;
}

public void refresh(){
	refreshSourceAnchor();
	refreshTargetAnchor();
	super.refresh();
}

protected void refreshSourceAnchor(){
	getConnectionFigure().setSourceAnchor(getSourceConnectionAnchor());
}

protected void refreshTargetAnchor(){
	getConnectionFigure().setTargetAnchor(getTargetConnectionAnchor());
}

public void setParent(EditPart parent){
	if (parent == null && getParent() != null)
		dispose();
	super.setParent(parent);
}

/**
 * Sets the source EditPart of this connection.
 *
 * @param editPart  EditPart which is the source.
 */
public void setSource(EditPart editPart){
	sourceEditPart = editPart;
	if (sourceEditPart != null)
		setParent(sourceEditPart.getRoot());
	else if (getTarget() == null)
		setParent(null);
	if (shouldInitialize())
		initialize();
	else
		refresh();
}

/**
 * Sets the target EditPart of this connection.
 *
 * @param editPart  EditPart which is the target.
 */
public void setTarget(EditPart editPart){
	targetEditPart = editPart;
	if (editPart != null)
		setParent(editPart.getRoot());
	else if (getSource() == null)
		setParent(null);
	if (shouldInitialize())
		initialize();
	else
		refresh();
}

protected boolean shouldInitialize(){
	return super.shouldInitialize()
		&& getSource() != null
		&& getTarget() != null;
}

}
