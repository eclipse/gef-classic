/*
 * Created on Nov 3, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.util.*;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.editparts.ZoomManager;


public class RulerLayout 
	extends AbstractLayout
{

private Map constraints;
private ZoomManager manager;

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
	return getMap().get(child);
}

protected Map getMap() {
	if (constraints == null) {
		constraints = new HashMap();
	}
	return constraints;
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
 */
public void layout(IFigure container) {
	List children = container.getChildren();
	Transposer transposer = new Transposer();
	transposer.setEnabled(((RulerFigure)container).isHorizontal());
	Rectangle rulerSize = transposer.t(container.getClientArea());
	for (int i = 0; i < children.size(); i++) {
		IFigure child = (IFigure)children.get(i);
		Dimension childSize = transposer.t(child.getPreferredSize());
		childSize.width = rulerSize.width;
		double position = ((Integer)getConstraint(child)).intValue();
		if (manager != null) {
			position = position * manager.getZoom() / manager.getUIMultiplier();				
		}
		position -= (childSize.height / 2.0);
		Rectangle.SINGLETON.setLocation(rulerSize.x, (int)Math.round(position));
		Rectangle.SINGLETON.setSize(childSize);
		child.setBounds(transposer.t(Rectangle.SINGLETON));
	}
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.AbstractLayout#remove(org.eclipse.draw2d.IFigure)
 */
public void remove(IFigure child) {
	getMap().remove(child);
	super.remove(child);
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.AbstractLayout#setConstraint(org.eclipse.draw2d.IFigure, java.lang.Object)
 */
public void setConstraint(IFigure child, Object constraint) {
	getMap().put(child, constraint);
	super.setConstraint(child, constraint);
}

public void setZoomManager(ZoomManager manager) {
	this.manager = manager;
}

}