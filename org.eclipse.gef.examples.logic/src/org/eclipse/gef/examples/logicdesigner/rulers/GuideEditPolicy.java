/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LineBorder;

import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;

/**
 * @author Pratik Shah
 */
public class GuideEditPolicy 
	extends SelectionHandlesEditPolicy 
{

/* (non-Javadoc)
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
 */
protected List createSelectionHandles() {
	return null;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#hideSelection()
 */
protected void hideSelection() {
	getHostFigure().setBorder(null);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
 */
protected void showSelection() {
	getHostFigure().setBorder(new LineBorder(ColorConstants.red));
}

}
