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

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.ui.parts.RulerChangeListener;
import org.eclipse.gef.ui.parts.RulerProvider;

/**
 * @author Pratik Shah
 */
public class GuideEditPart 
	extends AbstractGraphicalEditPart 
{
	
protected GraphicalViewer diagramViewer;
protected RulerProvider rulerProvider;
protected ZoomManager zoomManager;
private GuideFeedbackFigure guideFeedbackFig;
private boolean dragInProgress = false, isDelete = false;
private IFigure dummyGuideFigure, dummyFeedbackFigure; 
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

/*
 * @TODO:Pratik   you should change this class so that it does everything properly through
 * editpolicies, and there is minimum sharing of state between the several components.
 */
	
public GuideEditPart(Object model, GraphicalViewer primaryViewer) {
	setModel(model);
	diagramViewer = primaryViewer;
	zoomManager = (ZoomManager)diagramViewer.getProperty(ZoomManager.class.toString());
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	getRulerProvider().addRulerChangeListener(listener);
	zoomManager.addZoomListener(zoomListener);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new GuideEditPolicy());
}

protected GuideFeedbackFigure createDummyFeedbackFigure() {
	GuideFeedbackFigure fig = new GuideFeedbackFigure(
			isHorizontal()) {
		protected void paintFigure(Graphics graphics) {
			// don't paint anything in dummy figure
		}
	};
	return fig;
}

protected GuideFigure createDummyGuideFigure() {
	return new GuidePlaceHolder(isHorizontal());
}

