package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import java.text.*;

/**
 * This is an internal class used for debugging and code management
 */

final public class GEF{

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
