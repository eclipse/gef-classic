package org.eclipse.gef.examples.logicdesigner.rulers;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;


public class RulerLayout 
	extends XYLayout
{

/* (non-Javadoc)
 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
 */
protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
	return new Dimension(1, 1);
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.AbstractLayout#getConstraint(org.eclipse.draw2d.IFigure)
 */
public Object getConstraint(IFigure child) {
	return constraints.get(child);
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
 */
public void layout(IFigure container) {
	List children = container.getChildren();
	Rectangle rulerSize = container.getClientArea();
	for (int i = 0; i < children.size(); i++) {
		IFigure child = (IFigure)children.get(i);
		Dimension childSize = child.getPreferredSize();
		int position = ((Integer)getConstraint(child)).intValue();
		if (((RulerFigure)container).isHorizontal()) {
			childSize.height = rulerSize.height - 1;
			Rectangle.SINGLETON.setLocation(
					position - (childSize.width / 2), rulerSize.y);
		} else {
			childSize.width = rulerSize.width - 1;
			Rectangle.SINGLETON.setLocation(
					rulerSize.x, position - (childSize.height / 2));
		}
		Rectangle.SINGLETON.setSize(childSize);
		child.setBounds(Rectangle.SINGLETON);
	}
}

}