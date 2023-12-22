/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.gef.examples.text.model.Container;

/**
 * @since 3.1
 */
public class ImportsPart extends BlockTextPart {

	IFigure pane;

	public ImportsPart(Container model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new BlockFlow();
		figure.setBorder(new CompoundBorder(new MarginBorder(5, 2, 8, 0),
				new TreeBorder(Images.IMPORTS, "import declarations")));
		return figure;
	}

}