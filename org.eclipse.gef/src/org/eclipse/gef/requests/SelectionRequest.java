/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.requests;

import org.eclipse.draw2d.MouseEvent;

/**
 * A request to select an edit part.
 */
public class SelectionRequest 
	extends LocationRequest 
{

private int statemask;
private int lastButtonPressed;

/**
 * Returns the last button that was pressed.  This is useful if there is more than one
 * mouse button pressed and the most recent button pressed needs to be identified.
 * @return the last button pressed
 */
public int getLastButtonPressed() {
	return lastButtonPressed;
}

/**
 * Returns <code>true</code> if the ALT key is currently pressed.
 * @return whether the ALT key is pressed
 */
public boolean isAltKeyPressed() {
	return ((statemask & MouseEvent.ALT) != 0);
}

/**
 * Returns <code>true</code> if any mouse button is currently pressed.  
 * @return whether any mouse button is pressed
 */
public boolean isAnyMouseButtonPressed() {
	return ((statemask & MouseEvent.ANY_BUTTON) != 0);
}

/**
 * Returns <code>true</code> if the CTRL key is currently pressed.
 * @return whether the CTRL key is pressed
 */
public boolean isControlKeyPressed() {
	return ((statemask & MouseEvent.CONTROL) != 0);
}

/**
 * Returns <code>true</code> if the left mouse button is pressed.
 * @return whether the left mouse button is pressed
 */
public boolean isLeftMouseButtonPressed() {
	return ((statemask & MouseEvent.BUTTON1) != 0);
}

/**
 * Returns <code>true</code> if the right mouse button is pressed.
 * @return whether the right mouse button is pressed
 */
public boolean isRightMouseButtonPressed() {
	return ((statemask & MouseEvent.BUTTON3) != 0);
}

/**
 * Returns <code>true</code> if the SHIFT key is currently pressed.
 * @return whether the SHIFT key is pressed
 */
public boolean isShiftKeyPressed() {
	return ((statemask & MouseEvent.SHIFT) != 0);
}

/**
 * Sets the statemask for this request.
 * @param mask the statemask
 */
public void setModifiers(int mask) {
	statemask = mask;
}

/**
 * Sets the last mouse button that was pressed.
 * @param button the last button pressed
 */
public void setLastButtonPressed(int button) {
	lastButtonPressed = button;
}

}
