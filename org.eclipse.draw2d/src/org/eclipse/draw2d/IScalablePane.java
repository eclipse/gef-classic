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

/**
 * Interface for scaleable panes which provides the default functionality for
 * deciding if a scaled graphics or the given graphics should be used for
 * drawing.
 * 
 * @since 3.13
 */
public interface IScalablePane extends ScalableFigure {

	boolean useScaledGraphics();

	boolean optimizeClip();

	public default Rectangle getScaledRect(Rectangle rect) {
		double scale = getScale();
		rect.width /= scale;
		rect.height /= scale;
		rect.x /= scale;
		rect.y /= scale;
		return rect;
	}

	public static final class IScalablePaneHelper {

		static Graphics prepareScaledGraphics(final Graphics graphics, IScalablePane figurePane) {
			Graphics graphicsToUse = (figurePane.useScaledGraphics()) ? new ScaledGraphics(graphics) : graphics;
			if (!figurePane.optimizeClip()) {
				graphicsToUse.clipRect(figurePane.getBounds().getShrinked(figurePane.getInsets()));
			}
			graphicsToUse.scale(figurePane.getScale());
			graphicsToUse.pushState();
			return graphicsToUse;
		}

		static void cleanupScaledGraphics(final Graphics graphics, final Graphics graphicsUsed) {
			graphicsUsed.popState();

			if (graphicsUsed != graphics) {
				graphicsUsed.dispose();
			}
			graphics.restoreState();
		}

		private IScalablePaneHelper() {
			throw new UnsupportedOperationException("Helper class IScalablePaneHelper should not be instantiated"); //$NON-NLS-1$
		}
	}
}
