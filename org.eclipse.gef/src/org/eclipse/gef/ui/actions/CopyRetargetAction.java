package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class CopyRetargetAction extends RetargetAction {

/**
 * Constructs a new CopyRetargetAction with the default ID, label and image.
 */
public CopyRetargetAction() {
	super(IWorkbenchActionConstants.COPY, GEFMessages.CopyAction_ActionLabelText);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_COPY_EDIT));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_COPY_EDIT_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_COPY_EDIT_DISABLED));
}

}
