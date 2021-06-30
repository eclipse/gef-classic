/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

/**
 * A listener interface for receiving mouse motion events.
 */
public interface MouseMotionListener {

	/**
	 * Called when the mouse has moved over the listened to object while a
	 * button was pressed.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mouseDragged(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * Called when the mouse has entered the listened to object.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mouseEntered(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * Called when the mouse has exited the listened to object.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mouseExited(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * Called when the mouse hovers over the listened to object.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mouseHover(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * Called when the mouse has moved over the listened to object.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mouseMoved(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * An empty implementation of MouseMotionListener for convenience.
	 */
	public class Stub implements MouseMotionListener {
		/**
		 * @see MouseMotionListener#mouseDragged(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mouseDragged(org.eclipse.draw2dl.MouseEvent me) {
		}

		/**
		 * @see MouseMotionListener#mouseEntered(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mouseEntered(org.eclipse.draw2dl.MouseEvent me) {
		}

		/**
		 * @see MouseMotionListener#mouseExited(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mouseExited(org.eclipse.draw2dl.MouseEvent me) {
		}

		/**
		 * @see MouseMotionListener#mouseMoved(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mouseMoved(org.eclipse.draw2dl.MouseEvent me) {
		}

		/**
		 * @see MouseMotionListener#mouseHover(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mouseHover(MouseEvent me) {
		}
	}

}
