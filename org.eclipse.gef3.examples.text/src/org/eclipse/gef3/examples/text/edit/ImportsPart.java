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

package org.eclipse.gef3.examples.text.edit;

import org.eclipse.draw2dl.CompoundBorder;
import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.MarginBorder;
import org.eclipse.draw2dl.text.BlockFlow;
import org.eclipse.gef3.examples.text.figures.Images;
import org.eclipse.gef3.examples.text.figures.TreeBorder;

/**
 * @since 3.1
 */
public class ImportsPart extends BlockTextPart {

	IFigure pane;

	public ImportsPart(Object model) {
		super(model);
	}

	protected IFigure createFigure() {
		Figure figure = new BlockFlow();
		figure.setBorder(new CompoundBorder(new MarginBorder(5, 2, 8, 0),
				new TreeBorder(Images.IMPORTS, "import declarations")));
		return figure;
	}

}