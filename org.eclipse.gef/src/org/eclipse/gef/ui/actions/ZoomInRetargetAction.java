package org.eclipse.gef.ui.actions;

import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

/**
 * @author danlee
 */
public class ZoomInRetargetAction extends RetargetAction {

/**
 * Constructor for ZoomInRetargetAction.
 * @param actionID
 * @param label
 */
public ZoomInRetargetAction() {
	super(null, null);
	setText(GEFMessages.ZoomIn_Label);
	setId(GEFActionConstants.ZOOM_IN);
	setToolTipText(GEFMessages.ZoomIn_Tooltip);
	setImageDescriptor(InternalImages.DESC_ZOOM_IN);
	setActionDefinitionId(GEFActionConstants.ZOOM_IN);
}

}