package org.eclipse.gef.ui.actions;

import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

/**
 * @author danlee
 */
public class ZoomOutRetargetAction extends RetargetAction {
	
/**
 * Constructor for ZoomInRetargetAction.
 * @param actionID
 * @param label
 */
public ZoomOutRetargetAction() {
	super(null, null);
	setText(GEFMessages.ZoomAction_ZoomOut_ActionLabelText);
	setId(GEFActionConstants.ZOOM_OUT);
	setToolTipText(GEFMessages.ZoomAction_ZoomOut_ActionToolTipText);
	setImageDescriptor(InternalImages.DESC_ZOOM_OUT);
	setActionDefinitionId(GEFActionConstants.ZOOM_OUT);
}

}