/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.tools;

import org.eclipse.gef.Request;

import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextCommand;

/**
 * @since 3.1
 */
public class TextRequest extends Request {

public static final String REQ_INSERT = "$typing input";

public static final String REQ_BACKSPACE = "$typing backspace";

public static final String REQ_DELETE = "$typing delete";

public static final String REQ_NEWLINE= "$typing newline";

public static final String REQ_REMOVE_RANGE = "$typing remove range";

private TextCommand previous;

private SelectionRange range;

private String text;

/**
 * @param type
 * @since 3.1
 */
public TextRequest(SelectionRange range, String text, TextCommand prevoius) {
	super(REQ_INSERT);
	this.text = text;
	this.range = range;
	this.previous = prevoius;
}

public TextRequest(String type, SelectionRange range, TextCommand previous) {
	super(type);
	this.range = range;
	this.previous = previous;
}

public TextRequest(SelectionRange range) {
	super(REQ_REMOVE_RANGE);
	this.range = range;
}

public String getText() {
	return text;
}

public TextCommand getPreviousCommand() {
	return previous;
}

public int getInsertionOffset() {
	return range.begin.offset;
}

public SelectionRange getSelectionRange() {
	return range;
}

}