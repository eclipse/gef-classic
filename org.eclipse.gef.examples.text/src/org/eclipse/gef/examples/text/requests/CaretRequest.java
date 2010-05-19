/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.requests;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.edit.TextEditPart;
import org.eclipse.gef.requests.SelectionRequest;

public class CaretRequest extends SelectionRequest {

	public static final Object LOCATION = new Object();
	public static final Object COLUMN = new Object();
	public static final Object WORD_BOUNDARY = new Object();
	public static final Object LINE_BOUNDARY = new Object();
	public static final Object ROW = new Object();
	public static final Object PARAGRAPH = new Object();
	public static final Object WINDOW = new Object();
	// public static final Object PAGE = new Object();
	public static final Object DOCUMENT = new Object();

	/**
	 * Indicates whether the search is forward in direction. If the value is
	 * <code>true</code>, the search should be performed in a forwards direction
	 * relative to the document structure. Otherwise the search is backwards.
	 */
	public boolean isForward = true;

	/**
	 * Indicates that a search is being done by a parent element. If a search is
	 * recursive, the receiver should not implement the search by calling out to
	 * its container, since the container is the one performing the search.
	 */
	public boolean isRecursive;

	/**
	 * Indicates that the first available result should be returned. For
	 * example, normally advancing left and right would skip over at least one
	 * character. But, if the caret is moving from the end of one paragraph to
	 * the beginning of another, the first character in the next paragraph
	 * should not be skipped. The search is moving the caret into the paragraph.
	 */
	public boolean isInto;

	public TextLocation where;

	public CaretRequest() {

	}

	public void setReferenceTextLocation(TextEditPart part, int offset) {
		where = new TextLocation(part, offset);
	}

	public void setRecursive(boolean recursive) {
		isRecursive = recursive;
	}

}