protected GuideFeedbackFigure createFeedbackFigure() {
	return new GuideFeedbackFigure(isHorizontal());
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	guideFeedbackFig = createFeedbackFigure();
	getGuideContributionLayer().add(getGuideFeedbackFigure());
	return new GuideFigure(isHorizontal());
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	zoomManager.removeZoomListener(zoomListener);
	getRulerProvider().removeRulerChangeListener(listener);
	rulerProvider = null;
	if (getGuideContributionLayer().getChildren().contains(getGuideFeedbackFigure())) {
		getGuideContributionLayer().remove(getGuideFeedbackFigure());
	}
	super.deactivate();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPart#eraseSourceFeedback(org.eclipse.gef.Request)
 */
public void eraseSourceFeedback(Request request) {
	if (request.getType().equals(REQ_MOVE)) {
		updateLocationOfFigures(getZoomedPosition());
		if (getDummyGuideFigure().getParent() != null) {
			getDummyGuideFigure().getParent().remove(dummyGuideFigure);			
		}
		if (getDummyFeedbackFigure().getParent() != null) {
			getDummyFeedbackFigure().getParent().remove(getDummyFeedbackFigure());
		}
		dragInProgress = false;
	} else {
		super.eraseSourceFeedback(request);
	}
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPart#getCommand(org.eclipse.gef.Request)
 */
public Command getCommand(final Request request) {
	Command cmd = null;
	if (request.getType().equals(REQ_MOVE)) {
		final ChangeBoundsRequest req = (ChangeBoundsRequest)request;
		if (isDeleteRequest(req)) {
			cmd = getRulerProvider().getDeleteGuideCommand(getModel());
		} else {
			int pDelta;
			if (isHorizontal()) {
				pDelta = req.getMoveDelta().y;
			} else {
				pDelta = req.getMoveDelta().x;
			}
			if (zoomManager != null) {
				pDelta *= (zoomManager.getUIMultiplier() 
						/ zoomManager.getZoom());				
			}
			cmd = getRulerProvider().getMoveGuideCommand(getModel(), pDelta);
		}
	} else {
		cmd = super.getCommand(request);
	}
	return cmd;
}

protected Cursor getCurrentCursor() {
	if (dragInProgress && isDelete) {
		return SharedCursors.ARROW;
	} else {
		return getGuideFigure().getCursor();
	}
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
 */
public DragTracker getDragTracker(Request request) {
	return new DragEditPartsTracker(this) {
		protected void eraseSourceFeedback() {
			super.eraseSourceFeedback();
			setDefaultCursor(getCurrentCursor());
		}
		protected boolean isMove() {
			return true;
		}
		protected void showSourceFeedback() {
			super.showSourceFeedback();
			setDefaultCursor(getCurrentCursor());
		}
	};
}

protected IFigure getDummyFeedbackFigure() {
	if (dummyFeedbackFigure == null) {
		dummyFeedbackFigure = createDummyFeedbackFigure();
	}
	return dummyFeedbackFigure;
}

protected IFigure getDummyGuideFigure() {
	if (dummyGuideFigure == null) {
		dummyGuideFigure = createDummyGuideFigure();
	}
	return dummyGuideFigure;
}

protected IFigure getGuideContributionLayer() {
	return ((FreeformGraphicalRootEditPart)diagramViewer.getRootEditPart()).getLayer(
			LayerConstants.GUIDE_LAYER);
}

protected GuideFeedbackFigure getGuideFeedbackFigure() {
	return guideFeedbackFig;
}

protected GuideFigure getGuideFigure() {
	return (GuideFigure)getFigure();
}

protected RulerProvider getRulerProvider() {
	if (rulerProvider == null) {
		rulerProvider = isHorizontal() 
				? (RulerProvider)diagramViewer.getProperty(RulerProvider.VERTICAL)	
				: (RulerProvider)diagramViewer.getProperty(RulerProvider.HORIZONTAL);
	}
	return rulerProvider;
}

protected int getZoomedPosition() {
	double position = getRulerProvider().getGuidePosition(getModel());
	if (zoomManager != null) {
		position *= zoomManager.getZoom() / zoomManager.getUIMultiplier();
	}
	return (int)Math.round(position);
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

protected boolean isHorizontal() {
	return getParent().getModel() != ((RulerProvider)diagramViewer
			.getProperty(RulerProvider.HORIZONTAL)).getRuler();
}

protected boolean isDeleteRequest(ChangeBoundsRequest req) {
	int delta;
	if (isHorizontal()) {
		delta = req.getMoveDelta().x;
	} else {
		delta = req.getMoveDelta().y;
	}
	return delta < -20 || delta > 20;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	updateLocationOfFigures(getZoomedPosition());
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPart#showSourceFeedback(org.eclipse.gef.Request)
 */
public void showSourceFeedback(Request request) {
	if (request.getType().equals(REQ_MOVE)) {
		if (!dragInProgress) {
			dragInProgress = true;
			// add the placeholder guide figure to the ruler
			getGuideFigure().getParent().add(getDummyGuideFigure(), 0);
			((GraphicalEditPart)getParent()).setLayoutConstraint(
					this, getDummyGuideFigure(), new Integer(getZoomedPosition()));
			getDummyGuideFigure().setBounds(getGuideFigure().getBounds());
			// add the placeholder feedback figure to the primary viewer
			getGuideContributionLayer().add(getDummyFeedbackFigure(), 0);
			getDummyFeedbackFigure().setBounds(getGuideFeedbackFigure().getBounds());
			// move the guide being dragged to the last index so that it's drawn on
			// top of other guides
			List children = getFigure().getParent().getChildren();
			children.remove(getFigure());
			children.add(getFigure());
		}
		ChangeBoundsRequest req = (ChangeBoundsRequest)request;
		if (isDeleteRequest(req)) {
			isDelete = true;
			getFigure().setVisible(false);
			getGuideFeedbackFigure().setVisible(false);
		} else {
			int positionDelta;
			if (isHorizontal()) {
				positionDelta = req.getMoveDelta().y;
			} else {
				positionDelta = req.getMoveDelta().x;
			}
			updateLocationOfFigures(getZoomedPosition() + positionDelta);
			getFigure().setVisible(true);
			getGuideFeedbackFigure().setVisible(true);
			isDelete = false;
		}
	} else {
		super.showSourceFeedback(request);		
	}
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPart#understandsRequest(org.eclipse.gef.Request)
 */
public boolean understandsRequest(Request request) {
	if (request.getType().equals(REQ_MOVE)) {
		return true;
	}
	return super.understandsRequest(request);
}

protected void updateLocationOfFigures(int position) {
	((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), 
			new Integer(position));
	Point guideFeedbackLocation = getGuideFeedbackFigure().getLocation();
	if (isHorizontal()) {
		guideFeedbackLocation.y = position;
	} else {
		guideFeedbackLocation.x = position;
	}
	getGuideFeedbackFigure().setLocation(guideFeedbackLocation);
	getGuideFeedbackFigure().revalidate();
}

public static class GuideFeedbackFigure extends Figure {
	private boolean horizontal;
	public GuideFeedbackFigure(boolean isHorizontal) {
		horizontal = isHorizontal;
		setPreferredSize(1, 1);
	}
	public boolean isHorizontal() {
		return horizontal;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle bounds = getBounds();
		g.setLineStyle(Graphics.LINE_DOT);
		g.setXORMode(true);
		g.setForegroundColor(ColorConstants.darkGray);
		if (isHorizontal()) {
			g.drawLine(bounds.x, bounds.y, bounds.right(), bounds.y);
			g.drawLine(bounds.x + 2, bounds.y, bounds.right(), bounds.y);
		} else {
			g.drawLine(bounds.x, bounds.y, bounds.x, bounds.bottom());
			g.drawLine(bounds.x, bounds.y + 2, bounds.x, bounds.bottom());
		}
	}
}

public static class GuideEditPolicy extends SelectionEditPolicy {
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