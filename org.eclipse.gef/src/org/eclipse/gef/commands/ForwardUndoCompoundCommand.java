package org.eclipse.gef.commands;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ListIterator;

/**
 *  A CompoundCommand that performs undo on its contained Commands in the same order in
 *  which they were executed.
 */
public class ForwardUndoCompoundCommand
	extends CompoundCommand
{

/** * Constructs a ForwardUndoCompoundCommand with no label
 */
public ForwardUndoCompoundCommand() { }

/**
 * Constructs a ForwardUndoCompoundCommand with the specified label
 * @param label the label
 */
public ForwardUndoCompoundCommand(String label) {
	super(label);
}

/** * @see org.eclipse.gef.commands.AbstractCommand#getDebugLabel() */
public String getDebugLabel() {
	return "ForwardUndoCommand: " + super.getDebugLabel();//$NON-NLS-1$
}

/**
 * Undo the command.  For a Preorder compound command this
 * means undoing all of the commands that it contains.
 * Do it in the same order as applied.
 */
public void undo() {
	ListIterator itr = getCommands().listIterator();
	try {
		while (itr.hasNext()) {
			((Command) itr.next()).undo();
		}
	} catch (RuntimeException e) {
		itr.previous();	// Skip over the one that failed. It cleaned itself up.
		while (itr.hasPrevious()) {
			((Command) itr.previous()).redo();
		}
		throw e;
	}
}

}


