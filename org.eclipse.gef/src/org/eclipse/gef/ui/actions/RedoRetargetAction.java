package org.eclipse.gef.ui.actions;

import java.text.MessageFormat;

import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Eric Bordeau
 */
public class RedoRetargetAction extends LabelRetargetAction {

/**
 * Constructs a new RedoRetargetAction with the default ID, label and image.
 */
public RedoRetargetAction() {
	super(IWorkbenchActionConstants.REDO,
			MessageFormat.format(GEFMessages.RedoAction_Label, 
									new Object[] {""}).trim()); //$NON-NLS-1$
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_REDO_EDIT));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_REDO_EDIT_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_REDO_EDIT_DISABLED));
}

}
