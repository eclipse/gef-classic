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
package org.eclipse.gef.ui.parts;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.draw2d.ColorConstants;

class Splitter extends Composite {

private static final int SASH_WIDTH = 6;
private static final int DRAG_MINIMUM = 50;
private static final String MAINTAIN_SIZE = "maintain size";  //$NON-NLS-1$

private int fixedSize = 150;
private int orientation = SWT.HORIZONTAL;
private Sash[] sashes = new Sash[0];
private Control[] controls = new Control[0];
private Control maxControl = null;
private Listener sashListener;

/**
 * PropertyChangeSupport
 */
protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

/**
 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void addFixedSizeChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
}

/**
 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, int, int)
 */
protected void firePropertyChange(int oldValue, int newValue) {
	listeners.firePropertyChange(MAINTAIN_SIZE, oldValue, newValue);
}

/**
 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void removeFixedSizeChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

public int getFixedSize() {
	return fixedSize;
}

public void setFixedSize(int newSize) {
	if (newSize == fixedSize) {
		return;
	}
	
	firePropertyChange(fixedSize, fixedSize = newSize);
}

class SashPainter implements Listener {
	public void handleEvent(Event e) {
		paint ((Sash)e.widget, e.gc);
	}
}

public Splitter(Composite parent, int style) {
	super(parent, checkStyle(style));
	if ((style & SWT.VERTICAL) != 0) {
		orientation = SWT.VERTICAL;
	}
	
	this.addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event e) {
			layout(true);
		}
	});
	
	sashListener = new Listener() {
		public void handleEvent(Event e) {
			onDragSash(e);
		}
	};
}

private static int checkStyle (int style) {
	int mask = SWT.BORDER;
	return style & mask;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	
	Control[] controls = getControls(true);
	if (controls.length == 0) return new Point(wHint, hHint);
	
	int width = 0;
	int height = 0;
	boolean vertical = (getStyle() & SWT.VERTICAL) != 0;
	if (vertical) {
		width = wHint;
		height += (controls.length - 1) * SASH_WIDTH;
	} else {
		height = hHint;
		width += controls.length * SASH_WIDTH;
	}
	for (int i = 0; i < controls.length; i++) {
		if (vertical) {
			Point size = controls[i].computeSize(wHint, SWT.DEFAULT);
			height += size.y;	
		} else {
			Point size = controls[i].computeSize(SWT.DEFAULT, hHint);
			if (controls[i].getData(MAINTAIN_SIZE) != null) {
				size.x = fixedSize;
			}
			width += size.x;
		}
	}
//	if (wHint != SWT.DEFAULT) width = wHint;
//	if (hHint != SWT.DEFAULT) height = hHint;
	
	return new Point(width, height);
}

/**
 * Answer SWT.HORIZONTAL if the controls in the SashForm are laid out side by side.
 * Answer SWT.VERTICAL   if the controls in the SashForm are laid out top to bottom.
 */
public int getOrientation() {
	return orientation;
}

/**
 * Answer the control that currently is maximized in the SashForm.  This value may be null.
 */
public Control getMaximizedControl() {
	return this.maxControl;
}

private Control[] getControls(boolean onlyVisible) {
	Control[] children = getChildren();
	Control[] controls = new Control[0];
	for (int i = 0; i < children.length; i++) {
		if (children[i] instanceof Sash)
			continue;
		if (onlyVisible && !children[i].getVisible()) continue;

		Control[] newControls = new Control[controls.length + 1];
		System.arraycopy(controls, 0, newControls, 0, controls.length);
		newControls[controls.length] = children[i];
		controls = newControls;
	}
	return controls;
}

public void layout(boolean changed) {
	Rectangle area = getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	Control[] newControls = getControls(true);
	if (controls.length == 0 && newControls.length == 0) return;
	controls = newControls;
	
	if (maxControl != null && !maxControl.isDisposed()) {
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] != maxControl) {
				controls[i].setBounds(-200, -200, 0, 0);
			} else {
				controls[i].setBounds(area);
			}
		}
		return;
	}
	
	// keep just the right number of sashes
	if (sashes.length < controls.length - 1) {
		Sash[] newSashes = new Sash[controls.length - 1];
		System.arraycopy(sashes, 0, newSashes, 0, sashes.length);
		int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL : SWT.HORIZONTAL;
		for (int i = sashes.length; i < newSashes.length; i++) {
			newSashes[i] = new Sash(this, sashOrientation);
			newSashes[i].setBackground(ColorConstants.button);
			newSashes[i].addListener(SWT.Paint, new SashPainter());
			newSashes[i].addListener(SWT.Selection, sashListener);
		}
		sashes = newSashes;
	}
	if (sashes.length > controls.length - 1) {
		if (controls.length == 0) {
			for (int i = 0; i < sashes.length; i++) {
				sashes[i].dispose();
			}
			sashes = new Sash[0];
		} else {
			Sash[] newSashes = new Sash[controls.length - 1];
			System.arraycopy(sashes, 0, newSashes, 0, newSashes.length);
			for (int i = controls.length - 1; i < sashes.length; i++) {
				sashes[i].dispose();
			}
			sashes = newSashes;
		}
	}
	
	if (controls.length == 0) return;
	
	//@TODO
	// This only works if the orientation is horizontal, there are two children and one 
	// of them has been set to maintain its size.
	int x = area.x;
	int width;
	for (int i = 0; i < controls.length; i++) {
		Control control = controls[i];
		if (control.getData(MAINTAIN_SIZE) != null) {
			width = fixedSize;
			if (width > area.width) {
				width = area.width - SASH_WIDTH;
			}
			control.setBounds(x, area.y, width, area.height);
			x += width + SASH_WIDTH;
		} else {
			width = Math.max(area.width - fixedSize - SASH_WIDTH, 0);
			control.setBounds(x, area.y, width, area.height);
			x += (width + SASH_WIDTH);
		}
	}
	sashes[0].setBounds(controls[0].getBounds().x + controls[0].getBounds().width, area.y, 
	                    SASH_WIDTH, area.height);
}

