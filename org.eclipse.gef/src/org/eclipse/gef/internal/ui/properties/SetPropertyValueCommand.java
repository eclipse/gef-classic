package org.eclipse.gef.internal.ui.properties;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.*;
import org.eclipse.ui.views.properties.IPropertySource;

class SetPropertyValueCommand
	extends AbstractCommand {

protected Object propertyValue;
protected Object propertyName;
protected Object undoValue;
protected boolean resetOnUndo;
protected IPropertySource target;

public SetPropertyValueCommand(){
	super("Apply Property Value");//$NON-NLS-1$
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
		getTarget().resetPropertyValue(propertyName);
	else
		getTarget().setPropertyValue(propertyName,undoValue);
}

}
