package org.eclipse.gef.ui.actions;

import java.text.MessageFormat;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.actions.LabelRetargetAction;

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
								ISharedImages.IMG_TOOL_REDO));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								ISharedImages.IMG_TOOL_REDO_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								ISharedImages.IMG_TOOL_REDO_DISABLED));
}

}
