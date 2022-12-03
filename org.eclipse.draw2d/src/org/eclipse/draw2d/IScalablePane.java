/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

public interface IScalablePane extends ScalableFigure {

	boolean useScaledGraphics();

	default Graphics getScaledGraphics(final Graphics graphics) {
		Graphics graphicsToUse = graphics;
		if (getScale() != 1.0) {
			if (useScaledGraphics()) {
				graphicsToUse = new ScaledGraphics(graphics);
			}
			graphicsToUse.scale(getScale());
		}
		return graphicsToUse;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
	 */
	@Override
	public default void translateToParent(Translatable t) {
		t.performScale(getScale());
	}

	/**
	 * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
	 */
	@Override
	public default void translateFromParent(Translatable t) {
		t.performScale(1 / getScale());
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#isCoordinateSystem()
	 */
	@Override
	public default boolean isCoordinateSystem() {
		return true;
	}

	public default Rectangle getScaledRect(Rectangle rect) {
		double scale = getScale();
		rect.width /= scale;
		rect.height /= scale;
		rect.x /= scale;
		rect.y /= scale;
		return rect;
	}

}
