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

TextLocation getNextLocation(int identifier, TextLocation current);

TextLocation getLocation(Point absolute);

void setSelection(int start, int end);

/**
 * @since 3.1
 * @return
 */
boolean acceptsCaret();

}