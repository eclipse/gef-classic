/*
 * Created on Oct 26, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * @author Pratik Shah
 */
public class RulerRootEditPart
	extends AbstractGraphicalEditPart
	implements RootEditPart 
{
	
/*
 * @TODO:Pratik    maybe this class can inherit GraphicalRootEditPart
 */

private boolean horizontal;	
private EditPart contents;
private EditPartViewer viewer;

public RulerRootEditPart(boolean isHorzontal) {
	super();
	horizontal = isHorzontal;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	return new RulerViewport(horizontal);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
}

/* (non-Javadoc)
 * @see org.eclipse.gef.RootEditPart#getContents()
 */
public EditPart getContents() {
	return contents;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPart#getRoot()
 */
public RootEditPart getRoot() {
	return this;
}

public EditPartViewer getViewer() {
	return viewer;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.RootEditPart#setContents(org.eclipse.gef.EditPart)
 */
public void setContents(EditPart editpart) {
	if (contents != null)
		removeChild(contents);
	contents = editpart;
	if (contents != null)
		addChild(contents, 0);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.RootEditPart#setViewer(org.eclipse.gef.EditPartViewer)
 */
public void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

protected Viewport getViewport() {
	return (Viewport)getFigure();
}

public class RulerViewport extends Viewport {
	private Transposer transposer;
	public RulerViewport(boolean isHorizontal) {
		super(true);
		transposer = new Transposer();
		transposer.setEnabled(isHorizontal);
		setLayoutManager(null);
	}
	public void add(IFigure figure, Object constraint, int index) {
		if (this.getContents() != figure) {
			this.setContents(figure);
		} else {
			super.add(figure, constraint, index);
		}
	}
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (this.getContents() == null) {
			// this should never happen
			return super.getPreferredSize(wHint, hHint);
		}
		return this.getContents().getPreferredSize(wHint, hHint);
	}
	protected void readjustScrollBars() {
		// since the range model is shared with the editor, the ruler viewports should
		// not touch it
	}
	public void propertyChange(PropertyChangeEvent event) {
		if (this.getContents() != null && event.getSource() instanceof RangeModel) {
			repaint();
			String property = event.getPropertyName();
			if (property.equals(RangeModel.PROPERTY_MAXIMUM) 
					|| property.equals(RangeModel.PROPERTY_MINIMUM)
					|| property.equals(RangeModel.PROPERTY_VALUE)) {
				RangeModel rModel = (RangeModel)event.getSource();
				Rectangle contentBounds = Rectangle.SINGLETON;
				contentBounds.y = rModel.getMinimum();
				contentBounds.x = 0;
				contentBounds.height = rModel.getMaximum() - rModel.getMinimum();
				contentBounds.width = transposer.t(this.getContents().getPreferredSize())
						.width;
				contentBounds = transposer.t(contentBounds)
						.translate(getClientArea().x, getClientArea().y);
				if (!this.getContents().getBounds().equals(contentBounds)) {
					this.getContents().setBounds(contentBounds);
					this.getContents().revalidate();
				}						
			}
			getUpdateManager().performUpdate();
		}
	}
}

}