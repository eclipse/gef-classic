/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * LabelAnchors must have an owner of type {@link org.eclipse.draw2dl.Label}. The LabelAnchor
 * behaves like {@link org.eclipse.draw2dl.ChopboxAnchor} but {@link Connection Connections} will
 * point to the center of its owner's icon as opposed to the center of the
 * entire owning Label.
 */
public class LabelAnchor extends ChopboxAnchor {

	/**
	 * Constructs a LabelAnchor with no owner.
	 * 
	 * @since 2.0
	 */
	protected LabelAnchor() {
	}

	/**
	 * Constructs a LabelAnchor with owner <i>label</i>.
	 * 
	 * @param label
	 *            This LabelAnchor's owner
	 * @since 2.0
	 */
	public LabelAnchor(org.eclipse.draw2dl.Label label) {
		super(label);
	}

	/**
	 * Returns the bounds of this LabelAnchor's owning Label icon.
	 * 
	 * @return The bounds of this LabelAnchor's owning Label icon
	 * @since 2.0
	 */
	protected Rectangle getBox() {
		org.eclipse.draw2dl.Label label = (Label) getOwner();
		return label.getIconBounds();
	}

}
