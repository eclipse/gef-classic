package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import com.ibm.etools.gef.examples.logicdesigner.LogicResources;

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
	return LogicResources.getString("Circuit.LabelText")+" #" + getID();//$NON-NLS-2$//$NON-NLS-1$
}

public void update() {
	for (int i=0; i<8;i++)
		setOutput(TERMINALS_OUT[i],getInput(TERMINALS_IN[i]));
}

}
