package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LiveOutput
	extends SimpleOutput
{

private static Image LIVE_ICON = new Image (null, LiveOutput.class.getResourceAsStream("icons/live.gif"));  //$NON-NLS-1$
static final long serialVersionUID = 1;

public Image getIconImage() {
	return LIVE_ICON;
}

public boolean getResult() {
	return true;
}

public String toString(){
	return LogicMessages.LiveOutput_LabelText;
}

}
