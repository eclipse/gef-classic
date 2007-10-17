/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model;

import org.eclipse.core.runtime.Assert;

/**
 * @since 3.1
 */
public class TextRun 
	extends ModelElement 
{
	
private static final long serialVersionUID = 1;

public static final int TYPE_IMPORT = 1;
public static final int TYPE_PACKAGE = 2;
public static final int TYPE_BULLET = 3;
public static final int TYPE_UNDERLINE = 4;
public static final int TYPE_CODE  = 5;

private String text;

/**
 * @since 3.1
 */
public TextRun(String text) {
	this.text = text;
}

/**
 * @since 3.1
 */
public TextRun(String text, int type) {
	this.text = text;
	this.type = type;
}

/**
 * Divide this Run into two runs at the given offset.  The second run is return.
 * @since 3.1
 * @param offset where to divide
 * @return the second half
 */
public TextRun subdivideRun(int offset) {
	String remainder = removeRange(offset, size() - offset);
	return new TextRun(remainder, getType());
}

public String getText() {
	return text;
}

public void insertText(String someText, int offset) {
	text = text.substring(0, offset) + someText
			+ text.substring(offset, text.length());
	firePropertyChange("text", null, text);
}

public String overwriteText(String someText, int offset) {
	String result = text.substring(offset,
			Math.min(offset + someText.length(), text.length()));
	text = text.substring(0, offset) + someText 
			+ text.substring(offset + result.length());
	firePropertyChange("text", null, text);
	return result;
}

public String removeRange(int offset, int length) {
	Assert.isTrue(offset <= text.length());
	Assert.isTrue(offset + length <= text.length());
	String result = text.substring(offset, offset + length);
	text = text.substring(0, offset) + text.substring(offset + length);
	firePropertyChange("text", null, text);
	return result;
}

public void setText(String text) {
	this.text = text;
	firePropertyChange("text", null, text);
}

/**
 * @see org.eclipse.gef.examples.text.model.ModelElement#size()
 */
public int size() {
	return getText().length();
}

public String toString() {
	return text;
}

}