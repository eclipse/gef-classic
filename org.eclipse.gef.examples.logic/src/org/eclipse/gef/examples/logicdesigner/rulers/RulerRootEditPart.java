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
	
private static final Insets H_INSETS = new Insets(0, 1, 0, 0);
private static final Insets V_INSETS = new Insets(1, 0, 0, 0);

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
	IFigure fig = new RulerViewport(horizontal);
	fig.setBorder(new AbstractBorder() {
		public Insets getInsets(IFigure figure) {
			return horizontal ? H_INSETS : V_INSETS;
		}
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.setForegroundColor(ColorConstants.buttonDarker);
			Point reduction = null;
			if (horizontal) {
				reduction = new Point(0, -3);
				graphics.drawLine(figure.getBounds().getTopLeft(), 
						figure.getBounds().getBottomLeft().translate(reduction));
			} else {
				reduction = new Point(-3, 0);
				graphics.drawLine(figure.getBounds().getTopLeft(), 
						figure.getBounds().getTopRight().translate(reduction));
			}
		}
	});
	return fig;
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
public void setViewer(EditPartViewer editPartViewer) {
	viewer = editPartViewer;
}

protected Viewport getViewport() {
	return (Viewport)getFigure();
}

public class RulerViewport extends Viewport {
	private boolean horizontal;
	public RulerViewport(boolean isHorizontal) {
		super(true);
		horizontal = isHorizontal;
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
		Dimension prefSize = this.getContents().getPreferredSize(wHint, hHint);
		if (horizontal) {
			RangeModel rModel = getHorizontalRangeModel();
			prefSize.width = rModel.getMaximum() - rModel.getMinimum();
		} else {
			RangeModel rModel = getVerticalRangeModel();
			prefSize.height = rModel.getMaximum() - rModel.getMinimum();
		}
		return prefSize.expand(getInsets().getWidth(), getInsets().getHeight());
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
				Rectangle clientArea = getClientArea();
				Rectangle contentBounds = Rectangle.SINGLETON;
				if (horizontal) {
					contentBounds.y = 0;
					contentBounds.x = rModel.getMinimum();
					contentBounds.height = this.getContents().getPreferredSize().height;
					contentBounds.width = rModel.getMaximum() - rModel.getMinimum();
				} else {
					contentBounds.y = rModel.getMinimum();
					contentBounds.x = 0;
					contentBounds.height = rModel.getMaximum() - rModel.getMinimum();
					contentBounds.width = this.getContents().getPreferredSize().width;
				}
				contentBounds.translate(clientArea.x, clientArea.y);
				if (!this.getContents().getBounds().equals(contentBounds)) {
					this.getContents().setBounds(contentBounds);
					this.getContents().revalidate();
				}						
			}
			getUpdateManager().performUpdate();
		}
	}
	protected boolean useLocalCoordinates() {
		return true;
	}
}

}