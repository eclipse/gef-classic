/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.examples.text.TextLocation;

/**
 * @since 3.1
 */
public class InlineTextualPart extends CompoundTextualPart {

public InlineTextualPart(Object model) {
	super(model);
}

public TextLocation getNextLocation(int movement, TextLocation current, Rectangle caret) {
	switch (movement) {
		case LINE_START:
		case LINE_UP:
			return getTextParent().getNextLocation(movement,
					new TextLocation(this, getChildren().size()),
					caret);
		case LINE_DOWN:
		case LINE_END:
			return getTextParent().getNextLocation(movement,
					new TextLocation(this, 0), caret);
		
		default:
			break;
	}
	return super.getNextLocation(movement, current, caret);
}

}