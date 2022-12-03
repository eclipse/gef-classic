/*******************************************************************************
 * Copyright (c) 2022 Johannes Kepler University Linz and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alois Zoitl - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/** Interface for scaleable panes which provides the default functionality for deciding if a scaled graphics or the
 * given graphics should be used for drawing.
 * 
 * @since 3.13 */
public interface IScalablePane extends ScalableFigure {

	boolean useScaledGraphics();

	default Graphics getScaledGraphics(final Graphics graphics) {
		return (useScaledGraphics()) ? new ScaledGraphics(graphics) : graphics;
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
