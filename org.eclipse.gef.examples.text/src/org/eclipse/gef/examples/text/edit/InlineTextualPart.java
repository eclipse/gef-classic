/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.gef.examples.text.TextLocation;

/**
 * @since 3.1
 */
public class InlineTextualPart extends CompoundTextualPart {

public InlineTextualPart(Object model) {
	super(model);
}

public TextLocation getNextLocation(CaretSearch search) {
	switch (search.type) {
		case CaretSearch.LINE_BOUNDARY:
		case CaretSearch.ROW:
			if (search.isRecursive)
				break;
			return getTextParent().getNextLocation(
					search.continueSearch(this, search.isForward ? 0 : getLength()));		
		default:
			break;
	}
	return super.getNextLocation(search);
}

}