public void maintainSize(Control c) {
	Control[] controls = getControls(false);
	for (int i = 0; i < controls.length; i++) {
		Control ctrl = controls[i];
		if (ctrl == c) {
			ctrl.setData(MAINTAIN_SIZE, new Boolean(true));
		}
	}
}


void paint(Sash sash, GC gc) {
	Point size = sash.getSize();
	if (getOrientation() == SWT.HORIZONTAL) {
		gc.setForeground(ColorConstants.buttonDarker);
		gc.drawLine(0, 0, 0, size.y);
		gc.drawLine(SASH_WIDTH - 1, 0, SASH_WIDTH - 1, size.y);
		gc.setForeground(ColorConstants.buttonLightest);
		gc.drawLine(1, 0, 1, size.y);
	} else {
		gc.setForeground(ColorConstants.buttonDarker);
		gc.drawLine(0, 0, size.x, 0);
		gc.drawLine(0, SASH_WIDTH - 1, size.x, SASH_WIDTH - 1);
		gc.setForeground(ColorConstants.buttonLightest);
		gc.drawLine(0, 1, size.x, 1);
	}
}

private void onDragSash(Event event) {
	if (event.detail == SWT.DRAG) {
		// constrain feedback
		Rectangle area = getClientArea();
		if (orientation == SWT.HORIZONTAL) {
			if (controls[0].getData(MAINTAIN_SIZE) != null) {
				event.x = Math.max(event.x, DRAG_MINIMUM);
			} else {
				event.x = Math.min(event.x, area.width - DRAG_MINIMUM - SASH_WIDTH);
			}
		} else {
			event.y = Math.min(event.y, area.height - DRAG_MINIMUM - SASH_WIDTH);
		}
		return;
	}

	Sash sash = (Sash)event.widget;
	int sashIndex = -1;
	for (int i = 0; i < sashes.length; i++) {
		if (sashes[i] == sash) {
			sashIndex = i;
			break;
		}
	}
	if (sashIndex == -1) return;

	Control c1 = controls[sashIndex];
	Control c2 = controls[sashIndex + 1];
	Rectangle b1 = c1.getBounds();
	Rectangle b2 = c2.getBounds();
	
	Rectangle sashBounds = sash.getBounds();
	if (orientation == SWT.HORIZONTAL) {
		int shift = event.x - sashBounds.x;
		b1.width += shift;
		b2.x += shift;
		b2.width -= shift;
	} else {
		int shift = event.y - sashBounds.y;
		b1.height += shift;
		b2.y += shift;
		b2.height -= shift;
	}
	
	c1.setBounds(b1);
	sash.setBounds(event.x, event.y, event.width, event.height);
	c2.setBounds(b2);
	//@TODO
	// This will only work when you have two controls, one of whom is set to maintain size
	if (c1.getData(MAINTAIN_SIZE) != null) {
		setFixedSize(c1.getBounds().width);
	} else {
		setFixedSize(c2.getBounds().width);
	}
}

/**
 * If orientation is SWT.HORIZONTAL, lay the controls in the SashForm out side by side.
 * If orientation is SWT.VERTICAL,   lay the controls in the SashForm out top to bottom.
 */
public void setOrientation(int orientation) {
	if (this.orientation == orientation) return;
	if (orientation != SWT.HORIZONTAL && orientation != SWT.VERTICAL) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.orientation = orientation;
	
	int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL : SWT.HORIZONTAL;
	for (int i = 0; i < sashes.length; i++) {
		sashes[i].dispose();
		sashes[i] = new Sash(this, sashOrientation);
		sashes[i].setBackground(ColorConstants.buttonLightest);
		sashes[i].addListener(SWT.Selection, sashListener);
	}
	layout();
}

public void setLayout (Layout layout) {
	// SashForm does not use Layout
}

/**
 * Specify the control that should take up the entire client area of the SashForm.  
 * If one control has been maximized, and this method is called with a different control, 
 * the previous control will be minimized and the new control will be maximized..
 * if the value of control is null, the SashForm will minimize all controls and return to
 * the default layout where all controls are laid out separated by sashes.
 */
public void setMaximizedControl(Control control) {
	if (control == null) {
		if (maxControl != null) {
			this.maxControl = null;
			layout();
			for (int i = 0; i < sashes.length; i++) {
				sashes[i].setVisible(true);
			}
		}
		return;
	}
	
	for (int i = 0; i < sashes.length; i++) {
		sashes[i].setVisible(false);
	}
	maxControl = control;
	layout();

}

}