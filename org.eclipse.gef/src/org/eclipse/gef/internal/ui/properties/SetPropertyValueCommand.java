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
package org.eclipse.gef.internal.ui.properties;

import java.text.MessageFormat;

import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;

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
	resetOnUndo = !getTarget().isPropertySet(propertyName);
	if (!resetOnUndo) {
		undoValue = getTarget().getPropertyValue(propertyName);
		
//		org.eclipse.gef.GEF.hack();
		// Temporary until the PropertySheetEntry is fixed to call getEditableValue()
//		if (undoValue instanceof IPropertySource)
//			undoValue = ((IPropertySource) undoValue).getEditableValue();
	} else
		undoValue = null;
	getTarget().setPropertyValue(propertyName, propertyValue);
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
