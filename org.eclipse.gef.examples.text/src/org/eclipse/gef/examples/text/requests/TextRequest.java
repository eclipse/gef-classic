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

package org.eclipse.gef.examples.text.requests;

import org.eclipse.gef.Request;

import org.eclipse.gef.examples.text.AppendableCommand;
import org.eclipse.gef.examples.text.SelectionRange;

/**
 * @since 3.1
 */
public class TextRequest extends Request {

public static final Object REQ_BACKSPACE = new Object();

/**
 * The Request type for a break in the current line.  A line break is a newline within the
 * current paragraph or block, such as a bulleted or numbered list.  This request indicates
 * that SHIFT+ENTER was received
 */
public static final Object REQ_BREAK = new Object();

public static final Object REQ_DELETE = new Object();

public static final Object REQ_INDENT = new Object();

public static final Object REQ_INSERT = new Object();

public static final Object REQ_OVERWRITE = new Object();

public static final Object REQ_STYLE = new Object();

/**
 * The Request type for a new page.  A "Page" may be interpreted to mean anything based
 * on the context of the current selection range.  This request indicates that CTRL+ENTER
 * was received.
 */
public static final Object REQ_NEW_PAGE = new Object();

public static final Object REQ_NEWLINE= new Object();

public static final Object REQ_REMOVE_RANGE = new Object();

public static final Object REQ_UNINDENT = new Object();

private AppendableCommand previous;

private SelectionRange range;
private String text;
private String[] styleKeys;
private Object[] styleValues;

/**
 * @param type
 * @since 3.1
 */
public TextRequest(SelectionRange range, String text, AppendableCommand previous) {
	this (REQ_INSERT, range, text, previous);
}

public TextRequest(Object type, SelectionRange range) {
	this(type, range, null, null);
}

public TextRequest(Object type, SelectionRange range, AppendableCommand previous) {
	this(type, range, null, previous);
}

public TextRequest(Object type, SelectionRange range, String text, 
		AppendableCommand previous) {
	super(type == null ? REQ_INSERT : type);
	this.text = text;
	this.range = range;
	this.previous = previous;
}

public int getInsertionOffset() {
	return range.begin.offset;
}

public AppendableCommand getPreviousCommand() {
	return previous;
}

public SelectionRange getSelectionRange() {
	return range;
}

/**
 * @return the keys starting with most recent
 * @since 3.1
 */
public String[] getStyleKeys() {
	return styleKeys;
}

/**
 * 
 * @return the values starting with the most recent
 * @since 3.1
 */
public Object[] getStyleValues() {
	return styleValues;
}

public String getText() {
	return text;
}

public void setStyles(String keys[], Object values[]) {
	this.styleKeys = keys;
	this.styleValues = values;
}

}
