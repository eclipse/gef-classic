package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

public interface KeyListener {

void keyPressed(KeyEvent ke);

void keyReleased(KeyEvent ke);

class Stub
	implements KeyListener
{
	
	public void keyPressed(KeyEvent ke) {}

	public void keyReleased(KeyEvent ke) {}

}

}
