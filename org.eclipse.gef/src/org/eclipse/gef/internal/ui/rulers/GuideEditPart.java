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

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.*;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.ui.parts.RulerChangeListener;
import org.eclipse.gef.ui.parts.RulerProvider;

/**
 * @author Pratik Shah
 */
public class GuideEditPart
	extends AbstractGraphicalEditPart 
{

public static final int MIN_DISTANCE_BW_GUIDES = 5;
public static final int DELETE_THRESHOLD = 15;
	
protected GraphicalViewer diagramViewer;
private GuideLineFigure guideLineFig;
private Cursor cursor = null;
private ZoomListener zoomListener = new ZoomListener() {
	public void zoomChanged(double zoom) {
		handleZoomChanged();
	}
};
private RulerChangeListener listener = new RulerChangeListener.Stub() {
	public void notifyGuideMoved(Object guide) {
		if (getModel() == guide) {
			handleGuideMoved();
		}
	}
	public void notifyPartAttachmentChanged(Object part, Object guide) {
		if (getModel() == guide) {
			handlePartAttachmentChanged(part);
		}
	}
};

public GuideEditPart(Object model, GraphicalViewer primaryViewer) {
	setModel(model);
	diagramViewer = primaryViewer;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	getRulerProvider().addRulerChangeListener(listener);
	if (getZoomManager() != null) {
		getZoomManager().addZoomListener(zoomListener);		
	}
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new GuideSelectionPolicy());
	installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,	new DragGuidePolicy());
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	guideLineFig = createGuideLineFigure();
	getGuideLayer().add(getGuideLineFigure());
	getGuideLayer().setConstraint(getGuideLineFigure(), new Boolean(isHorizontal()));
	return new GuideFigure(isHorizontal());
}

protected GuideLineFigure createGuideLineFigure() {
	return new GuideLineFigure();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	if (getZoomManager() != null) {
		getZoomManager().removeZoomListener(zoomListener);		
	}
	getRulerProvider().removeRulerChangeListener(listener);
	if (getGuideLineFigure().getParent() != null) {
		getGuideLineFigure().getParent().remove(getGuideLineFigure());
	}
	super.deactivate();
}

public Cursor getCurrentCursor() {
	if (cursor == null) {
		return getFigure().getCursor();
	}
	return cursor;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
 */
public DragTracker getDragTracker(Request request) {
	/*
	 * @TODO:Pratik   maybe you shouldn't use a drag edit parts tracker here.  try a
	 * simpler drag tracker.
	 */
	return new DragEditPartsTracker(this) {
		protected Cursor calculateCursor() {
			return getCurrentCursor();
		}
		protected boolean isMove() {
			return true;
		}
	};
}

public IFigure getGuideLayer() {
	return ((FreeformGraphicalRootEditPart)diagramViewer.getRootEditPart()).getLayer(
			LayerConstants.GUIDE_LAYER);
}

public IFigure getGuideLineFigure() {
	return guideLineFig;
}

public RulerEditPart getRulerEditPart() {
	return (RulerEditPart)getParent();
}

public RulerProvider getRulerProvider() {
	return getRulerEditPart().getRulerProvider();
}

public int getZoomedPosition() {
	double position = getRulerProvider().getGuidePosition(getModel());
	if (getZoomManager() != null) {
		position = Math.round(position * getZoomManager().getZoom());
	}	
	return (int)position;
}

public ZoomManager getZoomManager() {
	return getRulerEditPart().getZoomManager();
}

protected void handleGuideMoved() {
	refreshVisuals();
}

protected void handlePartAttachmentChanged(Object part) {
	refreshVisuals();
}

protected void handleZoomChanged() {
	refreshVisuals();
}

public boolean isHorizontal() {
	return !getRulerEditPart().isHorizontal();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	updateLocationOfFigures(getZoomedPosition());
}

public void setCurrentCursor(Cursor c) {
	cursor = c;
}

public void updateLocationOfFigures(int position) {
	((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), 
			new Integer(position));
	Point guideFeedbackLocation = getGuideLineFigure().getBounds().getLocation();
	if (isHorizontal()) {
		guideFeedbackLocation.y = position;
	} else {
		guideFeedbackLocation.x = position;
	}
	getGuideLineFigure().setLocation(guideFeedbackLocation);
	getGuideLineFigure().revalidate();
}

public static class GuideLineFigure extends Figure {
	public GuideLineFigure() {
		setPreferredSize(1, 1);
	}
	protected void paintFigure(Graphics g) {
		g.setLineStyle(Graphics.LINE_DOT);
		g.setXORMode(true);
		g.setForegroundColor(ColorConstants.darkGray);
		if (bounds.width > bounds.height) {
			g.drawLine(bounds.x, bounds.y, bounds.right(), bounds.y);
			g.drawLine(bounds.x + 2, bounds.y, bounds.right(), bounds.y);
		} else {
			g.drawLine(bounds.x, bounds.y, bounds.x, bounds.bottom());
			g.drawLine(bounds.x, bounds.y + 2, bounds.x, bounds.bottom());
		}
	}
}

public static class GuideSelectionPolicy extends SelectionEditPolicy {
	protected void hideFocus() {
		getHostFigure().setBackgroundColor(ColorConstants.button);
	}
	protected void hideSelection() {
		// do nothing
	}
	protected void showFocus() {
		getHostFigure().setBackgroundColor(ColorConstants.blue);
	}
	protected void showSelection() {
		// do nothing
	}
}

}