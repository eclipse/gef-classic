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

import java.beans.*;
import java.util.*;

class AncestorHelper
	implements PropertyChangeListener, FigureListener 
{

protected final IFigure root;
protected List listeners = null;

public AncestorHelper(IFigure root) {
	this.root = root;
	addAncestors(root);
}

public void addAncestorListener(AncestorListener listener) {
	if (listeners == null) 
		listeners = new ArrayList();
	listeners.add(listener);
}

protected void addAncestors(IFigure rootFigure) {
	for (IFigure ancestor = rootFigure; 
				ancestor != null; 
				ancestor = ancestor.getParent()) {
		ancestor.addFigureListener(this);
		ancestor.addPropertyChangeListener(this);
	}
}

public void dispose() {
	removeAncestors(root);
	for (int i = 0; i < listeners.size(); i++)
		removeAncestorListener(((AncestorListener)listeners.get(i)));
}

public void figureMoved(IFigure ancestor) {
	fireAncestorMoved(ancestor);
}

protected void fireAncestorMoved(IFigure ancestor) {
//	if(root!=ancestor)((Figure)root).fireMoved();
	for (int i = 0; i < listeners.size(); i++)
		((AncestorListener)listeners.get(i)).ancestorMoved(ancestor);
}

protected void fireAncestorAdded(IFigure ancestor) {
	for (int i = 0; i < listeners.size(); i++)
		((AncestorListener)listeners.get(i)).ancestorAdded(ancestor);
}

protected void fireAncestorRemoved(IFigure ancestor) {
	for (int i = 0; i < listeners.size(); i++)
		((AncestorListener)listeners.get(i)).ancestorRemoved(ancestor);
}

public int getNumberOfListeners() {
	return listeners.size();
}

public void propertyChange(PropertyChangeEvent event) {
	if (event.getPropertyName().equals("parent")) { //$NON-NLS-1$
		IFigure oldParent = (IFigure)event.getOldValue();
		IFigure newParent = (IFigure)event.getNewValue();
		if (oldParent != null) {
			removeAncestors(oldParent);
			fireAncestorRemoved(oldParent);
		}
		if (newParent != null) {
			addAncestors(newParent);
			fireAncestorAdded(newParent);
		}
	}
}

public void removeAncestorListener(AncestorListener listener) {
	listeners.remove(listener);
}

protected void removeAncestors(IFigure rootFigure) {
	for (IFigure ancestor = rootFigure; 
				ancestor != null; 
				ancestor = ancestor.getParent()) {
		ancestor.removeFigureListener(this);
		ancestor.removePropertyChangeListener(this);
	}
}

}