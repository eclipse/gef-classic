/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.rulers.RulerFigure;

/**
 * @author Pratik Shah
 */
public class RulerEditPart 
	extends AbstractGraphicalEditPart
	implements PropertyChangeListener
{
	
protected GraphicalViewer diagramViewer;
protected ZoomManager zoomManager;
	
public RulerEditPart(Ruler model, GraphicalViewer primaryViewer) {
	setModel(model);
	diagramViewer = primaryViewer;
	RootEditPart root = diagramViewer.getRootEditPart();
	if (root instanceof ScalableFreeformRootEditPart) {
		zoomManager = ((ScalableFreeformRootEditPart)root).getZoomManager();
	} else if (root instanceof ScalableRootEditPart) {
		zoomManager = ((ScalableRootEditPart)root).getZoomManager();
	}
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	getRuler().addPropertyChangeListener(this);
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
	RulerFigure ruler = new RulerFigure((FigureCanvas)diagramViewer.getControl(), 
			getRuler().isHorizontal(), RulerFigure.UNIT_INCHES);
	ruler.setZoomManager(zoomManager);
	return ruler;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	getRuler().removePropertyChangeListener(this);
	super.deactivate();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
 */
protected List getModelChildren() {
	return getRuler().getGuides();
}

public Ruler getRuler() {
	return (Ruler)getModel();
}

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals(Ruler.PROPERTY_CHILDREN)) {
		refreshChildren();
	}
}

}
