package com.ibm.etools.gef.ui.console;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.resource.ImageDescriptor;
import com.ibm.etools.gef.GEF;
import org.eclipse.jface.action.*;

public class DebugGEFAction extends Action{

public static String DEBUG_FEEDBACK="DFeedback"; //$NON-NLS-1$
public static String DEBUG_PAINTING="DPainting"; //$NON-NLS-1$
public static String DEBUG_EDITPARTS="DEditParts"; //$NON-NLS-1$
public static String DEBUG_EVENTS="DEvents"; //$NON-NLS-1$
public static String DEBUG_TOOLS="DTools"; //$NON-NLS-1$
public static String DEBUG_GLOBAL="DGlobal"; //$NON-NLS-1$
public static String DEBUG_CLEAR="DClear"; //$NON-NLS-1$
public static String DEBUG_STATES="DStates"; //$NON-NLS-1$
public static String DEBUG_DND="DDND"; //$NON-NLS-1$

public DebugGEFAction(String label, ImageDescriptor desc){
	super(label,desc);
}

public void run(){
	String type = getText();
	if( type.compareTo(DEBUG_STATES)==0){
		GEF.DebugToolStates=!GEF.DebugToolStates;
	}
	if( type.compareTo(DEBUG_FEEDBACK)==0 ){
		GEF.DebugFeedback=!GEF.DebugFeedback;
	}
	if( type.compareTo(DEBUG_PAINTING)==0 ){
		GEF.DebugPainting=!GEF.DebugPainting;
	}
	if( type.compareTo(DEBUG_EDITPARTS)==0 ){
		GEF.DebugEditParts=!GEF.DebugEditParts;
	}
	if( type.compareTo(DEBUG_EVENTS)==0 ){
		GEF.DebugEvents=!GEF.DebugEvents;
	}
	if( type.compareTo(DEBUG_TOOLS)==0 ){
		GEF.DebugTools=!GEF.DebugTools;
	}
	if( type.compareTo(DEBUG_GLOBAL)==0 ){
		GEF.GlobalDebug=!GEF.GlobalDebug;
	}
	if( type.compareTo(DEBUG_CLEAR)==0 ){
		GEF.clearConsole();
	}
	if( type.compareTo(DEBUG_DND)==0 ){
		GEF.DebugDND=!GEF.DebugDND;
	}
}

}

