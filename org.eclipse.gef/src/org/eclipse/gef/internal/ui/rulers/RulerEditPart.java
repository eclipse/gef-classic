/*******************************************************************************
* Copyright (c) 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/
package org.eclipse.gef.internal.ui.rulers;

import java.util.List;

import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.*;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.rulers.*;

/**
* @author Pratik Shah
*/
public class RulerEditPart
	extends AbstractGraphicalEditPart
{

protected GraphicalViewer diagramViewer;
private RulerProvider rulerProvider;
private boolean horizontal;
private RulerChangeListener listener = new RulerChangeListener.Stub() {
	public void notifyGuideReparented(Object guide) {
		handleGuideReparented(guide);
	}
	public void notifyUnitsChanged(int newUnit) {
		handleUnitsChanged(newUnit);
	}
};

public RulerEditPart(Object model) {
	setModel(model);
}

/* (non-Javadoc)
* @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
*/
public void activate() {
	getRulerProvider().addRulerChangeListener(listener);
	getRulerFigure().setZoomManager(getZoomManager());
	super.activate();
}

/* (non-Javadoc)
* @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
*/
protected void createEditPolicies() {
	/*
	 * @TODO:Pratik   the right way of creating guides and figuring out the target
	 * edit part is by installing an edit policy with container role.  talk to randy about
	 * how this should work.  would isMove() in GuideEditPart's drag tracker still have to
	 * return true all the time?
	 */
//	installEditPolicy(EditPolicy.CONTAINER_ROLE, );
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new RulerSelectionPolicy());
}

/* (non-Javadoc)
* @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
*/
protected IFigure createFigure() {
	return new RulerFigure(isHorizontal(), getRulerProvider().getUnit());
}

/* (non-Javadoc)
* @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
*/
public void deactivate() {
	super.deactivate();
	getRulerProvider().removeRulerChangeListener(listener);
	rulerProvider = null;
	getRulerFigure().setZoomManager(null);
}

/*
 * @TODO:Pratik    Need to test this with JAWS 
 */
protected AccessibleEditPart getAccessibleEditPart() {
	return new AccessibleGraphicalEditPart() {
		public void getName(AccessibleEvent e) {
			e.result = isHorizontal() ? GEFMessages.Ruler_Horizontal_Label
									  : GEFMessages.Ruler_Vertical_Label;
		}
		public void getDescription(AccessibleEvent e) {
			e.result = GEFMessages.Ruler_Desc;
		}
	};
}

/**
 * Returns the GraphicalViewer associated with the diagram.
 * 
 * @return graphical viewer associated with the diagram.
 */
protected GraphicalViewer getDiagramViewer() {
	return diagramViewer;
}

/* (non-Javadoc)
* @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
*/
public DragTracker getDragTracker(Request request) {
	if (request.getType().equals(REQ_SELECTION)
			&& ((SelectionRequest)request).getLastButtonPressed() != 1) {
		return null;
	}
	return new RulerDragTracker(this);
}

public IFigure getGuideLayer() {
	return ((FreeformGraphicalRootEditPart)diagramViewer.getRootEditPart())
			.getLayer(LayerConstants.GUIDE_LAYER);
}

/* (non-Javadoc)
* @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
*/
protected List getModelChildren() {
	return getRulerProvider().getGuides();
}

protected RulerFigure getRulerFigure() {
	return (RulerFigure)getFigure();
}

public RulerProvider getRulerProvider() {
	return rulerProvider;
}

/* (non-Javadoc)
* @see org.eclipse.gef.EditPart#getTargetEditPart(org.eclipse.gef.Request)
*/
public EditPart getTargetEditPart(Request request) {
	if (request.getType().equals(REQ_MOVE)) {
		return this;
	} else {
		return super.getTargetEditPart(request);
	}
}

public ZoomManager getZoomManager() {
	return (ZoomManager)diagramViewer.getProperty(ZoomManager.class.toString());
}

public void handleGuideReparented(Object guide) {
	refreshChildren();
	EditPart guidePart = (EditPart)getViewer().getEditPartRegistry().get(guide);
	if (guidePart != null) {
		getViewer().select(guidePart);
	}
}

public void handleUnitsChanged(int newUnit) {
	getRulerFigure().setUnit(newUnit);
}

public boolean isHorizontal() {
	return horizontal;
}

public void setParent(EditPart parent) {
	super.setParent(parent);
	if (getParent() != null && diagramViewer == null) {
		diagramViewer = (GraphicalViewer)getViewer()
				.getProperty(GraphicalViewer.class.toString());
		RulerProvider hProvider = (RulerProvider)diagramViewer
				.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER);
		if (hProvider != null && hProvider.getRuler() == getModel()) {
			rulerProvider = hProvider;
			horizontal = true;
		} else {
			rulerProvider = (RulerProvider)diagramViewer
					.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER);
		}
	}
}

public static class RulerSelectionPolicy extends SelectionEditPolicy {
	protected void hideFocus() {
		((RulerFigure)getHostFigure()).setDrawFocus(false);
	}
	protected void hideSelection() {
		((RulerFigure)getHostFigure()).setDrawFocus(false);
	}
	protected void showFocus() {
		((RulerFigure)getHostFigure()).setDrawFocus(true);
	}
	protected void showSelection() {
		((RulerFigure)getHostFigure()).setDrawFocus(true);
	}
}

}