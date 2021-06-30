/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import java.util.List;

import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.Rectangle;

class FreeformHelper implements FreeformListener {

	class ChildTracker implements org.eclipse.draw2dl.FigureListener {
		public void figureMoved(org.eclipse.draw2dl.IFigure source) {
			invalidate();
		}
	}

	private org.eclipse.draw2dl.FreeformFigure host;
	private Rectangle freeformExtent;
	private FigureListener figureListener = new ChildTracker();

	FreeformHelper(org.eclipse.draw2dl.FreeformFigure host) {
		this.host = host;
	}

	public Rectangle getFreeformExtent() {
		if (freeformExtent != null)
			return freeformExtent;
		Rectangle r;
		List<IFigure> children = host.getChildren();
		for (IFigure child : children) {
			if (child instanceof FreeformFigure)
				r = ((FreeformFigure) child).getFreeformExtent();
			else
				r = child.getBounds();
			if (freeformExtent == null)
				freeformExtent = r.getCopy();
			else
				freeformExtent.union(r);
		}
		Insets insets = host.getInsets();
		if (freeformExtent == null)
			freeformExtent = new Rectangle(0, 0, insets.getWidth(),
					insets.getHeight());
		else {
			host.translateToParent(freeformExtent);
			freeformExtent.expand(insets);
		}
		// System.out.println("New extent calculated for " + host + " = " +
		// freeformExtent);
		return freeformExtent;
	}

	public void hookChild(org.eclipse.draw2dl.IFigure child) {
		invalidate();
		if (child instanceof org.eclipse.draw2dl.FreeformFigure)
			((org.eclipse.draw2dl.FreeformFigure) child).addFreeformListener(this);
		else
			child.addFigureListener(figureListener);
	}

	void invalidate() {
		freeformExtent = null;
		host.fireExtentChanged();
		if (host.getParent() != null)
			host.getParent().revalidate();
		else
			host.revalidate();
	}

	public void notifyFreeformExtentChanged() {
		// A childs freeform extent has changed, therefore this extent must be
		// recalculated
		invalidate();
	}

	public void setFreeformBounds(Rectangle bounds) {
		host.setBounds(bounds);
		bounds = bounds.getCopy();
		host.translateFromParent(bounds);
		List<IFigure> children = host.getChildren();
		for (IFigure child : children) {
			if (child instanceof FreeformFigure)
				((FreeformFigure) child).setFreeformBounds(bounds);
		}
	}

	public void unhookChild(IFigure child) {
		invalidate();
		if (child instanceof org.eclipse.draw2dl.FreeformFigure)
			((FreeformFigure) child).removeFreeformListener(this);
		else
			child.removeFigureListener(figureListener);
	}

}
