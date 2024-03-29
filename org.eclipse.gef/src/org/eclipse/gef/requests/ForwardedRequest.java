/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;

/**
 * A Request to be forwarded to another EditPart.
 */
public class ForwardedRequest extends Request {

	private EditPart sender;

	/**
	 * Creates a ForwardRequest with the given type.
	 *
	 * @param type The type of Request
	 */
	public ForwardedRequest(Object type) {
		setType(type);
	}

	/**
	 * Creates a ForwardRequest with the given type and sets the sender.
	 *
	 * @param type   The type of Request
	 * @param sender The EditPart that forwarded this Request
	 */
	public ForwardedRequest(Object type, EditPart sender) {
		setType(type);
		this.sender = sender;
	}

	/**
	 * Returns the EditPart that forwarded this Request.
	 *
	 * @return The EditPart that forwarded this Request
	 */
	public EditPart getSender() {
		return sender;
	}

}
