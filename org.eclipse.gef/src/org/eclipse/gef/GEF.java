/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.eclipse.swt.widgets.Text;

/**
 * This is an internal class used for debugging
 */
public final class GEF {

static final String TAB = "  ";//$NON-NLS-1$

static Text text;
static int msgCount;
static int tab;
static NumberFormat formatter = new DecimalFormat();

public static boolean
	DebugTools = false,
	DebugEvents = false,
	DebugEditParts = false,
	DebugPainting = false,
	DebugFeedback = false,
	GlobalDebug = false,
	DebugToolStates = false,
	DebugDND = false;

static public void clearConsole(){
	if (text == null) return;
	text.setText("");//$NON-NLS-1$
}

static public void setConsole(Text textBox){
	msgCount=0;
	formatter.setMinimumIntegerDigits(2);
	formatter.setMaximumFractionDigits(0);
	text = textBox;
}

static public void debugPop(){
	tab--;
}

static public void debugPush(String heading){
	debug(heading);
	tab++;
}

static public void debug(String message){
	String lineNumber = formatter.format(new Long(msgCount++));
	msgCount %= 100;
	String indent = "";//$NON-NLS-1$
	for (int i=0; i<tab; i++)
		indent += TAB;
	if(text!=null)text.append(
		'\n' + lineNumber + '\t' + indent + message);
}

static public void hack(){}
static public void optimize(){}
static public void ToDo(){}

}
