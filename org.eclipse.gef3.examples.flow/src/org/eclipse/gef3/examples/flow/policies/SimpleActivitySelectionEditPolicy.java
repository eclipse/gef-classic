/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.flow.policies;

import org.eclipse.gef3.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef3.examples.flow.figures.SimpleActivityLabel;
import org.eclipse.gef3.examples.flow.parts.SimpleActivityPart;

/**
 * Handles selection of SimpleActivites. Primary selection is denoted by
 * highlight and a focus rectangle. Normal selection is denoted by highlight
 * only.
 * 
 * @author Daniel Lee
 */
public class SimpleActivitySelectionEditPolicy extends NonResizableEditPolicy {

	private SimpleActivityLabel getLabel() {
		SimpleActivityPart part = (SimpleActivityPart) getHost();
		return ((SimpleActivityLabel) part.getFigure());
	}

	/**
	 * @see org.eclipse.gef3.editpolicies.NonResizableEditPolicy#hideFocus()
	 */
	protected void hideFocus() {
		getLabel().setFocus(false);
	}

	/**
	 * @see org.eclipse.gef3.editpolicies.SelectionHandlesEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		getLabel().setSelected(false);
		getLabel().setFocus(false);

	}

	/**
	 * @see org.eclipse.gef3.editpolicies.NonResizableEditPolicy#showFocus()
	 */
	protected void showFocus() {
		getLabel().setFocus(true);
	}

	/**
	 * @see org.eclipse.gef3.editpolicies.SelectionHandlesEditPolicy#showSelection()
	 */
	protected void showPrimarySelection() {
		getLabel().setSelected(true);
		getLabel().setFocus(true);
	}

	/**
	 * @see org.eclipse.gef3.editpolicies.SelectionHandlesEditPolicy#showSelection()
	 */
	protected void showSelection() {
		getLabel().setSelected(true);
		getLabel().setFocus(false);
	}

}
