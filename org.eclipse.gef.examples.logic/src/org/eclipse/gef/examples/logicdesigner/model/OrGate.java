package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class OrGate
	extends Gate 
{

static private Image OR_ICON = new Image(null, OrGate.class.getResourceAsStream("icons/or16.gif"));  //$NON-NLS-1$
static final long serialVersionUID = 1;

public Image getIconImage() {
	return OR_ICON;
}

public boolean getResult() {
	return getInput(TERMINAL_A) | getInput(TERMINAL_B);
}

public String toString() {
	return LogicMessages.OrGate_LabelText + " #" + getID();  //$NON-NLS-1$
}

}
