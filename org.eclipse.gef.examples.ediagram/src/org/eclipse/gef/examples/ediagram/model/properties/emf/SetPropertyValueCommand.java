/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties.emf;

import java.text.MessageFormat;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;

import org.eclipse.emf.edit.provider.IItemPropertySource;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;

/*
 * Copied from org.eclipse.gef.internal.ui.properties.SetPropertyValueCommand to provide
 * EMF compatibility.
 */
class SetPropertyValueCommand
	extends Command {

protected Object propertyValue;
protected Object propertyName;
protected Object undoValue;
protected boolean resetOnUndo;
protected IPropertySource target;

public SetPropertyValueCommand() {
	super(""); //$NON-NLS-1$
}

public SetPropertyValueCommand(String propLabel) {
	super(MessageFormat.format(GEFMessages.SetPropertyValueCommand_Label,
								new Object[]{propLabel}).trim());
}

public boolean canExecute() {
	return true;
}

public void execute() {
	/*
	 * Fix for Bug# 54250
	 * IPropertySource.isPropertySet(String) returns false both when there is no default 
	 * value, and when there is a default value and the property is set to that value.
	 * To correctly determine if a reset should be done during undo, we compare the
	 * return value of isPropertySet(String) before and after setPropertyValue(...) is
	 * invoked.  If they are different (it must have been false before and true after --
	 * it cannot be the other way around), then that means we need to reset.
	 */
	boolean wasPropertySet = getTarget().isPropertySet(propertyName);
	undoValue = getTarget().getPropertyValue(propertyName);
	if (undoValue instanceof IPropertySource)
		undoValue = ((IPropertySource)undoValue).getEditableValue();
	else if (undoValue instanceof IItemPropertySource)
		undoValue = ((IItemPropertySource)undoValue).getEditableValue(propertyName);
	if (propertyValue instanceof IPropertySource)
		propertyValue = ((IPropertySource)propertyValue).getEditableValue();
	getTarget().setPropertyValue(propertyName, propertyValue);
	if (getTarget() instanceof IPropertySource2)
		resetOnUndo = !wasPropertySet
				&& ((IPropertySource2)getTarget()).isPropertyResettable(propertyName);
	else
		resetOnUndo = !wasPropertySet && getTarget().isPropertySet(propertyName);
	if (resetOnUndo)
		undoValue = null;
}

public IPropertySource getTarget() {
	return target;
}

public void setTarget(IPropertySource aTarget) {
	target = aTarget;
}

public void redo() {
	execute();
}

public void setPropertyId(Object pName) {
	propertyName = pName;
}

public void setPropertyValue(Object val) {
	propertyValue = val;
}

public void undo() {
	if (resetOnUndo)
		getTarget().resetPropertyValue(propertyName);
	else
		getTarget().setPropertyValue(propertyName, undoValue);
}

}
