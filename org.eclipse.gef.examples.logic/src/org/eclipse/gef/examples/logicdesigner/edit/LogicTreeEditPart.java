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
package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.*;
import java.beans.*;

import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.*;
import org.eclipse.gef.examples.logicdesigner.model.*;

/**
 * EditPart for Logic components in the Tree.
 */
public class LogicTreeEditPart
	extends org.eclipse.gef.editparts.AbstractTreeEditPart
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
	if (change.getPropertyName().equals(LogicDiagram.CHILDREN)) {
		if (change.getOldValue() instanceof Integer)
			// new child
			addChild(createChild(change.getNewValue()), ((Integer)change.getOldValue()).intValue());	
		else
			// remove child
			removeChild((EditPart)getViewer().getEditPartRegistry().get(change.getOldValue()));
	} else
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