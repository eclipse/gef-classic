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

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.examples.text.TextLocation;

/**
 * @since 3.1
 */
public class BlockTextualPart extends CompoundTextualPart {

public BlockTextualPart(Object model) {
	super(model);
}

public TextLocation getNextLocation(int movement, TextLocation current, Rectangle caret) {
	
	TextLocation result;
	switch (movement) {
		case LINE_UP:
			result = searchLineAbove(current, caret);
			if (result == null && getParent() instanceof TextualEditPart)
				return getTextParent().getNextLocation(LINE_UP,
						new TextLocation(this, 0), caret);
			return result;
		case LINE_DOWN:
			result = searchLineBelow(current, caret);
			if (result == null && getParent() instanceof TextualEditPart)
				return getTextParent().getNextLocation(LINE_DOWN,
						new TextLocation(this, getLength()), caret);
			return result;
		case LINE_START:
			return searchLineBegin(caret);
		case LINE_END:
			return searchLineEnd(caret);

		default:
			return super.getNextLocation(movement, current, caret);
	}
}

}
