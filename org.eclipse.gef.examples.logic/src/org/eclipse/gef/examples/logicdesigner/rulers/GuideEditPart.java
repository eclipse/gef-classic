/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.rulers.GuideFigure;
import org.eclipse.gef.tools.DragEditPartsTracker;

import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

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
private boolean dragInProgress = false, isDelete = false;
private IFigure dummyGuideFigure, dummyFeedbackFigure; 
private ZoomListener zoomListener = new ZoomListener() {
	public void zoomChanged(double zoom) {
		handleZoomChanged();
	}
};

/*
 * @TODO:Pratik   you should change this class so that it does everything properly through
 * editpolicies, and there is minimum sharing of state between the several components.
 */
	
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
		/*
		 * @TODO:Pratik    Externalize the Strings after you have moved it to its correct
		 * package.
		 */
		if (isDeleteRequest(req)) {
			/*
			 * @TODO:Pratik    how exactly is this going to work?  this command will
			 * hold on to the guide and this editpart.  hence, it should probably be
			 * outside this class.  when the guide is re-added in undo, will a new edit
			 * part be created for it?  what'll happen to this one?  when is it
			 * deactivated?  this command is holding on to a lot of unnecessary objects.
			 */
			cmd = new Command("Delete Guide") {
				private Ruler parent = (Ruler)getParent().getModel();
				private Map oldParts;
				public void execute() {
					oldParts = new HashMap(getGuide().getMap());
					Iterator iter = oldParts.keySet().iterator();
					while (iter.hasNext()) {
						getGuide().removePart((LogicSubpart)iter.next());
					}
					parent.removeGuide(getGuide());
				}
				public void undo() {
					parent.addGuide(getGuide());
					Iterator iter = oldParts.keySet().iterator();
					while (iter.hasNext()) {
						LogicSubpart part = (LogicSubpart)iter.next();
						getGuide().addPart(part, ((Integer)oldParts.get(part)).intValue());
					}
				}
				
			};
		} else {
			cmd = new Command("Move Guide") {
				private int pDelta = Integer.MIN_VALUE;
				public void execute() {
					if (pDelta == Integer.MIN_VALUE) {
						if (getGuide().isHorizontal()) {
							pDelta = req.getMoveDelta().y;
						} else {
							pDelta = req.getMoveDelta().x;
						}
						if (zoomManager != null) {
							pDelta *= (zoomManager.getUIMultiplier() 
									/ zoomManager.getZoom());				
						}						
					}
					getGuide().setPosition(getGuide().getPosition() + pDelta);
					Iterator iter = getGuide().getParts().iterator();
					while (iter.hasNext()) {
						LogicSubpart part = (LogicSubpart)iter.next();
						Point location = part.getLocation().getCopy();
						if (getGuide().isHorizontal()) {
							location.y += pDelta;
						} else {
							location.x += pDelta;
						}
						part.setLocation(location);
					}
				}
				public void undo() {
					getGuide().setPosition(getGuide().getPosition() - pDelta);
					Iterator iter = getGuide().getParts().iterator();
					while (iter.hasNext()) {
						LogicSubpart part = (LogicSubpart)iter.next();
						Point location = part.getLocation().getCopy();
						if (getGuide().isHorizontal()) {
							location.y -= pDelta;
						} else {
							location.x -= pDelta;
						}
						part.setLocation(location);
					}
				}
			};
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

protected boolean isDeleteRequest(ChangeBoundsRequest req) {
	int delta;
	if (getGuide().isHorizontal()) {
		delta = req.getMoveDelta().x;
	} else {
		delta = req.getMoveDelta().y;
	}
	return delta < -20 || delta > 20;
}

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals(Guide.PROPERTY_CHILDREN)) {
		Object val = getGuide().getMap().get(evt.getNewValue());
		if (val == null) {
			System.out.println(evt.getNewValue() + " un-attached from " + (getGuide().isHorizontal() ? "horizontal" : "vertical") + " guide");
		} else {
			int value = ((Integer)val).intValue();
			System.out.println(evt.getNewValue() + " attached to " + (getGuide().isHorizontal() ? "horizontal" : "vertical") + " guide at " + value);
		}
	}
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
		if (isDeleteRequest(req)) {
			isDelete = true;
			getFigure().setVisible(false);
			getGuideFeedbackFigure().setVisible(false);
		} else {
			int positionDelta;
			if (getGuide().isHorizontal()) {
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