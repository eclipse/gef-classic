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

abstract public class InputEvent
	extends java.util.EventObject
{
private int state;
private EventDispatcher dispatcher;

private boolean consumed = false;

public static final int
	ALT = SWT.ALT,
	CONTROL = SWT.CONTROL,
	SHIFT	= SWT.SHIFT,
	BUTTON1 = SWT.BUTTON1,
	BUTTON2 = SWT.BUTTON2,
	BUTTON3 = SWT.BUTTON3,
	ANY_BUTTON = BUTTON1 | BUTTON2 | BUTTON3;

public InputEvent(EventDispatcher dispatcher, IFigure source, int state){
	super(source);
	this.state = state;
	this.dispatcher = dispatcher;
}

public void consume() {
	consumed = true;
}

//public EventDispatcher getEventDispatcher(){return dispatcher;}

public int getState(){
	return state;
}

public boolean isConsumed() {
	return consumed;
}

}