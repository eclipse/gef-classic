package com.ibm.etools.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.resource.ImageDescriptor;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.common.command.CommandStack;

import com.ibm.etools.gef.EditPart;
import com.ibm.etools.gef.Request;
import com.ibm.etools.gef.commands.CompoundCommand;

import com.ibm.etools.gef.examples.logicdesigner.model.LED;

public class IncrementDecrementAction
	extends com.ibm.etools.gef.ui.actions.SelectionAction
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
		setText(LogicResources.getString(
			"IncrementDecrementAction.Increment.ActionLabelText")); //$NON-NLS-1$
		setId(INCREMENT);
		setToolTipText(LogicResources.getString(
			"IncrementDecrementAction.Increment.ActionToolTipText")); //$NON-NLS-1$
		setImageDescriptor(
		ImageDescriptor.createFromFile(LogicPlugin.class,"icons/plus.gif")); //$NON-NLS-1$
	} else {
		request = new Request(DECREMENT_REQUEST);
		setText(LogicResources.getString(
			"IncrementDecrementAction.Decrement.ActionLabelText"));  //$NON-NLS-1$
		setId(DECREMENT);
		setToolTipText(LogicResources.getString(
			"IncrementDecrementAction.Decrement.ActionToolTipText"));  //$NON-NLS-1$
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

