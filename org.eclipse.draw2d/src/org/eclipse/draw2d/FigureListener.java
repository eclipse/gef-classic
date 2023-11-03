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
 * A listener interface for receiving notification that an IFigure has moved.
 */
public interface FigureListener {

	/**
	 * Called when the given IFigure has moved.
	 *
	 * @param source The IFigure that has moved.
	 */
	void figureMoved(IFigure source);

}
