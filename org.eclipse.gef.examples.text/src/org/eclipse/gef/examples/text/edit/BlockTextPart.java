/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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
		if (Style.PROPERTY_ALIGNMENT.equals(property) || Style.PROPERTY_ORIENTATION.equals(property)) {
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}

	@Override
	protected void refreshVisuals() {
		BlockFlow block = (BlockFlow) getFigure();
		Style style = getModel().getStyle();
		block.setHorizontalAligment(style.getAlignment());
		block.setOrientation(style.getOrientation());
	}

}