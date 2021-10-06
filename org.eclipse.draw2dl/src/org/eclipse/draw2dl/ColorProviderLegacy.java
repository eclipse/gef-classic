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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of color-related constants.
 */
public class ColorProviderLegacy implements ColorProvider {

	@Override
	public Color getButtonLightest() {
		return getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	}

	@Override
	public Color getButton() {
		return getColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	@Override
	public Color getButtonDarker() {
		return getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	}

	@Override
	public Color getButtonDarkest() {
		return getColor(SWT.COLOR_WIDGET_DARK_SHADOW);
	}

	@Override
	public Color getListBackground() {
		return getColor(SWT.COLOR_LIST_BACKGROUND);
	}

	@Override
	public Color getListForeground() {
		return getColor(SWT.COLOR_LIST_FOREGROUND);
	}

	@Override
	public Color getLineForeground() {
		return getColor(SWT.COLOR_GRAY);
	}

	@Override
	public Color getMenuBackground() {
		return getColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	@Override
	public Color getMenuForeground() {
		return getColor(SWT.COLOR_WIDGET_FOREGROUND);
	}

	@Override
	public Color getMenuBackgroundSelected() {
		return getColor(SWT.COLOR_LIST_SELECTION);
	}

	@Override
	public Color getMenuForegroundSelected() {
		return getColor(SWT.COLOR_LIST_SELECTION_TEXT);
	}

	@Override
	public Color getTitleBackground() {
		return getColor(SWT.COLOR_TITLE_BACKGROUND);
	}

	@Override
	public Color getTitleGradient() {
		return getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	}

	@Override
	public Color getTitleForeground() {
		return getColor(SWT.COLOR_TITLE_FOREGROUND);
	}

	@Override
	public Color getTitleInactiveForeground() {
		return getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
	}

	@Override
	public Color getTitleInactiveBackground() {
		return getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	}

	@Override
	public Color getTitleInactiveGradient() {
		return getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
	}

	@Override
	public Color getTooltipForeground() {
		return getColor(SWT.COLOR_INFO_FOREGROUND);
	}

	@Override
	public Color getTooltipBackground() {
		return getColor(SWT.COLOR_INFO_BACKGROUND);
	}

	@Override
	public Color getListHoverBackgroundColor() {
		return new Color(null, 252, 228, 179);
	}

	@Override
	public Color getListSelectedBackgroundColor() {
		return new Color(null, 207, 227, 250);
	}

	protected static Color getColor(final int which) {
		Display display = Display.getCurrent();
		if (display != null)
			return display.getSystemColor(which);
		display = Display.getDefault();
		final Color[] result = new Color[1];
		display.syncExec(() -> {
			synchronized (result) {
				result[0] = Display.getCurrent().getSystemColor(which);
			}
		});
		synchronized (result) {
			return result[0];
		}
	}

}
