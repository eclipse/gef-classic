package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving mouse motion events.
 */
public interface MouseMotionListener {

/**
 * Called when the mouse has moved over the listened to object while a button was pressed.
 * @param me The MouseEvent object
 */
void mouseDragged(MouseEvent me);

/**
 * Called when the mouse has entered the listened to object.
 * @param me The MouseEvent object
 */
void mouseEntered(MouseEvent me);

/**
 * Called when the mouse has exited the listened to object.
 * @param me The MouseEvent object
 */
void mouseExited(MouseEvent me);

/**
 * Called when the mouse hovers over the listened to object.
 * @param me The MouseEvent object
 */
void mouseHover(MouseEvent me);

/**
 * Called when the mouse has moved over the listened to object.
 * @param me The MouseEvent object
 */
void mouseMoved(MouseEvent me);

/**
 * An empty implementation of MouseMotionListener for convenience.
 */
public class Stub
	implements MouseMotionListener
{
	/**	 * @see org.eclipse.draw2d.MouseMotionListener#mouseDragged(MouseEvent)	 */
	public void mouseDragged(MouseEvent me) { }
	/**	 * @see org.eclipse.draw2d.MouseMotionListener#mouseEntered(MouseEvent)	 */
	public void mouseEntered(MouseEvent me) { }
	/**	 * @see org.eclipse.draw2d.MouseMotionListener#mouseExited(MouseEvent)	 */
	public void mouseExited(MouseEvent me) { }
	/**	 * @see org.eclipse.draw2d.MouseMotionListener#mouseMoved(MouseEvent)	 */
	public void mouseMoved(MouseEvent me) { }
	/**	 * @see org.eclipse.draw2d.MouseMotionListener#mouseHover(MouseEvent)	 */
	public void mouseHover(MouseEvent me) { }
}

}


