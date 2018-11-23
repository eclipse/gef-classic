/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	protected IFigure createFigure() {
		return new ParallelActivityFigure();
	}

	/**
	 * @see org.eclipse.gef.EditPart#setSelected(int)
	 */
	public void setSelected(int value) {
		super.setSelected(value);
		SubgraphFigure sf = (SubgraphFigure) getFigure();
		sf.setSelected(value != SELECTED_NONE);
	}

}
