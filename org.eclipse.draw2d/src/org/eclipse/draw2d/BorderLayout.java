package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Pratik Shah
 */
public class BorderLayout 
	extends AbstractHintLayout 
{

public static final Integer CENTER = new Integer(PositionConstants.CENTER);
public static final Integer TOP = new Integer(PositionConstants.TOP);
public static final Integer BOTTOM = new Integer(PositionConstants.BOTTOM);
public static final Integer LEFT = new Integer(PositionConstants.LEFT);
public static final Integer RIGHT = new Integer(PositionConstants.RIGHT);

private IFigure center, left, top, bottom, right;
private int vGap = 0, hGap = 0;

/**
 * @see org.eclipse.draw2d.AbstractHintLayout#calculateMinimumSize(IFigure, int, int)
 */
protected Dimension calculateMinimumSize(IFigure container, int wHint, int hHint) {
	int minWHint = 0, minHHint = 0;
	if (wHint < 0) {
		minWHint = -1;
	}
	if (hHint < 0){
		minHHint = -1;
	}
	Insets border = container.getInsets();
	wHint = Math.max(minWHint, wHint - border.getWidth());
	hHint = Math.max(minHHint, hHint - border.getHeight());
	Dimension minSize = new Dimension();
	int middleRowWidth = 0, middleRowHeight = 0;
	int rows = 0, columns = 0;

	if (top != null) {
		Dimension childSize = top.getMinimumSize(wHint, hHint);
		hHint = Math.max(minHHint, hHint - (childSize.height + vGap));
		minSize.copyFrom(childSize);
		rows += 1;
	}
	if (bottom != null) {
		Dimension childSize = bottom.getMinimumSize(wHint, hHint);
		hHint = Math.max(minHHint, hHint - (childSize.height + vGap));
		minSize.width = Math.max(minSize.width, childSize.width);
		minSize.height += childSize.height;
		rows += 1;
	}
	if (left != null) {
		Dimension childSize = left.getMinimumSize(wHint, hHint);
		middleRowWidth = childSize.width;
		middleRowHeight = childSize.height;
		wHint = Math.max(minWHint, wHint - (childSize.width + hGap));
		columns += 1;
	}
	if (right != null) {
		Dimension childSize = right.getMinimumSize(wHint, hHint);
		middleRowWidth += childSize.width;
		middleRowHeight = Math.max(childSize.height, middleRowHeight);
		wHint = Math.max(minWHint, wHint - (childSize.width + hGap));
		columns += 1;
	}
	if (center != null) {
		Dimension childSize = center.getMinimumSize(wHint, hHint);
		middleRowWidth += childSize.width;
		middleRowHeight = Math.max(childSize.height, middleRowHeight);
		columns += 1;
	}

	rows += columns > 0 ? 1 : 0;
	// Add spacing, insets, and the size of the middle row
	minSize.height += middleRowHeight + border.getHeight() + ((rows - 1) * vGap);
	minSize.width = Math.max(minSize.width, middleRowWidth) + border.getWidth() 
					+ ((columns - 1) * hGap);
	
	return minSize;
}

/**
 * @see AbstractLayout#calculatePreferredSize(IFigure, int, int)
 */
protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
	int minWHint = 0, minHHint = 0;
	if (wHint < 0) {
		minWHint = -1;
	}
	if (hHint < 0){
		minHHint = -1;
	}
	Insets border = container.getInsets();
	wHint = Math.max(minWHint, wHint - border.getWidth());
	hHint = Math.max(minHHint, hHint - border.getHeight());
	Dimension prefSize = new Dimension();
	int middleRowWidth = 0, middleRowHeight = 0;
	int rows = 0, columns = 0;

	if (top != null) {
		Dimension childSize = top.getPreferredSize(wHint, hHint);
		hHint = Math.max(minHHint, hHint - (childSize.height + vGap));
		prefSize.copyFrom(childSize);
		rows += 1;
	}
	if (bottom != null) {
		Dimension childSize = bottom.getPreferredSize(wHint, hHint);
		hHint = Math.max(minHHint, hHint - (childSize.height + vGap));
		prefSize.width = Math.max(prefSize.width, childSize.width);
		prefSize.height += childSize.height;
		rows += 1;
	}
	if (left != null) {
		Dimension childSize = left.getPreferredSize(wHint, hHint);
		middleRowWidth = childSize.width;
		middleRowHeight = childSize.height;
		wHint = Math.max(minWHint, wHint - (childSize.width + hGap));
		columns += 1;
	}
	if (right != null) {
		Dimension childSize = right.getPreferredSize(wHint, hHint);
		middleRowWidth += childSize.width;
		middleRowHeight = Math.max(childSize.height, middleRowHeight);
		wHint = Math.max(minWHint, wHint - (childSize.width + hGap));
		columns += 1;
	}
	if (center != null) {
		Dimension childSize = center.getPreferredSize(wHint, hHint);
		middleRowWidth += childSize.width;
		middleRowHeight = Math.max(childSize.height, middleRowHeight);
		columns += 1;
	}

	rows += columns > 0 ? 1 : 0;
	// Add spacing, insets, and the size of the middle row
	prefSize.height += middleRowHeight + border.getHeight() + ((rows - 1) * vGap);
	prefSize.width = Math.max(prefSize.width, middleRowWidth) + border.getWidth() 
					+ ((columns - 1) * hGap);
	
	return prefSize;
}

