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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.SimpleDragTracker;
import org.eclipse.gef.ui.parts.RulerChangeListener;
import org.eclipse.gef.ui.parts.RulerProvider;

/**
 * @author Pratik Shah
 */
public class RulerEditPart 
	extends AbstractGraphicalEditPart
{

private RulerProvider rulerProvider;
protected GraphicalViewer diagramViewer;
private RulerChangeListener listener = new RulerChangeListener.Stub() {
	public void notifyGuideReparented(Object guide) {
		handleGuideReparented(guide);
	}
	public void notifyUnitsChanged(int newUnit) {
		handleUnitsChanged(newUnit);
	}
};
	
public RulerEditPart(Object model, GraphicalViewer primaryViewer) {
	setModel(model);
	diagramViewer = primaryViewer;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	getRulerProvider().addRulerChangeListener(listener);
	getRulerFigure().setZoomManager(
			(ZoomManager)diagramViewer.getProperty(ZoomManager.class.toString()));
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	// no edit policies for a ruler
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
	getRulerProvider().removeRulerChangeListener(listener);		
	rulerProvider = null;
	getRulerFigure().setZoomManager(null);
	super.deactivate();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
 */
public DragTracker getDragTracker(Request request) {
	return new SimpleDragTracker() {
		protected boolean handleButtonDown(int button) {
			updateSourceRequest();
			setCurrentCommand(getCommand());
			executeCurrentCommand();
			return true;
		}
		protected Command getCommand() {
			Point pt = getStartLocation();
			getFigure().translateToRelative(pt);
			int position = isHorizontal() ? pt.x : pt.y;
			ZoomManager zoom = (ZoomManager)diagramViewer
					.getProperty(ZoomManager.class.toString());
			if (zoom != null) {
				position *= (zoom.getUIMultiplier() / zoom.getZoom());
			}
			return getRulerProvider().getCreateGuideCommand(position);
		}
		protected String getCommandName() {
			return REQ_CREATE;
		}
		protected String getDebugName() {
			return "Guide creation"; //$NON-NLS-1$
		}
	};
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

protected RulerProvider getRulerProvider() {
	if (rulerProvider == null) {
		rulerProvider = isHorizontal() 
				? (RulerProvider)diagramViewer.getProperty(RulerProvider.HORIZONTAL)	
				: (RulerProvider)diagramViewer.getProperty(RulerProvider.VERTICAL);
	}
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

public void handleGuideReparented(Object guide) {
	refreshChildren();
}

public void handleUnitsChanged(int newUnit) {
	getRulerFigure().setUnit(newUnit);
}

protected boolean isHorizontal() {
	return getModel() == ((RulerProvider)diagramViewer
			.getProperty(RulerProvider.HORIZONTAL)).getRuler();
}

}