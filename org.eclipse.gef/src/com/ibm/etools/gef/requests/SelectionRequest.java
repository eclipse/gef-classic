package com.ibm.etools.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.MouseEvent;

public class SelectionRequest 
	extends LocationRequest 
{

private int statemask;

public boolean isAltKeyPressed() {
	return ((statemask & MouseEvent.ALT) != 0);
}

public boolean isAnyMouseButtonPressed() {
	return ((statemask & MouseEvent.ANY_BUTTON) != 0);
}

public boolean isControlKeyPressed() {
	return ((statemask & MouseEvent.CONTROL) != 0);
}

public boolean isLeftMouseButtonPressed() {
	return ((statemask & MouseEvent.BUTTON1) != 0);
}

public boolean isRightMouseButtonPressed() {
	return ((statemask & MouseEvent.BUTTON3) != 0);
}

public boolean isShiftKeyPressed() {
	return ((statemask & MouseEvent.SHIFT) != 0);
}

public void setModifiers(int mask) {
	statemask = mask;
}

}