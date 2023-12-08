/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.shapes.model;

import org.eclipse.swt.graphics.Image;

/**
 * A rectangular shape.
 *
 * @author Elias Volanakis
 */
public class RectangularShape extends Shape {
	/** A 16x16 pictogram of a rectangular shape. */
	private static final Image RECTANGLE_ICON = createImage("icons/rectangle16.gif");

	private static final long serialVersionUID = 1;

	@Override
	public Image getIcon() {
		return RECTANGLE_ICON;
	}

	@Override
	public String toString() {
		return "Rectangle " + hashCode();
	}
}
