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
 * An event that occurs when an {@link org.eclipse.draw2dl.IFigure} gains or
 * loses focus.
 */
public class FocusEvent {

	/** The figure losing focus */
	public org.eclipse.draw2dl.IFigure loser;
	/** The figure gaining focus */
	public org.eclipse.draw2dl.IFigure gainer;

	/**
	 * Constructs a new FocusEvent.
	 * 
	 * @param loser
	 *            the figure losing focus
	 * @param gainer
	 *            the figure gaining focus
	 */
	public FocusEvent(org.eclipse.draw2dl.IFigure loser, IFigure gainer) {
		this.loser = loser;
		this.gainer = gainer;
	}

}
