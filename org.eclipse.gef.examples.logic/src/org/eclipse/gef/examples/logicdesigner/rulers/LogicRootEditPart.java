/*
 * Created on Nov 6, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.util.ListIterator;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;

/**
 * @author Pratik Shah
 */
public class LogicRootEditPart 
	extends ScalableFreeformRootEditPart 
{

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.ScalableFreeformRootEditPart#createLayers(org.eclipse.draw2d.LayeredPane)
 */
protected void createLayers(LayeredPane layeredPane) {
	super.createLayers(layeredPane);
	/*
	 * @TODO:Pratik    you should move this to FreeFormGraphicalEditPart when you put
	 * everything in their proper packages.  That would, however, require that
	 * Guide, GuideFigure, GuideEditPart, etc. also be in gef plug-in.
	 */
	Layer guideFeedbackLayer = new FreeformLayer() {
		public Rectangle getFreeformExtent() {
			int maxX = 0, minX = 0, maxY = 0, minY = 0;
			ListIterator children = getChildren().listIterator();
			while (children.hasNext()) {
				GuideEditPart.GuideFeedbackFigure child = 
						(GuideEditPart.GuideFeedbackFigure)children.next();
				if (child.isHorizontal()) {
					int position = child.getBounds().y;
					minY = Math.min(minY, position);
					maxY = Math.max(maxY, position);
				} else {
					int position = child.getBounds().x;
					minX = Math.min(minX, position);
					maxX = Math.max(maxX, position);
				}
			}
			return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
		}
		public void setFreeformBounds(Rectangle bounds) {
			super.setFreeformBounds(bounds);
			ListIterator children = getChildren().listIterator();
			while (children.hasNext()) {
				GuideEditPart.GuideFeedbackFigure child = 
						(GuideEditPart.GuideFeedbackFigure)children.next();
				if (child.isHorizontal()) {
					Rectangle.SINGLETON.setLocation(
							getBounds().x, child.getBounds().y);
					Rectangle.SINGLETON.setSize(getBounds().width, 1);
				} else {
					Rectangle.SINGLETON.setLocation(
							child.getBounds().x, getBounds().y);
					Rectangle.SINGLETON.setSize(1, getBounds().height);
				}
				child.setBounds(Rectangle.SINGLETON);
			}				
		}
	};
	layeredPane.add(guideFeedbackLayer, GUIDE_LAYER);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.FreeformGraphicalRootEditPart#createFigure()
 */
protected IFigure createFigure() {
	IFigure fig = super.createFigure();
	fig.setBorder(new AbstractBorder() {
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 1, 0, 0);
		}
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.setForegroundColor(ColorConstants.buttonDarker);
			graphics.drawLine(
					figure.getBounds().getTopLeft(), figure.getBounds().getTopRight());
			graphics.drawLine(
					figure.getBounds().getTopLeft(), figure.getBounds().getBottomLeft());
		}
	});
	return fig;
}
	
}