/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.requests.CaretRequest;
import org.eclipse.gef.examples.text.requests.SearchResult;

/**
 * @since 3.1
 */
public class InlineTextPart extends CompoundTextPart {

	public InlineTextPart(Container model) {
		super(model);
	}

	@Override
	public void getTextLocation(CaretRequest search, SearchResult result) {
		if (!search.isRecursive
				&& (search.getType() == CaretRequest.LINE_BOUNDARY || search.getType() == CaretRequest.ROW)) {
			search.setReferenceTextLocation(this, search.isForward ? 0 : getLength());
			getTextParent().getTextLocation(search, result);
		} else
			super.getTextLocation(search, result);
	}

}
