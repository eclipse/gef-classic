/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.TextCommand;
import org.eclipse.gef.examples.text.edit.TextEditPart;
import org.eclipse.gef.examples.text.model.ModelElement;

/**
 * @since 3.1
 */
public abstract class ExampleTextCommand extends Command implements TextCommand {

	protected ExampleTextCommand(String label) {
		super(label);
	}

	@SuppressWarnings("static-method") // allow subclasses to override
	public boolean canExecutePending() {
		return false;
	}

	public void executePending() {
	}

	protected static TextEditPart lookupModel(GraphicalTextViewer viewer, ModelElement model) {
		return (TextEditPart) viewer.getEditPartRegistry().get(model);
	}

}
