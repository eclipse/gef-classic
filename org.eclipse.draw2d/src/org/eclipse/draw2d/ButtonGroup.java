package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import java.util.*;
import java.beans.*;

/**
 * <p>ButtonGroup - Holds a group of {@link Clickable Clickable's} models
 * and provides unique selection in them. There is 
 * capability to add a default selection. Models who
 * want to belong to the group should just add themselves
 * to this group. By doing so they listen to this group
 * for changes. </p>
 * <p>Setting of the default selection results in its 
 *  being selected any time setSelected(ButtonModel,false) is called. If
 *  no default selection is set, the last entry selected 
 *  is not allowed to deselect.</p>
 */
public class ButtonGroup{
		
private ButtonModel selectedModel;
private ButtonModel defaultModel;
private List members = new ArrayList();
private List listeners = new ArrayList();

/**
 * Constructs a ButtonGroup with no default selection.
 * 
 * @since 2.0
 */
public ButtonGroup(){
}

/** 
 * Adds the passed ButtonModel to the ButtonGroup.
 *
 * @param model  ButtonModel to be added to this group.
 * @see  #remove(ButtonModel)
 * @since 2.0
 */
public void add(ButtonModel model){
	if (!members.contains(model)){
		model.setGroup(this);
		members.add(model);
	}
}

/**
 * Adds the passed listener. ButtonGroups use PropertyChangeListeners
 * to react to selection changes in the ButtonGroup.
 * 
 * @param listener  Listener to be added to this group.
 * @see  #removePropertyChangeListener(PropertyChangeListener)
 * @since 2.0
 */
public void addPropertyChangeListener(PropertyChangeListener listener){
	listeners.add(listener);
}

/**
 * Fires a PropertyChangeEvent to all PropertyChangeListeners 
 * added to this ButtonGroup.
 *
 * @param oldValue  Old selection value.
 * @param newValue  New selection value.
 * @since 2.0
 */
protected void firePropertyChange(Object oldValue, Object newValue){
	PropertyChangeEvent event = new PropertyChangeEvent(
		this, ButtonModel.SELECTED_PROPERTY,
		oldValue, newValue);
	for(int i=0;i<listeners.size();i++)
		((PropertyChangeListener)listeners.get(i)).propertyChange(event);
}

/**
 * Returns the ButtonModel which is selected by default for 
 * this ButtonGroup.
 * 
 * @since 2.0
 */
public ButtonModel getDefault(){
	return defaultModel;
}

/**
 * Returns a List which contains all of the 
 * {@link ButtonModel ButtonModels} added to this ButtonGroup.
 * 
 * @since 2.0
 */
public List getElements(){
	return members;
}

/**
 * Returns the ButtonModel for the currently selected button.
 * 
 * @since 2.0
 */
public ButtonModel getSelected(){
	return selectedModel;
}

/**
 * Determines if the given ButtonModel is selected or not.
 *
 * @param model  Model being tested for selected status.
 * @return  Selection state of the given model.
 *  @since 2.0
 */
public boolean isSelected(ButtonModel model){
	return(model == getSelected());
}

/**
 * Removes the given ButtonModel from this ButtonGroup. 
 * 
 * @param model  ButtonModel being removed.
 * @since 2.0
 */
public void remove(ButtonModel model){
	if (getSelected() == model)
		setSelected(getDefault(), true);
	if (defaultModel == model)
		defaultModel = null;
	members.remove(model);
}

/**
 * Removes the passed PropertyChangeListener from this ButtonGroup.
 *
 * @param listener  PropertyChangeListener to be removed.
 * @since 2.0
 */
public void removePropertyChangeListener(PropertyChangeListener listener){
	listeners.remove(listener);
}

/**
 * Sets the passed ButtonModel to be the currently selected
 * ButtonModel of this ButtonGroup. Fires a property change.
 * 
 * @param model ButtonModel to be selected.
 * @since 2.0
 */
protected void selectNewModel(ButtonModel model){
	ButtonModel oldModel = selectedModel;
	selectedModel = model;
	if (oldModel != null)
		oldModel.setSelected(false);
	firePropertyChange(oldModel, model);
}

/**
 * Sets the default selection of this ButtonGroup. Does nothing if it is
 * not present in the group. Sets selection to the passed ButtonModel.
 *
 * @param model  ButtonModel which is to be the default selection.
 * @since 2.0
 */
public void setDefault(ButtonModel model){
	defaultModel = model;
	if (getSelected() == null && defaultModel!=null)
		defaultModel.setSelected(true);
}

/**
 * Sets the button with the given ButtonModel to be selected.
 * 
 * @since 2.0
 */
public void setSelected(ButtonModel model){
	if (model == null)
		selectNewModel(null);
	else
		model.setSelected(true);
}

/**
 * Sets model to the passed state.  
 *
 * @param model  Model to be affected.
 * @param value  A value of <code>true</code>: Causes the passed
 *                ButtonModel to own selection
 *                A value of <code>false</code>: If the passed model owned
 *                selection, it will lose selection, and selection
 *                will be give to the default ButonModel.
 *                If the passed model was not selected, selection
 *                will remain as it was.
 *                If no default ButtonModel was set, selection
 *                will remain as it was, as one ButtonModel 
 *                must own selection at all times.
 * @since 2.0
 */
public void setSelected(ButtonModel model, boolean value){
	if (value){
		if (model == getSelected())
			return;
		selectNewModel(model);
	} else {
		if (model != getSelected())
			return;
		if (getDefault() == null)
			return;
		getDefault().setSelected(true);
	}
}

}