/**
 * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
 */
public void layout(IFigure container) {
	Rectangle area = container.getClientArea();
	Rectangle rect = new Rectangle();
	int wHint = area.width;
	int hHint = area.height;
	int centerXLoc = 0, centerYLoc = 0;
	if (top != null) {
		Dimension childSize = top.getPreferredSize(wHint, hHint);
		rect.setLocation(area.x, area.y);
		rect.setSize(childSize);
		rect.width = area.width;
		top.setBounds(rect);
		centerYLoc = rect.height + vGap;
		hHint = Math.max(0, hHint - centerYLoc);
	}
	if (bottom != null) {
		Dimension childSize = bottom.getPreferredSize(wHint, hHint);
		rect.setSize(childSize);
		rect.width = area.width;
		rect.setLocation(area.x, area.y + area.height - rect.height);
		bottom.setBounds(rect);
		hHint = Math.max(0, hHint - (rect.height + vGap));
	}
	if (left != null) {
		Dimension childSize = left.getPreferredSize(wHint, hHint);
		rect.setLocation(area.x, area.y + centerYLoc);
		rect.width = childSize.width;
		rect.height = hHint > 0 ? hHint : childSize.height;
		left.setBounds(rect);
		centerXLoc = rect.width + hGap;
		wHint = Math.max(0, wHint - centerXLoc);
	}
	if (right != null) {
		Dimension childSize = right.getPreferredSize(wHint, hHint);
		rect.width = childSize.width;
		rect.height = hHint > 0 ? hHint : childSize.height;
		rect.setLocation(area.x + area.width - rect.width, area.y + centerYLoc);
		right.setBounds(rect);
		wHint = Math.max(0, wHint - (rect.width + hGap));
	}
	if (center != null) {
		rect.setLocation(area.x + centerXLoc, area.y + centerYLoc);
		if (wHint > 0 && hHint > 0) {
			rect.width = wHint;
			rect.height = hHint;
		} else {
			Dimension childSize = center.getPreferredSize(wHint, hHint);
			rect.setSize(childSize);
			// All extra space goes to the center figure
			if (rect.width < wHint) {
				rect.width = wHint;
			}
			if (rect.height < hHint) {
				rect.height = hHint;
			}
		}
		center.setBounds(rect);
	}
}

/**
 * @see org.eclipse.draw2d.AbstractLayout#remove(IFigure)
 */
public void remove(IFigure child) {
	if (center == child) {
		center = null;
	} else if (top == child) {
		top = null;
	} else if (bottom == child) {
		bottom = null;
	} else if (right == child) {
		right = null;
	} else if (left == child) {
		left = null;
	}
}

/**
 * Sets the location of hte given child in this layout.  Valid constraints:
 * <UL>
 * 		<LI>{@link #CENTER}</LI>
 * 		<LI>{@link #TOP}</LI>
 * 		<LI>{@link #BOTTOM}</LI>
 * 		<LI>{@link #LEFT}</LI>
 * 		<LI>{@link #RIGHT}</LI>
 * 		<LI><code>null</code> (to remove a child's constraint)</LI>
 * </UL>
 * 
 * <p>
 * Ensure that the given Figure is indeed a child of the Figure on which this layout has
 * been set.  Proper behaviour cannot be guaranteed if that is not the case.  Also ensure
 * that every child has a valid constraint.  
 * </p>
 * <p> 
 * Passing a <code>null</code> constraint will invoke {@link #remove(IFigure)}.
 * </p>
 * <p> 
 * If the given child was assigned another constraint earlier, it will be re-assigned to
 * the new constraint.  If there is another child with the given constraint, it will be
 * over-ridden so that the given child now has that constraint.
 * </p>
 * 
 * @see org.eclipse.draw2d.AbstractLayout#setConstraint(IFigure, Object)
 */
public void setConstraint(IFigure child, Object constraint) {
	remove(child);
	super.setConstraint(child, constraint);
	if( constraint == null ){
		return;
	}
	
	switch (((Integer) constraint).intValue()) {
		case PositionConstants.CENTER :
			center = child;
			break;
		case PositionConstants.TOP :
			top = child;
			break;
		case PositionConstants.BOTTOM :
			bottom = child;
			break;
		case PositionConstants.RIGHT :
			right = child;
			break;
		case PositionConstants.LEFT :
			left = child;
			break;
		default :
			break;
	}
}

/**
 * Sets the horizontal spacing to be used between the children.  Default is 0.
 *  * @param gap	The horizontal spacing */
public void setHorizontalSpacing(int gap) {
	hGap = gap;
}

/**
 * Sets the vertical spacing ot be used between the children.  Default is 0.
 *  * @param gap	The vertical spacing */
public void setVerticalSpacing(int gap) {
	vGap = gap;
}

}
