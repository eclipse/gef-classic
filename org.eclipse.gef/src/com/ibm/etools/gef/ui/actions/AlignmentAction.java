package com.ibm.etools.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.draw2d.PositionConstants;
import com.ibm.etools.draw2d.geometry.Rectangle;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.commands.CompoundCommand;
import com.ibm.etools.gef.internal.GEFMessages;
import com.ibm.etools.gef.internal.SharedImages;
import com.ibm.etools.gef.requests.AlignmentRequest;
import com.ibm.etools.gef.tools.ToolUtilities;

final public class AlignmentAction extends SelectionAction {

public static String	ID_ALIGN_LEFT   = "$align left",	//$NON-NLS-1$
						ID_ALIGN_RIGHT  = "$align right",	//$NON-NLS-1$
						ID_ALIGN_TOP    = "$align top",		//$NON-NLS-1$
						ID_ALIGN_BOTTOM = "$align bottom", 	//$NON-NLS-1$
						ID_ALIGN_CENTER = "$align center", 	//$NON-NLS-1$
						ID_ALIGN_MIDDLE = "$align middle"; 	//$NON-NLS-1$

private List operationSet;
private int alignment;

public AlignmentAction(IEditorPart editor, int style, int align) {
	super(editor, style);
	alignment = align;
	init();
}

public AlignmentAction(IEditorPart editor, int align) {
	super(editor);
	alignment = align;
	init();
}

protected boolean calculateEnabled() {
	operationSet = null;
	Command cmd = createAlignmentCommand();
	if (cmd == null)
		return false;
	return cmd.canExecute();
}

protected Rectangle calculateAlignmentRectangle(Request request) {
	List editparts = getOperationSet(request);
	if (editparts == null || editparts.isEmpty())
		return null;
	Rectangle reference = ((GraphicalEditPart)editparts.get(0)).getFigure().getBounds().getCopy();
	for (int i=1; i<editparts.size(); i++)
		reference.union(((GraphicalEditPart)editparts.get(i)).getFigure().getBounds());
	return reference;
}

private Command createAlignmentCommand() {
	AlignmentRequest request = new AlignmentRequest(RequestConstants.REQ_ALIGN);
	request.setAlignmentRectangle(calculateAlignmentRectangle(request));
	request.setAlignment(alignment);
	List editparts = getOperationSet(request);
	if (editparts.size() < 2)
		return null;

	CompoundCommand command = new CompoundCommand();
	command.setDebugLabel(getText());
	for (int i=0; i<editparts.size(); i++) {
		EditPart editpart = (EditPart)editparts.get(i);
		command.add(editpart.getCommand(request));
	}
	return command;
}

public void dispose() {
	operationSet = Collections.EMPTY_LIST;
	super.dispose();
}

public ImageDescriptor getHoverImageDescriptor() {
	return super.getHoverImageDescriptor();
}

protected List getOperationSet(Request request) {
	if (operationSet != null)
		return operationSet;
	List editparts = new ArrayList(getSelectedObjects());
	if (editparts.isEmpty() || !(editparts.get(0) instanceof GraphicalEditPart))
		return Collections.EMPTY_LIST;
	editparts = ToolUtilities.getSelectionWithoutDependants(editparts);
	ToolUtilities.filterEditPartsUnderstanding(editparts, request);
	if (editparts.size() < 2)
		return Collections.EMPTY_LIST;
	EditPart parent = ((EditPart)editparts.get(0)).getParent();
	for (int i=1; i<editparts.size(); i++) {
		EditPart part = (EditPart)editparts.get(i);
		if (part.getParent() != parent)
			return Collections.EMPTY_LIST;
	}
	return editparts;
}

protected void init() {
	super.init();
	switch (alignment) {
		case PositionConstants.LEFT: {
			setId(ID_ALIGN_LEFT);
			setText(GEFMessages.AlignLeftAction_ActionLabelText);
			setToolTipText(GEFMessages.AlignLeftAction_ActionToolTipText);
			setImageDescriptor(SharedImages.DESC_HORZ_ALIGN_LEFT);
			break;
		}
		case PositionConstants.RIGHT: {
			setId(ID_ALIGN_RIGHT);
			setText(GEFMessages.AlignRightAction_ActionLabelText);
			setToolTipText(GEFMessages.AlignRightAction_ActionToolTipText);
			setImageDescriptor(SharedImages.DESC_HORZ_ALIGN_RIGHT);
			break;
		}
		case PositionConstants.TOP: {
			setId(ID_ALIGN_TOP);
			setText(GEFMessages.AlignTopAction_ActionLabelText);
			setToolTipText(GEFMessages.AlignTopAction_ActionToolTipText);
			setImageDescriptor(SharedImages.DESC_VERT_ALIGN_TOP);
			break;
		}
		case PositionConstants.BOTTOM: {
			setId(ID_ALIGN_BOTTOM);
			setText(GEFMessages.AlignBottomAction_ActionLabelText);
			setToolTipText(GEFMessages.AlignBottomAction_ActionToolTipText);
			setImageDescriptor(SharedImages.DESC_VERT_ALIGN_BOTTOM);
			break;
		}
		case PositionConstants.CENTER: {
			setId(ID_ALIGN_CENTER);
			setText(GEFMessages.AlignCenterAction_ActionLabelText);
			setToolTipText(GEFMessages.AlignCenterAction_ActionToolTipText);
			setImageDescriptor(SharedImages.DESC_HORZ_ALIGN_CENTER);
			break;
		}
		case PositionConstants.MIDDLE: {
			setId(ID_ALIGN_MIDDLE);
			setText(GEFMessages.AlignMiddleAction_ActionLabelText);
			setToolTipText(GEFMessages.AlignMiddleAction_ActionToolTipText);
			setImageDescriptor(SharedImages.DESC_VERT_ALIGN_MIDDLE);
			break;
		}
	}
}

public void run() {
	operationSet = null;
	execute(createAlignmentCommand());
}

}
