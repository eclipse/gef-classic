/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.outline;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import org.eclipse.gef.examples.ediagram.EDiagramImages;


public class InheritanceTreeEditPart extends AbstractTreeEditPart {
	public InheritanceTreeEditPart(InheritanceModel model) {
		super(model);
	}
	protected Image getImage() {
		return EDiagramImages.getImage(EDiagramImages.INHERITANCE);
	}
	protected String getText() {
		return "superType : " + ((InheritanceModel)getModel()).getSuperType().getName();
	}
}