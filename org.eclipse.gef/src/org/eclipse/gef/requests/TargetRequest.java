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

/**
 * A request that involves a target EditPart.
 */
public interface TargetRequest {

	/**
	 * Sets the target EditPart.
	 *
	 * @param part the target EditPart
	 */
	void setTargetEditPart(EditPart part);

}
