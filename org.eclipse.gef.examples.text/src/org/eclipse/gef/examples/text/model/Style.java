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

import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * @since 3.1
 */
public class Style 
	extends Notifier
{

private boolean bold;
private String fontFamily;
private int fontHeight = - 1;
private boolean italic;
private boolean underline;
private Style parentStyle;

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
	return false;
}

public boolean isUnderline() {
	return underline ||(parentStyle != null && parentStyle.isUnderline());
}

public void setBold(boolean value) {
	if (bold == value)
		return;
	bold = value;
	if (listeners != null)
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
