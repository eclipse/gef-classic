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
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;

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
	RulerFigure ruler = new RulerFigure((FigureCanvas)diagramViewer.getControl(), 
			getRuler().isHorizontal(), getRuler().getUnit());
	return ruler;
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