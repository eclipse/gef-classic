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
package org.eclipse.gef.examples.logicdesigner.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.LogicPlugin;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.jface.resource.ImageDescriptor;

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
		setText(LogicMessages.IncrementDecrementAction_Increment_ActionLabelText);
		setId(INCREMENT);
		setToolTipText(LogicMessages.IncrementDecrementAction_Increment_ActionToolTipText);
		setImageDescriptor(
		ImageDescriptor.createFromFile(LogicPlugin.class,"icons/plus.gif")); //$NON-NLS-1$
	} else {
		request = new Request(DECREMENT_REQUEST);
		setText(LogicMessages.IncrementDecrementAction_Decrement_ActionLabelText);
		setId(DECREMENT);
		setToolTipText(LogicMessages.IncrementDecrementAction_Decrement_ActionToolTipText);
		setImageDescriptor(
			ImageDescriptor.createFromFile(LogicPlugin.class,"icons/minus.gif")); //$NON-NLS-1$
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

