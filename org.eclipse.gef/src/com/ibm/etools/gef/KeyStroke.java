package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.events.KeyEvent;

/**
 * Encapsulates a Keyboard gesture (press or release) from the User.
 */
public class KeyStroke {

int stateMask;
char character;
boolean onPressed;
int keyCode;

/**
 * Creates a KeyStroke for the specified KeyEvent and pressed value.
 * @param event The KeyEvent
 * @param pressed 
 */
KeyStroke(KeyEvent event, boolean pressed){
	onPressed = pressed;
	stateMask = event.stateMask;
	character = event.character;
	keyCode = event.keyCode;
}

KeyStroke(int keyCode, int stateMask, boolean onPressed){
	this.keyCode = keyCode;
	this.stateMask = stateMask;
	this.onPressed = onPressed;
}

KeyStroke(char character, int stateMask, boolean onPressed){
	this.character = character;
	this.stateMask = stateMask;
	this.onPressed = onPressed;
}

public boolean equals(Object obj) {
	if (obj instanceof KeyStroke) {
		KeyStroke stroke = (KeyStroke) obj;
		return stroke.character == character
			&& stroke.keyCode == keyCode
			&& stroke.onPressed == onPressed
			&& stroke.stateMask == stateMask;
	}
	return false;
}

public static KeyStroke getPressed(char character, int stateMask){
	return new KeyStroke(character, stateMask, true);
}

public static KeyStroke getPressed(int keyCode, int stateMask){
	return new KeyStroke(keyCode, stateMask, true);
}

public static KeyStroke getReleased(char character, int stateMask){
	return new KeyStroke(character, stateMask, false);
}

public static KeyStroke getReleased(int keyCode, int stateMask){
	return new KeyStroke(keyCode, stateMask, false);
}

public int hashCode() {
	return (stateMask + 1) *
		((character ^ keyCode) + 1)  //One of these is always Zero.
		+ (onPressed ? 0 : 1);
}

}