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

public class AndGate
	extends Gate 
{

private static Image AND_ICON = new Image(null,	
	AndGate.class.getResourceAsStream("icons/and16.gif"));  //$NON-NLS-1$
static final long serialVersionUID = 1;

public Image getIconImage() {
	return AND_ICON;
}

public boolean getResult() {
	return getInput(TERMINAL_A) & getInput(TERMINAL_B);
}

public String toString(){
	return LogicResources.getString("AndGate.LabelText")+" #"+getID();//$NON-NLS-2$//$NON-NLS-1$
//	return "And Gate #"+getID() + ": " + getInput(TERMINAL_A) +
//		" AND " +
//		getInput(TERMINAL_B) + " = " + getResult();
}
}
