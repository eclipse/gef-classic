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

import org.eclipse.swt.graphics.Color;

/**
 * A collection of color-related constants.
 */
public interface ColorProvider {

	Color getButtonLightest();

	Color getButton();

	Color getButtonDarker();

	Color getButtonDarkest();

	Color getListBackground();

	Color getListForeground();

	Color getLineForeground();

	Color getMenuBackground();

	Color getMenuForeground();

	Color getMenuBackgroundSelected();

	Color getMenuForegroundSelected();

	Color getTitleBackground();

	Color getTitleGradient();

	Color getTitleForeground();

	Color getTitleInactiveForeground();

	Color getTitleInactiveBackground();

	Color getTitleInactiveGradient();

	Color getTooltipForeground();

	Color getTooltipBackground();

	Color getListHoverBackgroundColor();

	Color getListSelectedBackgroundColor();

	class SystemColorFactory {
		static ColorProvider colorProvider = new ColorProviderLegacy();

		public static void setColorProvider(ColorProvider colorProvider) {
			SystemColorFactory.colorProvider = colorProvider;
		}
	}
}
