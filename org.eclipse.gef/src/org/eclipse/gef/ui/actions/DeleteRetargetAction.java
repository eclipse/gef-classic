package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class DeleteRetargetAction extends RetargetAction {

/**
 * Constructs a new DeleteRetargetAction with the default ID, label and image.
 */
public DeleteRetargetAction() {
	super(IWorkbenchActionConstants.DELETE, GEFMessages.DeleteAction_ActionLabelText);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_DELETE_EDIT));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_DELETE_EDIT_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_DELETE_EDIT_DISABLED));
}

}
