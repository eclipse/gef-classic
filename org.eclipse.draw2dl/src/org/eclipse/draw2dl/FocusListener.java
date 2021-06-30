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
 * A listener interface for receiving {@link org.eclipse.draw2dl.FocusEvent FocusEvents}.
 */
public interface FocusListener {

	/**
	 * Called when the listened to object has gained focus.
	 * 
	 * @param fe
	 *            The FocusEvent object
	 */
	void focusGained(org.eclipse.draw2dl.FocusEvent fe);

	/**
	 * Called when the listened to object has lost focus.
	 * 
	 * @param fe
	 *            The FocusEvent object
	 */
	void focusLost(org.eclipse.draw2dl.FocusEvent fe);

	/**
	 * An empty implementation of FocusListener for convenience.
	 */
	public class Stub implements FocusListener {
		/**
		 * @see FocusListener#focusGained(org.eclipse.draw2dl.FocusEvent)
		 */
		public void focusGained(org.eclipse.draw2dl.FocusEvent fe) {
		}

		/**
		 * @see FocusListener#focusLost(org.eclipse.draw2dl.FocusEvent)
		 */
		public void focusLost(FocusEvent fe) {
		}
	}

}
