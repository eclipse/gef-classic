/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Rectangle;

public class FreeformLayeredPane
	extends LayeredPane
	implements FreeformFigure
{
private FreeformHelper helper = new FreeformHelper(this);

public FreeformLayeredPane() {
	setLayoutManager(null);
}

public void add(IFigure child, Object constraint, int index) {
	super.add(child, constraint, index);
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

protected void fireMoved() { }

protected FreeformHelper getFreeformHelper() {
	return helper;
}	

public Rectangle getFreeformExtent() {
	return helper.getFreeformExtent();
}

protected void primTranslate(int dx, int dy) {
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

public void setFreeformBounds(Rectangle bounds) {
	helper.setFreeformBounds(bounds);
}

protected void superFireMoved() {
	super.fireMoved();
}

public void validate() {
	if (isValid())
		return;
	super.validate();
}

}
