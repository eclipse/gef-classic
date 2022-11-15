/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class OrGate extends Gate {

	private static final Image OR_ICON = createImage(OrGate.class, "icons/or16.gif"); //$NON-NLS-1$
	static final long serialVersionUID = 1;

	@Override
	public Image getIconImage() {
		return OR_ICON;
	}

	@Override
	public boolean getResult() {
		return getInput(TERMINAL_A) | getInput(TERMINAL_B);
	}

	@Override
	public String toString() {
		return LogicMessages.OrGate_LabelText + " #" + getID(); //$NON-NLS-1$
	}

}
