/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.rulers.GuideFigure;

/**
 * @author Pratik Shah
 */
public class GuideEditPart 
	extends AbstractGraphicalEditPart 
	implements PropertyChangeListener
{
	
protected GraphicalViewer diagramViewer;
	
public GuideEditPart(Guide model, GraphicalViewer primaryViewer) {
	setModel(model);
	diagramViewer = primaryViewer;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	getGuide().addPropertyChangeListener(this);
	updateFeedbackInDiagram();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new GuideEditPolicy());
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	return new GuideFigure();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	getGuide().removePropertyChangeListener(this);
	super.deactivate();
	updateFeedbackInDiagram();
}

protected Guide getGuide() {
	return (Guide)getModel();
}

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	String property = evt.getPropertyName();
	if (property.equals(Guide.PROPERTY_POSITION)) {
		Point location = getFigure().getBounds().getLocation();
		if (getGuide().isHorizontal()) {
			location.y = getGuide().getPosition();
		} else {
			location.x = getGuide().getPosition();
		}
		getFigure().setLocation(location);
	} else if (property.equals(Guide.PROPERTY_CHILDREN)) {
		updateFeedbackInDiagram();
	}
}

/**
 * 
 */
protected void updateFeedbackInDiagram() {
	// @TODO:Pratik  draw feedback in diagram viewer
	if (isActive()) {
		// add the feedback figure if not added; if added, the update the "bookmarks"--
		// the special feedback on the line that shows that a figure is attached to that
		// point
	} else {
		// remove the feedback figure
	}
}

}
