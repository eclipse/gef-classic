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

import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * @since 3.1
 */
public class Style 
	extends Notifier
{
	
private static final long serialVersionUID = 1;
	
public static final String STYLE_ALIGNMENT = "alignment"; //$NON-NLS-1$

private int alignment = PositionConstants.NONE;
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

public boolean isSet(String styleID) {
	if (GEFActionConstants.STYLE_BOLD.equals(styleID))
		return bold;
	if (GEFActionConstants.STYLE_FONT_SIZE.equals(styleID))
		return fontHeight != -1;
	if (GEFActionConstants.STYLE_ITALIC.equals(styleID))
		return italic;
	if (GEFActionConstants.STYLE_UNDERLINE.equals(styleID))
		return underline;
	if (GEFActionConstants.STYLE_FONT_NAME.equals(styleID))
		return fontFamily != null;
	if (GEFActionConstants.BLOCK_ALIGN_LEFT.equals(styleID))
		return alignment == PositionConstants.LEFT;
	if (GEFActionConstants.BLOCK_ALIGN_CENTER.equals(styleID))
		return alignment == PositionConstants.CENTER;
	if (GEFActionConstants.BLOCK_ALIGN_RIGHT.equals(styleID))
		return alignment == PositionConstants.RIGHT;
	return false;
}

public boolean isUnderline() {
	return underline ||(parentStyle != null && parentStyle.isUnderline());
}

public void setAlignment(int value) {
	if (alignment == value 
			|| (value != PositionConstants.LEFT 
			&& value != PositionConstants.CENTER 
			&& value != PositionConstants.RIGHT
			&& value != PositionConstants.NONE))
		return;
	int oldValue = alignment;
	alignment = value;
	if (listeners != null)
		listeners.firePropertyChange(STYLE_ALIGNMENT, oldValue, alignment);
}

public void setBold(boolean value) {
	if (bold == value)
		return;
	bold = value;
	if (listeners != null)
		// TODO need to define properties for these?
		listeners.firePropertyChange("bold", !value, value);
}

public void setFontFamily(String fontFamily) {
	String oldName = this.fontFamily;
	this.fontFamily = fontFamily;
	firePropertyChange("fontFamily", oldName, fontFamily);
}

public void setFontHeight(int fontHeight) {
	this.fontHeight = fontHeight;
	if (listeners != null)
		listeners.firePropertyChange("fontHeight", fontHeight, fontHeight);
}

public void setItalic(boolean value) {
	if (italic == value)
		return;
	italic = value;
	if (listeners != null)
		listeners.firePropertyChange("italic", !value, value);
}

public void setParentStyle(Style style) {
	parentStyle = style;
}

public void setUnderline(boolean value) {
	if (underline == value)
		return;
	underline = value;
	if (listeners != null)
		listeners.firePropertyChange("underline", !value, value);
}

}
