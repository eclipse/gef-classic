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
 * An event that occurs when an {@link org.eclipse.draw2d.IFigure} gains or
 * loses focus.
 */
public class FocusEvent {

	/** The figure losing focus */
	public IFigure loser;
	/** The figure gaining focus */
	public IFigure gainer;

	/**
	 * Constructs a new FocusEvent.
	 *
	 * @param loser  the figure losing focus
	 * @param gainer the figure gaining focus
	 */
	public FocusEvent(IFigure loser, IFigure gainer) {
		this.loser = loser;
		this.gainer = gainer;
	}

}
