/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class Circuit
	extends LogicDiagram
{

static final long serialVersionUID = 1;

private static int count;
public static String TERMINALS_OUT [] = 
	new String [] {	"1","2","3","4", //$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
				"5","6","7","8"};//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$

public static String TERMINALS_IN [] = 
	new String [] {	"A","B","C","D", //$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
				"E","F","G","H"};//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$

public String getNewID() {
	return Integer.toString(count++);
}

public String toString() {
	return LogicMessages.Circuit_LabelText + " #" + getID();  //$NON-NLS-1$
}

public void update() {
	for (int i=0; i<8;i++)
		setOutput(TERMINALS_OUT[i],getInput(TERMINALS_IN[i]));
}

}
