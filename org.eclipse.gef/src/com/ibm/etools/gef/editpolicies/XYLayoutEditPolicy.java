package com.ibm.etools.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.*;

public abstract class XYLayoutEditPolicy
	extends ConstrainedLayoutEditPolicy
{

protected static final Dimension DEFAULT_SIZE = new Dimension(-1, -1);

public Object getConstraintFor(Rectangle r) {
	return new Rectangle(r);
}

public Object getConstraintFor(Point p) {
	return new Rectangle(p,DEFAULT_SIZE);
}

protected Object getConstraintFor(ChangeBoundsRequest request, GraphicalEditPart child) {
	Rectangle rect = child.getFigure().getBounds().getCopy();
	rect = request.getTransformedRectangle(rect);

	rect.translate(getLayoutOrigin().getNegated());
	if (RequestConstants.REQ_MOVE_CHILDREN.equals(request.getType())) {
		Rectangle cons = (Rectangle)getCurrentConstraintFor(child);
		rect.setSize(cons.width, cons.height);
	}
	if (RequestConstants.REQ_RESIZE_CHILDREN.equals(request.getType())) {
		Dimension minSize = child.getFigure().getMinimumSize();
		if (rect.width < minSize.width)
			rect.width = minSize.width;
		if (rect.height < minSize.height)
			rect.height = minSize.height;
	}
	return getConstraintFor(rect);
}

protected Rectangle getCurrentConstraintFor(GraphicalEditPart child){
	IFigure fig = child.getFigure();
	return (Rectangle)fig.getParent().getLayoutManager().getConstraint(fig);
}

public Object getDefaultConstraint() {
	return new Rectangle(new Point(10,10), DEFAULT_SIZE);
}

protected Point getLayoutOrigin(){
	IFigure container = getLayoutContainer();
	XYLayout layout = (XYLayout)container.getLayoutManager();
	return layout.getOrigin(container);
}

/**
*/
protected void showSizeOnDropFeedback(CreateRequest request) {	
	Point p = new Point(request.getLocation());
	IFigure feedback = getSizeOnDropFeedback();
	feedback.translateToRelative(p);
	feedback.setBounds(new Rectangle(p, request.getSize()));
}

/**
 * Called when selection changes in the viewer allowing parent editparts to 
 * contribute to the toolbar.
 *
public ToolBarManager contributeToChildToolBar(ToolBarManager tbm, IViewer viewer, EditPart child) {
	super.contributeToChildToolBar(tbm, viewer, child);
	addAlignmentToolBarItems(tbm, (IGEFViewer)viewer);
	return tbm;
}*/
/*
protected ToolBarManager addAlignmentToolBarItems(ToolBarManager tbm, IGEFViewer viewer) {

	IEditor editor = viewer.getEditor();
	IGraphViewer gViewer = (IGraphViewer)viewer;
	
	if (tbm.find("alignment") == null) {
		tbm.addSeparator("alignment");
		gViewer.addDynamicToolBarItem("alignment");
	}

	
	AlignmentAction action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.LEFT_ALIGN);
	if (tbm.find("alignLeftAction") == null) {
		tbm.appendToGroup("alignment", "alignLeftAction", action);
		gViewer.addDynamicToolBarItem("alignLeftAction");
	}
	
	action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.CENTER_ALIGN);
	if (tbm.find("alignCenterAction") == null) {
		tbm.appendToGroup("alignment", "alignCenterAction", action);
		gViewer.addDynamicToolBarItem("alignCenterAction");
	}
		
	action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.RIGHT_ALIGN);
	if (tbm.find("alignRightAction") == null) {
		tbm.appendToGroup("alignment", "alignRightAction", action); 
		gViewer.addDynamicToolBarItem("alignRightAction");
	}
		
	action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.TOP_ALIGN);
	if (tbm.find("alignTopAction") == null) {
		tbm.appendToGroup("alignment", "alignTopAction", action); 
		gViewer.addDynamicToolBarItem("alignTopAction");
	}
		
	action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.MIDDLE_ALIGN);
	if (tbm.find("alignMiddleAction") == null) {
		tbm.appendToGroup("alignment", "alignMiddleAction", action);
		gViewer.addDynamicToolBarItem("alignMiddleAction");
	}
		
	action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.BOTTOM_ALIGN);
	if (tbm.find("alignBottomAction") == null) {
		tbm.appendToGroup("alignment", "alignBottomAction", action); 
		gViewer.addDynamicToolBarItem("alignBottomAction");
	}
	
	if (tbm.find("match") == null) {
		tbm.addSeparator("match");
		gViewer.addDynamicToolBarItem("match");
	}
	
	action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.MATCH_HEIGHT);
	if (tbm.find("matchHeightAction") == null) {
		tbm.appendToGroup("match", "matchHeightAction", action);
		gViewer.addDynamicToolBarItem("matchHeightAction");
	}
		
	action = new AlignmentAction(IGEFNls.RESBUNDLE, editor, AlignmentAction.MATCH_WIDTH);
	if (tbm.find("matchWidthAction") == null) {
		tbm.appendToGroup("match", "matchWidthAction", action);
		gViewer.addDynamicToolBarItem("matchWidthAction");
	}
		
	return tbm;
	
}*/

}
