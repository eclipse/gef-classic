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
 * A listener interface for receiving mouse button events.
 */
public interface MouseListener {

	/**
	 * Called when a mouse button has been pressed while over the listened to
	 * object.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mousePressed(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * Called when a pressed mouse button has been released.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mouseReleased(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * Called when a mouse button has been double clicked over the listened to
	 * object.
	 * 
	 * @param me
	 *            The MouseEvent object
	 */
	void mouseDoubleClicked(org.eclipse.draw2dl.MouseEvent me);

	/**
	 * An empty implementation of MouseListener for convenience.
	 */
	public class Stub implements MouseListener {
		/**
		 * @see MouseListener#mousePressed(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mousePressed(org.eclipse.draw2dl.MouseEvent me) {
		}

		/**
		 * @see MouseListener#mouseReleased(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mouseReleased(org.eclipse.draw2dl.MouseEvent me) {
		}

		/**
		 * @see MouseListener#mouseDoubleClicked(org.eclipse.draw2dl.MouseEvent)
		 */
		public void mouseDoubleClicked(MouseEvent me) {
		}
	}

}
