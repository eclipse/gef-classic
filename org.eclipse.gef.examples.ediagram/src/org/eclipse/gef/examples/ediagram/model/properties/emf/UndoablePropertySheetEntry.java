/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties.emf;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.internal.ui.properties.PropertySheetEntry;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class UndoablePropertySheetEntry
	extends org.eclipse.gef.internal.ui.properties.UndoablePropertySheetEntry
{

public UndoablePropertySheetEntry() {
	super();
}

public UndoablePropertySheetEntry(CommandStack stack) {
	super(stack);
}

protected PropertySheetEntry createChildEntry() {
	return new UndoablePropertySheetEntry();
}

protected void valueChanged(
		org.eclipse.gef.internal.ui.properties.UndoablePropertySheetEntry entry,
		CompoundCommand command) {
	UndoablePropertySheetEntry child = (UndoablePropertySheetEntry)entry;
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
