package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

public class LogicActionBarContributor
	extends org.eclipse.gef.ui.actions.ActionBarContributor
{
	
/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#createActions()
 */
protected void createActions() {
	super.createActions();

	RetargetAction action;

	// Create increment action	
	action = new RetargetAction(IncrementDecrementAction.INCREMENT, 
						LogicMessages.IncrementDecrementAction_Increment_ActionLabelText);
	action.setImageDescriptor(ImageDescriptor.createFromFile(LogicPlugin.class, 
														"icons/plus.gif")); //$NON-NLS-1$
	getPage().addPartListener(action);
	retargetActions.put(IncrementDecrementAction.INCREMENT, action);
	
	// Create decrement action	
	action = new RetargetAction(IncrementDecrementAction.DECREMENT, 
						LogicMessages.IncrementDecrementAction_Decrement_ActionLabelText);
	action.setImageDescriptor(ImageDescriptor.createFromFile(LogicPlugin.class, 
														"icons/minus.gif")); //$NON-NLS-1$
	getPage().addPartListener(action);
	retargetActions.put(IncrementDecrementAction.DECREMENT, action);
	
	// Create align left action	
	action = new RetargetAction(AlignmentAction.ID_ALIGN_LEFT, 
								LogicMessages.AlignLeftAction_ActionLabelText);
	action.setImageDescriptor(SharedImages.DESC_HORZ_ALIGN_LEFT);
	getPage().addPartListener(action);
	retargetActions.put(AlignmentAction.ID_ALIGN_LEFT, action);
	
	// Create align center action	
	action = new RetargetAction(AlignmentAction.ID_ALIGN_CENTER, 
								LogicMessages.AlignCenterAction_ActionLabelText);
	action.setImageDescriptor(SharedImages.DESC_HORZ_ALIGN_CENTER);
	getPage().addPartListener(action);
	retargetActions.put(AlignmentAction.ID_ALIGN_CENTER, action);
	
	// Create align right action	
	action = new RetargetAction(AlignmentAction.ID_ALIGN_RIGHT, 
								LogicMessages.AlignRightAction_ActionLabelText);
	action.setImageDescriptor(SharedImages.DESC_HORZ_ALIGN_RIGHT);
	getPage().addPartListener(action);
	retargetActions.put(AlignmentAction.ID_ALIGN_RIGHT, action);
	
	// Create align top action	
	action = new RetargetAction(AlignmentAction.ID_ALIGN_TOP, 
								LogicMessages.AlignTopAction_ActionLabelText);
	action.setImageDescriptor(SharedImages.DESC_VERT_ALIGN_TOP);
	getPage().addPartListener(action);
	retargetActions.put(AlignmentAction.ID_ALIGN_TOP, action);
	
	// Create align middle action	
	action = new RetargetAction(AlignmentAction.ID_ALIGN_MIDDLE, 
								LogicMessages.AlignMiddleAction_ActionLabelText);
	action.setImageDescriptor(SharedImages.DESC_VERT_ALIGN_MIDDLE);
	getPage().addPartListener(action);
	retargetActions.put(AlignmentAction.ID_ALIGN_MIDDLE, action);
	
	// Create align bottom action	
	action = new RetargetAction(AlignmentAction.ID_ALIGN_BOTTOM, 
								LogicMessages.AlignBottomAction_ActionLabelText);
	action.setImageDescriptor(SharedImages.DESC_VERT_ALIGN_BOTTOM);
	getPage().addPartListener(action);
	retargetActions.put(AlignmentAction.ID_ALIGN_BOTTOM, action);

//	// Create copy action
//	action = new RetargetAction(IWorkbenchActionConstants.COPY, 
//								LogicMessages.CopyAction_ActionLabelText);
//	action.setImageDescriptor(WorkbenchImages.getImageDescriptor(
//								IWorkbenchGraphicConstants.IMG_CTOOL_COPY_EDIT));
//	action.setHoverImageDescriptor(WorkbenchImages.getImageDescriptor(
//								IWorkbenchGraphicConstants.IMG_CTOOL_COPY_EDIT_HOVER));
//	action.setDisabledImageDescriptor(WorkbenchImages.getImageDescriptor(
//								IWorkbenchGraphicConstants.IMG_CTOOL_COPY_EDIT_DISABLED));
//	getPage().addPartListener(action);
//	retargetActions.put(IWorkbenchActionConstants.COPY, action);

	// Create zoom in action
//	action = new RetargetAction(ZoomAction.ZOOM_OUT, 
//								LogicMessages.ZoomAction_ZoomOut_ActionLabelText);
//	action.setImageDescriptor(SharedImages.DESC_ZOOM_OUT);
//	getPage().addPartListener(action);
//	retargetActions.put(ZoomAction.ZOOM_OUT, action);

	// Create zoom in action
//	action = new RetargetAction(ZoomAction.ZOOM_IN, 
//								LogicMessages.ZoomAction_ZoomIn_ActionLabelText);
//	action.setImageDescriptor(SharedImages.DESC_ZOOM_IN);
//	getPage().addPartListener(action);
//	retargetActions.put(ZoomAction.ZOOM_IN, action);
}

/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareActions()
 */
protected void declareActions() {
	super.declareActions();

	globalActions.add(IncrementDecrementAction.INCREMENT);
	globalActions.add(IncrementDecrementAction.DECREMENT);
	globalActions.add(AlignmentAction.ID_ALIGN_LEFT);
	globalActions.add(AlignmentAction.ID_ALIGN_CENTER);
	globalActions.add(AlignmentAction.ID_ALIGN_RIGHT);
	globalActions.add(AlignmentAction.ID_ALIGN_TOP);
	globalActions.add(AlignmentAction.ID_ALIGN_MIDDLE);
	globalActions.add(AlignmentAction.ID_ALIGN_BOTTOM);

	toolbarActions.add(SEPARATOR);
	toolbarActions.add(IncrementDecrementAction.INCREMENT);
	toolbarActions.add(IncrementDecrementAction.DECREMENT);
	toolbarActions.add(SEPARATOR);
	toolbarActions.add(AlignmentAction.ID_ALIGN_LEFT);
	toolbarActions.add(AlignmentAction.ID_ALIGN_CENTER);
	toolbarActions.add(AlignmentAction.ID_ALIGN_RIGHT);
	toolbarActions.add(SEPARATOR);
	toolbarActions.add(AlignmentAction.ID_ALIGN_TOP);
	toolbarActions.add(AlignmentAction.ID_ALIGN_MIDDLE);
	toolbarActions.add(AlignmentAction.ID_ALIGN_BOTTOM);
}

}
