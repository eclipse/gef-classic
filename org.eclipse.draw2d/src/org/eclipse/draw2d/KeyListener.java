package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving {@link KeyEvent KeyEvents} from the keyboard.
 */
public interface KeyListener {

/**
 * Called when a key is pressed.
 * @param ke The KeyEvent object */
void keyPressed(KeyEvent ke);

/**
 * Called when a key is released.
 * @param ke The KeyEvent object
 */
void keyReleased(KeyEvent ke);

/**
 * An empty implementation of KeyListener for convenience. */
class Stub
	implements KeyListener
{
	/**	 * @see org.eclipse.draw2d.KeyListener#keyPressed(KeyEvent)	 */
	public void keyPressed(KeyEvent ke) { }
	/**	 * @see org.eclipse.draw2d.KeyListener#keyReleased(KeyEvent)	 */
	public void keyReleased(KeyEvent ke) { }

}

}
