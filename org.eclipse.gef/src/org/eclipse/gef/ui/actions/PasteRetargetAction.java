package org.eclipse.gef.ui.actions;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class PasteRetargetAction extends RetargetAction {

/**
 * Constructs a new PasteRetargetAction with the default ID, label and image.
 */
public PasteRetargetAction() {
	super(IWorkbenchActionConstants.PASTE, GEFMessages.PasteAction_Label);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								ISharedImages.IMG_TOOL_PASTE));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								ISharedImages.IMG_TOOL_PASTE_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								ISharedImages.IMG_TOOL_PASTE_DISABLED));
}

}
