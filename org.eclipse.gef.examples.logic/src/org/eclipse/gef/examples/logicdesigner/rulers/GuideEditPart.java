/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.rulers.GuideFigure;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * @author Pratik Shah
 */
public class GuideEditPart 
	extends AbstractGraphicalEditPart 
	implements PropertyChangeListener
{
	
protected GraphicalViewer diagramViewer;
protected ZoomManager zoomManager;
private GuideFeedbackFigure guideFeedbackFig;
private boolean dragInProgress = false;
private IFigure dummyGuideFigure, dummyFeedbackFigure; 
private ZoomListener zoomListener = new ZoomListener() {
	public void zoomChanged(double zoom) {
		handleZoomChanged();
	}
};
	
public GuideEditPart(Guide model, GraphicalViewer primaryViewer) {
	setModel(model);
	diagramViewer = primaryViewer;
	zoomManager = (ZoomManager)diagramViewer.getProperty(ZoomManager.class.toString());
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	getGuide().addPropertyChangeListener(this);
	zoomManager.addZoomListener(zoomListener);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new GuideEditPolicy());
}

protected GuideFeedbackFigure createDummyFeedbackFigure() {
	GuideFeedbackFigure fig = new GuideFeedbackFigure(getGuide().isHorizontal()) {
		protected void paintFigure(Graphics graphics) {
			// don't paint anything in dummy figure
		}
	};
	return fig;
}

protected GuideFigure createDummyGuideFigure() {
	GuideFigure fig = new GuideFigure(getGuide().isHorizontal()) {
		/*
		 * @TODO:Pratik  remove the method below, if not needed
		 */
		protected void paintFigure(Graphics graphics) {
//			graphics.setXORMode(true);
			super.paintFigure(graphics);
		}
	};
	fig.setBackgroundColor(ColorConstants.red);
	return fig;
}

protected GuideFeedbackFigure createFeedbackFigure() {
	return new GuideFeedbackFigure(getGuide().isHorizontal());
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	guideFeedbackFig = createFeedbackFigure();
	getGuideContributionLayer().add(getGuideFeedbackFigure());
	return new GuideFigure(getGuide().isHorizontal());
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	zoomManager.removeZoomListener(zoomListener);
	getGuide().removePropertyChangeListener(this);
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
		dragInProgress = false;
		if (getDummyGuideFigure().getParent() != null) {
			getDummyGuideFigure().getParent().remove(dummyGuideFigure);			
		}
		if (getDummyFeedbackFigure().getParent() != null) {
			getDummyFeedbackFigure().getParent().remove(getDummyFeedbackFigure());
		}
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
		/*
		 * @TODO:Pratik    Externalize this String after you have moved it to its correct
		 * package.
		 */
		cmd = new Command("Move Guide") {
			private int oldPosition;
			public void execute() {
				ChangeBoundsRequest req = (ChangeBoundsRequest)request;
				int positionDelta;
				if (getGuide().isHorizontal()) {
					positionDelta = req.getMoveDelta().y;
				} else {
					positionDelta = req.getMoveDelta().x;
				}
				if (zoomManager != null) {
					positionDelta *= (zoomManager.getUIMultiplier() / zoomManager.getZoom());					
				}
				oldPosition = getGuide().getPosition();
				getGuide().setPosition(getGuide().getPosition() + positionDelta);
			}
			public void undo() {
				getGuide().setPosition(oldPosition);
			}
		};
	} else {
		cmd = super.getCommand(request);
	}
	return cmd;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
 */
public DragTracker getDragTracker(Request request) {
	return new DragEditPartsTracker(this) {
		protected boolean isMove() {
			return true;
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

protected Guide getGuide() {
	return (Guide)getModel();
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

protected int getZoomedPosition() {
	double position = getGuide().getPosition();
	if (zoomManager != null) {
		position *= zoomManager.getZoom() / zoomManager.getUIMultiplier();
	}
	return (int)Math.round(position);
}

protected void handleZoomChanged() {
	refreshVisuals();
}

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	refreshVisuals();
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
			getGuideFigure().getParent().add(getDummyGuideFigure());
			((GraphicalEditPart)getParent()).setLayoutConstraint(
					this, getDummyGuideFigure(), new Integer(getZoomedPosition()));
			getDummyGuideFigure().setBounds(getGuideFigure().getBounds());
			// add the placeholder feedback figure to the primary viewer
			getGuideContributionLayer().add(getDummyFeedbackFigure());
			getDummyFeedbackFigure().setBounds(getGuideFeedbackFigure().getBounds());
		}
		ChangeBoundsRequest req = (ChangeBoundsRequest)request;
		int positionDelta;
		if (getGuide().isHorizontal()) {
			positionDelta = req.getMoveDelta().y;
		} else {
			positionDelta = req.getMoveDelta().x;
		}
		updateLocationOfFigures(getZoomedPosition() + positionDelta);		
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
	if (getGuide().isHorizontal()) {
		guideFeedbackLocation.y = position;
	} else {
		guideFeedbackLocation.x = position;
	}
	getGuideFeedbackFigure().setLocation(guideFeedbackLocation);
	getGuideFeedbackFigure().revalidate();
}

public static class GuideFeedbackFigure extends Figure {
	private boolean horizontal;
	protected int lineWidth = 5;
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
	protected void paintFigure(Graphics graphics) {
		/*
		 * @TODO:Pratik  maybe you should just draw what's in the clip, instead of the
		 * entire line.
		 */
		Rectangle clientArea = getClientArea(Rectangle.SINGLETON);
		if (isHorizontal()) {
			int startX = clientArea.x;
			int startY = clientArea.y;
			while (startX < clientArea.x + clientArea.width) {
				graphics.drawLine(startX, startY, startX + lineWidth, startY);
				startX += (lineWidth * 2);
			}
		} else {
			int startX = clientArea.x;
			int startY = clientArea.y;
			while (startY < clientArea.y + clientArea.height) {
				graphics.drawLine(startX, startY, startX, startY + lineWidth);
				startY += (lineWidth * 2);
			}			
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