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
public class UndoRetargetAction extends LabelRetargetAction {

/**
 * Constructs a new UndoRetargetAction with the default ID, label and image.
 */
public UndoRetargetAction() {
	super(IWorkbenchActionConstants.UNDO,
			MessageFormat.format(GEFMessages.UndoAction_ActionLabelText, 
									new Object[] {""}).trim());
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_UNDO_EDIT));
	setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_UNDO_EDIT_HOVER));
	setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
								IWorkbenchGraphicConstants.IMG_CTOOL_UNDO_EDIT_DISABLED));
}

}
