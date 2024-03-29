/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

/**
 * An event caused by the user interacting with the keyboard.
 */
public class KeyEvent extends InputEvent {

	/**
	 * The character that was pressed.
	 *
	 * @see org.eclipse.swt.events.KeyEvent#character
	 */
	public char character;

	/**
	 * The keycode.
	 *
	 * @see org.eclipse.swt.events.KeyEvent#keyCode
	 */
	public int keycode;

	/**
	 * Constructs a new KeyEvent.
	 *
	 * @param dispatcher the event dispatcher
	 * @param source     the source of the event
	 * @param ke         an SWT key event used to supply the statemask, keycode and
	 *                   character
	 */
	public KeyEvent(EventDispatcher dispatcher, IFigure source, org.eclipse.swt.events.KeyEvent ke) {
		super(dispatcher, source, ke.stateMask);
		character = ke.character;
		keycode = ke.keyCode;
	}

}
