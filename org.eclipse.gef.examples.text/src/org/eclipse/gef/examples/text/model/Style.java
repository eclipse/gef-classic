/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.gef.examples.text.model;

/**
 * @since 3.1
 */
public class Style extends Notifier {

private boolean bold;
private String fontFamily;
private int fontHeight = - 1;
private boolean italic;
private boolean underline;
private Style parentStyle;

public String getFontFamily() {
	if (fontFamily == null && parentStyle != null)
		return parentStyle.getFontFamily();
	return fontFamily;
}

public int getFontHeight() {
	if (fontHeight == - 1 && parentStyle != null)
		return parentStyle.getFontHeight();
	return fontHeight;
}

public boolean isBold() {
	return bold || (parentStyle != null && parentStyle.isBold());
}

public boolean isItalic() {
	return italic || (parentStyle != null && parentStyle.isItalic());
}

public boolean isUnderline() {
	return underline ||(parentStyle != null && parentStyle.isUnderline());
}

public void setBold(boolean value) {
	if (bold == value)
		return;
	bold = value;
	listeners.firePropertyChange("bold", !value, value);
}

public void setFontFamily(String fontFamily) {
	String oldName = this.fontFamily;
	this.fontFamily = fontFamily;
	firePropertyChange("fontFamily", oldName, fontFamily);
}

public void setFontHeight(int fontHeight) {
	this.fontHeight = fontHeight;
	listeners.firePropertyChange("fontHeight", fontHeight, fontHeight);
}

public void setItalic(boolean value) {
	if (italic == value)
		return;
	italic = value;
	listeners.firePropertyChange("italic", !value, value);
}

public void setParentStyle(Style style) {
	parentStyle = style;
}

public void setUnderline(boolean value) {
	if (underline == value)
		return;
	underline = value;
	listeners.firePropertyChange("underline", !value, value);
}

}
