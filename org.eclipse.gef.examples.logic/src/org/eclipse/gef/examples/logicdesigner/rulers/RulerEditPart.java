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
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.SimpleDragTracker;

import org.eclipse.gef.examples.logicdesigner.model.*;

/**
 * @author Pratik Shah
 */
public class RulerEditPart 
	extends AbstractGraphicalEditPart
	implements PropertyChangeListener
{
	
protected GraphicalViewer diagramViewer;
	
public RulerEditPart(Ruler model, GraphicalViewer primaryViewer) {
	setModel(model);
	diagramViewer = primaryViewer;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	getRuler().addPropertyChangeListener(this);
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
	return new RulerFigure(getRuler().isHorizontal(), getRuler().getUnit());
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	getRuler().removePropertyChangeListener(this);
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
			/*
			 * @TODO:Pratik    Externalize this string when this class is put in its
			 * proper package.
			 */
			Command cmd = new Command("Create Guide") {
				private Guide guide;
				public boolean canUndo() {
					return true;
				}
				public void execute() {
					guide = new Guide(!getRuler().isHorizontal());
					Point pt = getStartLocation();
					getFigure().translateToRelative(pt);
					int position = getRuler().isHorizontal() ? pt.x : pt.y;
					ZoomManager zoom = (ZoomManager)diagramViewer
							.getProperty(ZoomManager.class.toString());
					if (zoom != null) {
						position *= (zoom.getUIMultiplier() / zoom.getZoom());
					}
					guide.setPosition(position);
					getRuler().addGuide(guide);
				}
				public void undo() {
					getRuler().removeGuide(guide);
				}
			};
			return cmd;
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
	return getRuler().getGuides();
}

protected Ruler getRuler() {
	return (Ruler)getModel();
}

protected RulerFigure getRulerFigure() {
	return (RulerFigure)getFigure();
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

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	String property = evt.getPropertyName();
	if (property.equals(Ruler.PROPERTY_CHILDREN)) {
		refreshChildren();
	} else if (property.equals(Ruler.PROPERTY_UNIT)) {
		getRulerFigure().setUnit(getRuler().getUnit());
	}
}

}