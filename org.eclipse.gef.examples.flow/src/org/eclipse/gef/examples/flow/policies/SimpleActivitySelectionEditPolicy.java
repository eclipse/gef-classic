package org.eclipse.gef.examples.flow.policies;

import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

import org.eclipse.gef.examples.flow.figures.SimpleActivityLabel;
import org.eclipse.gef.examples.flow.parts.SimpleActivityPart;

/**
 * Handles selection of SimpleActivites. Primary
 * selection is denoted by highlight and a focus rectangle. Normal selection is denoted
 * by highlight only.
 * 
 * @author Daniel Lee
 */
public class SimpleActivitySelectionEditPolicy
	extends NonResizableEditPolicy {

private SimpleActivityLabel getLabel() {
	SimpleActivityPart part = (SimpleActivityPart)getHost();
	return ((SimpleActivityLabel)part.getFigure());
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
