/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.properties;

import java.util.EventObject;

import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.ForwardUndoCompoundCommand;
/**
 * <code>PropertySheetEntry</code> is an implementation of
 * <code>IPropertySheetEntry</code> which uses <code>IPropertySource</code>
 * and <code>IPropertyDescriptor</code> to interact with domain model objects.
 * <p>
 * Every property sheet entry has a single descriptor (except the root entry
 * which has none). This descriptor determines what property
 * of its objects it will display/edit.
 * </p>
 * <p>
 * Entries do not listen for changes in their objects. Since there is no
 * restriction on properties being independent, a change in one property
 * may affect other properties. The value of a parent's property may also
 * change. As a result we are forced to refresh the entire entry tree
 * when a property changes value.
 * </p>
 */
public class UndoablePropertySheetEntry extends PropertySheetEntry {

private CommandStackListener commandStackListener;

public UndoablePropertySheetEntry() { }
public UndoablePropertySheetEntry(CommandStack stack) {
	setCommandStack(stack);
}

protected CommandStack stack;

/**
 * Return an array of property sheet entries
 */
protected PropertySheetEntry[] createChildEntries(int size) {
	return new UndoablePropertySheetEntry[size];
}

protected PropertySheetEntry createChildEntry() {
	return new UndoablePropertySheetEntry();
}

public void dispose() {
	if (stack != null)
		stack.removeCommandStackListener(commandStackListener);
	super.dispose();
}

/**
 * returns the Command stack from the root entry
 * @return
 */
protected CommandStack getCommandStack() {
	//only the root has, and is listening too, the command stack
	if (getParent() != null)
		return ((UndoablePropertySheetEntry)getParent()).getCommandStack();
	return stack;
}

/* (non-Javadoc)
 * Method declared on IUndoablePropertySheetEntry.
 */
public void resetPropertyValue() {
	CompoundCommand cc = new CompoundCommand();
	RestoreDefaultPropertyValueCommand restoreCmd;

	if (getParent() == null)
		// root does not have a default value
		return;

	//	Use our parent's values to reset our values.
	boolean change = false;
	Object[] objects = getParent().getValues();
	for (int i = 0; i < objects.length; i++) {
		IPropertySource source = getPropertySource(objects[i]);
		if (source.isPropertySet(getDescriptor().getId())) {
			//source.resetPropertyValue(getDescriptor()getId());
			restoreCmd = new RestoreDefaultPropertyValueCommand();
			restoreCmd.setTarget(source);
			restoreCmd.setPropertyId(getDescriptor().getId());
			restoreCmd.setPropertyValue(source.getPropertyValue(getDescriptor().getId()));
			cc.add(restoreCmd);			
			change = true;
		}
	}
	if (change) {
		getCommandStack().execute(cc);
		refreshFromRoot();
	}
}

public void setCommandStack(CommandStack stack) {
	this.stack = stack;
	commandStackListener = new CommandStackListener() {
		public void commandStackChanged(EventObject e) {
			refreshFromRoot();
		}
	};
	stack.addCommandStackListener(commandStackListener);
}

/**
 * Set the value for this entry.
 * <p>
 * We set the given value as the value for all our value objects.
 * We then call our parent to update the property we represent
 * with the given value.
 * We then trigger a model refresh.
 * <p>
 *
 * @param newValue the new value
 */
protected void setValue(Object newValue) {
	// Set the value
	for (int i = 0; i < getValues().length; i++) {
		getValues()[i] = newValue;
	}

	// Inform our parent
	((UndoablePropertySheetEntry)getParent()).valueChanged(this, new ForwardUndoCompoundCommand());

	// Refresh the model
	refreshFromRoot();
}

/**
 * The value of the given child entry has changed.
 * Therefore we must set this change into our value objects.
 * <p>
 * We must inform our parent so that it can update its value objects
 * </p>
 * <p>
 * Subclasses may override to set the property value in some
 * custom way.
 * </p>
 *
 * @param child the child entry that changed its value
 */
protected void valueChanged(UndoablePropertySheetEntry child, CompoundCommand command) {
	CompoundCommand cc = new CompoundCommand();
	command.add(cc);

	SetPropertyValueCommand setCommand;
	for (int i = 0; i < getValues().length; i++) {
		setCommand = new SetPropertyValueCommand(child.getDisplayName());
		setCommand.setTarget(getPropertySource(getValues()[i]));
		setCommand.setPropertyId(child.getDescriptor().getId());
		setCommand.setPropertyValue(child.getValues()[i]);
		cc.add(setCommand);
	}

	// inform our parent
	if (getParent() != null)
		((UndoablePropertySheetEntry)getParent()).valueChanged(this, command);
	else {
		//I am the root entry
		stack.execute(command);
	}
}
}