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

import org.eclipse.draw2d.text.CaretInfo;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.examples.text.requests.CaretRequest;
import org.eclipse.gef.examples.text.requests.SearchResult;

/**
 * @since 3.1
 */
public interface TextEditPart extends GraphicalEditPart {

	/**
	 * @since 3.1
	 * @return
	 */
	boolean acceptsCaret();

	/**
	 * Returns the placement for the caret in absolute coordinates.
	 * 
	 * @since 3.1
	 * @param offset
	 *            the location of the caret within the receiver
	 * @return the absolute caret location
	 */
	CaretInfo getCaretPlacement(int offset, boolean trailing);

	/**
	 * Returns the length in characters of this part
	 * 
	 * @since 3.1
	 * @return value
	 */
	int getLength();

	void getTextLocation(CaretRequest search, SearchResult result);

	/**
	 * Sets the begin and ending selection indices. Indices are zero-based, and
	 * are specified in terms of caret offsets. In a part's length is N, there
	 * are N+1 caret positions ranging from 0..N. The begin index should always
	 * be less than or equal to the end index. No selection is indicated by
	 * setting both values to <code>-1</code>.
	 * 
	 * @since 3.1
	 * @param begin
	 *            the beginning offset or -1
	 * @param end
	 *            the end offset or -1
	 */
	void setSelection(int begin, int end);

}