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
package org.eclipse.gef.examples.ediagram.edit.policies;

import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

import org.eclipse.gef.examples.ediagram.figures.SelectableLabel;

/**
 * @author Dan Lee
 * @since 3.1
 */
public class LabelSelectionEditPolicy 
	extends NonResizableEditPolicy
{
	
protected SelectableLabel getLabel() {
	return (SelectableLabel)getHostFigure();
}
	
/**
 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#hideFocus()
 */
protected void hideFocus() {
	getLabel().setFocus(false);
}

/**
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#hideSelection()
 */
protected void hideSelection() {
	getLabel().setSelected(false);
	getLabel().setFocus(false);
	
}

/**
 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
 */
protected void showFocus() {
	getLabel().setFocus(true);
}

/**
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
 */
protected void showPrimarySelection() {
	getLabel().setSelected(true);
	getLabel().setFocus(true);
}

/**
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
 */
protected void showSelection() {
	getLabel().setSelected(true);
	getLabel().setFocus(false);
}

}