/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;

import org.eclipse.gef.examples.text.TextLocation;

/**
 * @since 3.1
 */
public interface TextualEditPart extends GraphicalEditPart {

int COLUMN_PREVIOUS = 1;
int COLUMN_NEXT = 2;
int COLUMN_PREVIOUS_INTO = 3;
int COLUMN_NEXT_INTO = 4;
int LINE_UP = 5;
int LINE_DOWN = 6;
int LINE_UP_INTO = 7;
int LINE_DOWN_INTO = 8;
int LINE_END = 9;
int LINE_START = 10;
int LINE_END_QUERY = 11;
int LINE_START_QUERY = 12;

/**
 * Returns the length in characters of this part
 * @since 3.1
 * @return value
 */
int getLength();

/**
 * Returns the placement for the caret in absolute coordinates.
 * @since 3.1
 * @param offset the location of the caret within the receiver
 * @return the absolute caret location
 */
Rectangle getCaretPlacement(int offset);

TextLocation getNextLocation(int movement, TextLocation current, Rectangle caret);

TextLocation getLocation(Point absolute);

/**
 * Sets the begin and ending selection indices.  Indices are zero-based, and are specified
 * in terms of caret offsets. In a part's length is N, there are N+1 caret positions
 * ranging from 0..N. The begin index should always be less than the end index. No
 * selection is indicated by setting both values to <code>-1</code>.
 * @since 3.1
 * @param begin the beginning offset or -1
 * @param end the end offset or -1
 */
void setSelection(int begin, int end);

/**
 * @since 3.1
 * @return
 */
boolean acceptsCaret();

}