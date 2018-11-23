/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.StructuredActivity;

/**
 * AddCommand
 * 
 * @author Daniel Lee
 */
public class AddCommand extends Command {

	private Activity child;
	private StructuredActivity parent;

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		parent.addChild(child);
	}

	/**
	 * Returns the StructuredActivity that is the parent
	 * 
	 * @return the parent
	 */
	public StructuredActivity getParent() {
		return parent;
	}

	/**
	 * Sets the child to the passed Activity
	 * 
	 * @param subpart
	 *            the child
	 */
	public void setChild(Activity newChild) {
		child = newChild;
	}

	/**
	 * Sets the parent to the passed StructuredActiivty
	 * 
	 * @param newParent
	 *            the parent
	 */
	public void setParent(StructuredActivity newParent) {
		parent = newParent;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		parent.removeChild(child);
	}

}
