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
package org.eclipse.gef.requests;

import org.eclipse.draw2d.MouseEvent;

public class SelectionRequest 
	extends LocationRequest 
{

private int statemask;
private int lastButtonPressed;

public int getLastButtonPressed() {
	return lastButtonPressed;
}

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

public void setLastButtonPressed(int button) {
	lastButtonPressed = button;
}

}