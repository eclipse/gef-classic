package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Rectangle;

public class FreeformLayeredPane
	extends LayeredPane
	implements FreeformFigure
{
private FreeformHelper helper = new FreeformHelper(this);

public FreeformLayeredPane(){
	setLayoutManager(null);
}

public void add(IFigure child,Object constraint, int index){
	super.add(child, constraint,index);
	helper.hookChild(child);
}

public void addFreeformListener(FreeformListener listener) {
	addListener(FreeformListener.class, listener);
}

public void fireExtentChanged() {
	Iterator iter = getListeners(FreeformListener.class);
	while (iter.hasNext())
		((FreeformListener)iter.next())
			.notifyFreeformExtentChanged();
}

protected void fireMoved() {}

public Rectangle getFreeformExtent(){
	//Invalidate the extent always because the layers will never fire figure moved.
	//We need some listening mechanism for the layers.
	return helper.getFreeformExtent();
}

protected void primTranslate(int dx, int dy){
	bounds.x += dx;
	bounds.y += dy;
}

public void remove(IFigure child) {
	helper.unhookChild(child);
	super.remove(child);
}

public void removeFreeformListener(FreeformListener listener) {
	removeListener(FreeformListener.class, listener);
}

public void setFreeformBounds(Rectangle bounds){
	helper.setFreeformBounds(bounds);
}

public void validate(){
	if (isValid())
		return;
	super.validate();
}

}
