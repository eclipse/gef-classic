package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving mouse motion
 * events.
 */
public interface MouseMotionListener {

/**
 * Called when the mouse has moved over the listened
 * to object while a button was pressed.
 */
void mouseDragged(MouseEvent me);

/**
 * Called when the mouse has entered the listened 
 * to object.
 */
void mouseEntered(MouseEvent me);

/**
 * Called when the mouse has exited the listened
 * to object.
 */
void mouseExited(MouseEvent me);

/**
 * Called when the mouse hovers over the listened
 * to object.
 */
void mouseHover(MouseEvent me);

/**
 * Called when the mouse has moved over the listened
 * to object.
 */
void mouseMoved(MouseEvent me);

public class Stub
	implements MouseMotionListener
{
	//To prevent people from spelling the method incorrectly
	final protected void mouseDrag(MouseEvent me){}
	final protected void mouseEnter(MouseEvent me){}
	final protected void mouseExit(MouseEvent me){}
	final protected void mouseMove(MouseEvent me){}

	         public void mouseDragged(MouseEvent me){}
	         public void mouseEntered(MouseEvent me){}
	         public void mouseExited(MouseEvent me){}
	         public void mouseMoved(MouseEvent me){}
	         public void mouseHover(MouseEvent me){}
}

}


