/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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
