/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.text.BlockFlow;

import org.eclipse.gef.examples.text.figures.Images;
import org.eclipse.gef.examples.text.figures.TreeBorder;

/**
 * @since 3.1
 */
public class ImportsPart 
	extends BlockTextualPart 
{

IFigure pane;

public ImportsPart(Object model) {
	super(model);
}

protected IFigure createFigure() {
	Figure figure = new BlockFlow();
	figure.setBorder(new CompoundBorder(
			new MarginBorder(5, 2, 8, 0),
			new TreeBorder(Images.IMPORTS, "import declarations")));
	return figure;
}

}