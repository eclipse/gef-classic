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
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;

/**
 * The base class for Draw2d events.
 */
public abstract class InputEvent
	extends java.util.EventObject
{

private int state;
private EventDispatcher dispatcher;

private boolean consumed = false;

/** @see SWT#ALT */
public static final int ALT = SWT.ALT;
/** @see SWT#CONTROL */
public static final int CONTROL = SWT.CONTROL;
/** @see SWT#SHIFT */
public static final int SHIFT = SWT.SHIFT;
/** @see SWT#BUTTON1 */
public static final int BUTTON1 = SWT.BUTTON1;
/** @see SWT#BUTTON2 */
public static final int BUTTON2 = SWT.BUTTON2;
/** @see SWT#BUTTON3 */
public static final int BUTTON3 = SWT.BUTTON3;
/** A bitwise OR'ing of {@link #BUTTON1}, {@link #BUTTON2} and {@link #BUTTON3} */
public static final int ANY_BUTTON = BUTTON1 | BUTTON2 | BUTTON3;

/**
 * Constructs a new InputEvent.
 * @param dispatcher the event dispatcher
 * @param source the source of the event
 * @param state the state
 */
public InputEvent(EventDispatcher dispatcher, IFigure source, int state) {
	super(source);
	this.state = state;
	this.dispatcher = dispatcher;
}

/**
 * Marks this event as consumed so that it doesn't get passed on to other listeners.
 */
public void consume() {
	consumed = true;
}

/**
 * Returns the event statemask, which is a bitwise OR'ing of any of the following: 
 * {@link #ALT}, {@link #CONTROL}, {@link #SHIFT}, {@link #BUTTON1}, {@link #BUTTON2}, 
 * and {@link #BUTTON3}.
 * @return the state
 */
public int getState() {
	return state;
}

/**
 * @return whether this event has been consumed.
 */
public boolean isConsumed() {
	return consumed;
}

}