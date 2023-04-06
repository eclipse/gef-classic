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
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of color-related constants.
 * 
 * @since 3.13
 */
public interface ColorConstants {

	class SystemColorFactory {
		/**
		 * @since 3.13
		 */
		public static Color getColor(final int which) {
			Display display = Display.getCurrent();
			if (display != null)
				return display.getSystemColor(which);
			display = Display.getDefault();
			final Color result[] = new Color[1];
			display.syncExec(new Runnable() {
				public void run() {
					synchronized (result) {
						result[0] = Display.getCurrent().getSystemColor(which);
					}
				}
			});
			synchronized (result) {
				return result[0];
			}
		}
	}

	/**
	 * @see SWT#COLOR_WIDGET_HIGHLIGHT_SHADOW
	 */
	Color buttonLightest = ColorProvider.SystemColorFactory.colorProvider.getButtonLightest();
	/**
	 * @see SWT#COLOR_WIDGET_BACKGROUND
	 */
	Color button = ColorProvider.SystemColorFactory.colorProvider.getButton();
	/**
	 * @see SWT#COLOR_WIDGET_NORMAL_SHADOW
	 */
	Color buttonDarker = ColorProvider.SystemColorFactory.colorProvider.getButtonDarker();
	/**
	 * @see SWT#COLOR_WIDGET_DARK_SHADOW
	 */
	Color buttonDarkest = ColorProvider.SystemColorFactory.colorProvider.getButtonDarkest();

	/**
	 * @see SWT#COLOR_LIST_BACKGROUND
	 */
	Color listBackground = ColorProvider.SystemColorFactory.colorProvider.getListBackground();
	/**
	 * @see SWT#COLOR_LIST_FOREGROUND
	 * @since 3.13
	 */
	Color listForeground = ColorProvider.SystemColorFactory.colorProvider.getListForeground();

	/**
	 * @since 3.13
	 */
	Color lineForeground = ColorProvider.SystemColorFactory.colorProvider.getLineForeground();

	/**
	 * @see SWT#COLOR_WIDGET_BACKGROUND
	 */
	Color menuBackground = ColorProvider.SystemColorFactory.colorProvider.getMenuBackground();
	/**
	 * @see SWT#COLOR_WIDGET_FOREGROUND
	 */
	Color menuForeground = ColorProvider.SystemColorFactory.colorProvider.getMenuForeground();
	/**
	 * @see SWT#COLOR_LIST_SELECTION
	 */
	Color menuBackgroundSelected = ColorProvider.SystemColorFactory.colorProvider.getMenuBackgroundSelected();
	/**
	 * @see SWT#COLOR_LIST_SELECTION_TEXT
	 */
	Color menuForegroundSelected = ColorProvider.SystemColorFactory.colorProvider.getMenuForegroundSelected();

	/**
	 * @see SWT#COLOR_TITLE_BACKGROUND
	 */
	Color titleBackground = ColorProvider.SystemColorFactory.colorProvider.getTitleBackground();
	/**
	 * @see SWT#COLOR_TITLE_BACKGROUND_GRADIENT
	 */
	Color titleGradient = ColorProvider.SystemColorFactory.colorProvider.getTitleGradient();
	/**
	 * @see SWT#COLOR_TITLE_FOREGROUND
	 */
	Color titleForeground = ColorProvider.SystemColorFactory.colorProvider.getTitleForeground();
	/**
	 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
	 */
	Color titleInactiveForeground = ColorProvider.SystemColorFactory.colorProvider.getTitleInactiveForeground();
	/**
	 * @see SWT#COLOR_TITLE_INACTIVE_BACKGROUND
	 */
	Color titleInactiveBackground = ColorProvider.SystemColorFactory.colorProvider.getTitleInactiveBackground();
	/**
	 * @see SWT#COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT
	 */
	Color titleInactiveGradient = ColorProvider.SystemColorFactory.colorProvider.getTitleInactiveGradient();

	/**
	 * @see SWT#COLOR_INFO_FOREGROUND
	 */
	Color tooltipForeground = ColorProvider.SystemColorFactory.colorProvider.getTooltipForeground();
	/**
	 * @see SWT#COLOR_INFO_BACKGROUND
	 * @since 3.13
	 */
	Color tooltipBackground = ColorProvider.SystemColorFactory.colorProvider.getTooltipBackground();

	/**
	 * @since 3.13
	 */
	Color listHoverBackgroundColor = ColorProvider.SystemColorFactory.colorProvider.getListHoverBackgroundColor();

	/**
	 * @since 3.13
	 */
	Color listSelectedBackgroundColor = ColorProvider.SystemColorFactory.colorProvider.getListSelectedBackgroundColor();
	/*
	 * Misc. colors
	 */
	/** One of the pre-defined colors */
	Color white = new Color(null, 255, 255, 255);
	/** One of the pre-defined colors */
	Color lightGray = new Color(null, 192, 192, 192);
	/** One of the pre-defined colors */
	Color gray = new Color(null, 128, 128, 128);
	/** One of the pre-defined colors */
	Color darkGray = new Color(null, 64, 64, 64);
	/** One of the pre-defined colors */
	Color black = new Color(null, 0, 0, 0);
	/** One of the pre-defined colors */
	Color red = new Color(null, 255, 0, 0);
	/** One of the pre-defined colors */
	Color orange = new Color(null, 255, 196, 0);
	/** One of the pre-defined colors */
	Color yellow = new Color(null, 255, 255, 0);
	/** One of the pre-defined colors */
	Color green = new Color(null, 0, 255, 0);
	/** One of the pre-defined colors */
	Color lightGreen = new Color(null, 96, 255, 96);
	/** One of the pre-defined colors */
	Color darkGreen = new Color(null, 0, 127, 0);
	/** One of the pre-defined colors */
	Color cyan = new Color(null, 0, 255, 255);
	/** One of the pre-defined colors */
	Color lightBlue = new Color(null, 127, 127, 255);
	/** One of the pre-defined colors */
	Color blue = new Color(null, 0, 0, 255);
	/** One of the pre-defined colors */
	Color darkBlue = new Color(null, 0, 0, 127);

}
