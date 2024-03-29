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

import java.text.MessageFormat;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.shapes.ShapesExampleMessages;

/**
 * An elliptical shape.
 *
 * @author Elias Volanakis
 */
public class EllipticalShape extends Shape {

	/** A 16x16 pictogram of an elliptical shape. */
	private static final Image ELLIPSE_ICON = createImage("icons/ellipse16.gif"); //$NON-NLS-1$

	private static final long serialVersionUID = 1;

	@Override
	public Image getIcon() {
		return ELLIPSE_ICON;
	}

	@Override
	public String toString() {
		return MessageFormat.format(ShapesExampleMessages.EllipticalShape_Ellipse, hashCode());
	}
}
