package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.swt.graphics.Image;
import com.ibm.etools.gef.examples.logicdesigner.LogicResources;

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
	return LogicResources.getString("OrGate.LabelText")+" #" + getID();//$NON-NLS-2$//$NON-NLS-1$
//	return "Or gate #" + getID() + ": " + getInput(TERMINAL_A) + " OR " + getInput(TERMINAL_B) + " = " + getResult();
}

}
