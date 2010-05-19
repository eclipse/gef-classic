/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.SWT;

/**
 * @since 3.1
 */
public class Style extends Notifier {

	private static final long serialVersionUID = 1;

	public static final String PROPERTY_ALIGNMENT = "alignment"; //$NON-NLS-1$
	public static final String PROPERTY_FONT = "font"; //$NON-NLS-1$
	public static final String PROPERTY_FONT_SIZE = "fontSize"; //$NON-NLS-1$
	public static final String PROPERTY_BOLD = "bold"; //$NON-NLS-1$
	public static final String PROPERTY_ITALIC = "italics"; //$NON-NLS-1$
	public static final String PROPERTY_UNDERLINE = "underline"; //$NON-NLS-1$
	public static final String PROPERTY_ORIENTATION = "orientation"; //$NON-NLS-1$

	private int alignment = PositionConstants.NONE;
	private int orientation = SWT.NONE;
	private boolean bold;
	private String fontFamily;
	private int fontHeight = -1;
	private boolean italic;
	private Style parentStyle;
	private boolean underline;

	public int getAlignment() {
		if (alignment != PositionConstants.NONE)
			return alignment;
		if (parentStyle != null)
			return parentStyle.getAlignment();
		return PositionConstants.NONE;
	}

	public String getFontFamily() {
		if (fontFamily != null)
			return fontFamily;
		if (parentStyle != null)
			return parentStyle.getFontFamily();
		return "";
	}

	public int getFontHeight() {
		if (fontHeight != -1)
			return fontHeight;
		if (parentStyle != null)
			return parentStyle.getFontHeight();
		return -1;
	}

	public int getOrientation() {
		if (orientation != SWT.NONE)
			return orientation;
		if (parentStyle != null)
			return parentStyle.getOrientation();
		return SWT.NONE;
	}

	public boolean isAlignedLeft() {
		return alignment == PositionConstants.LEFT
				|| (parentStyle != null && parentStyle.isAlignedLeft());
	}

	public boolean isBold() {
		return bold || (parentStyle != null && parentStyle.isBold());
	}

	public boolean isItalic() {
		return italic || (parentStyle != null && parentStyle.isItalic());
	}

	public boolean isSet(String property) {
		if (PROPERTY_BOLD.equals(property))
			return bold;
		if (PROPERTY_FONT_SIZE.equals(property))
			return fontHeight != -1;
		if (PROPERTY_ITALIC.equals(property))
			return italic;
		if (PROPERTY_UNDERLINE.equals(property))
			return underline;
		if (PROPERTY_FONT.equals(property))
			return fontFamily != null;
		if (PROPERTY_ALIGNMENT.equals(property))
			return alignment != PositionConstants.NONE;
		if (PROPERTY_ORIENTATION.equals(property))
			return orientation != SWT.NONE;
		return false;
	}

	public boolean isUnderline() {
		return underline || (parentStyle != null && parentStyle.isUnderline());
	}

	public void setAlignment(int value) {
		if (alignment == value)
			return;
		if (value != PositionConstants.ALWAYS_RIGHT
				&& value != PositionConstants.CENTER
				&& value != PositionConstants.RIGHT
				&& value != PositionConstants.NONE
				&& value != PositionConstants.LEFT
				&& value != PositionConstants.ALWAYS_LEFT)
			throw new IllegalArgumentException(
					"Alignment must be LEFT, CENTER, RIGHT, ALWAYS_LEFT, ALWAYS_RIGHT or NONE."); //$NON-NLS-1$
		int oldValue = alignment;
		alignment = value;
		if (listeners != null)
			listeners.firePropertyChange(PROPERTY_ALIGNMENT, oldValue,
					alignment);
	}

	public void setBold(boolean value) {
		if (bold == value)
			return;
		bold = value;
		if (listeners != null)
			listeners.firePropertyChange(PROPERTY_BOLD, !value, value);
	}

	public void setFontFamily(String fontFamily) {
		String oldName = this.fontFamily;
		this.fontFamily = fontFamily;
		firePropertyChange(PROPERTY_FONT, oldName, fontFamily);
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
		if (listeners != null)
			listeners.firePropertyChange(PROPERTY_FONT_SIZE, fontHeight,
					fontHeight);
	}

	public void setItalic(boolean value) {
		if (italic == value)
			return;
		italic = value;
		if (listeners != null)
			listeners.firePropertyChange(PROPERTY_ITALIC, !value, value);
	}

	public void setOrientation(int value) {
		if (orientation == value)
			return;
		if (value != SWT.RIGHT_TO_LEFT && value != SWT.LEFT_TO_RIGHT
				&& value != SWT.NONE)
			throw new IllegalArgumentException(
					"Orientation must LTR, RTL or NONE.");
		int oldValue = orientation;
		orientation = value;
		if (listeners != null)
			listeners.firePropertyChange(PROPERTY_ORIENTATION, oldValue,
					orientation);
	}

	public void setParentStyle(Style style) {
		parentStyle = style;
	}

	public void setUnderline(boolean value) {
		if (underline == value)
			return;
		underline = value;
		if (listeners != null)
			listeners.firePropertyChange(PROPERTY_UNDERLINE, !value, value);
	}

}