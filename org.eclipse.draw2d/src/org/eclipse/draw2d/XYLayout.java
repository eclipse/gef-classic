package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.draw2d.geometry.*;

/**
 * This class implements the {@link org.eclipse.draw2d.LayoutManager} 
 * interface using the XY Layout algorithm. This lays out the components 
 * using the layout constraints as defined by each component.
 */
public class XYLayout
	extends AbstractLayout
{
protected Map constraints = new HashMap();

/**
 * Sets the layout constraint of the given figure.
 * The constraints can only be of type Rectangle.
 *
 * @param figure  Figure for which the constarint is being set.
 * @param newConstraint  Constraint for the input figure.
 * @see  #getConstraint(IFigure)
 * @since 2.0
 */
public void setConstraint(IFigure figure, Object newConstraint) {
	super.setConstraint(figure, newConstraint);
	if (newConstraint != null)
		constraints.put(figure, newConstraint);
}

/**
 * Calculates and returns the preferred size of the input figure.
 * Since in XYLayout the location of the child should be preserved,
 * the preferred size would be a region which would hold all the
 * children of the input figure. If no constraint is set, that
 * child is ignored for calculation. If width and height are not
 * positive, the preferred dimensions of the child are taken.
 * 
 * @param f  Figure for which the preferred size is required.
 * @return  Preferred size of the input figure.
 * @since 2.0
 */
protected Dimension calculatePreferredSize(IFigure f, int wHint, int hHint) {
	Rectangle rect = new Rectangle();
	ListIterator children = f.getChildren().listIterator();
	while (children.hasNext()) {
		IFigure child = (IFigure)children.next();
		Rectangle r = (Rectangle)constraints.get(child);
		if (r == null)
			continue;
		if (r.width == -1 && r.height == -1)
			r = r.getResized(child.getPreferredSize());
		rect.union(r);
	}
	Dimension d = rect.getSize();
	Insets insets = f.getInsets();
	return new Dimension(d.width + insets.getWidth(), d.height + insets.getHeight()).
		union(getBorderPreferredSize(f));
}

public Object getConstraint(IFigure figure) {
	return (Rectangle) constraints.get(figure);
}

public Point getOrigin(IFigure parent) {
	return parent.getClientArea().getLocation();
}

/*
 * Implements the algorithm to layout the components of the given container figure.
 * Each component is laid out using it's own layout constraint specifying it's size
 * and position.
 */
public void layout(IFigure parent) {
	Iterator children = parent.getChildren().iterator();
	Point offset = getOrigin(parent);
	IFigure f;
	while (children.hasNext()) {
		f = (IFigure)children.next();
		Rectangle bounds = (Rectangle) getConstraint(f);
		if (bounds == null) continue;
		bounds = bounds.getTranslated(offset);
		if (bounds.width == -1 || bounds.height == -1) {
			Dimension preferredSize = f.getPreferredSize();
			bounds = bounds.getCopy();
			if (bounds.width == -1)
				bounds.width = preferredSize.width;
			if (bounds.height == -1)
				bounds.height = preferredSize.height;
		}
		f.setBounds(bounds);
	}
}

public void remove(IFigure figure){
	super.remove(figure);
	constraints.remove(figure);
}

}