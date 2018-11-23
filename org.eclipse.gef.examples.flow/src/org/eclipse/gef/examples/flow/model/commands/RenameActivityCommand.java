/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

/**
 * Command to rename Activities.
 * 
 * @author Daniel Lee
 */
public class RenameActivityCommand extends Command {

	private Activity source;
	private String name, oldName;

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		source.setName(name);
	}

	/**
	 * Sets the new Activity name
	 * 
	 * @param string
	 *            the new name
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * Sets the old Activity name
	 * 
	 * @param string
	 *            the old name
	 */
	public void setOldName(String string) {
		oldName = string;
	}

	/**
	 * Sets the source Activity
	 * 
	 * @param activity
	 *            the source Activity
	 */
	public void setSource(Activity activity) {
		source = activity;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		source.setName(oldName);
	}

}
