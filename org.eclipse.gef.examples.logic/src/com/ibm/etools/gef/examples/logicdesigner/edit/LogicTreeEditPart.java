package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.beans.*;

import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.graphics.Image;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.examples.logicdesigner.model.*;

/**
 * EditPart for Logic components in the Tree.
 */
public class LogicTreeEditPart
	extends com.ibm.etools.gef.editparts.AbstractTreeEditPart
	implements PropertyChangeListener
{

/**
 * Constructor initializes this with the given model.
 *
 * @param model  Model for this.
 */
public LogicTreeEditPart(Object model) {
	super (model);
}

public void activate(){
	super.activate();
	getLogicSubpart().addPropertyChangeListener(this);
}

/**
 * Creates and installs pertinent EditPolicies
 * for this.
 */
protected void createEditPolicies() {
	EditPolicy component;
	if (getModel() instanceof LED)
		component = new LEDEditPolicy();
	else
		component = new LogicElementEditPolicy();
	installEditPolicy(EditPolicy.COMPONENT_ROLE, component);
	installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new LogicTreeEditPolicy());
	installEditPolicy(EditPolicy.NODE_ROLE, new LogicTreeNodeEditPolicy());
}

public void deactivate(){
	getLogicSubpart().removePropertyChangeListener(this);
	super.deactivate();
}

/**
 * Returns the model of this as a LogicSubPart.
 *
 * @return Model of this.
 */
protected LogicSubpart getLogicSubpart() {
	return (LogicSubpart)getModel();
}

/**
 * Returns <code>null</code> as a Tree EditPart holds
 * no children under it.
 *
 * @return <code>null</code>
 */
protected List getModelChildren() {
	return Collections.EMPTY_LIST;
}

public void propertyChange(PropertyChangeEvent change){
	if (change.getPropertyName().equals(LogicDiagram.CHILDREN))
		refreshChildren();
	else
		refreshVisuals();
}

/**
 * Refreshes the Widget of this based on the property
 * given to update. All major properties are updated
 * irrespective of the property input.
 *
 * @param property  Property to be refreshed.
 */
protected void refreshVisuals(){
	if (getWidget() instanceof Tree)
		return;
	Image image = getLogicSubpart().getIcon();
	TreeItem item = (TreeItem)getWidget();
	if (image != null)
		image.setBackground(item.getParent().getBackground());
	setWidgetImage(image);
	setWidgetText(getLogicSubpart().toString());
}

}