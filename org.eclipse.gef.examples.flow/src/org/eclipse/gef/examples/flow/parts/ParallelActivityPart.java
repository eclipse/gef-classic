/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
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
package org.eclipse.gef.examples.flow.parts;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.examples.flow.figures.ParallelActivityFigure;
import org.eclipse.gef.examples.flow.figures.SubgraphFigure;

/**
 * @author hudsonr
 */
public class ParallelActivityPart extends StructuredActivityPart {

	@Override
	protected IFigure createFigure() {
		return new ParallelActivityFigure();
	}

	/**
	 * @see org.eclipse.gef.EditPart#setSelected(int)
	 */
	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		SubgraphFigure sf = (SubgraphFigure) getFigure();
		sf.setSelected(value != SELECTED_NONE);
	}

}
