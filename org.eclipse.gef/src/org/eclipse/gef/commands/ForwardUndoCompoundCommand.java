package org.eclipse.gef.commands;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ListIterator;

/**
 *  This command will undo its commands in the same order applied.
 *
 *  However any Chained commands will undo in reverse order since
 *  that can't be controlled.
 */
public class ForwardUndoCompoundCommand
	extends CompoundCommand
{
	
public ForwardUndoCompoundCommand() {}
public ForwardUndoCompoundCommand(String label) {
	super(label);
}

public String getDebugLabel(){
	return "PropertySheet:" + getLabel();//$NON-NLS-1$
}

/**
 * Undo the command.  For a Preorder compound command this
 * means undoing all of the commands that it contains.
 * Do it in the same order as applied.
 */
public void undo(){
	ListIterator itr = commandList.listIterator();
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


