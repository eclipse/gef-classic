/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
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
		protected void paintFigure(Graphics graphics) {
			/*
			 * @TODO:Pratik    should this be cached?
			 */
			PointList list = new PointList();
			if (isHorizontal()) {
				Rectangle clientArea = getClientArea();
				clientArea.x = clientArea.getTopRight().x - 7;
				clientArea.y++;
				list.addPoint(clientArea.x, clientArea.y);
				list.addPoint(clientArea.x + 3, clientArea.y);
				list.addPoint(clientArea.x + 6, clientArea.y + 3);
				list.addPoint(clientArea.x + 3, clientArea.y + 6);
				list.addPoint(clientArea.x, clientArea.y + 6);
				graphics.fillPolygon(list);
				graphics.drawPolygon(list);
				graphics.setForegroundColor(ColorConstants.buttonLightest);
				graphics.drawLine(clientArea.x - 1, clientArea.y, 
						clientArea.x - 1, clientArea.y + 6);
				graphics.drawLine(clientArea.x, clientArea.y - 1, 
						clientArea.x + 3, clientArea.y - 1);
				graphics.drawLine(clientArea.x, clientArea.y + 7, 
						clientArea.x + 3, clientArea.y + 7);
			} else {
				Rectangle clientArea = getClientArea();
				clientArea.y = clientArea.getBottomLeft().y - 7;
				clientArea.x++;
				list.addPoint(clientArea.x, clientArea.y);
				list.addPoint(clientArea.x + 6, clientArea.y);
				list.addPoint(clientArea.x + 6, clientArea.y + 3);
				list.addPoint(clientArea.x + 3, clientArea.y + 6);
				list.addPoint(clientArea.x, clientArea.y + 3);
				graphics.fillPolygon(list);
				graphics.drawPolygon(list);
				graphics.setForegroundColor(ColorConstants.buttonLightest);
				graphics.drawLine(clientArea.x, clientArea.y - 1, 
						clientArea.x + 6, clientArea.y - 1);
				graphics.drawLine(clientArea.x - 1, clientArea.y,
						clientArea.x - 1, clientArea.y + 3);
				graphics.drawLine(clientArea.x + 7, clientArea.y, 
						clientArea.x + 7, clientArea.y + 3);
			}
		}
	};
	fig.setBackgroundColor(ColorConstants.lightGray);
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
			getGuideFigure().getParent().add(getDummyGuideFigure(), 0);
			((GraphicalEditPart)getParent()).setLayoutConstraint(
					this, getDummyGuideFigure(), new Integer(getZoomedPosition()));
			getDummyGuideFigure().setBounds(getGuideFigure().getBounds());
			// add the placeholder feedback figure to the primary viewer
			getGuideContributionLayer().add(getDummyFeedbackFigure(), 0);
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