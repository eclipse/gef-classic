/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.text.TextFlow;

/**
 * @since 3.1
 */
public abstract class ListItemBorder extends AbstractBorder {

public final void paint(IFigure figure, Graphics graphics, Insets insets) {
	if (((TextFlow)figure.getChildren().get(0)).getText().length() > 0)
		paintBorder(figure, graphics, insets);
}

protected abstract void paintBorder(IFigure figure, Graphics graphics, Insets insets);

}
