package org.eclipse.gef.ui.actions;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class DeleteRetargetAction extends RetargetAction {

/**
 * Constructs a new DeleteRetargetAction with the default ID, label and image.
 */
public DeleteRetargetAction() {
	super(IWorkbenchActionConstants.DELETE, GEFMessages.DeleteAction_Label);
	setHoverImageDescriptor(
		WorkbenchImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_HOVER));
	
	setImageDescriptor(WorkbenchImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_DELETE_DISABLED));
}

}
