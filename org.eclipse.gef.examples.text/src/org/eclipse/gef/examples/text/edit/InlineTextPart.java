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

package org.eclipse.gef.examples.text.edit;

import org.eclipse.gef.examples.text.requests.CaretRequest;
import org.eclipse.gef.examples.text.requests.SearchResult;

/**
 * @since 3.1
 */
public class InlineTextPart extends CompoundTextPart {

	public InlineTextPart(Object model) {
		super(model);
	}

	public void getTextLocation(CaretRequest search, SearchResult result) {
		if (!search.isRecursive
				&& (search.getType() == CaretRequest.LINE_BOUNDARY || search
						.getType() == CaretRequest.ROW)) {
			search.setReferenceTextLocation(this, search.isForward ? 0
					: getLength());
			getTextParent().getTextLocation(search, result);
		} else
			super.getTextLocation(search, result);
	}

}
