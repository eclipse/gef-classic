package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.tools.ToolUtilities;

final public class AlignmentAction extends SelectionAction {

/** @deprecated */
public static final String ID_ALIGN_LEFT   = GEFActionConstants.ALIGN_LEFT;
/** @deprecated */
public static final String ID_ALIGN_RIGHT  = GEFActionConstants.ALIGN_RIGHT;
/** @deprecated */
public static final String ID_ALIGN_TOP    = GEFActionConstants.ALIGN_TOP;
/** @deprecated */
public static final String ID_ALIGN_BOTTOM = GEFActionConstants.ALIGN_BOTTOM;
/** @deprecated */
public static final String ID_ALIGN_CENTER = GEFActionConstants.ALIGN_CENTER;
/** @deprecated */
public static final String ID_ALIGN_MIDDLE = GEFActionConstants.ALIGN_MIDDLE;

private List operationSet;
private int alignment;

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
	GraphicalEditPart part = (GraphicalEditPart)editparts.get(editparts.size()-1);
	Rectangle rect = part.getFigure().getBounds().getCopy();
	part.getFigure().translateToAbsolute(rect);
	return rect;
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
			setId(GEFActionConstants.ALIGN_LEFT);
			setText(GEFMessages.AlignLeftAction_Label);
			setToolTipText(GEFMessages.AlignLeftAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_LEFT);
			break;
		}
		case PositionConstants.RIGHT: {
			setId(GEFActionConstants.ALIGN_RIGHT);
			setText(GEFMessages.AlignRightAction_Label);
			setToolTipText(GEFMessages.AlignRightAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_RIGHT);
			break;
		}
		case PositionConstants.TOP: {
			setId(GEFActionConstants.ALIGN_TOP);
			setText(GEFMessages.AlignTopAction_Label);
			setToolTipText(GEFMessages.AlignTopAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_VERT_ALIGN_TOP);
			break;
		}
		case PositionConstants.BOTTOM: {
			setId(GEFActionConstants.ALIGN_BOTTOM);
			setText(GEFMessages.AlignBottomAction_Label);
			setToolTipText(GEFMessages.AlignBottomAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_VERT_ALIGN_BOTTOM);
			break;
		}
		case PositionConstants.CENTER: {
			setId(GEFActionConstants.ALIGN_CENTER);
			setText(GEFMessages.AlignCenterAction_Label);
			setToolTipText(GEFMessages.AlignCenterAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_CENTER);
			break;
		}
		case PositionConstants.MIDDLE: {
			setId(GEFActionConstants.ALIGN_MIDDLE);
			setText(GEFMessages.AlignMiddleAction_Label);
			setToolTipText(GEFMessages.AlignMiddleAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_VERT_ALIGN_MIDDLE);
			break;
		}
	}
}

public void run() {
	operationSet = null;
	execute(createAlignmentCommand());
}

}
