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

import org.eclipse.swt.graphics.Color;

/**
 * Provides a lowered border.
 */
public final class SimpleLoweredBorder extends SchemeBorder {

	private static final Scheme DOUBLE = new Scheme(
			new Color[] { ColorConstants.buttonDarkest, ColorConstants.buttonDarker },
			new Color[] { ColorConstants.buttonLightest, ColorConstants.button });

	/**
	 * Constructs a SimpleLoweredBorder with the predefined button-pressed Scheme
	 * set as default.
	 *
	 * @since 2.0
	 */
	public SimpleLoweredBorder() {
		super(SCHEMES.BUTTON_PRESSED);
	}

	/**
	 * Constructs a SimpleLoweredBorder with the width of all sides provided as
	 * input. If width == 2, this SimpleLoweredBorder will use the local DOUBLE
	 * Scheme, otherwise it will use the {@link SchemeBorder.SCHEMES#BUTTON_PRESSED}
	 * Scheme.
	 *
	 * @param width the width of all the sides of the border
	 * @since 2.0
	 */
	public SimpleLoweredBorder(int width) {
		super(width == 2 ? DOUBLE : SCHEMES.BUTTON_PRESSED);
	}

}
