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
package org.eclipse.gef.ui.palette.customize;

/**
 * An <code>EntryPageContainer</code> allows an <code>EntryPage</code> to report
 * errors to it.
 * 
 * @author Pratik Shah
 */
public interface EntryPageContainer {

	/**
	 * Clears the error.
	 */
	void clearProblem();

	/**
	 * Shows the error to the user.
	 * 
	 * @param description A description of the problem. Should be as brief as
	 *                    possible.
	 */
	void showProblem(String description);

}
