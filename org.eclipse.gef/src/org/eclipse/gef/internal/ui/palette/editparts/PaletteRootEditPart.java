/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.*;

import org.eclipse.gef.editparts.GraphicalRootEditPart;

public class PaletteRootEditPart extends GraphicalRootEditPart {
	
public IFigure createFigure() {
	Figure figure = new Figure();
	figure.setLayoutManager(new StackLayout());
	return figure;
}

public IFigure getContentPane() {
	return getFigure();
}

}