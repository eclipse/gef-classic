package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class XORGate
	extends Gate 
{

static final long serialVersionUID = 1;
private static Image XOR_ICON = new Image(null,	XORGate.class.getResourceAsStream("icons/xor16.gif"));  //$NON-NLS-1$

public Image getIconImage() {
	return XOR_ICON;
}

public boolean getResult() {
	return getInput(TERMINAL_A) & !getInput(TERMINAL_B) ||
		!getInput(TERMINAL_A) & getInput(TERMINAL_B);
}

public String toString() {
	return LogicMessages.XORGate_LabelText + " #" + getID();//$NON-NLS-1$
}

}
