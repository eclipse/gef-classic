package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving mouse button events.
 */
public interface MouseListener {

/**
 * Called when a mouse button has been pressed while over the listened to object.
 * @param me The MouseEvent object
 */
void mousePressed(MouseEvent me);

/**
 * Called when a pressed mouse button has been released.
 * @param me The MouseEvent object
 */
void mouseReleased(MouseEvent me);

/**
 * Called when a mouse button has been double clicked over the listened to object.
 * @param me The MouseEvent object
 */
void mouseDoubleClicked(MouseEvent me);

/**
 * An empty implementation of MouseListener for convenience.
 */
public class Stub
	implements MouseListener
{
	/**	 * @see org.eclipse.draw2d.MouseListener#mousePressed(MouseEvent)	 */
	public void mousePressed(MouseEvent me) { }
	/**	 * @see org.eclipse.draw2d.MouseListener#mouseReleased(MouseEvent)	 */
	public void mouseReleased(MouseEvent me) { }
	/**	 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(MouseEvent)	 */
	public void mouseDoubleClicked(MouseEvent me) { }
}

}