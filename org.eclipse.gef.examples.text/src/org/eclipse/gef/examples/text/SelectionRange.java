/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text;

import org.eclipse.gef.examples.text.edit.TextualEditPart;

public class SelectionRange {

public final TextLocation begin;

public final TextLocation end;

public final boolean isForward;

/**
 * Constructs a selection range which starts and ends at the given location. The direction
 * of the range is forward.
 * @since 3.1
 * @param location
 */
public SelectionRange(TextLocation location) {
	this(location, location, true);
}

/**
 * Constructs a selection range which starts and ends at the given locations. The
 * direction of the range is forward.
 * @since 3.1
 * @param begin
 * @param end
 */
public SelectionRange(TextLocation begin, TextLocation end) {
	this(begin, end, true);
}

/**
 * Constructs a selection range which starts and ends at the given locations with the
 * given direction. If a range is forward, the caret will be placed at the end of the
 * range. Otherwise, it is placed at the beginning.
 * @since 3.1
 * @param begin
 * @param end
 * @param forward
 */
public SelectionRange(TextLocation begin, TextLocation end, boolean forward) {
	this.begin = begin;
	this.end = end;
	this.isForward = forward;
}

public SelectionRange(TextualEditPart part, int offset) {
	this(new TextLocation(part, offset));
}

public SelectionRange(TextualEditPart begin, int bo, TextualEditPart end, int eo) {
	this(new TextLocation(begin, bo), new TextLocation(end, eo));
}

public boolean isEmpty() {
	return begin.equals(end);
}

}