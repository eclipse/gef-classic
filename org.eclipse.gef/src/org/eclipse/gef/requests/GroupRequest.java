/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
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
package org.eclipse.gef.requests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.EditPart;

/**
 * A Request from multiple EditParts.
 */
public class GroupRequest extends org.eclipse.gef.Request {

	List<? extends EditPart> parts;

	/**
	 * Creates a GroupRequest with the given type.
	 *
	 * @param type The type of Request.
	 */
	public GroupRequest(Object type) {
		setType(type);
	}

	/**
	 * Default constructor.
	 */
	public GroupRequest() {
	}

	/**
	 * Returns a List containing the EditParts making this Request.
	 *
	 * @return A List containing the EditParts making this Request.
	 */
	public List<? extends EditPart> getEditParts() {
		return parts;
	}

	/**
	 * Sets the EditParts making this Request to the given List.
	 *
	 * @param list The List of EditParts.
	 */
	public void setEditParts(List<? extends EditPart> list) {
		parts = list;
	}

	/**
	 * A helper method to set the given EditPart as the requester.
	 *
	 * @param part The EditPart making the request.
	 */
	public void setEditParts(EditPart part) {
		parts = new ArrayList<>(Arrays.asList(part));
	}

}
