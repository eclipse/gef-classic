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

	@Override
	public final void paint(IFigure figure, Graphics graphics, Insets insets) {
		if (((TextFlow) figure.getChildren().get(0)).getText().length() > 0) {
			paintBorder(figure, graphics, insets);
		}
	}

	protected abstract void paintBorder(IFigure figure, Graphics graphics, Insets insets);

}
