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

import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gef.commands.Command;

public class RestoreDefaultPropertyValueCommand extends Command {

protected Object propertyValue;
protected Object propertyName;
protected Object undoValue;
protected boolean resetOnUndo;
protected IPropertySource target;

public RestoreDefaultPropertyValueCommand() { }

public boolean canExecute() {
	return true;
}

public void execute() {
	resetOnUndo = getTarget().isPropertySet(propertyName);
	if (resetOnUndo) {
		undoValue = getTarget().getPropertyValue(propertyName);
		getTarget().resetPropertyValue(propertyName);	
	} else
		undoValue = null;
		
}

public IPropertySource getTarget() { return target;}

public void setTarget(IPropertySource aTarget) {target = aTarget;}

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
		getTarget().setPropertyValue(propertyName, undoValue);
	else
		getTarget().resetPropertyValue(propertyName);
}

}


