package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

public class KeyEvent
	extends InputEvent
{

public char character;
public int keycode;

public KeyEvent(
	EventDispatcher dispatcher,
	IFigure source,
	org.eclipse.swt.events.KeyEvent ke)
{
	super(dispatcher, source, ke.stateMask);
	character = ke.character;
	keycode = ke.keyCode;
}

}
