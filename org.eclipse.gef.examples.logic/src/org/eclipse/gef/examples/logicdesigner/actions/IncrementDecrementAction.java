package org.eclipse.gef.examples.logicdesigner.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.ui.IEditorPart;

public class IncrementDecrementAction
	extends org.eclipse.gef.ui.actions.SelectionAction
{

private static final String
	INCREMENT_REQUEST = "Increment",  //$NON-NLS-1$
	DECREMENT_REQUEST = "Decrement";  //$NON-NLS-1$

public static final String
	INCREMENT = "Increment",   //$NON-NLS-1$
	DECREMENT = "Decrement";   //$NON-NLS-1$

Request request;

public IncrementDecrementAction(IEditorPart editor, boolean increment) {
	super(editor);
	if (increment) {
		request = new Request(INCREMENT_REQUEST);
		setId(INCREMENT);
	} else {
		request = new Request(DECREMENT_REQUEST);
		setId(DECREMENT);
	}
	setHoverImageDescriptor(getImageDescriptor());
}

protected boolean calculateEnabled() {
	return canPerformAction();
}

private boolean canPerformAction() {
	if (getSelectedObjects().isEmpty())
		return false;
	List parts = getSelectedObjects();
	for (int i=0; i<parts.size(); i++){
		Object o = parts.get(i);
		if (!(o instanceof EditPart))
			return false;
		EditPart part = (EditPart)o;
		if (!(part.getModel() instanceof LED))
			return false;
	}
	return true;
}

private Command getCommand() {
	List editparts = getSelectedObjects();
	CompoundCommand cc = new CompoundCommand();
	cc.setDebugLabel("Increment/Decrement LEDs");//$NON-NLS-1$
	for (int i=0; i < editparts.size(); i++) {
		EditPart part = (EditPart)editparts.get(i);
		cc.add(part.getCommand(request));
	}
	return cc;
}

public void run() {
	execute(getCommand());
}

}

