/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Style;

/**
 * @since 3.1
 */
public class BlockTextPart extends CompoundTextPart {

	public BlockTextPart(Container model) {
		super(model);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (Style.PROPERTY_ALIGNMENT.equals(property) || Style.PROPERTY_ORIENTATION.equals(property))
			refreshVisuals();
		else
			super.propertyChange(evt);
	}

	@Override
	protected void refreshVisuals() {
		BlockFlow block = (BlockFlow) getFigure();
		Style style = getModel().getStyle();
		block.setHorizontalAligment(style.getAlignment());
		block.setOrientation(style.getOrientation());
	}

}