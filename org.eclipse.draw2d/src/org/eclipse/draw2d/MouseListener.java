package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving mouse button
 * events.
 */
public interface MouseListener {

/**
 * Called when a mouse button has been pressed while 
 * over the listened to object.
 */
void mousePressed(MouseEvent me);

/**
 * Called when a pressed mouse button has been
 * released.
 */
void mouseReleased(MouseEvent me);

/**
 * Called when a mouse button has been double
 * clicked over the listened to object.
 */
void mouseDoubleClicked(MouseEvent me);

public class Stub
	implements MouseListener
{
	final protected void mousePress(MouseEvent me){}
	         public void mousePressed(MouseEvent me){}
	final protected void mouseRelease(MouseEvent me){}
	         public void mouseReleased(MouseEvent me){}
	final protected void mouseDoubleClick(MouseEvent me){}
	         public void mouseDoubleClicked(MouseEvent me){}
}

}