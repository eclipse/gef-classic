package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class PrintRetargetAction extends RetargetAction {

/**
 * Constructs a new PrintRetargetAction with the default ID, label and image.
 */
public PrintRetargetAction() {
	super(IWorkbenchActionConstants.PRINT, GEFMessages.PrintAction_ActionLabelText);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_PRINT_EDIT));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_PRINT_EDIT_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_PRINT_EDIT_DISABLED));
}

}
