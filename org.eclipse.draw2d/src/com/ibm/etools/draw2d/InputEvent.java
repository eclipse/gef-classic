package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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