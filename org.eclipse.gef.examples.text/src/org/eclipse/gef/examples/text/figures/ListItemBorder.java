/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.gef.examples.text.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @since 3.1
 */
public abstract class ListItemBorder extends AbstractBorder {

private boolean enabled;

public void setEnabled(boolean enabled) {
	this.enabled = enabled;
	
}

public final void paint(IFigure figure, Graphics graphics, Insets insets) {
	if (enabled)
		paintBorder(figure, graphics, insets);
}

protected abstract void paintBorder(IFigure figure, Graphics graphics, Insets insets);

}
