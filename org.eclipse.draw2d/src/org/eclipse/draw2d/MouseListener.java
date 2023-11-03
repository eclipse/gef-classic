/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

/**
 * A listener interface for receiving mouse button events.
 */
public interface MouseListener {

	/**
	 * Called when a mouse button has been pressed while over the listened to
	 * object.
	 *
	 * @param me The MouseEvent object
	 */
	void mousePressed(MouseEvent me);

	/**
	 * Called when a pressed mouse button has been released.
	 *
	 * @param me The MouseEvent object
	 */
	void mouseReleased(MouseEvent me);

	/**
	 * Called when a mouse button has been double clicked over the listened to
	 * object.
	 *
	 * @param me The MouseEvent object
	 */
	void mouseDoubleClicked(MouseEvent me);

	/**
	 * An empty implementation of MouseListener for convenience.
	 */
	public class Stub implements MouseListener {
		/**
		 * @see org.eclipse.draw2d.MouseListener#mousePressed(MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent me) {
		}

		/**
		 * @see org.eclipse.draw2d.MouseListener#mouseReleased(MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent me) {
		}

		/**
		 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(MouseEvent)
		 */
		@Override
		public void mouseDoubleClicked(MouseEvent me) {
		}
	}

}
