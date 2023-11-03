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
 * A listener interface for receiving {@link KeyEvent KeyEvents} from the
 * keyboard.
 */
public interface KeyListener {

	/**
	 * Called when a key is pressed.
	 *
	 * @param ke The KeyEvent object
	 */
	void keyPressed(KeyEvent ke);

	/**
	 * Called when a key is released.
	 *
	 * @param ke The KeyEvent object
	 */
	void keyReleased(KeyEvent ke);

	/**
	 * An empty implementation of KeyListener for convenience.
	 */
	class Stub implements KeyListener {
		/**
		 * @see org.eclipse.draw2d.KeyListener#keyPressed(KeyEvent)
		 */
		@Override
		public void keyPressed(KeyEvent ke) {
		}

		/**
		 * @see org.eclipse.draw2d.KeyListener#keyReleased(KeyEvent)
		 */
		@Override
		public void keyReleased(KeyEvent ke) {
		}

	}

}
