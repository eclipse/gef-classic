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

package org.eclipse.gef.examples.text.edit;

import org.eclipse.gef.examples.text.TextLocation;

/**
 * @since 3.1
 */
public class CaretSearch implements Cloneable {

/**
 * Indicates whether the search is forward in direction.  If the value is
 * <code>true</code>, the search should be performed in a forwards direction relative to
 * the document structure.  Otherwise the search is backwards.
 */
public boolean isForward;

/**
 * Indicates that a search is being done by a parent element.  If a search is recursive,
 * the receiver should not implement the search by calling out to its container, since the
 * container is the one performing the search.
 */
public boolean isRecursive;

/**
 * Indicates that the first available result should be returned.  For example, normally
 * advancing left and right would skip over at least one character.  But, if the caret is
 * moving from the end of one paragraph to the beginning of another, the first character
 * in the next paragraph should not be skipped.  The search is moving the caret into the
 * paragraph.
 */
public boolean isInto;
public int type;
public TextLocation where;
/**
 * The absolute x location of the caret.
 */
public int x;
public int baseline;

public static final int COLUMN = 1;
public static final int ROW = 2;
public static final int WORD_BOUNDARY = 3;
public static final int LINE_BOUNDARY = 4;
public static final int PARAGRAPH = 5;
public static final int DOCUMENT = 6;

/**
 * @since 3.1
 */
public CaretSearch() { }

private CaretSearch(TextLocation loc, int type, boolean isForward, boolean isSearch) {
	this.where = loc;
	this.type = type;
	this.isForward = isForward;
	this.isRecursive = isSearch;
}

/**
 * Clones this search but resets the TextLocation to the given part and offset.
 * @param part
 * @param offset
 * @return
 * @since 3.1
 */
public CaretSearch continueSearch(TextualEditPart part, int offset) {
	try {
		CaretSearch result = (CaretSearch)clone();
		result.where = new TextLocation(part, offset);
		return result;
	} catch (CloneNotSupportedException exc) {
		throw new RuntimeException(exc);
	}
}

public CaretSearch recurseSearch() {
	if (isRecursive)
		return this;
	try {
		CaretSearch result = (CaretSearch)clone();
		result.isRecursive = true;
		result.where = null;
		return result;
	} catch (CloneNotSupportedException exc) {
		throw new RuntimeException(exc);
	}
}

}
