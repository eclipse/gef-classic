package org.eclipse.gef.internal.ui.properties;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

public class RestoreDefaultPropertyValueCommand extends Command {

protected Object propertyValue;
protected Object propertyName;
protected Object undoValue;
protected boolean resetOnUndo;
protected IPropertySource target;

public RestoreDefaultPropertyValueCommand(){
	super("Restore Default Property Value");//$NON-NLS-1$
}
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


