package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class PasteRetargetAction extends RetargetAction {

/**
 * Constructs a new PasteRetargetAction with the default ID, label and image.
 */
public PasteRetargetAction() {
	super(IWorkbenchActionConstants.PASTE, GEFMessages.PasteAction_ActionLabelText);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_PASTE_EDIT));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_PASTE_EDIT_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_PASTE_EDIT_DISABLED));
}

}
