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
 * A listener interface for receiving {@link org.eclipse.draw2dl.KeyEvent KeyEvents} from the
 * keyboard.
 */
public interface KeyListener {

	/**
	 * Called when a key is pressed.
	 * 
	 * @param ke
	 *            The KeyEvent object
	 */
	void keyPressed(org.eclipse.draw2dl.KeyEvent ke);

	/**
	 * Called when a key is released.
	 * 
	 * @param ke
	 *            The KeyEvent object
	 */
	void keyReleased(org.eclipse.draw2dl.KeyEvent ke);

	/**
	 * An empty implementation of KeyListener for convenience.
	 */
	class Stub implements KeyListener {
		/**
		 * @see KeyListener#keyPressed(org.eclipse.draw2dl.KeyEvent)
		 */
		public void keyPressed(org.eclipse.draw2dl.KeyEvent ke) {
		}

		/**
		 * @see KeyListener#keyReleased(org.eclipse.draw2dl.KeyEvent)
		 */
		public void keyReleased(KeyEvent ke) {
		}

	}

}
