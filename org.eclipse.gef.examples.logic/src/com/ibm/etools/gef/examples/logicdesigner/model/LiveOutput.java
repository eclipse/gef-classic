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
	return LogicResources.getString("LiveOutput.LabelText");  //$NON-NLS-1$
}

}
